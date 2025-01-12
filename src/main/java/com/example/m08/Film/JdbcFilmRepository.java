package com.example.m08.Film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.m08.Models.Actor;

import java.util.*;

@Repository
public class JdbcFilmRepository implements FilmRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFeaturedMovies() {
        String sql = """
                SELECT f.*, g.Nama_Genre
                FROM Film f
                JOIN Genre g ON f.ID_Genre = g.ID_Genre
                WHERE f.Stok > 0
                ORDER BY RANDOM()
                LIMIT 6
                """;
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getMyList(Integer ID_Pelanggan) {
        String checkSql = "SELECT COUNT(*) FROM Penyewaan WHERE ID_Pelanggan = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, ID_Pelanggan);

        if (count > 0) {
            String sql = """
                    SELECT DISTINCT f.*, g.Nama_Genre, p.Tanggal
                    FROM Film f
                    JOIN Genre g ON f.ID_Genre = g.ID_Genre
                    JOIN Penyewaan p ON f.ID_Film = p.ID_Film
                    WHERE p.ID_Pelanggan = ?
                    ORDER BY p.Tanggal DESC
                    LIMIT 10
                    """;
            return jdbcTemplate.query(sql, this::mapRowToFilm, ID_Pelanggan);
        } else {
            String sql = """
                    SELECT f.*, g.Nama_Genre
                    FROM Film f
                    JOIN Genre g ON f.ID_Genre = g.ID_Genre
                    WHERE f.Stok > 0
                    ORDER BY RANDOM()
                    LIMIT 10
                    """;
            return jdbcTemplate.query(sql, this::mapRowToFilm);
        }
    }

