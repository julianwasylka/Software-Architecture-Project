package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataBookRepository extends BookRepository, CrudRepository<Book, Isbn> {

    @Query("SELECT b " + "FROM Book b " + "WHERE b.isbn.isbn = :isbn")
    Optional<Book> findByIsbn(@Param("isbn") String isbn);

    @Override
    @Query("SELECT b " + "FROM Book b " + "WHERE b.genre.genre LIKE %:genre%")
    List<Book> findByGenre(@Param("genre") String genre);

    @Override
    @Query("SELECT b FROM Book b WHERE b.title.title LIKE %:title%")
    List<Book> findByTitle(@Param("title") String title);

    @Override
    @Query(value = "SELECT b.* " + "FROM Book b " + "JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK "
            + "JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER "
            + "WHERE a.NAME LIKE :authorName", nativeQuery = true)
    List<Book> findByAuthorName(@Param("authorName") String authorName);

    @Override
    @Query(value = "SELECT b.* " + "FROM Book b " + "JOIN BOOK_AUTHORS on b.pk = BOOK_AUTHORS.BOOK_PK "
            + "JOIN AUTHOR a on BOOK_AUTHORS.AUTHORS_AUTHOR_NUMBER = a.AUTHOR_NUMBER "
            + "WHERE a.AUTHOR_NUMBER = :authorNumber ", nativeQuery = true)
    List<Book> findBooksByAuthorNumber(Long authorNumber);

}