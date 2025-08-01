package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqClientConfig {
    // New exchanges for ReaderDetails Created
    @Bean
    public FanoutExchange readerDetailsCreatedExchange() {
        return new FanoutExchange("ReaderDetails.Created");
    }

    // New exchanges for ReaderDetails Updated
    @Bean
    public FanoutExchange readerDetailsUpdatedExchange() {
        return new FanoutExchange("ReaderDetails.Updated");
    }

    // Separate queues for each event type
    @Bean
    public Queue readerDetailsCreatedQueue() {
        return new AnonymousQueue(); // Queue for ReaderDetails.Created events
    }

    @Bean
    public Queue readerDetailsUpdatedQueue() {
        return new AnonymousQueue(); // Queue for ReaderDetails.Updated events
    }

    // Bind queues to their respective exchanges
    @Bean
    public Binding bindReaderDetailsCreatedQueue(FanoutExchange readerDetailsCreatedExchange, Queue readerDetailsCreatedQueue) {
        return BindingBuilder.bind(readerDetailsCreatedQueue).to(readerDetailsCreatedExchange);
    }

    @Bean
    public Binding bindReaderDetailsUpdatedQueue(FanoutExchange readerDetailsUpdatedExchange, Queue readerDetailsUpdatedQueue) {
        return BindingBuilder.bind(readerDetailsUpdatedQueue).to(readerDetailsUpdatedExchange);
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
