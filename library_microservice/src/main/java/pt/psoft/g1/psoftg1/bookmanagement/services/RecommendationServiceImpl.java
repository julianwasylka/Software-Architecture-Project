package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.bookmanagement.publishers.RecommendationEventsPublisher;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.RecommendationRepository;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationEventsPublisher recommendationEventsPublisher;

    @Override
    @Transactional
    public Recommendation createRecommendation(String comment, Boolean isPositive, Book book, String lendingNumber) {
        Recommendation recommendation = new Recommendation();
        recommendation.setComment(comment);
        recommendation.setIsPositive(isPositive);
        recommendation.setBook(book);
        recommendation.setLendingNumber(lendingNumber);

        try {
            recommendation = recommendationRepository.create(recommendation);
            recommendationEventsPublisher.publishRecommendationCreatedEvent(comment, isPositive, book.getIsbn(), lendingNumber);
            return recommendation;

        } catch (Exception ex) {
            recommendationEventsPublisher.publishCreateRecommendationCreationFailureEvent(lendingNumber);

            System.err.println("Error creating recommendation " + ex.getMessage());
            return null;
        }
    }
}


