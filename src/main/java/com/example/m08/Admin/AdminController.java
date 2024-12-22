package com.example.m08.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String homePage(HttpSession session, Model model) {
        // Verify user is logged in
        if (session.getAttribute("adminUsername") == null) {
            return "redirect:/login";
        }

        // // Add username to the model to display on home page
        // String username = (String) session.getAttribute("username");
        // model.addAttribute("username", username);

        return "/admin/dashboard_admin";
    }
}
