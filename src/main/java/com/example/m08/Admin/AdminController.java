package com.example.m08.Admin;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.m08.Penyewaan.Penyewaan;
import com.example.m08.Penyewaan.PenyewaanService;
import com.example.m08.Repositories.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.m08.PDFService;
import com.example.m08.Film.*;
import com.example.m08.Laporan.LaporanService;
import com.example.m08.Models.*;

import java.util.Map;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private JdbcGenreRepository genreRepository;
    @Autowired
    private JdbcFilmRepository filmRepository;
    @Autowired
    private JdbcActorRepository actorRepository;
    @Autowired
    private LaporanService laporanService;
    @Autowired
    private PenyewaanService penyewaanService;
    @Autowired
    private PDFService pdfService;

    @GetMapping("/admin")
    public String homePage(HttpSession session, Model model) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("adminUsername");
        model.addAttribute("username", username);
        return "admin/dashboard_admin";
    }

    @GetMapping("/admin/tambah")
    public String tambahPage(HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }
        return "admin/tambah";
    }

    @GetMapping("/admin/add-genre")
    public String addGenrePage(Model model, HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }
        model.addAttribute("genre", new Genre());
        return "admin/add_genre";
    }

    @PostMapping("/admin/add-genre")
    public String addGenre(@Valid @ModelAttribute Genre genre, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/add_genre";
        }
        genreRepository.save(genre);
        model.addAttribute("success", true);
        return "admin/add_genre";
    }

    @GetMapping("/admin/add-film")
    public String addFilmPage(Model model, HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }
        model.addAttribute("film", new Film());
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll()); // Tambahkan ini
        return "admin/add_film";
    }

    @PostMapping("/admin/add-film")
    public String addFilm(@Valid @ModelAttribute Film film,
            BindingResult result,
            @RequestParam("coverFile") MultipartFile coverFile,
            @RequestParam("videoFile") MultipartFile videoFile,
            Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("genres", genreRepository.findAll());
            model.addAttribute("actors", actorRepository.findAll());
            return "admin/add_film";
        }

        // Handle cover file upload
        if (!coverFile.isEmpty()) {
            String uploadDir = "src/main/resources/static/covers/";
            String filename = handleFileUpload(coverFile, uploadDir);
            film.setFoto_Cover("/covers/" + filename);
        }

        // Handle video file upload
        if (!videoFile.isEmpty()) {
            String uploadDir = "src/main/resources/static/videos/";
            String filename = handleFileUpload(videoFile, uploadDir);
            film.setVideo_Path("/videos/" + filename);
        }

        filmRepository.save(film);
        model.addAttribute("success", true);
        model.addAttribute("genres", genreRepository.findAll());
        model.addAttribute("actors", actorRepository.findAll());
        return "admin/add_film";
    }

    // Helper method untuk handle file upload
    private String handleFileUpload(MultipartFile file, String uploadDir) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String baseFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String filename = originalFilename;
        int counter = 1;
        while (Files.exists(Paths.get(uploadDir + filename))) {
            filename = baseFilename + "(" + counter + ")" + fileExtension;
            counter++;
        }

        // Save the file with unique name
        Files.copy(file.getInputStream(), Paths.get(uploadDir + filename));
        return filename;
    }

    @GetMapping("/admin/add-actor")
    public String addActorPage(Model model, HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }
        model.addAttribute("actor", new Actor());
        return "admin/add_actor";
    }

    @PostMapping("/admin/add-actor")
    public String addActor(@Valid @ModelAttribute Actor actor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin/add_actor";
        }
        actorRepository.save(actor);
        model.addAttribute("success", true); // Tambahkan atribut success ke model
        return "admin/add_actor";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/admin/grafik")
    public String showGrafik(Model model, HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }

        // Data untuk grafik penyewaan per bulan
        Map<String, Integer> monthlyRentals = penyewaanService.getMonthlyRentalStats();
        model.addAttribute("monthlyRentals", monthlyRentals);

        // Data untuk grafik film terpopuler
        Map<String, Integer> popularFilms = penyewaanService.getPopularFilms();
        model.addAttribute("popularFilms", popularFilms);

        return "admin/grafik";
    }

    @GetMapping("/admin/laporan")
    public String showLaporan(Model model, HttpSession session) {
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }

        try {
            // Get summary statistics
            Map<String, Object> summary = laporanService.getLaporanSummary();
            model.addAttribute("summary", summary);

            // Get monthly reports
            List<Map<String, Object>> reports = laporanService.getMonthlyReports();
            model.addAttribute("reports", reports);

            // Get top customers
            List<Map<String, Object>> topCustomers = laporanService.getTopCustomers();
            model.addAttribute("topCustomers", topCustomers);

            return "admin/laporan";
        } catch (Exception e) {
            model.addAttribute("error", "Terjadi kesalahan saat memuat laporan");
            return "error";
        }
    }

    @GetMapping("/admin/laporan/download")
    public void downloadLaporanPDF(HttpServletResponse response) throws IOException {
        // Get all rental data
        List<Penyewaan> penyewaans = penyewaanService.getAllPenyewaan();

        // Generate PDF
        ByteArrayInputStream bis = pdfService.generateLaporanPDF(penyewaans);

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=laporan_penyewaan.pdf");

        // Write PDF content to response
        IOUtils.copy(bis, response.getOutputStream());
        response.flushBuffer();
    }
}