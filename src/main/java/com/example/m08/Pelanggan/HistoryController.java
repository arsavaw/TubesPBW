package com.example.m08.Pelanggan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HistoryController {

    @GetMapping("/history")
    public String historyView(HttpSession session) {
        // Cek apakah pengguna sudah login
        if (session.getAttribute("username") == null) {
            return "redirect:/login";  // Redirect ke login jika belum login
        }
        
        // Jika sudah login, tampilkan halaman history
        return "history";  // Nama view untuk halaman history (history.html)
    }
}
