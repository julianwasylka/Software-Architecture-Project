package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.RecommendationRepository;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;

    @Override
    @Transactional
    public Recommendation createRecommendation(String comment, Boolean isPositive, Book book, String lendingNumber) {
        boolean exists = recommendationRepository.existsByLendingNumber(lendingNumber);
        if (exists)
            throw new ConflictException("A recommendation with the same lendingNumber already exists.");

        Recommendation recommendation = new Recommendation();
        recommendation.setComment(comment);
        recommendation.setIsPositive(isPositive);
        recommendation.setBook(book);
        recommendation.setLendingNumber(lendingNumber);
        return recommendationRepository.create(recommendation);
    }
}


