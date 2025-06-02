package micro.inventario.repository;

import micro.inventario.model.Inventory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface InventoryRepository extends R2dbcRepository<Inventory, Long> {
    Mono<Inventory> findByProductId(String productId);
}
