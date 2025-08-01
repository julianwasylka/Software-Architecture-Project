package pt.psoft.g1.psoftg1.bookmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.RecommendationService;
import pt.psoft.g1.psoftg1.lendingmanagement.api.RecommendationCreatedEvent;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class BookRabbitmqController {

    private final BookService bookService;
    private final RecommendationService recommendationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "#{bookCreatedQueue.name}")
    public void handleBookCreated(Message message) {
        processBookEvent(message, BookEvents.BOOK_CREATED);
    }

    @RabbitListener(queues = "#{bookUpdatedQueue.name}")
    public void handleBookUpdated(Message message) {
        processBookEvent(message, BookEvents.BOOK_UPDATED);
    }

    private void processBookEvent(Message message, String eventType) {
        try {
            System.out.println("--- Received an event of type: " + eventType + " ---");

            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            BookViewAMQP bookViewAMQP = objectMapper.readValue(json, BookViewAMQP.class);

            if (BookEvents.BOOK_CREATED.equals(eventType)) {
                bookService.create(bookViewAMQP); // Sync new book to local database
            } else if (BookEvents.BOOK_UPDATED.equals(eventType)) {
                bookService.update(bookViewAMQP); // Sync updated book
            }

            System.out.println("--- Event processed with the following info: " + bookViewAMQP);

        } catch (Exception e) {
            System.err.println("Error processing " + eventType + " Event: " + e.getMessage());
        }
    }

    @RabbitListener(queues = "#{recommendationCreatedQueue.name}")
    public void handleRecommendationCreated(Message message) {
        try {
            System.out.println("--- Received an event of type: Recommendation Created ---");

            String json = new String(message.getBody(), StandardCharsets.UTF_8);
            RecommendationCreatedEvent event = objectMapper.readValue(json, RecommendationCreatedEvent.class);
            Book book = bookService.findByIsbn(event.getBookIsbn());
            recommendationService.createRecommendation(event.getComment(), event.getIsPositive(), book, event.getLendingNumber());
            System.out.println("--- Event of recommendation created processed ---");
        } catch (Exception e) {
            System.err.println("Error processing recommendation created event" + e.getMessage());
        }
    }
}