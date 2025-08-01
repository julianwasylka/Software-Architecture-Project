package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.RecommendationRepository;

@Repository
public class SpringDataRecommendationRepositoryImpl implements RecommendationRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Recommendation create(Recommendation recommendation) {
        entityManager.persist(recommendation);
        return recommendation;
    }

    @Override
    public boolean existsByLendingNumber(String lendingNumber) {
        String query = "SELECT COUNT(r) FROM Recommendation r WHERE r.lendingNumber = :lendingNumber";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("lendingNumber", lendingNumber)
                .getSingleResult();
        return count > 0;
    }

}
