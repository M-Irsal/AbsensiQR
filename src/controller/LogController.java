package controller;

import config.Koneksi;
import model.LogAktivitas;
import utils.SessionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogController {
    
    // Method untuk mencatat log
    public void logActivity(String aktivitas, String status) {
        String sql = "INSERT INTO log_aktivitas (username, aktivitas, status, ip_address, sekolah_id) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            String username = SessionManager.getInstance().getCurrentAdmin() != null ? 
                SessionManager.getInstance().getCurrentAdmin().getUsername() : "system";
            String ipAddress = "127.0.0.1"; // Bisa diganti dengan IP real
            int sekolahId = SessionManager.getInstance().getSekolahId();
            
            stmt.setString(1, username);
            stmt.setString(2, aktivitas);
            stmt.setString(3, status);
            stmt.setString(4, ipAddress);
            if (sekolahId > 0) {
                stmt.setInt(5, sekolahId);
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method untuk mengambil semua log
    public List<LogAktivitas> getAllLogs() {
        List<LogAktivitas> list = new ArrayList<>();
        String sql = "SELECT l.*, s.nama_sekolah FROM log_aktivitas l " +
                     "LEFT JOIN sekolah s ON l.sekolah_id = s.id " +
                     "ORDER BY l.waktu DESC";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LogAktivitas log = new LogAktivitas();
                log.setId(rs.getInt("id"));
                log.setWaktu(rs.getTimestamp("waktu").toLocalDateTime());
                log.setUsername(rs.getString("username"));
                log.setAktivitas(rs.getString("aktivitas"));
                log.setStatus(rs.getString("status"));
                log.setIpAddress(rs.getString("ip_address"));
                log.setNamaSekolah(rs.getString("nama_sekolah"));
                
                list.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Method untuk mengambil log dengan filter tanggal
    public List<LogAktivitas> getLogsByDate(String filter) {
        List<LogAktivitas> list = new ArrayList<>();
        String sql = "SELECT l.*, s.nama_sekolah FROM log_aktivitas l " +
                     "LEFT JOIN sekolah s ON l.sekolah_id = s.id ";
        
        LocalDateTime now = LocalDateTime.now();
        switch (filter) {
            case "Hari Ini":
                sql += "WHERE DATE(l.waktu) = CURDATE() ";
                break;
            case "Kemarin":
                sql += "WHERE DATE(l.waktu) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) ";
                break;
            case "7 Hari Terakhir":
                sql += "WHERE l.waktu >= DATE_SUB(NOW(), INTERVAL 7 DAY) ";
                break;
            case "30 Hari Terakhir":
                sql += "WHERE l.waktu >= DATE_SUB(NOW(), INTERVAL 30 DAY) ";
                break;
        }
        
        sql += "ORDER BY l.waktu DESC";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                LogAktivitas log = new LogAktivitas();
                log.setId(rs.getInt("id"));
                log.setWaktu(rs.getTimestamp("waktu").toLocalDateTime());
                log.setUsername(rs.getString("username"));
                log.setAktivitas(rs.getString("aktivitas"));
                log.setStatus(rs.getString("status"));
                log.setIpAddress(rs.getString("ip_address"));
                log.setNamaSekolah(rs.getString("nama_sekolah"));
                
                list.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}