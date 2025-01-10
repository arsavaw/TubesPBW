package com.example.m08.Repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.m08.Models.*;

@Repository
public class JdbcGenreRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Genre genre) {
        String sql = "INSERT INTO Genre (Nama_Genre) VALUES (?)";
        jdbcTemplate.update(sql, genre.getNama_Genre());
    }

    public List<Genre> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM Genre",
            (rs, rowNum) -> {
                Genre genre = new Genre();
                genre.setID_Genre(rs.getInt("ID_Genre"));
                genre.setNama_Genre(rs.getString("Nama_Genre"));
                return genre;
            });
    }
}