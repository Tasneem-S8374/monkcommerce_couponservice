package monkcommerce.couponservices.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private Double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;

    private Boolean isActive = true;

    private LocalDateTime expirationDate;

    private Integer maxUsage;
    private Integer usedCount = 0;

    private Integer repetitionLimit;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getMaxUsage() {
		return maxUsage;
	}

	public void setMaxUsage(Integer maxUsage) {
		this.maxUsage = maxUsage;
	}

	public Integer getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(Integer usedCount) {
		this.usedCount = usedCount;
	}

	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}

	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public CouponCartRule getCartRule() {
		return cartRule;
	}

	public void setCartRule(CouponCartRule cartRule) {
		this.cartRule = cartRule;
	}

	public List<CouponProductRule> getProductRules() {
		return productRules;
	}

	public void setProductRules(List<CouponProductRule> productRules) {
		this.productRules = productRules;
	}

	public List<CouponBxgyBuy> getBxgyBuyRules() {
		return bxgyBuyRules;
	}

	public void setBxgyBuyRules(List<CouponBxgyBuy> bxgyBuyRules) {
		this.bxgyBuyRules = bxgyBuyRules;
	}

	public List<CouponBxgyGet> getBxgyGetRules() {
		return bxgyGetRules;
	}

	public void setBxgyGetRules(List<CouponBxgyGet> bxgyGetRules) {
		this.bxgyGetRules = bxgyGetRules;
	}

	@OneToOne(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CouponCartRule cartRule;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CouponProductRule> productRules;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CouponBxgyBuy> bxgyBuyRules;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CouponBxgyGet> bxgyGetRules;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}