package com.example.m08.Penyewaan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.LinkedHashMap;

@Service
public class PenyewaanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PenyewaanRepository penyewaanRepository;
    
    public boolean isFilmAvailable(Integer filmId) {
        return penyewaanRepository.isFilmAvailable(filmId);
    }

    @Transactional
    public void rentFilm(Integer filmId, Integer userId) {
        Penyewaan penyewaan = new Penyewaan();
        penyewaan.setID_Film(filmId);
        penyewaan.setID_Pelanggan(userId);
        penyewaan.setTanggal(new Date());
        penyewaan.setStatus("ACTIVE");
        
        penyewaanRepository.save(penyewaan);
    }

    @Transactional
    public void returnFilm(Integer penyewaanId) {
        penyewaanRepository.returnFilm(penyewaanId);
    }
    
    public List<Penyewaan> getActiveRentals(Integer userId) {
        return penyewaanRepository.findActiveRentals(userId);
    }

    public Map<String, Integer> getMonthlyRentalStats() {
        String sql = """
            SELECT TO_CHAR(Tanggal, 'Month YYYY') as month,
                   COUNT(*) as count
            FROM Penyewaan
            WHERE Tanggal >= CURRENT_DATE - INTERVAL '12 months'
            GROUP BY TO_CHAR(Tanggal, 'Month YYYY'), 
                     EXTRACT(YEAR FROM Tanggal),
                     EXTRACT(MONTH FROM Tanggal)
            ORDER BY EXTRACT(YEAR FROM Tanggal),
                     EXTRACT(MONTH FROM Tanggal)
            """;
        
        Map<String, Integer> stats = new LinkedHashMap<>();
        jdbcTemplate.query(sql, (rs) -> {
            stats.put(rs.getString("month"), rs.getInt("count"));
        });
        return stats;
    }

    public Map<String, Integer> getPopularFilms() {
        String sql = """
            SELECT f.Nama_Film, COUNT(*) as rental_count
            FROM Penyewaan p
            JOIN Film f ON p.ID_Film = f.ID_Film
            GROUP BY f.ID_Film, f.Nama_Film
            ORDER BY rental_count DESC
            LIMIT 10
            """;
        
        Map<String, Integer> stats = new LinkedHashMap<>();
        jdbcTemplate.query(sql, (rs) -> {
            stats.put(rs.getString("Nama_Film"), rs.getInt("rental_count"));
        });
        return stats;
    }

    public List<Penyewaan> getRentalHistory(Integer userId) {
        return penyewaanRepository.findByUserId(userId);
    }

    public List<Penyewaan> getFilteredHistory(Integer userId, String status, String sort, String dateRange) {
        return penyewaanRepository.findFilteredHistory(userId, status, sort);
    }

    public List<Penyewaan> getAllPenyewaan() {
        return penyewaanRepository.findAll();
    }
}