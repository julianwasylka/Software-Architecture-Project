package pt.psoft.g1.psoftg1.bookmanagement.publishers;

public interface RecommendationEventsPublisher {
    void publishRecommendationCreatedEvent(String comment, Boolean isPositive, String isbn, String lendingNumber);
    void publishCreateRecommendationCreationFailureEvent(String lendingNumber);
}

