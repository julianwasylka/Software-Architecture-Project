package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface BookRepository {

    List<Book> findByGenre(@Param("genre") String genre);

    List<Book> findByTitle(@Param("title") String title);

    List<Book> findByAuthorName(@Param("authorName") String authorName);

    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    List<Book> findBooksByAuthorNumber(Long authorNumber);

    Book save(Book book);
}
