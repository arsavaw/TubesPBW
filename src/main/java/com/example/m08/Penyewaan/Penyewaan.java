package com.example.m08.Penyewaan;

import java.util.Date;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penyewaan {
    private Integer ID_Penyewaan;
    private Date Tanggal;
    private Integer ID_Film;
    private Integer ID_Pelanggan;
    private Date Tanggal_Kembali;
    private String status;
    
    private String Nama_Film;
    private String Foto_Cover;
    private String DeskripsiFilm;
    private String Nama_Genre;

    private double harga;
}