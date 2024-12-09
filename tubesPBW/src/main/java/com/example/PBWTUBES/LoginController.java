package main.java.com.example.PBWTUBES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import com.example.m08.User.User;
import com.example.m08.User.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "login"; 
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, 
                            @RequestParam String password, 
                            HttpSession session, 
                            Model model) {
        
        User user = userService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Invalid username or password!");
            return "login";  
        }

        
        session.setAttribute("user", user);

        return "redirect:/home"; 
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();  
        return "redirect:/login";  
    }
}
