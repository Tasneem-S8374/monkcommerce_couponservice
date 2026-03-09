package monkcommerce.couponservices.entity.service;
import lombok.RequiredArgsConstructor;
import monkcommerce.couponservices.entity.Coupon;
import monkcommerce.couponservices.entity.CouponBxgyBuy;
import monkcommerce.couponservices.entity.CouponBxgyGet;
import monkcommerce.couponservices.entity.CouponCartRule;
import monkcommerce.couponservices.entity.CouponProductRule;
import monkcommerce.couponservices.entity.DiscountType;
import monkcommerce.couponservices.entity.dto.ApplicableCouponDTO;
import monkcommerce.couponservices.entity.dto.ApplyCouponResponseDTO;
import monkcommerce.couponservices.entity.dto.CartItemDTO;
import monkcommerce.couponservices.entity.dto.CartRequestDTO;
import monkcommerce.couponservices.entity.dto.UpdatedCartItemDTO;
import monkcommerce.couponservices.entity.repository.CouponRepository;
import monkcommerce.couponservices.exception.CouponExpiredException;
import monkcommerce.couponservices.exception.CouponNotFoundException;
import monkcommerce.couponservices.exception.InvalidCouponException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponApplicationServiceImpl implements CouponApplicationService {

    private final CouponRepository couponRepository;

    // ==========================
    // GET APPLICABLE COUPONS
    // ==========================
    @Override
    public List<ApplicableCouponDTO> getApplicableCoupons(CartRequestDTO cartRequest) {

        List<Coupon> validCoupons =
                couponRepository.findValidCoupons(LocalDateTime.now());

        return validCoupons.stream()
                .map(coupon -> calculateDiscount(coupon, cartRequest))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // ==========================
    // APPLY COUPON
    // ==========================
    @Override
    public ApplyCouponResponseDTO applyCoupon(Long couponId,
                                              CartRequestDTO cartRequest) {

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        if (!coupon.getIsActive()) {
            throw new InvalidCouponException("Coupon is not active");
        }

        if (coupon.getExpirationDate() != null &&
                coupon.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException("Coupon has expired");
        }

        if (coupon.getRepetitionLimit() != null &&
                coupon.getRepetitionLimit() < 0) {
            throw new InvalidCouponException("Invalid repetition limit");
        }

        double totalPrice = calculateCartTotal(cartRequest);

        double discount = calculateDiscountAmount(coupon, cartRequest);

        double finalPrice = totalPrice - discount;

        List<UpdatedCartItemDTO> updatedItems =
                buildUpdatedCart(cartRequest, discount);

        return ApplyCouponResponseDTO.builder()
                .updatedCart(updatedItems)
                .totalPrice(totalPrice)
                .totalDiscount(discount)
                .finalPrice(finalPrice)
                .build();
    }

    // ==========================
    // CORE DISCOUNT CALCULATOR
    // ==========================

    private ApplicableCouponDTO calculateDiscount(Coupon coupon,
                                                  CartRequestDTO cartRequest) {

        double discount = calculateDiscountAmount(coupon, cartRequest);

        if (discount <= 0) return null;

        return ApplicableCouponDTO.builder()
                .couponId(coupon.getId())
                .type(coupon.getType().name())
                .discount(discount)
                .build();
    }

    private double calculateDiscountAmount(Coupon coupon,
                                           CartRequestDTO cartRequest) {

        switch (coupon.getType()) {

            case CART:
                return applyCartCoupon(coupon, cartRequest);

            case PRODUCT:
                return applyProductCoupon(coupon, cartRequest);

            case BXGY:
                return applyBxGyCoupon(coupon, cartRequest);

            default:
                return 0;
        }
    }

    // ==========================
    // CART-WISE LOGIC
    // ==========================

    private double applyCartCoupon(Coupon coupon,
                                   CartRequestDTO cartRequest) {

        CouponCartRule rule = coupon.getCartRule();

        double total = calculateCartTotal(cartRequest);

        if (total < rule.getMinCartValue()) return 0;

        if (rule.getDiscountType() == DiscountType.PERCENTAGE) {
            double discount = total * rule.getDiscountValue() / 100;

            if (rule.getMaxDiscountCap() != null)
                return Math.min(discount, rule.getMaxDiscountCap());

            return discount;
        } else {
            return rule.getDiscountValue();
        }
    }

    // ==========================
    // PRODUCT-WISE LOGIC
    // ==========================

    private double applyProductCoupon(Coupon coupon,
                                      CartRequestDTO cartRequest) {

        double discount = 0;

        Map<Long, CartItemDTO> cartMap = cartRequest.getItems()
                .stream()
                .collect(Collectors.toMap(
                        CartItemDTO::getProductId,
                        item -> item
                ));

        for (CouponProductRule rule : coupon.getProductRules()) {

            CartItemDTO item = cartMap.get(rule.getProductId());

            if (item != null &&
                    item.getQuantity() >= rule.getMinQuantity()) {

                if (rule.getDiscountType() == DiscountType.PERCENTAGE) {
                    discount += item.getPrice() *
                            item.getQuantity() *
                            rule.getDiscountValue() / 100;
                } else {
                    discount += rule.getDiscountValue()
                            * item.getQuantity();
                }
            }
        }

        return discount;
    }

    // ==========================
    // BXGY LOGIC
    // ==========================

    private double applyBxGyCoupon(Coupon coupon, CartRequestDTO cart) {

        // Step 1: Calculate how many full buy groups exist
        int possibleGroups = Integer.MAX_VALUE;

        for (CouponBxgyBuy buyRule : coupon.getBxgyBuyRules()) {

            CartItemDTO cartItem = cart.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(buyRule.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem == null) {
                return 0; // Required product not in cart
            }

            int groupsForThisProduct = cartItem.getQuantity() / buyRule.getRequiredQuantity();

            possibleGroups = Math.min(possibleGroups, groupsForThisProduct);
        }

        if (possibleGroups <= 0) {
            return 0;
        }

        // Step 2: Apply repetition limit
        if (coupon.getRepetitionLimit() != null) {
            possibleGroups = Math.min(possibleGroups, coupon.getRepetitionLimit());
        }

        double totalDiscount = 0;

        // Step 3: Apply GET products discount
        for (CouponBxgyGet getRule : coupon.getBxgyGetRules()) {

            CartItemDTO cartItem = cart.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(getRule.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (cartItem == null) {
                continue;
            }

            int totalFreeQuantity = possibleGroups * getRule.getFreeQuantity();

            int applicableFreeQty = Math.min(totalFreeQuantity, cartItem.getQuantity());

            totalDiscount += applicableFreeQty * cartItem.getPrice();
        }

        return totalDiscount;
    }

    // ==========================
    // HELPER METHODS
    // ==========================

    private boolean isCouponValid(Coupon coupon) {
        return coupon.getIsActive()
                && (coupon.getExpirationDate() == null
                || coupon.getExpirationDate().isAfter(LocalDateTime.now()));
    }

    private double calculateCartTotal(CartRequestDTO cartRequest) {
        return cartRequest.getItems()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private List<UpdatedCartItemDTO> buildUpdatedCart(
            CartRequestDTO cartRequest,
            double totalDiscount) {

        double totalCartValue = cartRequest.getItems()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        if (totalCartValue == 0) {
            return Collections.emptyList();
        }

        return cartRequest.getItems()
                .stream()
                .map(item -> {

                    double itemTotal = item.getPrice() * item.getQuantity();

                    double proportionalDiscount =
                            (itemTotal / totalCartValue) * totalDiscount;

                    double finalItemTotal = itemTotal - proportionalDiscount;

                    return UpdatedCartItemDTO.builder()
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .originalTotal(itemTotal)
                            .discount(proportionalDiscount)
                            .finalTotal(finalItemTotal)
                            .build();
                })
                .collect(Collectors.toList());
    }
}