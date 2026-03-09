package monkcommerce.couponservices.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.Coupon;
import monkcommerce.couponservices.entity.CouponType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // Fetch active coupons
    List<Coupon> findByIsActiveTrue();

    // Fetch active and not expired coupons
    List<Coupon> findByIsActiveTrueAndExpirationDateAfter(LocalDateTime now);

    // Fetch by type
    List<Coupon> findByType(CouponType type);

    // Fetch active coupon by ID
    Optional<Coupon> findByIdAndIsActiveTrue(Long id);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND (c.expirationDate IS NULL OR c.expirationDate > :now)")
    List<Coupon> findValidCoupons(@Param("now") LocalDateTime now);
}
