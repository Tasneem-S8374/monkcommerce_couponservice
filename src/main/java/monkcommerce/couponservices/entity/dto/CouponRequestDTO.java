package monkcommerce.couponservices.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import monkcommerce.couponservices.entity.CouponType;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class CouponRequestDTO {
 
    @NotBlank(message = "Name must not be blank")
    private String name;
    private String description;
    private Double discountValue;
    private CouponType type; // CART, PRODUCT, BXGY
    private Boolean isActive;
    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;
    private Integer maxUsage;
    @Min(value = 1, message = "Repetition limit must be at least 1")
    private Integer repetitionLimit;

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
	public Integer getRepetitionLimit() {
		return repetitionLimit;
	}
	public void setRepetitionLimit(Integer repetitionLimit) {
		this.repetitionLimit = repetitionLimit;
	}
	public CartRuleDTO getCartRule() {
		return cartRule;
	}
	public void setCartRule(CartRuleDTO cartRule) {
		this.cartRule = cartRule;
	}
	public List<ProductRuleDTO> getProductRules() {
		return productRules;
	}
	public void setProductRules(List<ProductRuleDTO> productRules) {
		this.productRules = productRules;
	}
	public BxgyRuleDTO getBxgyRule() {
		return bxgyRule;
	}
	public void setBxgyRule(BxgyRuleDTO bxgyRule) {
		this.bxgyRule = bxgyRule;
	}
	// Rule DTOs
    private CartRuleDTO cartRule;
    private List<ProductRuleDTO> productRules;
    private BxgyRuleDTO bxgyRule;
}