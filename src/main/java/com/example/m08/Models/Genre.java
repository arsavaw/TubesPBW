package com.example.m08.Models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Genre {
    private Integer ID_Genre;
    
    @NotBlank(message = "Genre name cannot be empty")
    private String Nama_Genre;
}