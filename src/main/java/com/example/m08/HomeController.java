package com.example.m08;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        // Verify user is logged in
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        return "home";
    }
}