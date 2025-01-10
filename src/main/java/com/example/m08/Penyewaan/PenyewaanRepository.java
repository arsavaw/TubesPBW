package com.example.m08.Penyewaan;

import java.util.List;

public interface PenyewaanRepository {
    void save(Penyewaan penyewaan);
    boolean isFilmAvailable(Integer filmId);
    void returnFilm(Integer penyewaanId);
    List<Penyewaan> findActiveRentals(Integer ID_Pelanggan);
    List<Penyewaan> findByUserId(Integer userId);
    List<Penyewaan> findFilteredHistory(Integer userId, String status, String sort);
    List<Penyewaan> findAll();
}