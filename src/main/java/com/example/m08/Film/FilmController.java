package com.example.m08.Film;

import com.example.m08.Genre.Genre;
import com.example.m08.Film.FilmService;
import com.example.m08.Film.FilmRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @PostMapping("/films")
    public ResponseEntity<?> addFilm(@RequestBody FilmRequest filmRequest) {
        try {
            Film film = filmService.addFilm(filmRequest.getNamaFilm(), filmRequest.getIdGenre(), filmRequest.getActors());
            return ResponseEntity.ok(film);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getGenres() {
        return ResponseEntity.ok(filmService.getAllGenres());
    }
}
