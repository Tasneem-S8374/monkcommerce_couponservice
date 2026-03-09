package monkcommerce.couponservices.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.CouponBxgyGet;

import java.util.List;

@Repository
public interface CouponBxgyGetRepository extends JpaRepository<CouponBxgyGet, Long> {

    List<CouponBxgyGet> findByCouponId(Long couponId);
}