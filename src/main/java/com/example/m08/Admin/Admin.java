package com.example.m08.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Integer ID_admin;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 30 characters")
    private String username_admin;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, max = 60, message = "Password must be between 4 and 60 characters")
    private String password_admin;
}