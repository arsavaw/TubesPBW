package com.example.m08.Pelanggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private PelangganService pelangganService;

    @GetMapping("/login")
    public String loginView(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home"; // Redirect to home if already logged in
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        Model model, 
                        HttpSession session) {
        Pelanggan pelanggan = pelangganService.login(username, password);

        if (pelanggan == null) {
            model.addAttribute("error", "Invalid username or password");
            return "login";  // Stay on the login page if invalid credentials
        }

        // Save username and ID to session
        session.setAttribute("username", pelanggan.getUsername_pelanggan());
        session.setAttribute("ID_Pelanggan", pelanggan.getID_Pelanggan());
        return "redirect:/home";  // Redirect to home if login successful
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("ID_Pelanggan");
        return "redirect:/login";  // Redirect back to login after logout
    }
}