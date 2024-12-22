package com.example.m08.Admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin login(String username, String rawPassword) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);
        if (optionalAdmin.isPresent() && optionalAdmin.get().getPassword_admin().equals(rawPassword)) {
            return optionalAdmin.get();
        }
        return null;
    }
}
