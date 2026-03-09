package monkcommerce.couponservices.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatedCartItemDTO {

	private Long productId;
	private Integer quantity;
	private Double originalTotal;
	private Double discount;
	private Double finalTotal;
}