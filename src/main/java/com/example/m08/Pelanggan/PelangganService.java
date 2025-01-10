package com.example.m08.Pelanggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PelangganService {

    @Autowired
    private PelangganRepository pelangganRepository;

    public boolean register(Pelanggan pelanggan) {
        try {
            Optional<Pelanggan> existingPelanggan = pelangganRepository.findByUsername(pelanggan.getUsername_pelanggan());
            if (existingPelanggan.isPresent()) {
                return false;
            }
            pelangganRepository.save(pelanggan);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Pelanggan login(String username, String password) {
        try {
            Optional<Pelanggan> optionalPelanggan = pelangganRepository.findByUsername(username);
            if (optionalPelanggan.isPresent()) {
                Pelanggan pelanggan = optionalPelanggan.get();
                if (pelanggan.getPassword_pelanggan().equals(password)) {
                    return pelanggan;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
