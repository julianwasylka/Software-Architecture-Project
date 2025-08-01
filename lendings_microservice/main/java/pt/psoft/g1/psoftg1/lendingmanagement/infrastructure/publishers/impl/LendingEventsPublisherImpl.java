package pt.psoft.g1.psoftg1.lendingmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingReturnedEvent;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.SetLendingReturnedRequest;
import pt.psoft.g1.psoftg1.publishers.LendingEventsPublisher;

@Service
@RequiredArgsConstructor
public class LendingEventsPublisherImpl implements LendingEventsPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange lendingReturnedExchange;

    @Override
    public void publishLendingReturnedEvent(Lending lending, SetLendingReturnedRequest request) {
        try {
            System.out.println("--- Sending Lending Returned Event to AMQP Broker of lending with number: "
                    + lending.getLendingNumber() + ". ---");

            LendingReturnedEvent lendingReturnedEvent = new LendingReturnedEvent();
            lendingReturnedEvent.setLendingNumber(lending.getLendingNumber());
            lendingReturnedEvent.setComment(request.getCommentary());
            lendingReturnedEvent.setBookIsbn(lending.getBook().getIsbn());
            lendingReturnedEvent.setIsPositive(request.getIsPositive());

            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(lendingReturnedEvent);
            rabbitTemplate.convertAndSend(lendingReturnedExchange.getName(), "", message);

            System.out.println("--- Lending Returned Event Sent! ---");
        } catch (Exception ex) {
            System.out.println(" [x] Exception sending lending returned event: '" + ex.getMessage() + "'");
        }
    }
}

