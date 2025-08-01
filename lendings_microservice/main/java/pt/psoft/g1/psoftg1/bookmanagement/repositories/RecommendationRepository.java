package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import pt.psoft.g1.psoftg1.bookmanagement.model.Recommendation;

public interface RecommendationRepository {
    Recommendation create(Recommendation recommendation);
    boolean existsByLendingNumber(String lendingNumber);
}
