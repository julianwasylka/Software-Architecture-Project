package pt.psoft.g1.psoftg1.publishers;

import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.lendingmanagement.services.SetLendingReturnedRequest;

public interface LendingEventsPublisher {
    void publishLendingReturnedEvent(Lending lending, SetLendingReturnedRequest request);
}
