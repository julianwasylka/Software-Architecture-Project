package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.RecommendationCreatedEvent;
import pt.psoft.g1.psoftg1.bookmanagement.api.RecommendationCreationFailureEvent;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.RecommendationEventsPublisher;

@Service
@RequiredArgsConstructor
public class RecommendationEventsPublisherImpl implements RecommendationEventsPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange recommendationCreatedExchange;
    private final FanoutExchange recommendationCreationFailureExchange;

    @Override
    public void publishRecommendationCreatedEvent(String comment, Boolean isPositive, String isbn, String lendingNumber) {
        System.out.println("--- Sending Recommendation Created Event to AMQP Broker of book with ISBN: "
                + isbn + ". ---");
        try {
            RecommendationCreatedEvent event = new RecommendationCreatedEvent(comment, isPositive, isbn, lendingNumber);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(recommendationCreatedExchange.getName(), "", message);

            System.out.println("--- Recommendation Created Event Sent! ---");
        } catch (Exception e) {
            System.out.println(" [x] Exception sending recommendation created event: " + e.getMessage());
        }
    }

    @Override
    public void publishCreateRecommendationCreationFailureEvent(String lendingNumber) {
        try {
            System.out.println("--- Sending Recommendation Creation Failed Event to AMQP Broker of lending with number: "
                    + lendingNumber + ". ---");

            RecommendationCreationFailureEvent event = new RecommendationCreationFailureEvent(lendingNumber);
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(recommendationCreationFailureExchange.getName(), "", message);

            System.out.println("--- Recommendation Creation Failed Event Sent! ---");
        } catch (Exception e) {
            System.out.println(" [x] Exception sending recommendation creation failure event: " + e.getMessage());
        }
    }
}
