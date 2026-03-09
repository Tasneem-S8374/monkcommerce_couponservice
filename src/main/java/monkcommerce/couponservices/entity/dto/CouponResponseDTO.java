package monkcommerce.couponservices.entity.dto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CouponResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Boolean isActive;
    private LocalDateTime expirationDate;
    private Double discountValue;
    
    
}