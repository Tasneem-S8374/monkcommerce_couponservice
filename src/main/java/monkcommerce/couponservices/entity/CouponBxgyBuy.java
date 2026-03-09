package monkcommerce.couponservices.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon_bxgy_buy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponBxgyBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Integer requiredQuantity;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}