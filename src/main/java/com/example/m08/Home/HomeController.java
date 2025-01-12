package com.example.m08.Home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import com.example.m08.Film.FilmService;
import com.example.m08.Film.Film;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private FilmService filmService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }

        try {
            List<Film> featuredMovies = filmService.getFeaturedMovies();
            model.addAttribute("featuredMovies", featuredMovies);

            List<Film> availableFilms = filmService.getLatestMovies();
            model.addAttribute("availableFilms", availableFilms);

            List<Film> latestMovies = filmService.getLatestMovies();
            logger.debug("Latest movies: {}", latestMovies);
            model.addAttribute("latestMovies", latestMovies);

            return "home";
        } catch (Exception e) {
            logger.error("Error loading home page: ", e);
            model.addAttribute("error", "Error loading movies");
            return "error";
        }
    }
    @GetMapping("/about")
    public String showAbout(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        return "about";
    }
}