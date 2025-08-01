package pt.psoft.g1.psoftg1.readermanagement.publishers;

import pt.psoft.g1.psoftg1.readermanagement.api.ReaderDetailsViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

public interface ReaderDetailsEventsPublisher {
    ReaderDetailsViewAMQP sendReaderDetailsCreated(ReaderDetails readerDetails);
    ReaderDetailsViewAMQP sendReaderDetailsUpdated(ReaderDetails readerDetails);
}
