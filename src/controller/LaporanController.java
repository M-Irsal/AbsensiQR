package controller;

import config.Koneksi;
import model.Laporan;
import utils.SessionManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import view.Dashboard;

public class LaporanController {
    
    // ========== METHOD MEMBUAT LAPORAN BARU ==========
    public boolean buatLaporan(Laporan laporan) {
        String sql = "INSERT INTO laporan (judul, deskripsi, kategori, status, created_by, sekolah_id, lampiran) " +
                     "VALUES (?, ?, ?, 'Baru', ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = Koneksi.getConnection();
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, laporan.getJudul());
            stmt.setString(2, laporan.getDeskripsi());
            stmt.setString(3, laporan.getKategori());
            stmt.setString(4, laporan.getCreatedBy());
            
            if (laporan.getSekolahId() > 0) {
                stmt.setInt(5, laporan.getSekolahId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setString(6, laporan.getLampiran());
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                System.out.println("✅ Laporan berhasil disimpan");
                
                // Catat log
                LogController logController = new LogController();
                logController.logActivity("Buat laporan baru: " + laporan.getJudul() + " (" + laporan.getKategori() + ")", "Berhasil");
                
                // Notifikasi untuk super admin
                Dashboard.notifikasiLaporanBaru();
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal buat laporan", "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("❌ Error saat menyimpan laporan: " + e.getMessage());
            e.printStackTrace();
            
            LogController logController = new LogController();
            logController.logActivity("Error buat laporan: " + e.getMessage(), "Error");
            
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // ========== METHOD MENGAMBIL SEMUA LAPORAN (SUPER ADMIN) ==========
    public List<Laporan> getAllLaporan() {
        List<Laporan> list = new ArrayList<>();
        String sql = "SELECT l.*, s.nama_sekolah FROM laporan l " +
                     "LEFT JOIN sekolah s ON l.sekolah_id = s.id " +
                     "ORDER BY " +
                     "CASE l.status " +
                     "   WHEN 'Baru' THEN 1 " +
                     "   WHEN 'Diproses' THEN 2 " +
                     "   ELSE 3 " +
                     "END, l.tanggal_lapor DESC";
        
        try (Statement stmt = Koneksi.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Laporan l = new Laporan();
                l.setId(rs.getInt("id"));
                l.setJudul(rs.getString("judul"));
                l.setDeskripsi(rs.getString("deskripsi"));
                l.setKategori(rs.getString("kategori"));
                l.setStatus(rs.getString("status"));
                l.setTanggalLapor(rs.getTimestamp("tanggal_lapor").toLocalDateTime());
                if (rs.getTimestamp("tanggal_selesai") != null) {
                    l.setTanggalSelesai(rs.getTimestamp("tanggal_selesai").toLocalDateTime());
                }
                l.setCreatedBy(rs.getString("created_by"));
                l.setSekolahId(rs.getInt("sekolah_id"));
                l.setNamaSekolah(rs.getString("nama_sekolah"));
                l.setLampiran(rs.getString("lampiran"));
                
                list.add(l);
            }
            
            // Catat log (hanya untuk super admin)
            if (SessionManager.getInstance().isSuperAdmin()) {
                LogController logController = new LogController();
                logController.logActivity("Melihat semua laporan (" + list.size() + " data)", "Berhasil");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat semua laporan: " + e.getMessage(), "Error");
        }
        return list;
    }
    
    // ========== METHOD MENGAMBIL LAPORAN PER SEKOLAH (ADMIN SEKOLAH) ==========
    public List<Laporan> getLaporanBySekolah(int sekolahId) {
        List<Laporan> list = new ArrayList<>();
        String sql = "SELECT * FROM laporan WHERE sekolah_id = ? OR (sekolah_id IS NULL AND created_by = 'superadmin') " +
                     "ORDER BY " +
                     "CASE status " +
                     "   WHEN 'Baru' THEN 1 " +
                     "   WHEN 'Diproses' THEN 2 " +
                     "   ELSE 3 " +
                     "END, tanggal_lapor DESC";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, sekolahId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Laporan l = new Laporan();
                l.setId(rs.getInt("id"));
                l.setJudul(rs.getString("judul"));
                l.setDeskripsi(rs.getString("deskripsi"));
                l.setKategori(rs.getString("kategori"));
                l.setStatus(rs.getString("status"));
                l.setTanggalLapor(rs.getTimestamp("tanggal_lapor").toLocalDateTime());
                if (rs.getTimestamp("tanggal_selesai") != null) {
                    l.setTanggalSelesai(rs.getTimestamp("tanggal_selesai").toLocalDateTime());
                }
                l.setCreatedBy(rs.getString("created_by"));
                l.setSekolahId(rs.getInt("sekolah_id"));
                l.setLampiran(rs.getString("lampiran"));
                
                list.add(l);
            }
            
            // Catat log
            LogController logController = new LogController();
            logController.logActivity("Melihat laporan sekolah (" + list.size() + " data)", "Berhasil");
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat laporan sekolah: " + e.getMessage(), "Error");
        }
        return list;
    }
    
