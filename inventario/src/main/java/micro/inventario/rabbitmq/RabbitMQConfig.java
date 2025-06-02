package micro.inventario.rabbitmq;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange stockExchange() {
        return new TopicExchange("stock.exchange");
    }

    @Bean
    public Queue catalogStockQueue() {
        return new Queue("catalog.stock.queue");
    }

    @Bean
    public Binding stockUpdateBinding() {
        return BindingBuilder.bind(catalogStockQueue())
                .to(stockExchange())
                .with("stock.update");
    }
}
