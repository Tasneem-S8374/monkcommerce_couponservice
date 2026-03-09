package monkcommerce.couponservices.entity.dto;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ApplyCouponResponseDTO {

    private List<UpdatedCartItemDTO> updatedCart;
    private Double totalPrice;
    private Double totalDiscount;
    private Double finalPrice;
}