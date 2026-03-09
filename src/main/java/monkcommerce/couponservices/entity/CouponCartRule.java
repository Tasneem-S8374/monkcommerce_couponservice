package monkcommerce.couponservices.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coupon_cart_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponCartRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double minCartValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Double discountValue;

    private Double maxDiscountCap;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getMinCartValue() {
		return minCartValue;
	}

	public void setMinCartValue(Double minCartValue) {
		this.minCartValue = minCartValue;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Double getMaxDiscountCap() {
		return maxDiscountCap;
	}

	public void setMaxDiscountCap(Double maxDiscountCap) {
		this.maxDiscountCap = maxDiscountCap;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	@OneToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
}