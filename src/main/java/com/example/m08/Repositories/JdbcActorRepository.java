package com.example.m08.Repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.m08.Models.*;

@Repository
public class JdbcActorRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Actor actor) {
        String sql = "INSERT INTO Actor (Nama_Actor) VALUES (?)";
        jdbcTemplate.update(sql, actor.getNama_Actor());
    }

    public List<Actor> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM Actor",
            (rs, rowNum) -> {
                Actor actor = new Actor();
                actor.setID_Actor(rs.getInt("ID_Actor"));
                actor.setNama_Actor(rs.getString("Nama_Actor"));
                return actor;
            }
        );
    }
}