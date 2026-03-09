package monkcommerce.couponservices.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicableCouponDTO {

    private Long couponId;
    private String type;
    private Double discount;
}