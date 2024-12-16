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
            // Tidak melakukan enkripsi password lagi
            pelangganRepository.save(pelanggan);  // Simpan dengan password plaintext
            return true;
        } catch (Exception e) {
            // Log exception jika ada error
            return false;
        }
    }

    public Pelanggan login(String username, String rawPassword) {
        // Temukan pelanggan berdasarkan username
        Optional<Pelanggan> optionalPelanggan = pelangganRepository.findByUsername(username);
        
        // Jika pelanggan ditemukan dan password cocok, kembalikan pelanggan
        if (optionalPelanggan.isPresent() && optionalPelanggan.get().getPassword().equals(rawPassword)) {
            return optionalPelanggan.get();
        }

        // Jika tidak cocok, return null
        return null;
    }
}
