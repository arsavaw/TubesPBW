package com.example.m08.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Actor {
    private Integer ID_Actor;

    @NotBlank(message = "Actor name cannot be empty")
    private String Nama_Actor;
}