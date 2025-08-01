package pt.psoft.g1.psoftg1.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue suggestionQueue() {
        return new Queue("suggestionQueue", true);
    }

    @Bean
    public Queue userAuthQueue() {
        return new Queue("user.auth.queue");
    }

    @Bean
    public Queue userAuthResponseQueue() {
        return new Queue("user.auth.response.queue");
    }
}
