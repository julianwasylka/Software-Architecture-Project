package pt.psoft.g1.psoftg1.lendingmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Fine;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.FineRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LendingRabbitmqController {
    private final LendingService lendingService;
    private final LendingRepository lendingRepository;
    private final FineRepository fineRepository;
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "#{recommendationCreationFailureQueue.name}")
    public void handleRecommendationCreationFailureEvent(Message message) {
        try {
            System.out.println("--- Received an event of recommendation creation failure ---");

            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            RecommendationCreationFailureEvent event = objectMapper.readValue(json, RecommendationCreationFailureEvent.class);
            lendingService.rollbackLending(event.getLendingNumber());

            System.out.println("--- Event of recommendation creation failure processed and lending rolled back to previous state. ---");

        } catch (Exception ex) {
            System.err.println("Error processing " + ex.getMessage());
        }
    }

    @RabbitListener(queues = "#{lendingReturnedQueue.name}")
    public void handleLendingReturnedEvent(Message message) {
        LendingReturnedEvent event = null;
        try {
            System.out.println("--- Received an event of lending created ---");

            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            event = objectMapper.readValue(json, LendingReturnedEvent.class);
        } catch (Exception e) {
            System.err.println("Error processing lending returned event " + e.getMessage());
        }

        try {
            assert event != null;
            Optional<Lending> lendingOptional = lendingService.findByLendingNumber(event.getLendingNumber());

            if(lendingOptional.isEmpty())
                throw new NotFoundException("Lending number doesn't exist!");

            Lending lending = lendingOptional.get();

            lending.setReturned(event.getComment());
            lendingRepository.save(lending);

            if(lending.getDaysDelayed() > 0){
                final var fine = new Fine(lending);
                fineRepository.save(fine);
            }

            System.out.println("--- Event of lending created processed ---");

        } catch (Exception ex) {
            System.err.println("Error processing lending returned event " + ex.getMessage());
        }
    }
}
