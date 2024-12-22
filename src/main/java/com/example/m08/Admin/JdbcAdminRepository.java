package com.example.m08.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        List<Admin> results = jdbcTemplate.query(sql, this::mapRowToAdmin, username);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    private Admin mapRowToAdmin(ResultSet resultSet, int rowNum) throws SQLException {
        Admin admin = new Admin();
        admin.setID_admin(resultSet.getInt("ID_admin"));
        admin.setUsername_admin(resultSet.getString("username_admin"));
        admin.setPassword_admin(resultSet.getString("password_admin"));
        return admin;
    }
}
