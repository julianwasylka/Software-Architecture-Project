package pt.psoft.g1.psoftg1.readermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.publishers.ReaderDetailsEventsPublisher;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepo;
    private final UserRepository userRepo;
    private final ReaderMapper readerMapper;
    private final GenreRepository genreRepo;
    private final ReaderDetailsEventsPublisher readerDetailsEventsPublisher;

    @Override
    public ReaderDetails create(CreateReaderRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent())
            throw new ConflictException("Username already exists!");

        List<String> stringInterestList = request.getInterestList();
        List<Genre> interestList = this.getGenreListFromStringList(stringInterestList);

        int count = readerRepo.getCountFromCurrentYear();
        Reader reader = readerMapper.createReader(request);
        ReaderDetails rd = readerMapper.createReaderDetails(count+1, reader, request, interestList);

        var savedUser = userRepo.save(reader);
        if( savedUser!=null ) {
            var savedReader = readerRepo.save(rd);

            if( savedReader!=null )
                readerDetailsEventsPublisher.sendReaderDetailsCreated(savedReader);
            return savedReader;
        }

        return null;
    }

    @Override
    public ReaderDetails update(final Long id, final UpdateReaderRequest request){
        final ReaderDetails readerDetails = readerRepo.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        List<String> stringInterestList = request.getInterestList();
        List<Genre> interestList = this.getGenreListFromStringList(stringInterestList);

        readerDetails.applyPatch(request, interestList);

        var savedUser = userRepo.save(readerDetails.getReader());

        if( savedUser!=null ) {
            var savedReader = readerRepo.save(readerDetails);

            if( savedReader!=null )
                readerDetailsEventsPublisher.sendReaderDetailsUpdated(savedReader);

            return savedReader;
        }
        return null;
    }


    @Override
    public ReaderDetails create(ReaderDetailsViewAMQP request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent())
            throw new ConflictException("Username already exists!");

        Reader user = Reader.newReader(request.getUsername(), request.getPassword(), request.getFullName());

        var savedUser = userRepo.save(user);

        if (readerRepo.findByReaderNumber(request.getReaderNumber()).isPresent())
            throw new ConflictException("Reader already exists!");

        List<String> stringInterestList = request.getInterestList();
        List<Genre> interestList = this.getGenreListFromStringList(stringInterestList);

        String fullReaderNumber = request.getReaderNumber();
        String[] parts = fullReaderNumber.split("/");
        int number = Integer.parseInt(parts[1]);

        var reader = new ReaderDetails(
                number,
                savedUser,
                request.getBirthDate(),
                request.getPhoneNumber(),
                request.getGdprConsent(),
                request.getMarketingConsent(),
                request.getThirdPartySharingConsent(),
                interestList
        );

        return readerRepo.save(reader);
    }

    @Override
    public ReaderDetails update(ReaderDetailsViewAMQP request) {
        var readerDetails = readerRepo.findByReaderNumber(request.getReaderNumber())
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        Optional<User> userOptional = userRepo.findByUsername(request.getUsername());

        if (userOptional.isEmpty())
            throw new ConflictException("Username doesn't exist!");

        User user = userOptional.get();

        user.setName(request.getFullName());

        userRepo.save(user);

        List<String> stringInterestList = request.getInterestList();
        List<Genre> interestList = this.getGenreListFromStringList(stringInterestList);

        readerDetails.setInterestList(interestList);

        return readerRepo.save(readerDetails);
    }

    @Override
    public Optional<ReaderDetails> findByReaderNumber(String readerNumber) {
        return this.readerRepo.findByReaderNumber(readerNumber);
    }

    @Override
    public List<ReaderDetails> findByPhoneNumber(String phoneNumber) {
        return this.readerRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<ReaderDetails> findByUsername(final String username) {
        return this.readerRepo.findByUsername(username);
    }

    @Override
    public Iterable<ReaderDetails> findAll() {
        return this.readerRepo.findAll();
    }

//    @Override
//    public List<ReaderDetails> findTopReaders(int minTop) {
//        if(minTop < 1) {
//            throw new IllegalArgumentException("Minimum top reader must be greater than 0");
//        }
//
//        Pageable pageableRules = PageRequest.of(0,minTop);
//        Page<ReaderDetails> page = readerRepo.findTopReaders(pageableRules);
//        return page.getContent();
//    }

    private List<Genre> getGenreListFromStringList(List<String> interestList) {
        if(interestList == null) {
            return null;
        }

        if(interestList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Genre> genreList = new ArrayList<>();
        for(String interest : interestList) {
            Optional<Genre> optGenre = genreRepo.findByString(interest);
            if(optGenre.isEmpty()) {
                throw new NotFoundException("Could not find genre with name " + interest);
            }

            genreList.add(optGenre.get());
        }

        return genreList;
    }

//    @Override
//    public List<ReaderDetails> searchReaders(pt.psoft.g1.psoftg1.shared.services.Page page, SearchReadersQuery query) {
//        if (page == null)
//            page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
//
//        if (query == null)
//            query = new SearchReadersQuery("", "","");
//
//        final var list = readerRepo.searchReaderDetails(page, query);
//
//        if(list.isEmpty())
//            throw new NotFoundException("No results match the search query");
//
//        return list;
//    }
//
//    @Override
//    public List<ReaderBookCountDTO> findTopByGenre(String genre, LocalDate startDate, LocalDate endDate){
//        if(startDate.isAfter(endDate)){
//            throw new IllegalArgumentException("Start date cannot be after end date");
//        }
//        Pageable pageableRules = PageRequest.of(0,5);
//        return this.readerRepo.findTopByGenre(pageableRules, genre, startDate, endDate).getContent();
//    }
}
