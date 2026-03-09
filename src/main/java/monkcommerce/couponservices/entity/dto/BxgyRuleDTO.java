package monkcommerce.couponservices.entity.dto;


import lombok.Data;
import java.util.List;

@Data
public class BxgyRuleDTO {

    private List<BxgyBuyDTO> buyProducts;
    private List<BxgyGetDTO> getProducts;
}