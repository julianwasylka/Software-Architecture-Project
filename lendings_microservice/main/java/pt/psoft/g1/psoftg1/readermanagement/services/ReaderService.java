package pt.psoft.g1.psoftg1.readermanagement.services;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;
import java.util.Optional;

public interface ReaderService {
    ReaderDetails create(ReaderDetailsViewAMQP request);
    ReaderDetails update(ReaderDetailsViewAMQP request);
    Optional<ReaderDetails> findByUsername(final String username);
    Optional<ReaderDetails> findByReaderNumber(String readerNumber);
    List<ReaderDetails> findByPhoneNumber(String phoneNumber);
    Iterable<ReaderDetails> findAll();
}
