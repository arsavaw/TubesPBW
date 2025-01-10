package com.example.m08.Penyewaan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcPenyewaan implements PenyewaanRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    @Transactional
    public void save(Penyewaan penyewaan) {
        // Kurangi stok film
        String updateStockSql = "UPDATE Film SET Stok = Stok - 1 WHERE ID_Film = ? AND Stok > 0";
        int updated = jdbcTemplate.update(updateStockSql, penyewaan.getID_Film());
        
        if (updated == 0) {
            throw new RuntimeException("Film tidak tersedia");
        }

        // Simpan data penyewaan
        String sql = "INSERT INTO Penyewaan (Tanggal, ID_Film, ID_Pelanggan, status) VALUES (CURRENT_DATE, ?, ?, 'ACTIVE')";
        jdbcTemplate.update(sql,
            penyewaan.getID_Film(),
            penyewaan.getID_Pelanggan()
        );
    }
    
    @Override
    public boolean isFilmAvailable(Integer filmId) {
        String sql = "SELECT Stok FROM Film WHERE ID_Film = ?";
        Integer stok = jdbcTemplate.queryForObject(sql, Integer.class, filmId);
        return stok != null && stok > 0;
    }

    @Override
    @Transactional
    public void returnFilm(Integer penyewaanId) {
        // Dapatkan ID_Film dari penyewaan
        String getFilmIdSql = "SELECT ID_Film FROM Penyewaan WHERE ID_Penyewaan = ?";
        Integer filmId = jdbcTemplate.queryForObject(getFilmIdSql, Integer.class, penyewaanId);

        if (filmId != null) {
            // Update status penyewaan
            String updatePenyewaanSql = "UPDATE Penyewaan SET status = 'RETURNED', Tanggal_Kembali = CURRENT_DATE WHERE ID_Penyewaan = ?";
            jdbcTemplate.update(updatePenyewaanSql, penyewaanId);

            // Tambah stok film
            String updateStockSql = "UPDATE Film SET Stok = Stok + 1 WHERE ID_Film = ?";
            jdbcTemplate.update(updateStockSql, filmId);
        } else {
            throw new RuntimeException("Data penyewaan tidak ditemukan");
        }
    }

    @Override
    public List<Penyewaan> findActiveRentals(Integer ID_Pelanggan) {
        String sql = """
            SELECT p.*, f.Nama_Film, f.Foto_Cover, f.DeskripsiFilm, g.Nama_Genre
            FROM Penyewaan p 
            JOIN Film f ON p.ID_Film = f.ID_Film 
            JOIN Genre g ON f.ID_Genre = g.ID_Genre
            WHERE p.ID_Pelanggan = ? AND p.status = 'ACTIVE'
            ORDER BY p.Tanggal DESC
            """;
        return jdbcTemplate.query(sql, this::mapRowToPenyewaan, ID_Pelanggan);
    }

    public List<Penyewaan> findByUserId(Integer userId) {
        String sql = """
            SELECT p.*, f.Nama_Film, f.Foto_Cover, f.DeskripsiFilm, g.Nama_Genre
            FROM Penyewaan p
            JOIN Film f ON p.ID_Film = f.ID_Film
            JOIN Genre g ON f.ID_Genre = g.ID_Genre
            WHERE p.ID_Pelanggan = ?
            ORDER BY p.Tanggal DESC
            """;
        return jdbcTemplate.query(sql, this::mapRowToPenyewaan, userId);
    }

    public List<Penyewaan> findFilteredHistory(Integer userId, String status, String sort) {
        List<Object> params = new ArrayList<>();
        params.add(userId);

        StringBuilder sql = new StringBuilder("""
            SELECT p.*, f.Nama_Film, f.Foto_Cover, f.DeskripsiFilm, g.Nama_Genre
            FROM Penyewaan p
            JOIN Film f ON p.ID_Film = f.ID_Film
            JOIN Genre g ON f.ID_Genre = g.ID_Genre
            WHERE p.ID_Pelanggan = ?
        """);

        if (status != null && !status.equals("all")) {
            sql.append(" AND p.status = ?");
            params.add(status);
        }

        if ("date-desc".equals(sort)) {
            sql.append(" ORDER BY p.Tanggal DESC");
        } else {
            sql.append(" ORDER BY p.Tanggal ASC");
        }

        return jdbcTemplate.query(sql.toString(), params.toArray(), this::mapRowToPenyewaan);
    }

    
    private Penyewaan mapRowToPenyewaan(ResultSet rs, int rowNum) throws SQLException {
        Penyewaan penyewaan = new Penyewaan();
        penyewaan.setID_Penyewaan(rs.getInt("ID_Penyewaan"));
        penyewaan.setTanggal(rs.getDate("Tanggal"));
        penyewaan.setID_Film(rs.getInt("ID_Film"));
        penyewaan.setID_Pelanggan(rs.getInt("ID_Pelanggan"));
        penyewaan.setTanggal_Kembali(rs.getDate("Tanggal_Kembali"));
        penyewaan.setStatus(rs.getString("status"));
        penyewaan.setNama_Film(rs.getString("Nama_Film"));
        penyewaan.setFoto_Cover(rs.getString("Foto_Cover"));
        penyewaan.setDeskripsiFilm(rs.getString("DeskripsiFilm"));
        return penyewaan;
    }

    @Override
public List<Penyewaan> findAll() {
    String sql = """
        SELECT p.*, f.Nama_Film, f.Foto_Cover, f.DeskripsiFilm, g.Nama_Genre
        FROM Penyewaan p
        JOIN Film f ON p.ID_Film = f.ID_Film
        JOIN Genre g ON f.ID_Genre = g.ID_Genre
        ORDER BY p.Tanggal DESC
        """;
    return jdbcTemplate.query(sql, this::mapRowToPenyewaan);
}
}