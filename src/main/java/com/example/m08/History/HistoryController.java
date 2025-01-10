package com.example.m08.History;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.m08.Penyewaan.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {
   
   @Autowired
   private PenyewaanService penyewaanService;

   @GetMapping("/history")
   public String viewHistory(Model model, HttpSession session) {
       // Check if user is logged in
       if (session.getAttribute("username") == null) {
           return "redirect:/login";
       }

       try {
           // Get user ID from session
           Integer userId = (Integer) session.getAttribute("ID_Pelanggan");

           // Get rental history
           List<Penyewaan> rentals = penyewaanService.getRentalHistory(userId);
           model.addAttribute("rentals", rentals);

           // Add counts for different statuses
           long activeCount = rentals.stream()
               .filter(r -> "ACTIVE".equals(r.getStatus()))
               .count();
           
           long returnedCount = rentals.stream()
               .filter(r -> "RETURNED".equals(r.getStatus())) 
               .count();

           long overdueCount = rentals.stream()
               .filter(r -> "OVERDUE".equals(r.getStatus()))
               .count();

           model.addAttribute("activeCount", activeCount);
           model.addAttribute("returnedCount", returnedCount);
           model.addAttribute("overdueCount", overdueCount);
           model.addAttribute("totalCount", rentals.size());

           return "history";

       } catch (Exception e) {
           model.addAttribute("error", "Failed to load rental history");
           return "error";
       }
   }

   @GetMapping("/history/filter")
   @ResponseBody
   public List<Penyewaan> filterHistory(
           @RequestParam(required = false) String status,
           @RequestParam(required = false) String sort,
           @RequestParam(required = false) String dateRange,
           HttpSession session) {
       
       Integer userId = (Integer) session.getAttribute("ID_Pelanggan");
       if (userId == null) {
           return new ArrayList<>();
       }

       return penyewaanService.getFilteredHistory(userId, status, sort, dateRange);
   }

   @GetMapping("/history/export")
    public void exportHistory(HttpServletResponse response, HttpSession session) throws IOException {
        Integer userId = (Integer) session.getAttribute("ID_Pelanggan");
        if (userId == null) {
            return;
        }

        List<Penyewaan> rentals = penyewaanService.getRentalHistory(userId);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"rental_history.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Movie Title,Rental Date,Return Date,Status");
            
            for (Penyewaan rental : rentals) {
                writer.println(String.format("%s,%s,%s,%s",
                    rental.getNama_Film(),
                    rental.getTanggal(),
                    rental.getTanggal_Kembali() != null ? rental.getTanggal_Kembali() : "",
                    rental.getStatus()
                ));
            }
        }
    }

   @PostMapping("/history/return/{rentalId}")
   public String returnFilm(@PathVariable Integer rentalId, 
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {
       
       if (session.getAttribute("username") == null) {
           return "redirect:/login";
       }

       try {
           penyewaanService.returnFilm(rentalId);
           redirectAttributes.addFlashAttribute("success", "Film successfully returned");
       } catch (Exception e) {
           redirectAttributes.addFlashAttribute("error", "Failed to return film: " + e.getMessage());
       }

       return "redirect:/history";
   }
}