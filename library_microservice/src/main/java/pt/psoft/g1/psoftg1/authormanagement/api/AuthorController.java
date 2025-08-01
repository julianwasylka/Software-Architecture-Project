package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorViewMapper authorViewMapper;
    private final BookViewMapper bookViewMapper;

    // Create
    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorView> create(@Valid CreateAuthorRequest resource) {
        final var author = authorService.create(resource);

        final var newauthorUri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();

        return ResponseEntity.created(newauthorUri)
                .body(authorViewMapper.toAuthorView(author));
    }

    // Update
    @Operation(summary = "Updates a specific author")
    @PatchMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> partialUpdate(
            @PathVariable("authorNumber")
            @Parameter(description = "The number of the Author to find") final Long authorNumber,
            final WebRequest request,
            @Valid @RequestBody UpdateAuthorRequest resource) {

        Author author = authorService.partialUpdate(authorNumber, resource);

        return ResponseEntity.ok().body(authorViewMapper.toAuthorView(author));
    }

    // Gets
    @Operation(summary = "Know an author’s detail given its author number")
    @GetMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> findByAuthorNumber(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final Long authorNumber) {

        final var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return ResponseEntity.ok().body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Search authors by name")
    @GetMapping
    public ListResponse<AuthorView> findByName(@RequestParam("name") final String name) {
        final var authors = authorService.findByName(name);
        return new ListResponse<>(authorViewMapper.toAuthorView(authors));
    }

    // Know the books of an Author
    @Operation(summary = "Know the books of an author")
    @GetMapping("/{authorNumber}/books")
    public ListResponse<BookView> getBooksByAuthorNumber(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final Long authorNumber) {

        // Checking if author exists with this id
        authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return new ListResponse<>(bookViewMapper.toBookView(authorService.findBooksByAuthorNumber(authorNumber)));
    }

    // Co-authors and their respective books
    @Operation(summary = "Get co-authors and their respective books for a specific author")
    @GetMapping("/{authorNumber}/coauthors")
    public AuthorCoAuthorBooksView getAuthorWithCoAuthors(@PathVariable("authorNumber") Long authorNumber) {
        var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        var coAuthors = authorService.findCoAuthorsByAuthorNumber(authorNumber);
        List<CoAuthorView> coAuthorViews = new ArrayList<>();
        for (Author coAuthor : coAuthors) {
            var books = authorService.findBooksByAuthorNumber(coAuthor.getAuthorNumber());
            var coAuthorView = authorViewMapper.toCoAuthorView(coAuthor, books);
            coAuthorViews.add(coAuthorView);
        }
        return authorViewMapper.toAuthorCoAuthorBooksView(author, coAuthorViews);
    }
}
