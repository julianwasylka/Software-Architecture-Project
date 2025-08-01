package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQPMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;

import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Service
@RequiredArgsConstructor
public class BookEventsRabbitmqPublisherImpl implements BookEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange bookCreatedExchange;
    private final FanoutExchange bookUpdatedExchange;
    private final BookViewAMQPMapper bookViewAMQPMapper;

    @Override
    public BookViewAMQP sendBookCreated(Book book) {
        return sendBookEvent(book, BookEvents.BOOK_CREATED, this.bookCreatedExchange);
    }

    @Override
    public BookViewAMQP sendBookUpdated(Book book) {
        return sendBookEvent(book, BookEvents.BOOK_UPDATED, this.bookUpdatedExchange);
    }


    private BookViewAMQP sendBookEvent(Book book, String bookEventType, FanoutExchange exchange) {
        try {
            System.out.println("--- Sending Book Event " + bookEventType +
                    " to AMQP Broker of book with isbn: " + book.getIsbn() + ". ---");

            BookViewAMQP bookViewAMQP = bookViewAMQPMapper.toBookViewAMQP(book);
            ObjectMapper objectMapper = new ObjectMapper();
            String bookViewAMQPinString = objectMapper.writeValueAsString(bookViewAMQP);
            this.rabbitTemplate.convertAndSend(exchange.getName(), bookEventType, bookViewAMQPinString);

            System.out.println("--- Book Event Sent! ---");
            return bookViewAMQP;
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending book event: '" + ex.getMessage() + "'");
            return null;
        }
    }
}