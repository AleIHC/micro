package micro.catalogo.repository;

import micro.catalogo.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String > {
}
