package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.*;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class UpdateBookRequest {
    @Setter
    private String isbn;

    @Setter
    private String description;

    private String title;

    @Setter
    private Genre genreObj;

    private String genre;

    private List<Long> authors;

    private List<Author> authorObjList;

    public UpdateBookRequest(String isbn, String title, String genre, @NonNull List<Long> authors, String description) {
        this.isbn = isbn;
        this.genre = genre;
        this.title = title;
        this.description = description;
        this.authors = authors;
    }
}
