package model;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String namaLengkap;
    private int sekolahId;
    private String role;
    
    // Constructors
    public Admin() {}
    
    public Admin(int id, String username, String namaLengkap, int sekolahId, String role) {
        this.id = id;
        this.username = username;
        this.namaLengkap = namaLengkap;
        this.sekolahId = sekolahId;
        this.role = role;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    
    public int getSekolahId() { return sekolahId; }
    public void setSekolahId(int sekolahId) { this.sekolahId = sekolahId; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isSuperAdmin() {
        return "super_admin".equals(role);
    }
}