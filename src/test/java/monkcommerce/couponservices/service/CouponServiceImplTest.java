package monkcommerce.couponservices.service;

import monkcommerce.couponservices.entity.*;
import monkcommerce.couponservices.entity.dto.CouponRequestDTO;
import monkcommerce.couponservices.entity.dto.CouponResponseDTO;
import monkcommerce.couponservices.entity.repository.CouponRepository;
import monkcommerce.couponservices.entity.service.CouponServiceImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ==========================================================
    // ✅ CREATE COUPON TEST
    // ==========================================================

    @Test
    void createCoupon_ShouldSaveAndReturnCouponResponseDTO() {

        CouponRequestDTO request = CouponRequestDTO.builder()
                .name("Test Coupon")
                .type(CouponType.CART)
                .discountValue(100.0)
                .isActive(true)
                .build();

        Coupon savedCoupon = Coupon.builder()
                .id(1L)
                .name("Test Coupon")
                .type(CouponType.CART)
                .discountValue(100.0)
                .isActive(true)
                .build();

        when(couponRepository.save(any(Coupon.class)))
                .thenReturn(savedCoupon);

        CouponResponseDTO response = couponService.createCoupon(request);

        assertNotNull(response);
        assertEquals("Test Coupon", response.getName());
        assertEquals(100.0, response.getDiscountValue());

        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    // ==========================================================
    // ✅ GET COUPON BY ID SUCCESS
    // ==========================================================

    @Test
    void getCouponById_ShouldReturnCouponResponseDTO() {

        Coupon coupon = Coupon.builder()
                .id(1L)
                .name("Test Coupon")
                .type(CouponType.CART)
                .discountValue(50.0)
                .build();

        when(couponRepository.findById(1L))
                .thenReturn(Optional.of(coupon));

        CouponResponseDTO response = couponService.getCouponById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Coupon", response.getName());
    }

    // ==========================================================
    // ✅ GET COUPON BY ID NOT FOUND
    // ==========================================================

    @Test
    void getCouponById_ShouldThrowException_WhenNotFound() {

        when(couponRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ConfigDataResourceNotFoundException.class,
                () -> couponService.getCouponById(1L));
    }
}