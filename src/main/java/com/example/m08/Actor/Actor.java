package com.example.m08.Actor;

import com.example.m08.Film.Film;

import javax.persistence.*;

@Entity
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idActor;

    private String namaActor;

    @ManyToOne
    @JoinColumn(name = "ID_Film", nullable = false)
    private Film film;

    // Getters and Setters
    public Integer getIdActor() {
        return idActor;
    }

    public void setIdActor(Integer idActor) {
        this.idActor = idActor;
    }

    public String getNamaActor() {
        return namaActor;
    }

    public void setNamaActor(String namaActor) {
        this.namaActor = namaActor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
