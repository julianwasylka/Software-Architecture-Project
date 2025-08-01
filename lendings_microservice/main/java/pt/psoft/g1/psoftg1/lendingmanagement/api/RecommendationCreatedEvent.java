package pt.psoft.g1.psoftg1.lendingmanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationCreatedEvent {
    private String comment;
    private Boolean isPositive;
    private String bookIsbn;
    private String lendingNumber;
}