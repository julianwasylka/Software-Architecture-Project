package pt.psoft.g1.psoftg1.lendingmanagement.services;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.List;
import java.util.Optional;

public interface LendingService {
    Optional<Lending> findByLendingNumber(String lendingNumber);
    List<Lending> listByReaderNumberAndIsbn(String readerNumber, String isbn, Optional<Boolean> returned);
    Lending create(CreateLendingRequest resource); //No ID passed, as it is auto generated
    Lending setReturned(String id, SetLendingReturnedRequest resource);
    Double getAverageDuration();
    List<Lending> getOverdue(Page page);
    Double getAvgLendingDurationByIsbn(String isbn);
    List<Lending> searchLendings(Page page, SearchLendingQuery request);
    void rollbackLending(String lendingNumber);
}
