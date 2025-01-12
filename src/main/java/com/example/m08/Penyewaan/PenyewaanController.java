package com.example.m08.Penyewaan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class PenyewaanController {
    
    private static final Logger logger = LoggerFactory.getLogger(PenyewaanController.class);
    
    @Autowired
    private PenyewaanService penyewaanService;

    @GetMapping("/rental/active")
    public String viewActiveRentals(Model model, HttpSession session) {
        try {
            Integer userId = (Integer) session.getAttribute("ID_Pelanggan");
            if (userId == null) {
                return "redirect:/login";
            }

            List<Penyewaan> activeRentals = penyewaanService.getActiveRentals(userId);
            model.addAttribute("activeRentals", activeRentals);
            
            logger.info("Found {} active rentals for user {}", activeRentals.size(), userId);
            
            return "rental/active";

        } catch (Exception e) {
            logger.error("Error viewing active rentals: ", e);
            model.addAttribute("error", "Terjadi kesalahan saat memuat daftar penyewaan aktif");
            return "error";
        }
    }

    @PostMapping("/rental/rent/{filmId}")
    public String rentFilm(@PathVariable Integer filmId, 
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("ID_Pelanggan");
            if (userId == null) {
                return "redirect:/login";
            }

            penyewaanService.rentFilm(filmId, userId);
            redirectAttributes.addFlashAttribute("success", "Film berhasil disewa!");
            return "redirect:/rental/active";

        } catch (Exception e) {
            logger.error("Error renting film: ", e);
            redirectAttributes.addFlashAttribute("error", "Gagal menyewa film: " + e.getMessage());
            return "redirect:/home";
        }
    }

    @PostMapping("/rental/return/{penyewaanId}")
    public String returnFilm(@PathVariable Integer penyewaanId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            Integer userId = (Integer) session.getAttribute("ID_Pelanggan");
            if (userId == null) {
                return "redirect:/login";
            }

            penyewaanService.returnFilm(penyewaanId);
            redirectAttributes.addFlashAttribute("success", "Film berhasil dikembalikan!");
            return "redirect:/rental/active";

        } catch (Exception e) {
            logger.error("Error returning film: ", e);
            redirectAttributes.addFlashAttribute("error", "Gagal mengembalikan film: " + e.getMessage());
            return "redirect:/rental/active";
        }
    }
}