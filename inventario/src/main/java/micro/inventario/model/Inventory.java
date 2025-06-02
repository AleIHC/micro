package micro.inventario.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("inventory")
public class Inventory {

    @Id
    private Long id;
    private String productId;  // ID del producto en el servicio Catálogo
    private Integer availableStock;
    private Integer reservedStock;
    private Integer minStockLevel;

    // Constructores, getters y setters (o usar Lombok)
    public Inventory() {
    }

    public Inventory(Long id, String productId, Integer availableStock, Integer reservedStock, Integer minStockLevel) {
        this.id = id;
        this.productId = productId;
        this.availableStock = availableStock;
        this.reservedStock = reservedStock;
        this.minStockLevel = minStockLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(Integer reservedStock) {
        this.reservedStock = reservedStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(id, inventory.id) && Objects.equals(productId, inventory.productId) && Objects.equals(availableStock, inventory.availableStock) && Objects.equals(reservedStock, inventory.reservedStock) && Objects.equals(minStockLevel, inventory.minStockLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, availableStock, reservedStock, minStockLevel);
    }
}
