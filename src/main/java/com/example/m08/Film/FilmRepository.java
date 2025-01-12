package com.example.m08.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> getFeaturedMovies();
    List<Film> getMyList(Integer ID_Pelanggan);
    List<Film> getLatestMovies();
    List<Film> getRecommendedMovies(Integer ID_Pelanggan);
    List<Film> searchFilms(String genre, String actor, String title);
    Film findById(Integer id);
     List<Film> findAll();
     void update(Film film);
     void deleteById(Integer id);
     void save(Film film);
}