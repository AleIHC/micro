package micro.inventario.service;

import micro.inventario.dto.StockUpdateEvent;
import micro.inventario.exception.InsufficientStockException;
import micro.inventario.exception.NotFoundException;
import micro.inventario.model.Inventory;
import micro.inventario.repository.InventoryRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final RabbitTemplate rabbitTemplate;

    public InventoryService(InventoryRepository inventoryRepository, RabbitTemplate rabbitTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Mono<Inventory> getInventoryForProduct(String productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public Mono<Inventory> updateStock(String productId, Integer quantityChange) {
        return inventoryRepository.findByProductId(productId)
                .flatMap(inventory -> {
                    int newAvailable = inventory.getAvailableStock() + quantityChange;
                    if (newAvailable < 0) {
                        return Mono.error(new InsufficientStockException("Stock insuficiente"));
                    }
                    inventory.setAvailableStock(newAvailable);
                    return inventoryRepository.save(inventory)
                            .doOnSuccess(updated -> sendStockUpdateEvent(updated));
                })
                .switchIfEmpty(Mono.error(new NotFoundException("Producto no encontrado")));
    }

    private void sendStockUpdateEvent(Inventory inventory) {
        StockUpdateEvent event = new StockUpdateEvent(
                inventory.getProductId(),
                inventory.getAvailableStock()
        );
        rabbitTemplate.convertAndSend("stock.exchange", "stock.update", event);
    }
}