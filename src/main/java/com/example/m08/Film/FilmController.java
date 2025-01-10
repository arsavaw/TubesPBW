package com.example.m08.Film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import com.example.m08.Film.FilmService;
import com.example.m08.Film.Film;
import java.util.List;

@Controller
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping("/movie/{filmId}")
    public String getMovieDetails(@PathVariable("filmId") Integer filmId, Model model) {
        Film film = filmService.findById(filmId);
        if (film == null) {
            return "redirect:/home";
        }

        model.addAttribute("movie", film);
        return "movie-details";
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Film>> searchMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String title) {
        
        List<Film> results = filmService.searchFilms(genre, actor, title);
        return ResponseEntity.ok(results);
    }
}