    // ========== METHOD MENGAMBIL LAPORAN BY ID ==========
    public Laporan getLaporanById(int id) {
        String sql = "SELECT l.*, s.nama_sekolah FROM laporan l " +
                     "LEFT JOIN sekolah s ON l.sekolah_id = s.id " +
                     "WHERE l.id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Laporan l = new Laporan();
                l.setId(rs.getInt("id"));
                l.setJudul(rs.getString("judul"));
                l.setDeskripsi(rs.getString("deskripsi"));
                l.setKategori(rs.getString("kategori"));
                l.setStatus(rs.getString("status"));
                l.setTanggalLapor(rs.getTimestamp("tanggal_lapor").toLocalDateTime());
                if (rs.getTimestamp("tanggal_selesai") != null) {
                    l.setTanggalSelesai(rs.getTimestamp("tanggal_selesai").toLocalDateTime());
                }
                l.setCreatedBy(rs.getString("created_by"));
                l.setSekolahId(rs.getInt("sekolah_id"));
                l.setNamaSekolah(rs.getString("nama_sekolah"));
                l.setLampiran(rs.getString("lampiran"));
                
                LogController logController = new LogController();
                logController.logActivity("Melihat detail laporan ID: " + id, "Berhasil");
                
                return l;
            } else {
                LogController logController = new LogController();
                logController.logActivity("Laporan tidak ditemukan ID: " + id, "Gagal");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat detail laporan: " + e.getMessage(), "Error");
        }
        return null;
    }
    
    // ========== METHOD MENGHITUNG JUMLAH LAPORAN BARU ==========
    public int getJumlahLaporanBaru() {
        String sql = "SELECT COUNT(*) as total FROM laporan WHERE status = 'Baru'";
        
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
    
    // ========== METHOD MENGUPDATE STATUS LAPORAN ==========
    public boolean updateStatusLaporan(int id, String status) {
        String sql = "UPDATE laporan SET status = ?, tanggal_selesai = ? WHERE id = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            
            if ("Selesai".equals(status)) {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }
            stmt.setInt(3, id);
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Update status laporan ID: " + id + " menjadi " + status, "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal update status laporan ID: " + id, "Gagal");
            }
            
            return success;
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error update status laporan: " + e.getMessage(), "Error");
            return false;
        }
    }
    
    // ========== METHOD MENGHITUNG TOTAL LAPORAN ==========
    public int getTotalLaporan() {
        String sql = "SELECT COUNT(*) as total FROM laporan";
        
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
    
    // ========== METHOD MENGHITUNG LAPORAN PER STATUS ==========
    public int getLaporanByStatus(String status) {
        String sql = "SELECT COUNT(*) as total FROM laporan WHERE status = ?";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}