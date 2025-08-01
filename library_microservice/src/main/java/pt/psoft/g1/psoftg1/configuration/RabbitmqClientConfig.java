package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqClientConfig {
    // New exchanges for Book Created
    @Bean
    public FanoutExchange bookCreatedExchange() {
        return new FanoutExchange("Book.Created");
    }

    // New exchanges for Book Updated
    @Bean
    public FanoutExchange bookUpdatedExchange() {
        return new FanoutExchange("Book.Updated");
    }

    @Bean
    public FanoutExchange lendingReturnedExchange() {
        return new FanoutExchange("Lending.Returned");
    }

    @Bean
    public FanoutExchange recommendationCreatedExchange() {
        return new FanoutExchange("Recommendation.Created");
    }

    @Bean
    public FanoutExchange recommendationCreationFailureExchange() {
        return new FanoutExchange("Recommendation.Creation.Failure");
    }

    // Separate queues for each event type
    @Bean
    public Queue bookCreatedQueue() {
        return new AnonymousQueue(); // Queue for Book.Created events
    }

    @Bean
    public Queue bookUpdatedQueue() {
        return new AnonymousQueue(); // Queue for Book.Updated events
    }

    @Bean
    public Queue lendingReturnedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue recommendationCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue recommendationCreationFailureQueue() {
        return new AnonymousQueue();
    }

    // Bind queues to their respective exchanges
    @Bean
    public Binding bindBookCreatedQueue(FanoutExchange bookCreatedExchange, Queue bookCreatedQueue) {
        return BindingBuilder.bind(bookCreatedQueue).to(bookCreatedExchange);
    }

    @Bean
    public Binding bindBookUpdatedQueue(FanoutExchange bookUpdatedExchange, Queue bookUpdatedQueue) {
        return BindingBuilder.bind(bookUpdatedQueue).to(bookUpdatedExchange);
    }

    @Bean
    public Binding bindLendingReturnedQueue(FanoutExchange lendingReturnedExchange, Queue lendingReturnedQueue) {
        return BindingBuilder.bind(lendingReturnedQueue).to(lendingReturnedExchange);
    }

    @Bean
    public Binding bindRecommendationCreatedQueue(FanoutExchange recommendationCreatedExchange, Queue recommendationCreatedQueue) {
        return BindingBuilder.bind(recommendationCreatedQueue).to(recommendationCreatedExchange);
    }

    @Bean
    public Binding bindRecommendationCreationFailureQueue(FanoutExchange recommendationCreationFailureExchange, Queue recommendationCreationFailureQueue) {
        return BindingBuilder.bind(recommendationCreationFailureQueue).to(recommendationCreationFailureExchange);
    }
}
