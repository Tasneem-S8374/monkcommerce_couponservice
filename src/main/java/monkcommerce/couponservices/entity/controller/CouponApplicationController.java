package monkcommerce.couponservices.entity.controller;

import lombok.RequiredArgsConstructor;
import monkcommerce.couponservices.entity.dto.ApplicableCouponDTO;
import monkcommerce.couponservices.entity.dto.ApplyCouponResponseDTO;
import monkcommerce.couponservices.entity.dto.CartRequestDTO;
import monkcommerce.couponservices.entity.service.CouponApplicationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponApplicationController {

    private final CouponApplicationService couponApplicationService;

    // GET ALL APPLICABLE COUPONS
    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<ApplicableCouponDTO>> getApplicableCoupons(
            @RequestBody CartRequestDTO cartRequest) {

        return ResponseEntity.ok(
                couponApplicationService.getApplicableCoupons(cartRequest)
        );
    }

    // APPLY SPECIFIC COUPON
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<ApplyCouponResponseDTO> applyCoupon(
            @PathVariable Long id,
             @Valid @RequestBody CartRequestDTO cartRequest) {

        return ResponseEntity.ok(
                couponApplicationService.applyCoupon(id, cartRequest)
        );
    }
}