package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ReaderService {
    ReaderDetails create(CreateReaderRequest request);
    ReaderDetails create(ReaderDetailsViewAMQP request);
    ReaderDetails update(Long id, UpdateReaderRequest request);
    ReaderDetails update(ReaderDetailsViewAMQP request);
    Optional<ReaderDetails> findByUsername(final String username);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
    List<ReaderDetails> findByPhoneNumber(String phoneNumber);
    Iterable<ReaderDetails> findAll();

//    List<ReaderDetails> findTopReaders(int minTop);
//    List<ReaderBookCountDTO> findTopByGenre(String genre, LocalDate startDate, LocalDate endDate);
//    List<ReaderDetails> searchReaders(Page page, SearchReadersQuery query);
}
