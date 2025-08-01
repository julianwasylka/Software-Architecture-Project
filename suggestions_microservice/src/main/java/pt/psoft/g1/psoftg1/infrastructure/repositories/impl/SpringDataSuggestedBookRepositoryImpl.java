package pt.psoft.g1.psoftg1.infrastructure.repositories.impl;

import org.springframework.data.repository.CrudRepository;
import pt.psoft.g1.psoftg1.model.SuggestedBook;
import pt.psoft.g1.psoftg1.repositories.SuggestedBookRepository;

public interface SpringDataSuggestedBookRepositoryImpl extends SuggestedBookRepository, CrudRepository<SuggestedBook, Long> {

}
