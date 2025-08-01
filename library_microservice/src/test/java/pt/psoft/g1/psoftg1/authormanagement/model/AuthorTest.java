package pt.psoft.g1.psoftg1.authormanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio);

    @BeforeEach
    void setUp() {
    }

    @Test
    void ensureNameNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio));
    }

    @Test
    void ensureBioNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio);
        assertNotNull(author);
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio);
        Author author = new Author(request.getName(), request.getBio());
        assertNotNull(author);
    }

}
