package com.example.m08.Pelanggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.m08.Admin.Admin;
import com.example.m08.Admin.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private PelangganService pelangganService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String loginView(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home";
        }
        if (session.getAttribute("adminUsername") != null) {
            return "redirect:/admin";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        logger.info("Login attempt for username: {}", username);
        
        try {
            Pelanggan pelanggan = pelangganService.login(username, password);
            if (pelanggan != null) {
                session.setAttribute("username", pelanggan.getUsername_pelanggan());
                session.setAttribute("ID_Pelanggan", pelanggan.getID_Pelanggan());
                logger.info("Successful customer login for: {}", username);
                return "redirect:/home";
            }

            Admin admin = adminService.login(username, password);
            if (admin != null) {
                session.setAttribute("adminUsername", admin.getUsername_admin());
                session.setAttribute("ID_Admin", admin.getID_admin());
                logger.info("Successful admin login for: {}", username);
                return "redirect:/admin";
            }

            logger.warn("Failed login attempt for username: {}", username);
            model.addAttribute("error", "Invalid username or password");
            return "login";
            
        } catch (Exception e) {
            logger.error("Error during login: ", e);
            model.addAttribute("error", "An error occurred during login");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}