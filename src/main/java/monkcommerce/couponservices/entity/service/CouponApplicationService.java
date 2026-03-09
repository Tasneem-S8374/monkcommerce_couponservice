package monkcommerce.couponservices.entity.service;



import java.util.List;

import monkcommerce.couponservices.entity.dto.ApplicableCouponDTO;
import monkcommerce.couponservices.entity.dto.ApplyCouponResponseDTO;
import monkcommerce.couponservices.entity.dto.CartRequestDTO;

public interface CouponApplicationService {

    List<ApplicableCouponDTO> getApplicableCoupons(CartRequestDTO cartRequest);

    ApplyCouponResponseDTO applyCoupon(Long couponId, CartRequestDTO cartRequest);
}
