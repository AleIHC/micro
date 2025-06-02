package micro.inventario.controller;

import micro.inventario.dto.StockAdjustmentRequest;
import micro.inventario.model.Inventory;
import micro.inventario.service.InventoryService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {

    private final InventoryService inventoryService;

    public InventoryRestController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public Mono<Inventory> getInventory(@PathVariable String productId) {
        return inventoryService.getInventoryForProduct(productId);
    }

    @PostMapping("/{productId}/adjust")
    public Mono<Inventory> adjustStock(
            @PathVariable String productId,
            @RequestBody StockAdjustmentRequest request) {
        return inventoryService.updateStock(productId, request.getQuantityChange());
    }
}
