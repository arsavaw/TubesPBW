package com.example.m08.Film;

import com.example.m08.Actor.Actor;
import com.example.m08.Actor.ActorRepository;
import com.example.m08.Genre.Genre;
import com.example.m08.Genre.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ActorRepository actorRepository;

    public Film addFilm(String namaFilm, Integer idGenre, List<String> actors) {
        Genre genre = genreRepository.findById(idGenre)
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        Film film = new Film();
        film.setNamaFilm(namaFilm);
        film.setGenre(genre);

        Film savedFilm = filmRepository.save(film);

        for (String actorName : actors) {
            Actor actor = new Actor();
            actor.setNamaActor(actorName);
            actor.setFilm(savedFilm);
            actorRepository.save(actor);
        }

        return savedFilm;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
