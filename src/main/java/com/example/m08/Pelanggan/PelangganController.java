package com.example.m08.Pelanggan;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PelangganController {
    @Autowired
    private PelangganService pelangganService;

    @GetMapping("/register")
    public String registerView(Pelanggan pelanggan) {
        return "signup";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute Pelanggan pelanggan,
            BindingResult bindingResult,
            @RequestParam("confirm_password") String confirmPassword) {

        if (!pelanggan.getPassword_pelanggan().equals(confirmPassword)) {
            bindingResult.rejectValue("password_pelanggan", "error.pelanggan", "Password and Confirm Password do not match.");
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        try {
            boolean registrationSuccess = pelangganService.register(pelanggan);

            if (!registrationSuccess) {
                bindingResult.rejectValue("username_pelanggan", "error.pelanggan", "Registration failed. Username might already exist.");
                return "signup";
            }

            return "redirect:/register?success=true";
        } catch (Exception e) {
            bindingResult.rejectValue("username_pelanggan", "error.pelanggan", "An error occurred during registration");
            return "signup";
        }
    }
}