    @Override
    public List<Film> getLatestMovies() {
        String sql = """
                SELECT f.*, g.Nama_Genre
                FROM Film f
                JOIN Genre g ON f.ID_Genre = g.ID_Genre
                WHERE f.Stok > 0
                ORDER BY f.ID_Film DESC
                LIMIT 10
                """;
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public List<Film> getRecommendedMovies(Integer ID_Pelanggan) {
        String checkSql = "SELECT COUNT(*) FROM Penyewaan WHERE ID_Pelanggan = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, ID_Pelanggan);

        if (count > 0) {
            String sql = """
                    WITH UserGenres AS (
                        SELECT DISTINCT g.ID_Genre
                        FROM Penyewaan p
                        JOIN Film f ON p.ID_Film = f.ID_Film
                        JOIN Genre g ON f.ID_Genre = g.ID_Genre
                        WHERE p.ID_Pelanggan = ?
                    )
                    SELECT f.*, g.Nama_Genre
                    FROM Film f
                    JOIN Genre g ON f.ID_Genre = g.ID_Genre
                    WHERE f.ID_Genre IN (SELECT ID_Genre FROM UserGenres)
                    AND f.Stok > 0
                    AND f.ID_Film NOT IN (
                        SELECT ID_Film
                        FROM Penyewaan
                        WHERE ID_Pelanggan = ?
                    )
                    ORDER BY RANDOM()
                    LIMIT 10
                    """;
            return jdbcTemplate.query(sql, this::mapRowToFilm, ID_Pelanggan, ID_Pelanggan);
        } else {
            String sql = """
                    SELECT f.*, g.Nama_Genre
                    FROM Film f
                    JOIN Genre g ON f.ID_Genre = g.ID_Genre
                    WHERE f.Stok > 0
                    ORDER BY RANDOM()
                    LIMIT 10
                    """;
            return jdbcTemplate.query(sql, this::mapRowToFilm);
        }
    }

    @Override
    public List<Film> searchFilms(String genre, String actor, String title) {
        StringBuilder sql = new StringBuilder("""
                SELECT DISTINCT f.*, g.Nama_Genre
                FROM Film f
                JOIN Genre g ON f.ID_Genre = g.ID_Genre
                LEFT JOIN Film_Actor fa ON f.ID_Film = fa.ID_Film
                LEFT JOIN Actor a ON fa.ID_Actor = a.ID_Actor
                WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (genre != null && !genre.isEmpty()) {
            sql.append(" AND LOWER(g.Nama_Genre) LIKE LOWER(?)");
            params.add("%" + genre + "%");
        }
        if (actor != null && !actor.isEmpty()) {
            sql.append(" AND LOWER(a.Nama_Actor) LIKE LOWER(?)");
            params.add("%" + actor + "%");
        }
        if (title != null && !title.isEmpty()) {
            sql.append(" AND LOWER(f.Nama_Film) LIKE LOWER(?)");
            params.add("%" + title + "%");
        }

        sql.append(" ORDER BY f.Nama_Film");
        return jdbcTemplate.query(sql.toString(), params.toArray(), this::mapRowToFilm);
    }

    @Override
    public Film findById(Integer id) {
        String sql = """
                SELECT f.*, g.Nama_Genre
                FROM Film f
                JOIN Genre g ON f.ID_Genre = g.ID_Genre
                WHERE f.ID_Film = ?
                """;
        return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
    }

    private Film mapRowToFilm(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Film film = new Film();
        film.setID_Film(rs.getInt("ID_Film"));
        film.setNama_Film(rs.getString("Nama_Film"));
        film.setID_Genre(rs.getInt("ID_Genre"));
        film.setStok(rs.getInt("Stok"));
        film.setHarga(rs.getDouble("harga"));
        film.setFoto_Cover(rs.getString("Foto_Cover"));
        film.setDeskripsiFilm(rs.getString("DeskripsiFilm"));
        film.setNama_Genre(rs.getString("Nama_Genre"));
        film.setVideo_Path(rs.getString("Video_Path"));

        String actorsSql = """
                SELECT a.Nama_Actor
                FROM Actor a
                JOIN Film_Actor fa ON a.ID_Actor = fa.ID_Actor
                WHERE fa.ID_Film = ?
                """;
        List<String> actors = jdbcTemplate.queryForList(actorsSql, String.class, film.getID_Film());
        film.setNama_Actors(actors.toArray(new String[0]));

        return film;
    }

    public void save(Film film) {
        String filmSql = "INSERT INTO Film (Nama_Film, ID_Genre, Stok, Foto_Cover, DeskripsiFilm, harga, Video_Path) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING ID_Film";
        Integer filmId = jdbcTemplate.queryForObject(filmSql, Integer.class,
                film.getNama_Film(),
                film.getID_Genre(),
                film.getStok(),
                film.getFoto_Cover(),
                film.getDeskripsiFilm(),
                film.getHarga(),
                film.getVideo_Path());
        String actorSql = "INSERT INTO Film_Actor (ID_Film, ID_Actor) VALUES (?, ?)";
        for (Integer actorId : film.getActorIds()) {
            jdbcTemplate.update(actorSql, filmId, actorId);
        }
    }

    public List<Film> findAll() {
        String sql = """
                SELECT f.*, a.ID_Actor, a.Nama_Actor
                FROM Film f
                LEFT JOIN Film_Actor fa ON f.ID_Film = fa.ID_Film
                LEFT JOIN Actor a ON fa.ID_Actor = a.ID_Actor
                """;

        Map<Integer, Film> filmMap = new HashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            Integer filmId = rs.getInt("ID_Film");
            Film film = filmMap.get(filmId);

            if (film == null) {
                film = new Film();
                film.setID_Film(filmId);
                film.setNama_Film(rs.getString("Nama_Film"));
                film.setID_Genre(rs.getInt("ID_Genre"));
                film.setStok(rs.getInt("Stok"));
                film.setFoto_Cover(rs.getString("Foto_Cover"));
                film.setDeskripsiFilm(rs.getString("DeskripsiFilm"));
                film.setHarga(rs.getDouble("harga"));
                filmMap.put(filmId, film);
            }

            Actor actor = new Actor();
            actor.setID_Actor(rs.getInt("ID_Actor"));
            actor.setNama_Actor(rs.getString("Nama_Actor"));

            return film;
        });

        return new ArrayList<>(filmMap.values());
    }


    @Transactional
    public void update(Film film) {
        String sql = """
            UPDATE Film 
            SET Nama_Film = ?, ID_Genre = ?, Stok = ?, 
                DeskripsiFilm = ?, harga = ?
            WHERE ID_Film = ?
            """;
        
        jdbcTemplate.update(sql,
            film.getNama_Film(),
            film.getID_Genre(),
            film.getStok(),
            film.getDeskripsiFilm(),
            film.getHarga(),
            film.getID_Film()
        );

        if (film.getFoto_Cover() != null) {
            jdbcTemplate.update("UPDATE Film SET Foto_Cover = ? WHERE ID_Film = ?",
                film.getFoto_Cover(), film.getID_Film());
        }
        if (film.getVideo_Path() != null) {
            jdbcTemplate.update("UPDATE Film SET Video_Path = ? WHERE ID_Film = ?",
                film.getVideo_Path(), film.getID_Film());
        }

        if (film.getActorIds() != null) {
            jdbcTemplate.update("DELETE FROM Film_Actor WHERE ID_Film = ?", film.getID_Film());
            
            String actorSql = "INSERT INTO Film_Actor (ID_Film, ID_Actor) VALUES (?, ?)";
            for (Integer actorId : film.getActorIds()) {
                jdbcTemplate.update(actorSql, film.getID_Film(), actorId);
            }
        }
    }


    public List<Map<String, Object>> findAllWithGenres() {
        String sql = """
            SELECT f.*, g.Nama_Genre 
            FROM Film f 
            LEFT JOIN Genre g ON f.ID_Genre = g.ID_Genre 
            ORDER BY f.ID_Film
        """;
        
        return jdbcTemplate.queryForList(sql);
    }

    

    @Transactional
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM Film_Actor WHERE ID_Film = ?", id);
        jdbcTemplate.update("DELETE FROM Penyewaan WHERE ID_Film = ?", id);
        jdbcTemplate.update("DELETE FROM Film WHERE ID_Film = ?", id);
    }

}