package pt.psoft.g1.psoftg1.readermanagement.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.api.MapperInterface;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReaderDetailsViewAMQPMapper extends MapperInterface {

    @Mapping(target = "interestList", expression = "java(mapGenres(readerDetails.getInterestList()))")
    @Mapping(target = "username", expression = "java(readerDetails != null && readerDetails.getReader() != null ? readerDetails.getReader().getUsername() : null)")
    @Mapping(target = "password", expression = "java(readerDetails != null && readerDetails.getReader() != null ? readerDetails.getReader().getPassword() : null)")
    @Mapping(target = "fullName", expression = "java(readerDetails != null && readerDetails.getReader() != null ? readerDetails.getReader().getName().toString() : null)")
    @Mapping(target = "birthDate", expression = "java(readerDetails.getBirthDate() != null ? readerDetails.getBirthDate().getBirthDate().format(java.time.format.DateTimeFormatter.ofPattern(\"yyyy-MM-dd\")) : null)")
    public abstract ReaderDetailsViewAMQP toReaderDetailsViewAMQP(ReaderDetails readerDetails);

    public abstract List<ReaderDetailsViewAMQP> toReaderDetailsViewAMQP(List<ReaderDetails> readerDetailsList);

    protected List<String> mapGenres(List<Genre> genres) {
        if (genres == null)
            return Collections.emptyList();

        return genres.stream().map(Genre::getGenre).toList();
    }
}
