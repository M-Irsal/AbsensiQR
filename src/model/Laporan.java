package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Laporan {
    private int id;
    private String judul;
    private String deskripsi;
    private String kategori;
    private String status;
    private LocalDateTime tanggalLapor;
    private LocalDateTime tanggalSelesai;
    private String createdBy;
    private int sekolahId;
    private String namaSekolah;
    private String lampiran;
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getTanggalLapor() { return tanggalLapor; }
    public void setTanggalLapor(LocalDateTime tanggalLapor) { this.tanggalLapor = tanggalLapor; }
    
    public LocalDateTime getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDateTime tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public int getSekolahId() { return sekolahId; }
    public void setSekolahId(int sekolahId) { this.sekolahId = sekolahId; }
    
    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }
    
    public String getLampiran() { return lampiran; }
    public void setLampiran(String lampiran) { this.lampiran = lampiran; }
    
    public String getTanggalLaporFormatted() {
        return tanggalLapor.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    public String getTanggalSelesaiFormatted() {
        return tanggalSelesai != null ? 
            tanggalSelesai.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "-";
    }
    
    public String getStatusIcon() {
        switch (status) {
            case "Baru": return "🆕 Baru";
            case "Diproses": return "⏳ Diproses";
            case "Selesai": return "✅ Selesai";
            default: return status;
        }
    }
}