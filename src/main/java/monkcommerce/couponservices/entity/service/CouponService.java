package monkcommerce.couponservices.entity.service;


import java.util.List;

import monkcommerce.couponservices.entity.dto.CouponRequestDTO;
import monkcommerce.couponservices.entity.dto.CouponResponseDTO;

public interface CouponService {

    CouponResponseDTO createCoupon(CouponRequestDTO request);

    List<CouponResponseDTO> getAllCoupons();

    CouponResponseDTO getCouponById(Long id);

    CouponResponseDTO updateCoupon(Long id, CouponRequestDTO request);

    void deleteCoupon(Long id);
}