package com.example.m08.Laporan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class LaporanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Map<String, Object>> getMonthlyReports() {
        String sql = """
            SELECT 
                CAST(EXTRACT(MONTH FROM p.Tanggal) AS INTEGER) as Bulan,
                CAST(EXTRACT(YEAR FROM p.Tanggal) AS INTEGER) as Tahun,
                COUNT(*) as total_penyewaan,
                COUNT(CASE WHEN p.status = 'RETURNED' THEN 1 END) as returned_count,
                COUNT(CASE WHEN p.status = 'ACTIVE' THEN 1 END) as active_count,
                COUNT(DISTINCT p.ID_Pelanggan) as unique_customers,
                SUM(f.harga) as total_pendapatan
            FROM Penyewaan p
            JOIN film f ON p.id_film = f.id_film  -- Menghubungkan tabel Penyewaan dan Film
            GROUP BY 
                EXTRACT(MONTH FROM p.Tanggal),
                EXTRACT(YEAR FROM p.Tanggal)
            ORDER BY Tahun DESC, Bulan DESC;

            """;
        return jdbcTemplate.queryForList(sql);
    }
    
    public Map<String, Object> getLaporanSummary() {
        String sql = """
            SELECT 
                COUNT(DISTINCT p.ID_Pelanggan) as total_customers,
                SUM(f.harga) as total_pendapatan,
                COUNT(*) as total_rentals,
                COUNT(CASE WHEN p.status = 'ACTIVE' THEN 1 END) as active_rentals
            FROM Penyewaan p
            JOIN film f ON p.id_film = f.id_film
            """;
        return jdbcTemplate.queryForMap(sql);
    }

    public List<Map<String, Object>> getTopCustomers() {
        String sql = """
            SELECT 
                p2.username_pelanggan,
                COUNT(*) as rental_count
            FROM Penyewaan p1
            JOIN Pelanggan p2 ON p1.ID_Pelanggan = p2.ID_Pelanggan
            GROUP BY p2.ID_Pelanggan, p2.username_pelanggan
            ORDER BY rental_count DESC
            LIMIT 5
            """;
        return jdbcTemplate.queryForList(sql);
    }
}