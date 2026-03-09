package monkcommerce.couponservices.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.CouponProductRule;

import java.util.List;

@Repository
public interface CouponProductRuleRepository extends JpaRepository<CouponProductRule, Long> {

    List<CouponProductRule> findByCouponId(Long couponId);
}