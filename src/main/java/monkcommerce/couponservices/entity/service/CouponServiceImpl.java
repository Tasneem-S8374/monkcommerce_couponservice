package monkcommerce.couponservices.entity.service;

import lombok.RequiredArgsConstructor;
import monkcommerce.couponservices.entity.Coupon;
import monkcommerce.couponservices.entity.CouponBxgyBuy;
import monkcommerce.couponservices.entity.CouponBxgyGet;
import monkcommerce.couponservices.entity.CouponCartRule;
import monkcommerce.couponservices.entity.CouponProductRule;
import monkcommerce.couponservices.entity.CouponType;
import monkcommerce.couponservices.entity.DiscountType;
import monkcommerce.couponservices.entity.dto.CartRuleDTO;
import monkcommerce.couponservices.entity.dto.CouponRequestDTO;
import monkcommerce.couponservices.entity.dto.CouponResponseDTO;
import monkcommerce.couponservices.entity.repository.CouponRepository;
import monkcommerce.couponservices.exception.CouponNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public CouponResponseDTO createCoupon(CouponRequestDTO request) {

        Coupon coupon = mapToEntity(request);

        Coupon savedCoupon = couponRepository.save(coupon);

        return mapToResponse(savedCoupon);
    }

    @Override
    public List<CouponResponseDTO> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponseDTO getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        return mapToResponse(coupon);
    }

    @Override
    public CouponResponseDTO updateCoupon(Long id, CouponRequestDTO request) {

        Coupon existing = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setIsActive(request.getIsActive());
        existing.setExpirationDate(request.getExpirationDate());

        Coupon updated = couponRepository.save(existing);

        return mapToResponse(updated);
    }

    @Override
    public void deleteCoupon(Long id) {

        if (!couponRepository.existsById(id)) {
            throw new CouponNotFoundException("Coupon not found");
        }

        couponRepository.deleteById(id);
    }

    // ===========================
    // Mapping Methods
    // ===========================

    private Coupon mapToEntity(CouponRequestDTO request) {

    	CouponType type = request.getType();

        Coupon coupon = Coupon.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(type)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .expirationDate(request.getExpirationDate())
                .maxUsage(request.getMaxUsage())
                .repetitionLimit(request.getRepetitionLimit())
                .build();

        // CART RULE
        if (type == CouponType.CART && request.getCartRule() != null) {
            CartRuleDTO dto = request.getCartRule();

            CouponCartRule rule = CouponCartRule.builder()
                    .minCartValue(dto.getMinCartValue())
                    .discountType(DiscountType.valueOf(dto.getDiscountType()))
                    .discountValue(dto.getDiscountValue())
                    .maxDiscountCap(dto.getMaxDiscountCap())
                    .coupon(coupon)
                    .build();

            coupon.setCartRule(rule);
        }

        // PRODUCT RULES
        if (type == CouponType.PRODUCT && request.getProductRules() != null) {

            List<CouponProductRule> productRules = request.getProductRules()
                    .stream()
                    .map(dto -> CouponProductRule.builder()
                            .productId(dto.getProductId())
                            .minQuantity(dto.getMinQuantity())
                            .discountType(DiscountType.valueOf(dto.getDiscountType()))
                            .discountValue(dto.getDiscountValue())
                            .coupon(coupon)
                            .build())
                    .collect(Collectors.toList());

            coupon.setProductRules(productRules);
        }

        // BXGY RULES
        if (type == CouponType.BXGY && request.getBxgyRule() != null) {

            List<CouponBxgyBuy> buyRules = request.getBxgyRule()
                    .getBuyProducts()
                    .stream()
                    .map(dto -> CouponBxgyBuy.builder()
                            .productId(dto.getProductId())
                            .requiredQuantity(dto.getRequiredQuantity())
                            .coupon(coupon)
                            .build())
                    .collect(Collectors.toList());

            List<CouponBxgyGet> getRules = request.getBxgyRule()
                    .getGetProducts()
                    .stream()
                    .map(dto -> CouponBxgyGet.builder()
                            .productId(dto.getProductId())
                            .freeQuantity(dto.getFreeQuantity())
                            .coupon(coupon)
                            .build())
                    .collect(Collectors.toList());

            coupon.setBxgyBuyRules(buyRules);
            coupon.setBxgyGetRules(getRules);
        }

        return coupon;
    }

    private CouponResponseDTO mapToResponse(Coupon coupon) {
        return CouponResponseDTO.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .description(coupon.getDescription())
                .type(coupon.getType().name())
                .isActive(coupon.getIsActive())
                .expirationDate(coupon.getExpirationDate())
                .build();
    }
}
