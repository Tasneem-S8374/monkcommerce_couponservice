package monkcommerce.couponservices.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.CouponBxgyBuy;

import java.util.List;

@Repository
public interface CouponBxgyBuyRepository extends JpaRepository<CouponBxgyBuy, Long> {

    List<CouponBxgyBuy> findByCouponId(Long couponId);
}