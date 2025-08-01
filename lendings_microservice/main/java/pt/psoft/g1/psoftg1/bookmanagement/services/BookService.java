package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;

/**
 *
 */
public interface BookService {

    Book create(BookViewAMQP bookViewAMQP); // AMQP request

    Book findByIsbn(String isbn);

    Book update(BookViewAMQP bookViewAMQP);

    List<Book> findByGenre(String genre);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorName(String authorName);
}
