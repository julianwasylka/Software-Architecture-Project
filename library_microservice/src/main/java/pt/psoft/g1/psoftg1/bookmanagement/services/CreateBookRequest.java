package pt.psoft.g1.psoftg1.bookmanagement.services;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@Schema(description = "A DTO for creating a Book")
public class CreateBookRequest {

    @Setter
    private String description;

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @NotNull
    private List<Long> authors;
}
