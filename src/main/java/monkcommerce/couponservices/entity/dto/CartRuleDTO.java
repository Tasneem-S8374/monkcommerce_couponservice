package monkcommerce.couponservices.entity.dto;


import lombok.Data;

@Data
public class CartRuleDTO {

    private Double minCartValue;
    private String discountType; // PERCENTAGE / FIXED
    private Double discountValue;
    private Double maxDiscountCap;
	public Double getMinCartValue() {
		return minCartValue;
	}
	public void setMinCartValue(Double minCartValue) {
		this.minCartValue = minCartValue;
	}
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
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
}