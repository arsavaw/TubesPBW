package com.example.m08.Film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilmService {
    
    @Autowired
    private FilmRepository filmRepository;
    
    public List<Film> getFeaturedMovies() {
        return filmRepository.getFeaturedMovies();
    }
    
    public List<Film> getMyList(Integer ID_Pelanggan) {
        return filmRepository.getMyList(ID_Pelanggan);
    }
    
    public List<Film> getLatestMovies() {
        return filmRepository.getLatestMovies();
    }
    
    public List<Film> getRecommendedMovies(Integer ID_Pelanggan) {
        return filmRepository.getRecommendedMovies(ID_Pelanggan);
    }
    
    public List<Film> searchFilms(String genre, String actor, String title) {
        return filmRepository.searchFilms(genre, actor, title);
    }
    
    public Film findById(Integer id) {
        return filmRepository.findById(id);
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    public void updateFilm(Integer id, Film film) {
        film.setID_Film(id);
        filmRepository.update(film);
    }

    public void deleteFilm(Integer id) {
        filmRepository.deleteById(id);
    }
    
}