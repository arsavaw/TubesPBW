package com.example.m08.Pelanggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class PelangganController {
    @Autowired
    private PelangganService pelangganService;

    @GetMapping("/register")
    public String registerView(Pelanggan pelanggan) {
        return "signup";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Pelanggan pelanggan, BindingResult bindingResult) {
        // If there are validation errors, return to signup page
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // Try to register the pelanggan
        try {
            boolean registrationSuccess = pelangganService.register(pelanggan);
            
            if (!registrationSuccess) {
                bindingResult.rejectValue("username_pelanggan", "error.pelanggan", "Registration failed. Username might already exist.");
                return "signup";
            }
            
            return "redirect:/login";
        } catch (Exception e) {
            bindingResult.rejectValue("username_pelanggan", "error.pelanggan", "An error occurred during registration");
            return "signup";
        }
    }
}