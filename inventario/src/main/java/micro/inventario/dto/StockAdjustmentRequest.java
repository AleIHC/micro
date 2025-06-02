package micro.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockAdjustmentRequest {
    private Integer quantityChange;

}
