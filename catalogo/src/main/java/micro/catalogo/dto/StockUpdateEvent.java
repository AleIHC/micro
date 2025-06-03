package micro.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockUpdateEvent {
    private String productId;
    private Integer newStock;

}