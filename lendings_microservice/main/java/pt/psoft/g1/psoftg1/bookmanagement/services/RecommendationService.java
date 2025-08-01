package pt.psoft.g1.psoftg1.bookmanagement.services;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Recommendation;

public interface RecommendationService {
    Recommendation createRecommendation(String comment, Boolean isPositive, Book book, String lendingNumber);
}
