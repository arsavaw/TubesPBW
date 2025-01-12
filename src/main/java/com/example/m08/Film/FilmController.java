package com.example.m08.Film;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class FilmController {
    @Autowired
    private FilmService filmService;

    private String handleFileUpload(MultipartFile file, String uploadDir) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String baseFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = originalFilename;
        int counter = 1;
        Path filePath = Paths.get(uploadDir, filename);
        
        while (Files.exists(filePath)) {
            filename = baseFilename + "(" + counter + ")" + fileExtension;
            filePath = Paths.get(uploadDir, filename);
            counter++;
        }

        Files.copy(file.getInputStream(), filePath);
        return filename;
    }
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
    public String searchMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String title,
            Model model) {

        model.addAttribute("genre", genre);
        model.addAttribute("actor", actor);
        model.addAttribute("title", title);
        List<Film> results = filmService.searchFilms(genre, actor, title);
        model.addAttribute("movies", results);
        return "search-results";
    }

}
