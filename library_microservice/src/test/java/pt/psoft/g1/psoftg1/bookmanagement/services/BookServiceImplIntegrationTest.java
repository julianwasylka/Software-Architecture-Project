package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.BookEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplIntegrationTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookEventsPublisher bookEventsPublisher;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookSuccessfully() {
        // Arrange
        String isbn = "9782826012092";
        String title = "Sample Book";
        String description = "A test book";
        String genreName = "Fiction";
        List<Long> authorIds = List.of(1L);

        Genre genre = new Genre("Fiction");

        Author author = new Author("author", "bio");

        Book book = new Book(isbn, title, description, genre, List.of(author));

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(genreName)).thenReturn(Optional.of(genre));
        when(authorRepository.findByAuthorNumber(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book createdBook = bookService.create(isbn, title, description, genreName, authorIds);

        // Assert
        assertNotNull(createdBook);
        assertEquals(isbn, createdBook.getIsbn());
        assertEquals(title, createdBook.getTitle().toString());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testCreateBookNotFoundExceptionForGenre() {
        // Arrange
        String isbn = "9782826012092";
        String genreName = "NonExistentGenre";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(genreRepository.findByString(genreName)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () ->
                bookService.create(isbn, "Sample Book", "A test book", genreName, Collections.emptyList())
        );
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testFindBookByIsbn() {
        // Arrange
        String isbn = "9782826012092";
        String title = "Sample Book";
        String description = "A test book";
        String genreName = "Fiction";
        List<Long> authorIds = List.of(1L);

        Genre genre = new Genre("Fiction");

        Author author = new Author("author", "bio");

        Book book = new Book(isbn, title, description, genre, List.of(author));

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));

        // Act
        Book foundBook = bookService.findByIsbn(isbn);

        // Assert
        assertNotNull(foundBook);
        assertEquals(isbn, foundBook.getIsbn());
    }

    @Test
    void testFindBookByIsbnNotFoundException() {
        // Arrange
        String isbn = "9782826012092";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> bookService.findByIsbn(isbn));
    }

    @Test
    void testPublishBookEvent() {
        Book book = new Book("9782826012092", "Test Book", "Description", new Genre("Fiction"), List.of(new Author("Some Name", "Some Bio")));

        bookEventsPublisher.sendBookCreated(book);

        verify(bookEventsPublisher, times(1)).sendBookCreated(book);
    }
}
