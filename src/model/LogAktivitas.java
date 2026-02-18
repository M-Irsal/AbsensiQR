package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogAktivitas {
    private int id;
    private LocalDateTime waktu;
    private String username;
    private String aktivitas;
    private String status;
    private String ipAddress;
    private String namaSekolah;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getAktivitas() { return aktivitas; }
    public void setAktivitas(String aktivitas) { this.aktivitas = aktivitas; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    
    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }
    
    public String getWaktuFormatted() {
        return waktu.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}