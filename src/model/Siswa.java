package model;

public class Siswa {
    private int id;
    private String nis;
    private String nama;
    private String kelas;
    private String jurusan;
    private String qrCode;
    
    public Siswa() {}
    
    public Siswa(String nis, String nama, String kelas, String jurusan) {
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    
    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
    
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}