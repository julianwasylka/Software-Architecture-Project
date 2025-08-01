package pt.psoft.g1.psoftg1.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.model.SuggestedBook;
import pt.psoft.g1.psoftg1.repositories.SuggestedBookRepository;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestedBookRepository suggestedBookRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public SuggestedBook suggestBook(SuggestedBook resource) {
        SuggestedBook suggestedBook = new SuggestedBook();
        suggestedBook.setIsbn(resource.getIsbn());
        suggestedBook.setSuggestedBy(resource.getSuggestedBy());

        try {
            suggestedBook = suggestedBookRepository.save(suggestedBook);
            rabbitTemplate.convertAndSend("suggestionQueue", suggestedBook);

            System.out.println("Suggestion added: " + suggestedBook.getIsbn() + " by " + suggestedBook.getSuggestedBy());
            return suggestedBook;
        } catch (Exception e) {
            System.err.println("Error creating suggested book " + e.getMessage());
            return null;
        }
    };
}
