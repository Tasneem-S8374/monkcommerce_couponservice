package monkcommerce.couponservices.entity.controller;


import lombok.RequiredArgsConstructor;
import monkcommerce.couponservices.entity.dto.CouponRequestDTO;
import monkcommerce.couponservices.entity.dto.CouponResponseDTO;
import monkcommerce.couponservices.entity.service.CouponService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    // CREATE COUPON
    @PostMapping
    public ResponseEntity<CouponResponseDTO> createCoupon(
            @Valid @RequestBody CouponRequestDTO request) {

        CouponResponseDTO response = couponService.createCoupon(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET ALL COUPONS
    @GetMapping
    public ResponseEntity<List<CouponResponseDTO>> getAllCoupons() {

        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> getCouponById(
            @PathVariable Long id) {

        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> updateCoupon(
            @PathVariable Long id,
            @RequestBody CouponRequestDTO request) {

        return ResponseEntity.ok(couponService.updateCoupon(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {

        couponService.deleteCoupon(id);

        return ResponseEntity.noContent().build();
    }
}
