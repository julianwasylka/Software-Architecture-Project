package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

public interface GenreService {
    Iterable<Genre> findAll();
    Genre save(Genre genre);
}
