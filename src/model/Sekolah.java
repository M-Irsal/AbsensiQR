package model;

public class Sekolah {
    private int id;
    private String kodeSekolah;
    private String namaSekolah;
    private String alamat;
    private String telepon;
    private String status;
    
    // Constructors
    public Sekolah() {}
    
    public Sekolah(int id, String kodeSekolah, String namaSekolah) {
        this.id = id;
        this.kodeSekolah = kodeSekolah;
        this.namaSekolah = namaSekolah;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKodeSekolah() { return kodeSekolah; }
    public void setKodeSekolah(String kodeSekolah) { this.kodeSekolah = kodeSekolah; }
    
    public String getNamaSekolah() { return namaSekolah; }
    public void setNamaSekolah(String namaSekolah) { this.namaSekolah = namaSekolah; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}