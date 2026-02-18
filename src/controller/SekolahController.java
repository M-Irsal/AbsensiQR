package controller;

import config.Koneksi;
import model.Sekolah;
import utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SekolahController {
    
    // ========== METHOD TAMBAH SEKOLAH ==========
    public boolean addSekolah(Sekolah sekolah) {
        String sql = "INSERT INTO sekolah (kode_sekolah, nama_sekolah, alamat, telepon, status) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, sekolah.getKodeSekolah());
            stmt.setString(2, sekolah.getNamaSekolah());
            stmt.setString(3, sekolah.getAlamat());
            stmt.setString(4, sekolah.getTelepon());
            stmt.setString(5, sekolah.getStatus());
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Tambah sekolah baru: " + sekolah.getNamaSekolah() + " (" + sekolah.getKodeSekolah() + ")", "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal tambah sekolah: " + sekolah.getNamaSekolah(), "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error tambah sekolah: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD GET ALL SEKOLAH ==========
    public List<Sekolah> getAllSekolah() {
        List<Sekolah> list = new ArrayList<>();
        String sql = "SELECT * FROM sekolah ORDER BY id";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Sekolah s = new Sekolah();
                s.setId(rs.getInt("id"));
                s.setKodeSekolah(rs.getString("kode_sekolah"));
                s.setNamaSekolah(rs.getString("nama_sekolah"));
                s.setAlamat(rs.getString("alamat"));
                s.setTelepon(rs.getString("telepon"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
            
            // Catat log (hanya jika dipanggil oleh super admin)
            if (SessionManager.getInstance().isSuperAdmin()) {
                LogController logController = new LogController();
                logController.logActivity("Melihat daftar semua sekolah", "Berhasil");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat daftar sekolah: " + e.getMessage(), "Error");
        }
        return list;
    }
    
    // ========== METHOD GET SEKOLAH BY ID ==========
    public Sekolah getSekolahById(int id) {
        String sql = "SELECT * FROM sekolah WHERE id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Sekolah s = new Sekolah();
                s.setId(rs.getInt("id"));
                s.setKodeSekolah(rs.getString("kode_sekolah"));
                s.setNamaSekolah(rs.getString("nama_sekolah"));
                s.setAlamat(rs.getString("alamat"));
                s.setTelepon(rs.getString("telepon"));
                s.setStatus(rs.getString("status"));
                
                LogController logController = new LogController();
                logController.logActivity("Melihat detail sekolah ID: " + id, "Berhasil");
                
                return s;
            } else {
                LogController logController = new LogController();
                logController.logActivity("Sekolah tidak ditemukan ID: " + id, "Gagal");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error cari sekolah: " + e.getMessage(), "Error");
        }
        return null;
    }
    
    // ========== METHOD UPDATE SEKOLAH ==========
    public boolean updateSekolah(Sekolah sekolah) {
        String sql = "UPDATE sekolah SET kode_sekolah = ?, nama_sekolah = ?, alamat = ?, telepon = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, sekolah.getKodeSekolah());
            stmt.setString(2, sekolah.getNamaSekolah());
            stmt.setString(3, sekolah.getAlamat());
            stmt.setString(4, sekolah.getTelepon());
            stmt.setString(5, sekolah.getStatus());
            stmt.setInt(6, sekolah.getId());
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Update sekolah: " + sekolah.getNamaSekolah() + " (ID: " + sekolah.getId() + ")", "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal update sekolah ID: " + sekolah.getId(), "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error update sekolah: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD DELETE SEKOLAH ==========
    public boolean deleteSekolah(int id) {
        // Ambil nama sekolah untuk log
        String namaSekolah = "";
        try {
            Sekolah s = getSekolahById(id);
            if (s != null) {
                namaSekolah = s.getNamaSekolah();
            }
        } catch (Exception e) {
            // Abaikan
        }
        
        String sql = "DELETE FROM sekolah WHERE id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Hapus sekolah: " + namaSekolah + " (ID: " + id + ")", "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal hapus sekolah ID: " + id, "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error hapus sekolah: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD GET SEKOLAH AKTIF ==========
    public List<Sekolah> getSekolahAktif() {
        List<Sekolah> list = new ArrayList<>();
        String sql = "SELECT * FROM sekolah WHERE status = 'active' ORDER BY nama_sekolah";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Sekolah s = new Sekolah();
                s.setId(rs.getInt("id"));
                s.setKodeSekolah(rs.getString("kode_sekolah"));
                s.setNamaSekolah(rs.getString("nama_sekolah"));
                s.setAlamat(rs.getString("alamat"));
                s.setTelepon(rs.getString("telepon"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // ========== METHOD GET TOTAL SEKOLAH ==========
    public int getTotalSekolah() {
        String sql = "SELECT COUNT(*) as total FROM sekolah";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}