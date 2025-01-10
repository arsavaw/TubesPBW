package com.example.m08.Film;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Integer ID_Film;
    private String Nama_Film;
    private Integer ID_Genre;
    private Integer Stok;
    private String Foto_Cover;
    private String DeskripsiFilm;
    private Double harga; // Menambahkan harga pada entitas Film

    // Additional fields for display
    private String Nama_Genre;
    private String[] Nama_Actors;  // To store multiple actors
    private List<Integer> actorIds;
}