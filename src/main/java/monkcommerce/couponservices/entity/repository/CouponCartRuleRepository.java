package monkcommerce.couponservices.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.CouponCartRule;

import java.util.Optional;

@Repository
public interface CouponCartRuleRepository extends JpaRepository<CouponCartRule, Long> {

    Optional<CouponCartRule> findByCouponId(Long couponId);
}