package micro.catalogo.graphql;

import lombok.RequiredArgsConstructor;
import micro.catalogo.model.Product;
import micro.catalogo.service.ProductService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ProductGraphQLController {

    private final ProductService productService;

    @QueryMapping
    public Flux<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Mono<Product> getProduct(@Argument String id) {
        return productService.getProductById(id);
    }

    @MutationMapping
    public Mono<Product> createProduct(@Argument("product") Product product) {
        return productService.createProduct(product);
    }

    @MutationMapping
    public Mono<Product> updateProduct(@Argument String id, @Argument("product") Product product) {
        return productService.updateProduct(id, product);
    }

    @MutationMapping
    public Mono<Boolean> deleteProduct(@Argument String id) {
        return productService.deleteProduct(id)
                .then(Mono.just(true))
                .onErrorReturn(false);
    }
}
