package com.example.m08.Admin;

import java.util.Optional;

public interface AdminRepository {
    Optional<Admin> findByUsername(String username);
}