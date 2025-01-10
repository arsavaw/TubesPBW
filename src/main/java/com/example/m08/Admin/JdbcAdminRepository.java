package com.example.m08.Admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAdminRepository implements AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Admin> findByUsername(String username) {
        String sql = "SELECT * FROM Admin WHERE username_admin = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Admin(
            rs.getInt("ID_admin"),
            rs.getString("username_admin"),
            rs.getString("password_admin")
        ), username).stream().findFirst();
    }

}
