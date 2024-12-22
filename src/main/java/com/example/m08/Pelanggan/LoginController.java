package com.example.m08.Pelanggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.m08.Admin.Admin;
import com.example.m08.Admin.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
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
        Pelanggan pelanggan = pelangganService.login(username, password);
        if (pelanggan != null) {
            session.setAttribute("username", pelanggan.getUsername_pelanggan());
            session.setAttribute("ID_Pelanggan", pelanggan.getID_Pelanggan());
            return "redirect:/home";
        }

        Admin admin = adminService.login(username, password);
        if (admin != null) {
            session.setAttribute("adminUsername", admin.getUsername_admin());
            session.setAttribute("ID_Admin", admin.getID_admin());
            return "redirect:/admin";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login"; // Tetap di halaman login jika kredensial salah
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username");
        session.removeAttribute("ID_Pelanggan");
        session.removeAttribute("adminUsername");
        session.removeAttribute("ID_Admin");
        return "redirect:/login";
    }
}