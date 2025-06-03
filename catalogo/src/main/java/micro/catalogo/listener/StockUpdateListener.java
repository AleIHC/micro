package micro.catalogo.listener;

import micro.catalogo.dto.StockUpdateEvent;
import micro.catalogo.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockUpdateListener {

    private final ProductRepository productRepository;

    public StockUpdateListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RabbitListener(queues = "catalog.stock.queue")
    public void handleStockUpdate(StockUpdateEvent event) {
        productRepository.findById(event.getProductId())
                .flatMap(product -> {
                    product.setStock(event.getNewStock());
                    return productRepository.save(product);
                })
                .subscribe();
    }
}
