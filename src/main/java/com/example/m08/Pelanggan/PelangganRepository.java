package com.example.m08.Pelanggan;

import java.util.Optional;

public interface PelangganRepository {
    void save(Pelanggan pelanggan) throws Exception;
    Optional<Pelanggan> findByUsername(String username);
}