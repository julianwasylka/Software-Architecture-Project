package pt.psoft.g1.psoftg1.authormanagement.services;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuthorRequest {
    @Size(max = 4096)
    private String bio;

    @Size(max = 150)
    private String name;
}
