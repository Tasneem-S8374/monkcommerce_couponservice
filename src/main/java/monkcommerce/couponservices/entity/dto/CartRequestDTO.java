package monkcommerce.couponservices.entity.dto;


import lombok.Data;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@Data
public class CartRequestDTO {

    @NotEmpty(message = "Cart must contain at least one item")
    private List<@Valid CartItemDTO> items;
}