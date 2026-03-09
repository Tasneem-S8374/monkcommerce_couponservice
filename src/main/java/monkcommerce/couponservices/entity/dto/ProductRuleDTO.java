package monkcommerce.couponservices.entity.dto;


import lombok.Data;

@Data
public class ProductRuleDTO {

    private Long productId;
    private Integer minQuantity;
    private String discountType;
    private Double discountValue;
}