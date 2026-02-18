package controller;

import config.Koneksi;
import model.Admin;
import utils.SessionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    
    // ========== METHOD TAMBAH ADMIN ==========
    public boolean addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, password, nama_lengkap, sekolah_id, role) VALUES (?, MD5(?), ?, ?, ?)";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getNamaLengkap());
            stmt.setInt(4, admin.getSekolahId());
            stmt.setString(5, admin.getRole());
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                LogController logController = new LogController();
                String role = "admin_sekolah".equals(admin.getRole()) ? "Admin Sekolah" : "Super Admin";
                logController.logActivity("Tambah admin baru: " + admin.getUsername() + " (" + role + ")", "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal tambah admin: " + admin.getUsername(), "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error tambah admin: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD GET ALL ADMIN ==========
    public List<Admin> getAllAdmin() {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT a.*, s.nama_sekolah FROM admin a " +
                     "LEFT JOIN sekolah s ON a.sekolah_id = s.id " +
                     "ORDER BY a.id";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setNamaLengkap(rs.getString("nama_lengkap"));
                a.setSekolahId(rs.getInt("sekolah_id"));
                a.setRole(rs.getString("role"));
                // Set nama sekolah jika perlu ditambahkan di model Admin
                list.add(a);
            }
            
            // Catat log (hanya untuk super admin)
            if (SessionManager.getInstance().isSuperAdmin()) {
                LogController logController = new LogController();
                logController.logActivity("Melihat daftar semua admin", "Berhasil");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat daftar admin: " + e.getMessage(), "Error");
        }
        return list;
    }
    
    // ========== METHOD GET ADMIN BY ID ==========
    public Admin getAdminById(int id) {
        String sql = "SELECT a.*, s.nama_sekolah FROM admin a " +
                     "LEFT JOIN sekolah s ON a.sekolah_id = s.id " +
                     "WHERE a.id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setNamaLengkap(rs.getString("nama_lengkap"));
                a.setSekolahId(rs.getInt("sekolah_id"));
                a.setRole(rs.getString("role"));
                
                LogController logController = new LogController();
                logController.logActivity("Melihat detail admin ID: " + id, "Berhasil");
                
                return a;
            } else {
                LogController logController = new LogController();
                logController.logActivity("Admin tidak ditemukan ID: " + id, "Gagal");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error cari admin: " + e.getMessage(), "Error");
        }
        return null;
    }
    
    // ========== METHOD UPDATE ADMIN ==========
    public boolean updateAdmin(Admin admin) {
        String sql;
        boolean updatePassword = (admin.getPassword() != null && !admin.getPassword().isEmpty());
        
        if (updatePassword) {
            // Update termasuk password
            sql = "UPDATE admin SET username = ?, password = MD5(?), nama_lengkap = ?, sekolah_id = ?, role = ? WHERE id = ?";
        } else {
            // Update tanpa password
            sql = "UPDATE admin SET username = ?, nama_lengkap = ?, sekolah_id = ?, role = ? WHERE id = ?";
        }
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            if (updatePassword) {
                stmt.setString(1, admin.getUsername());
                stmt.setString(2, admin.getPassword());
                stmt.setString(3, admin.getNamaLengkap());
                stmt.setInt(4, admin.getSekolahId());
                stmt.setString(5, admin.getRole());
                stmt.setInt(6, admin.getId());
            } else {
                stmt.setString(1, admin.getUsername());
                stmt.setString(2, admin.getNamaLengkap());
                stmt.setInt(3, admin.getSekolahId());
                stmt.setString(4, admin.getRole());
                stmt.setInt(5, admin.getId());
            }
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                String action = updatePassword ? "Update admin (dengan password)" : "Update admin (tanpa password)";
                logController.logActivity(action + " - " + admin.getUsername(), "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal update admin: " + admin.getUsername(), "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error update admin: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD DELETE ADMIN ==========
    public boolean deleteAdmin(int id) {
        // Ambil username admin untuk log
        String username = "";
        try {
            Admin a = getAdminById(id);
            if (a != null) {
                username = a.getUsername();
            }
        } catch (Exception e) {
            // Abaikan
        }
        
        String sql = "DELETE FROM admin WHERE id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Hapus admin: " + username + " (ID: " + id + ")", "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal hapus admin ID: " + id, "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error hapus admin: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD GET TOTAL ADMIN ==========
    public int getTotalAdmin() {
        String sql = "SELECT COUNT(*) as total FROM admin";
        
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
    
    // ========== METHOD GET ADMIN BY SEKOLAH ==========
    public List<Admin> getAdminBySekolah(int sekolahId) {
        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM admin WHERE sekolah_id = ? ORDER BY username";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, sekolahId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Admin a = new Admin();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setNamaLengkap(rs.getString("nama_lengkap"));
                a.setSekolahId(rs.getInt("sekolah_id"));
                a.setRole(rs.getString("role"));
                list.add(a);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}