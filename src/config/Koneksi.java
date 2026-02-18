package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {
    private static Connection connection;
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Untuk MySQL 8 ke atas (termasuk 9.6.0)
                String url = "jdbc:mysql://localhost:3306/absensi_qr?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
                String user = "root";
                String password = "";
                
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Koneksi database MySQL berhasil");
                
            } catch (SQLException e) {
                System.err.println("❌ Koneksi database gagal: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Koneksi database gagal: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("🔌 Koneksi database ditutup");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}