package utils;

import model.Sekolah;
import model.Admin;

public class SessionManager {
    private static SessionManager instance;
    private Admin currentAdmin;
    private Sekolah currentSekolah;
    private boolean isSuperAdmin;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void setLoginInfo(Admin admin, Sekolah sekolah, boolean isSuperAdmin) {
        this.currentAdmin = admin;
        this.currentSekolah = sekolah;
        this.isSuperAdmin = isSuperAdmin;
    }
    
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
    
    public Sekolah getCurrentSekolah() {
        return currentSekolah;
    }
    
    public int getSekolahId() {
        if (currentSekolah != null) {
            return currentSekolah.getId();
        }
        return 0; // 0 untuk super admin (semua data)
    }
    
    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }
    
    public void logout() {
        currentAdmin = null;
        currentSekolah = null;
        isSuperAdmin = false;
    }
}