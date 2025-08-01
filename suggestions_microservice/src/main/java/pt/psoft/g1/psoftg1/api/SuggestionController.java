package pt.psoft.g1.psoftg1.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.psoft.g1.psoftg1.model.Role;
import pt.psoft.g1.psoftg1.model.SuggestedBook;
import pt.psoft.g1.psoftg1.services.SuggestionService;

@Tag(name = "Suggestions", description = "Endpoints to suggest books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suggestions")
public class SuggestionController {
    private final SuggestionService service;
    private final UserServiceClient userServiceClient;

    @Operation(summary = "Suggests a new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> suggestBook(@RequestBody SuggestedBook request, Authentication authentication) {

        String rawName = authentication.getName();
        String username = rawName.split(",")[1]; //else it returned "id,email", we needed only email
        String name = userServiceClient.getAuthenticatedUserName(username);

        SuggestedBook suggestedBook = new SuggestedBook();
        suggestedBook.setIsbn(request.getIsbn());
        suggestedBook.setSuggestedBy(name);
        service.suggestBook(suggestedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book suggested successfully");
    }
}

