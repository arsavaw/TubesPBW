package com.example.m08.Pelanggan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPelangganRepository implements PelangganRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Pelanggan pelanggan) throws Exception {
        String sql = "INSERT INTO Pelanggan (username_pelanggan, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, pelanggan.getUsername_pelanggan(), pelanggan.getPassword());
    }

    @Override
    public Optional<Pelanggan> findByUsername(String username) {
        String sql = "SELECT * FROM Pelanggan WHERE username_pelanggan = ?";
        List<Pelanggan> results = jdbcTemplate.query(sql, this::mapRowToPelanggan, username);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    private Pelanggan mapRowToPelanggan(ResultSet resultSet, int rowNum) throws SQLException {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setID_Pelanggan(resultSet.getInt("ID_Pelanggan"));
        pelanggan.setUsername_pelanggan(resultSet.getString("username_pelanggan"));
        pelanggan.setPassword(resultSet.getString("password_pelanggan"));
        return pelanggan;
    }
}