package com.example.m08.Pelanggan;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PelangganService {

    @Autowired
    private PelangganRepository pelangganRepository;

    public boolean register(Pelanggan pelanggan) {
        try {
            // Simpan data pelanggan dengan password plaintext
            pelangganRepository.save(pelanggan);
            return true;
        } catch (Exception e) {
            // Tangani jika ada error
            return false;
        }
    }

    public Pelanggan login(String username, String rawPassword) {
        Optional<Pelanggan> optionalPelanggan = pelangganRepository.findByUsername(username);
        if (optionalPelanggan.isPresent() && optionalPelanggan.get().getPassword_pelanggan().equals(rawPassword)) {
            return optionalPelanggan.get();
        }
        return null;
    }
}
