package monkcommerce.couponservices.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon_product_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponProductRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Integer minQuantity;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Double discountValue;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}