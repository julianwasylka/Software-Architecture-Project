package pt.psoft.g1.psoftg1.readermanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Readers", description = "Endpoints to manage readers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readers")
class ReaderController {
    private final ReaderService readerService;
    private final UserService userService;
    private final ReaderViewMapper readerViewMapper;
//    private final LendingService lendingService;
//    private final LendingViewMapper lendingViewMapper;


    @Operation(summary = "Creates a reader")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReaderView> createReader(@Valid CreateReaderRequest readerRequest) throws ValidationException {
        ReaderDetails readerDetails = readerService.create(readerRequest);

        final var newReaderUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(readerDetails.getReaderNumber())
                .build().toUri();

        return ResponseEntity.created(newReaderUri)
                .body(readerViewMapper.toReaderView(readerDetails));
    }


    @Operation(summary = "Updates a reader")
    @RolesAllowed(Role.READER)
    @PatchMapping
    public ResponseEntity<ReaderView> updateReader(
            @Valid UpdateReaderRequest readerRequest,
            Authentication authentication,
            final WebRequest request) {

        User loggedUser = userService.getAuthenticatedUser(authentication);
        ReaderDetails readerDetails = readerService
                .update(loggedUser.getId(), readerRequest);

        return ResponseEntity.ok().body(readerViewMapper.toReaderView(readerDetails));
    }

    @Operation(summary = "Gets the reader data if authenticated as Reader or all readers if authenticated as Librarian")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            // Use the `array` property instead of `schema`
            array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
    @GetMapping
    public ResponseEntity<?> getData(Authentication authentication) {
        User loggedUser = userService.getAuthenticatedUser(authentication);

        if (!(loggedUser instanceof Librarian)) {
            ReaderDetails readerDetails = readerService.findByUsername(loggedUser.getUsername())
                    .orElseThrow(() -> new NotFoundException(ReaderDetails.class, loggedUser.getUsername()));
            return ResponseEntity.ok().body(readerViewMapper.toReaderView(readerDetails));
        }

        return ResponseEntity.ok().body(readerViewMapper.toReaderView(readerService.findAll()));
    }

    @Operation(summary = "Gets a list of Readers by phoneNumber")
    @GetMapping(params = "phoneNumber")
    public ListResponse<ReaderView> findByPhoneNumber(@RequestParam(name = "phoneNumber", required = false) final String phoneNumber) {
        List<ReaderDetails> readerDetailsList  = readerService.findByPhoneNumber(phoneNumber);

        if(readerDetailsList.isEmpty())
            throw new NotFoundException(ReaderDetails.class, phoneNumber);

        return new ListResponse<>(readerViewMapper.toReaderView(readerDetailsList));
    }

    @RolesAllowed(Role.LIBRARIAN)
    @GetMapping(params = "name")
    public ListResponse<ReaderView> findByReaderName(@RequestParam("name") final String name) {
        List<User> userList = this.userService.findByNameLike(name);
        List<ReaderDetails> readerDetailsList = new ArrayList<>();

        for(User user : userList) {
            Optional<ReaderDetails> readerDetails = this.readerService.findByUsername(user.getUsername());
            if(readerDetails.isPresent()) {
                readerDetailsList.add(readerDetails.get());
            }
        }

        if (readerDetailsList.isEmpty())
            throw new NotFoundException("Could not find reader with name: " + name);

        return new ListResponse<>(readerViewMapper.toReaderView(readerDetailsList));
    }

    @Operation(summary = "Gets reader by number")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
    @GetMapping(value="/{year}/{seq}")
    public ResponseEntity<ReaderView> findByReaderNumber(@PathVariable("year")
                                                              @Parameter(description = "The year of the Reader to find")
                                                              final Integer year,
                                                              @PathVariable("seq")
                                                              @Parameter(description = "The sequencial of the Reader to find")
                                                              final Integer seq) {
        String readerNumber = year+"/"+seq;
        var readerDetails = readerService.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Could not find reader from specified reader number"));

        if (readerDetails == null)
            throw new NotFoundException("Could not find reader from specified reader number");

        return ResponseEntity.ok()
                .body(readerViewMapper.toReaderView(readerDetails));
    }

//    @Operation(summary = "Gets the lendings of this reader by ISBN")
//    @GetMapping(value = "/{year}/{seq}/lendings")
//    public List<LendingView> getReaderLendings(
//            Authentication authentication,
//            @PathVariable("year")
//                @Parameter(description = "The year of the Reader to find")
//                final Integer year,
//            @PathVariable("seq")
//                @Parameter(description = "The sequencial of the Reader to find")
//                final Integer seq,
//            @RequestParam("isbn")
//                @Parameter(description = "The ISBN of the Book to find")
//                final String isbn,
//            @RequestParam(value = "returned", required = false)
//                @Parameter(description = "Filter by returned")
//                final Optional<Boolean> returned)
//    {
//        String urlReaderNumber = year + "/" + seq;
//
//        final var urlReaderDetails = readerService.findByReaderNumber(urlReaderNumber)
//                .orElseThrow(() -> new NotFoundException(Lending.class, urlReaderNumber));
//
//        User loggedUser = userService.getAuthenticatedUser(authentication);
//
//        //if Librarian is logged in, skip ahead
//        if (!(loggedUser instanceof Librarian)) {
//            final var loggedReaderDetails = readerService.findByUsername(loggedUser.getUsername())
//                    .orElseThrow(() -> new NotFoundException(ReaderDetails.class, loggedUser.getUsername()));
//
//            //if logged Reader matches the one associated with the lendings, skip ahead
//            if(!Objects.equals(loggedReaderDetails.getReaderNumber(), urlReaderDetails.getReaderNumber())){
//                throw new AccessDeniedException("Reader does not have permission to view these lendings");
//            }
//        }
//        final var lendings = lendingService.listByReaderNumberAndIsbn(urlReaderNumber, isbn, returned);
//
//        if(lendings.isEmpty())
//            throw new NotFoundException("No lendings found with provided ISBN");
//
//        return lendingViewMapper.toLendingView(lendings);
//    }

//    @GetMapping("/top5")
//    public ListResponse<ReaderView> getTop() {
//        return new ListResponse<>(readerViewMapper.toReaderView(readerService.findTopReaders(5)));
//    }
//
//    @GetMapping("/top5ByGenre")
//    public ListResponse<ReaderCountView> getTop5ReaderByGenre(
//            @RequestParam("genre") String genre,
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
//    {
//        final var books = readerService.findTopByGenre(genre,startDate,endDate);
//
//        if(books.isEmpty())
//            throw new NotFoundException("No lendings found with provided parameters");
//
//        return new ListResponse<>(readerViewMapper.toReaderCountViewList(books));
//    }
//
//    @PostMapping("/search")
//    public ListResponse<ReaderView> searchReaders(
//            @RequestBody final SearchRequest<SearchReadersQuery> request) {
//        final var readerList = readerService.searchReaders(request.getPage(), request.getQuery());
//        return new ListResponse<>(readerViewMapper.toReaderView(readerList));
//    }
}
