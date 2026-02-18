package controller;

import config.Koneksi;
import model.Admin;
import model.Sekolah;
import model.Siswa;
import utils.SessionManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AbsensiController {
    
    // ========== METHOD LOGIN DENGAN MULTI-TENANCY ==========
    public Admin login(String username, String password) {
        String sql = "SELECT a.*, s.kode_sekolah, s.nama_sekolah FROM admin a " +
                     "LEFT JOIN sekolah s ON a.sekolah_id = s.id " +
                     "WHERE a.username = ? AND a.password = MD5(?)";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setNamaLengkap(rs.getString("nama_lengkap"));
                admin.setSekolahId(rs.getInt("sekolah_id"));
                admin.setRole(rs.getString("role"));
                
                // Catat log login berhasil
                LogController logController = new LogController();
                logController.logActivity("Login berhasil untuk user: " + username, "Berhasil");
                
                // Jika bukan super admin, set informasi sekolah
                if (!admin.isSuperAdmin() && admin.getSekolahId() > 0) {
                    Sekolah sekolah = new Sekolah();
                    sekolah.setId(rs.getInt("sekolah_id"));
                    sekolah.setKodeSekolah(rs.getString("kode_sekolah"));
                    sekolah.setNamaSekolah(rs.getString("nama_sekolah"));
                    
                    // Simpan ke session
                    SessionManager.getInstance().setLoginInfo(admin, sekolah, false);
                } else {
                    // Super admin
                    SessionManager.getInstance().setLoginInfo(admin, null, true);
                }
                
                return admin;
            } else {
                // Catat log login gagal
                LogController logController = new LogController();
                logController.logActivity("Login gagal untuk user: " + username, "Gagal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error login: " + e.getMessage(), "Error");
        }
        return null;
    }

    // ========== METHOD GET ALL SISWA DENGAN FILTER SEKOLAH ==========
    public List<Siswa> getAllSiswa() {
        List<Siswa> list = new ArrayList<>();
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        String sql = "SELECT * FROM siswa";
        if (sekolahId > 0) {
            sql += " WHERE sekolah_id = ?";
        }
        sql += " ORDER BY nis";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            if (sekolahId > 0) {
                stmt.setInt(1, sekolahId);
            }
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Siswa s = new Siswa();
                s.setId(rs.getInt("id"));
                s.setNis(rs.getString("nis"));
                s.setNama(rs.getString("nama"));
                s.setKelas(rs.getString("kelas"));
                s.setJurusan(rs.getString("jurusan"));
                s.setQrCode(rs.getString("qr_code"));
                list.add(s);
            }
            
            LogController logController = new LogController();
            logController.logActivity("Melihat daftar siswa", "Berhasil");
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Gagal melihat daftar siswa: " + e.getMessage(), "Error");
        }
        return list;
    }

    // ========== METHOD ADD SISWA DENGAN SEKOLAH ID ==========
    public boolean addSiswa(Siswa siswa) {
        int sekolahId = SessionManager.getInstance().getSekolahId();
        if (sekolahId == 0) {
            LogController logController = new LogController();
            logController.logActivity("Super admin mencoba tambah siswa", "Gagal");
            return false;
        }
        
        String sql = "INSERT INTO siswa (nis, nama, kelas, jurusan, sekolah_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, siswa.getNis());
            stmt.setString(2, siswa.getNama());
            stmt.setString(3, siswa.getKelas());
            stmt.setString(4, siswa.getJurusan());
            stmt.setInt(5, sekolahId);
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Tambah siswa baru: " + siswa.getNis() + " - " + siswa.getNama(), "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal tambah siswa: " + siswa.getNis(), "Gagal");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error tambah siswa: " + e.getMessage(), "Error");
            return false;
        }
    }

    // ========== METHOD GET SISWA BY NIS ==========
    public Siswa getSiswaByNIS(String nis) {
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        String sql = "SELECT * FROM siswa WHERE nis = ?";
        if (sekolahId > 0) {
            sql += " AND sekolah_id = ?";
        }
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, nis);
            if (sekolahId > 0) {
                stmt.setInt(2, sekolahId);
            }
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Siswa s = new Siswa();
                s.setId(rs.getInt("id"));
                s.setNis(rs.getString("nis"));
                s.setNama(rs.getString("nama"));
                s.setKelas(rs.getString("kelas"));
                s.setJurusan(rs.getString("jurusan"));
                
                LogController logController = new LogController();
                logController.logActivity("Mencari siswa NIS: " + nis, "Berhasil");
                
                return s;
            } else {
                LogController logController = new LogController();
                logController.logActivity("Siswa tidak ditemukan NIS: " + nis, "Gagal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error mencari siswa: " + e.getMessage(), "Error");
        }
        return null;
    }

    // ========== METHOD PROSES ABSENSI ==========
    public String prosesAbsensi(String nis) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        int sekolahId = SessionManager.getInstance().getSekolahId();
        LogController logController = new LogController();
        
        if (sekolahId == 0) {
            logController.logActivity("Super admin mencoba absensi", "Gagal");
            return "Error: Super admin tidak dapat melakukan absensi";
        }
        
        // Cek apakah siswa terdaftar di sekolah ini
        Siswa siswa = getSiswaByNIS(nis);
        if (siswa == null) {
            logController.logActivity("Absensi gagal - NIS tidak ditemukan: " + nis, "Gagal");
            return "Data Tidak Ditemukan";
        }
        
        // Cek apakah sudah absen hari ini
        String cekSql = "SELECT * FROM absensi WHERE nis = ? AND tanggal = ? AND sekolah_id = ?";
        try (PreparedStatement cekStmt = Koneksi.getConnection().prepareStatement(cekSql)) {
            cekStmt.setString(1, nis);
            cekStmt.setDate(2, Date.valueOf(today));
            cekStmt.setInt(3, sekolahId);
            ResultSet rs = cekStmt.executeQuery();
            
            if (rs.next()) {
                logController.logActivity("Absensi gagal - sudah absen NIS: " + nis, "Gagal");
                return "Sudah Absen";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logController.logActivity("Error cek absensi: " + e.getMessage(), "Error");
            return "Error";
        }
        
        // Simpan absensi
        String insertSql = "INSERT INTO absensi (nis, tanggal, jam_masuk, status, sekolah_id) VALUES (?, ?, ?, 'Hadir', ?)";
        try (PreparedStatement insertStmt = Koneksi.getConnection().prepareStatement(insertSql)) {
            insertStmt.setString(1, nis);
            insertStmt.setDate(2, Date.valueOf(today));
            insertStmt.setTime(3, Time.valueOf(now));
            insertStmt.setInt(4, sekolahId);
            
            if (insertStmt.executeUpdate() > 0) {
                logController.logActivity("Absensi berhasil NIS: " + nis, "Berhasil");
                return "Hadir";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logController.logActivity("Error simpan absensi: " + e.getMessage(), "Error");
        }
        
        return "Error";
    }

    // ========== METHOD UPDATE SISWA ==========
    public boolean updateSiswa(Siswa siswa) {
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        String sql = "UPDATE siswa SET nama = ?, kelas = ?, jurusan = ? WHERE nis = ?";
        if (sekolahId > 0) {
            sql += " AND sekolah_id = ?";
        }
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, siswa.getNama());
            stmt.setString(2, siswa.getKelas());
            stmt.setString(3, siswa.getJurusan());
            stmt.setString(4, siswa.getNis());
            if (sekolahId > 0) {
                stmt.setInt(5, sekolahId);
            }
            
            int rowsAffected = stmt.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Update siswa NIS: " + siswa.getNis(), "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal update siswa NIS: " + siswa.getNis(), "Gagal");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error update siswa: " + e.getMessage(), "Error");
            return false;
        }
    }

    // ========== METHOD DELETE SISWA ==========
    public boolean deleteSiswa(String nis) {
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        // Hapus dulu data absensi yang terkait (foreign key)
        String deleteAbsensiSql = "DELETE FROM absensi WHERE nis = ?";
        String deleteSiswaSql = "DELETE FROM siswa WHERE nis = ?";
        if (sekolahId > 0) {
            deleteAbsensiSql += " AND sekolah_id = ?";
            deleteSiswaSql += " AND sekolah_id = ?";
        }
        
        try (PreparedStatement stmtAbsensi = Koneksi.getConnection().prepareStatement(deleteAbsensiSql);
             PreparedStatement stmtSiswa = Koneksi.getConnection().prepareStatement(deleteSiswaSql)) {
            
            // Hapus absensi terkait
            stmtAbsensi.setString(1, nis);
            if (sekolahId > 0) {
                stmtAbsensi.setInt(2, sekolahId);
            }
            stmtAbsensi.executeUpdate();
            
            // Hapus data siswa
            stmtSiswa.setString(1, nis);
            if (sekolahId > 0) {
                stmtSiswa.setInt(2, sekolahId);
            }
            int rowsAffected = stmtSiswa.executeUpdate();
            boolean success = rowsAffected > 0;
            
            if (success) {
                LogController logController = new LogController();
                logController.logActivity("Hapus siswa NIS: " + nis, "Berhasil");
            } else {
                LogController logController = new LogController();
                logController.logActivity("Gagal hapus siswa NIS: " + nis, "Gagal");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error hapus siswa: " + e.getMessage(), "Error");
            return false;
        }
    }

    // ========== METHOD GET RIWAYAT ABSENSI ==========
    public List<Object[]> getRiwayatAbsensi(String tanggal) {
        List<Object[]> list = new ArrayList<>();
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        String sql = "SELECT a.tanggal, a.jam_masuk, s.nis, s.nama, s.kelas, s.jurusan, a.status " +
                    "FROM absensi a JOIN siswa s ON a.nis = s.nis " +
                    "WHERE a.tanggal = ?";
        if (sekolahId > 0) {
            sql += " AND a.sekolah_id = ?";
        }
        sql += " ORDER BY a.jam_masuk";
        
        try (PreparedStatement stmt = Koneksi.getConnection().prepareStatement(sql)) {
            stmt.setString(1, tanggal);
            if (sekolahId > 0) {
                stmt.setInt(2, sekolahId);
            }
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("tanggal"),
                    rs.getString("jam_masuk"),
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("kelas"),
                    rs.getString("jurusan"),
                    rs.getString("status")
                };
                list.add(row);
            }
            
            LogController logController = new LogController();
            logController.logActivity("Melihat riwayat absensi tanggal: " + tanggal, "Berhasil");
            
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat riwayat: " + e.getMessage(), "Error");
        }
        return list;
    }

    // ========== METHOD REKAP SEMESTER ==========
    public Map<String, Object> getRekapSemester(String tahunAjaran, String semester, 
            String filterKelas, String filterJurusan) {
        
        Map<String, Object> result = new HashMap<>();
        List<Object[]> rekapList = new ArrayList<>();
        int sekolahId = SessionManager.getInstance().getSekolahId();
        
        // Parse tahun ajaran
        String[] tahun = tahunAjaran.split("/");
        int tahunMulai = Integer.parseInt(tahun[0]);
        int tahunSelesai = Integer.parseInt(tahun[1]);
        
        // Tentukan rentang tanggal berdasarkan semester
        String tanggalMulai, tanggalSelesai;
        if (semester.equals("Ganjil")) {
            tanggalMulai = tahunMulai + "-07-01";
            tanggalSelesai = tahunMulai + "-12-31";
        } else {
            tanggalMulai = tahunSelesai + "-01-01";
            tanggalSelesai = tahunSelesai + "-06-30";
        }
        
        // Query siswa dengan filter sekolah
        StringBuilder siswaSql = new StringBuilder("SELECT nis, nama, kelas, jurusan FROM siswa WHERE 1=1");
        if (sekolahId > 0) {
            siswaSql.append(" AND sekolah_id = ").append(sekolahId);
        }
        if (!filterKelas.equals("Semua Kelas")) {
            siswaSql.append(" AND kelas = '").append(filterKelas).append("'");
        }
        if (!filterJurusan.equals("Semua Jurusan")) {
            siswaSql.append(" AND jurusan = '").append(filterJurusan).append("'");
        }
        siswaSql.append(" ORDER BY kelas, jurusan, nama");
        
        Connection conn = null;
        try {
            conn = Koneksi.getConnection();
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rsSiswa = stmt.executeQuery(siswaSql.toString())) {
                
                while (rsSiswa.next()) {
                    String nis = rsSiswa.getString("nis");
                    String nama = rsSiswa.getString("nama");
                    String kelas = rsSiswa.getString("kelas");
                    String jurusan = rsSiswa.getString("jurusan");
                    
                    // Hitung jumlah hadir dalam rentang semester
                    String hitungSql = "SELECT COUNT(*) as total FROM absensi WHERE nis = ? " +
                                      "AND tanggal BETWEEN ? AND ?";
                    if (sekolahId > 0) {
                        hitungSql += " AND sekolah_id = ?";
                    }
                    
                    try (PreparedStatement ps = conn.prepareStatement(hitungSql)) {
                        ps.setString(1, nis);
                        ps.setString(2, tanggalMulai);
                        ps.setString(3, tanggalSelesai);
                        if (sekolahId > 0) {
                            ps.setInt(4, sekolahId);
                        }
                        
                        try (ResultSet rsHitung = ps.executeQuery()) {
                            if (rsHitung.next()) {
                                int totalHadir = rsHitung.getInt("total");
                                
                                // Hitung total hari efektif (senin-jumat) dalam rentang semester
                                int totalHariEfektif = hitungHariEfektif(tanggalMulai, tanggalSelesai);
                                int totalTidakHadir = totalHariEfektif - totalHadir;
                                if (totalTidakHadir < 0) totalTidakHadir = 0;
                                
                                double persentase = totalHariEfektif > 0 ? 
                                    (double) totalHadir / totalHariEfektif * 100 : 0;
                                
                                Object[] row = {
                                    nis,
                                    nama,
                                    kelas,
                                    jurusan,
                                    totalHadir,
                                    totalTidakHadir,
                                    String.format("%.1f%%", persentase)
                                };
                                rekapList.add(row);
                            }
                        }
                    }
                }
                
                LogController logController = new LogController();
                logController.logActivity("Melihat rekap semester: " + tahunAjaran + " " + semester, "Berhasil");
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LogController logController = new LogController();
            logController.logActivity("Error lihat rekap: " + e.getMessage(), "Error");
        }
        
        result.put("data", rekapList);
        result.put("tanggalMulai", tanggalMulai);
        result.put("tanggalSelesai", tanggalSelesai);
        
        return result;
    }

    // ========== METHOD BANTU HITUNG HARI EFEKTIF ==========
    private int hitungHariEfektif(String tglMulai, String tglSelesai) {
        try {
            LocalDate mulai = LocalDate.parse(tglMulai);
            LocalDate selesai = LocalDate.parse(tglSelesai);
            
            int hariEfektif = 0;
            LocalDate current = mulai;
            
            while (!current.isAfter(selesai)) {
                int dayOfWeek = current.getDayOfWeek().getValue();
                if (dayOfWeek >= 1 && dayOfWeek <= 5) {
                    hariEfektif++;
                }
                current = current.plusDays(1);
            }
            
            return hariEfektif;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}