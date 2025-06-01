package micro.catalogo;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import micro.catalogo.model.Product;
import micro.catalogo.repository.ProductRepository;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Limpiar y precargar datos de prueba
        productRepository.deleteAll()
                .thenMany(productRepository.saveAll(List.of(
                        new Product("1", "Laptop", "Laptop HP", new BigDecimal("999.99"), 10, "Electrónicos"),
                        new Product("2", "Mouse", "Mouse inalámbrico", new BigDecimal("29.99"), 50, "Periféricos")
                )))
                .blockLast(); // Solo para testing, en producción evitar block()
    }

    @Test
    void retornaListaProductos() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/products", String.class);

        // Verificar status HTTP
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Parsear respuesta JSON
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int productsCount = documentContext.read("$.length()");
        assertThat(productsCount).isEqualTo(2);

        // Verificar algunos campos
        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder("1", "2");

        JSONArray names = documentContext.read("$..name");
        assertThat(names).containsExactlyInAnyOrder("Laptop", "Mouse");
    }

    @Test
    void retornaUnProductoPorId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/products/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        assertThat(id).isEqualTo("1");

        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Laptop");

        BigDecimal price = documentContext.read("$.price", BigDecimal.class);
        assertThat(price).isEqualByComparingTo("999.99");
    }

    @Test
    void noRetornaUnProductoConIdDesconocido() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/api/products/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void creaUnNuevoProducto() {
        Product newProduct = new Product(null, "Teclado", "Teclado mecánico", new BigDecimal("89.99"), 30, "Periféricos");

        ResponseEntity<Product> response = restTemplate
                .postForEntity("/api/products", newProduct, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Product createdProduct = response.getBody();
        assertThat(createdProduct.getId()).isNotNull();
        assertThat(createdProduct.getName()).isEqualTo("Teclado");
        assertThat(createdProduct.getPrice()).isEqualByComparingTo("89.99");
    }

    @Test
    @DirtiesContext
    void actualizaUnProductoExistente() {
        Product updatedProduct = new Product(null, "Laptop Pro", "Laptop actualizada", new BigDecimal("1099.99"), 5, "Electrónicos");
        HttpEntity<Product> request = new HttpEntity<>(updatedProduct);

        ResponseEntity<Product> response = restTemplate
                .exchange("/api/products/1", HttpMethod.PUT, request, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Product product = response.getBody();
        assertThat(product.getId()).isEqualTo("1");
        assertThat(product.getName()).isEqualTo("Laptop Pro");
        assertThat(product.getPrice()).isEqualByComparingTo("1099.99");
    }

    @Test
    @DirtiesContext
    void shouldDeleteAProductById() {
        ResponseEntity<Void> response = restTemplate
                .exchange("/api/products/1", HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .getForEntity("/api/products/1", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}