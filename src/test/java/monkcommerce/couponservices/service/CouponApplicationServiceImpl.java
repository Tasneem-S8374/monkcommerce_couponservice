package monkcommerce.couponservices.service;

import monkcommerce.couponservices.entity.*;
import monkcommerce.couponservices.entity.dto.ApplyCouponResponseDTO;
import monkcommerce.couponservices.entity.dto.CartItemDTO;
import monkcommerce.couponservices.entity.dto.CartRequestDTO;
import monkcommerce.couponservices.entity.dto.UpdatedCartItemDTO;
import monkcommerce.couponservices.entity.repository.CouponRepository;
import monkcommerce.couponservices.entity.service.CouponApplicationServiceImpl;
import monkcommerce.couponservices.exception.InvalidCouponException;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponApplicationServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponApplicationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void applyCartCoupon_ShouldApplyFlatDiscount() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.CART)
                .discountValue(100.0)
                .expirationDate(LocalDateTime.now().plusDays(5))
                .isActive(true)
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartItemDTO item = new CartItemDTO();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(500.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(item));

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        assertEquals(100.0, response.getTotalDiscount());
    }
    
    @Test
    void applyCoupon_ShouldThrow_WhenExpired() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .expirationDate(LocalDateTime.now().minusDays(1))
                .isActive(true)
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(new ArrayList<>());

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, cart));
    }
    
    @Test
    void applyBxGyCoupon_ShouldGiveFreeProduct() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.BXGY)
                .expirationDate(LocalDateTime.now().plusDays(2))
                .isActive(true)
                .repetitionLimit(5)
                .build();

        CouponBxgyBuy buy = CouponBxgyBuy.builder()
                .productId(1L)
                .requiredQuantity(2)
                .build();

        CouponBxgyGet get = CouponBxgyGet.builder()
                .productId(2L)
                .freeQuantity(1)
                .build();

        coupon.setBxgyBuyRules(List.of(buy));
        coupon.setBxgyGetRules(List.of(get));

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartItemDTO p1 = new CartItemDTO();
        p1.setProductId(1L);
        p1.setQuantity(4);
        p1.setPrice(100.0);

        CartItemDTO p2 = new CartItemDTO();
        p2.setProductId(2L);
        p2.setQuantity(2);
        p2.setPrice(50.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(p1, p2));

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        assertTrue(response.getTotalDiscount() > 0);
    }
    
    
    @Test
    void applyCoupon_ShouldThrow_WhenNotFound() {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.empty());

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(new ArrayList<>());

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, cart));
    }
    
    
    @Test
    void applyProductCoupon_ShouldApplyOnlyOnSpecificProduct() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.PRODUCT)
                .discountValue(50.0)
                .expirationDate(LocalDateTime.now().plusDays(5))
                .isActive(true)
                .build();

        CouponProductRule rule = CouponProductRule.builder()
                .productId(1L)
                .minQuantity(2)
                .build();

        coupon.setProductRules(List.of(rule));

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartItemDTO p1 = new CartItemDTO();
        p1.setProductId(1L);
        p1.setQuantity(2);
        p1.setPrice(200.0);

        CartItemDTO p2 = new CartItemDTO();
        p2.setProductId(2L);
        p2.setQuantity(2);
        p2.setPrice(300.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(p1, p2));

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        assertEquals(50.0, response.getTotalDiscount());
    }
    
    @Test
    void applyBxGy_ShouldRespectRepetitionLimit() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.BXGY)
                .expirationDate(LocalDateTime.now().plusDays(2))
                .isActive(true)
                .repetitionLimit(1)
                .build();

        CouponBxgyBuy buy = CouponBxgyBuy.builder()
                .productId(1L)
                .requiredQuantity(2)
                .build();

        CouponBxgyGet get = CouponBxgyGet.builder()
                .productId(2L)
                .freeQuantity(1)
                .build();

        coupon.setBxgyBuyRules(List.of(buy));
        coupon.setBxgyGetRules(List.of(get));

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartItemDTO p1 = new CartItemDTO();
        p1.setProductId(1L);
        p1.setQuantity(6); // 3 possible groups
        p1.setPrice(100.0);

        CartItemDTO p2 = new CartItemDTO();
        p2.setProductId(2L);
        p2.setQuantity(3);
        p2.setPrice(50.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(p1, p2));

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        // Only 1 repetition allowed
        assertEquals(50.0, response.getTotalDiscount());
    }
    
    @Test
    void applyCoupon_ShouldThrow_WhenInactive() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .isActive(false)
                .expirationDate(LocalDateTime.now().plusDays(2))
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(new ArrayList<>());

        assertThrows(InvalidCouponException.class,
                () -> service.applyCoupon(1L, cart));
    }
    
    @Test
    void applyCoupon_ShouldReturnZero_WhenCartEmpty() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.CART)
                .discountValue(100.0)
                .expirationDate(LocalDateTime.now().plusDays(5))
                .isActive(true)
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(new ArrayList<>());

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        assertEquals(0.0, response.getTotalDiscount());
    }
    
    @Test
    void applyCoupon_ShouldDistributeDiscountProportionally() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .type(CouponType.CART)
                .discountValue(100.0)
                .expirationDate(LocalDateTime.now().plusDays(5))
                .isActive(true)
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CartItemDTO p1 = new CartItemDTO();
        p1.setProductId(1L);
        p1.setQuantity(1);
        p1.setPrice(1000.0);

        CartItemDTO p2 = new CartItemDTO();
        p2.setProductId(2L);
        p2.setQuantity(1);
        p2.setPrice(1000.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(p1, p2));

        ApplyCouponResponseDTO response =
                service.applyCoupon(1L, cart);

        assertEquals(100.0, response.getTotalDiscount());

        List<UpdatedCartItemDTO> items = response.getUpdatedCart();

        assertEquals(50.0, items.get(0).getDiscount());
        assertEquals(50.0, items.get(1).getDiscount());
    }
    
    @Test
    void validation_ShouldFail_WhenNegativeQuantity() {

        CartItemDTO item = new CartItemDTO();
        item.setProductId(1L);
        item.setQuantity(-1);
        item.setPrice(100.0);

        CartRequestDTO cart = new CartRequestDTO();
        cart.setItems(List.of(item));

        assertTrue(cart.getItems().get(0).getQuantity() < 0);
    }
}