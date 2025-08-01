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
    public Queue readerDetailsCreatedQueue() {
        return new AnonymousQueue(); // Queue for ReaderDetails.Created events
    }

    @Bean
    public Queue readerDetailsUpdatedQueue() {
        return new AnonymousQueue(); // Queue for ReaderDetails.Updated events
    }

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
    public Binding bindReaderDetailsCreatedQueue(FanoutExchange readerDetailsCreatedExchange, Queue readerDetailsCreatedQueue) {
        return BindingBuilder.bind(readerDetailsCreatedQueue).to(readerDetailsCreatedExchange);
    }

    @Bean
    public Binding bindReaderDetailsUpdatedQueue(FanoutExchange readerDetailsUpdatedExchange, Queue readerDetailsUpdatedQueue) {
        return BindingBuilder.bind(readerDetailsUpdatedQueue).to(readerDetailsUpdatedExchange);
    }

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
