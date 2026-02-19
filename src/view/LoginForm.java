package view;

import controller.AbsensiController;
import model.Admin;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;
    private AbsensiController controller;
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Biru
    private final Color SECONDARY_COLOR = new Color(52, 73, 94);      // Abu-abu gelap
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Abu-abu muda
    private final Color TEXT_COLOR = new Color(44, 62, 80);           // Dark blue-gray
    
    public LoginForm() {
        controller = new AbsensiController();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Login - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(true); // Izinkan maximize
        
        // Main panel dengan GridBagLayout agar proporsional
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(52, 152, 219), 
                    getWidth(), getHeight(), new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1.0;
        
        // ========== PANEL KIRI (ILUSTRASI) ==========
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        
        // Konten panel kiri
        JLabel qrIconLabel = new JLabel("📱");
        qrIconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 160));
        qrIconLabel.setForeground(new Color(255, 255, 255, 150));
        
        JLabel appNameLabel = new JLabel("QR ABSENSI");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        appNameLabel.setForeground(Color.WHITE);
        
        JLabel appDescLabel = new JLabel("<html><center>Sistem Absensi Berbasis QR Code<br>untuk Sekolah dan Perusahaan</center></html>");
        appDescLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        appDescLabel.setForeground(new Color(255, 255, 255, 200));
        appDescLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(0, 0, 30, 0);
        gbcLeft.anchor = GridBagConstraints.CENTER;
        leftPanel.add(qrIconLabel, gbcLeft);
        
        gbcLeft.gridy = 1;
        leftPanel.add(appNameLabel, gbcLeft);
        
        gbcLeft.gridy = 2;
        gbcLeft.insets = new Insets(15, 0, 0, 0);
        leftPanel.add(appDescLabel, gbcLeft);
        
        // ========== PANEL KANAN (FORM LOGIN) ==========
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        
        // Form panel dengan ukuran tetap di tengah
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 15, 12, 15);
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Selamat Datang");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        welcomeLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(welcomeLabel, gbc);
        
        JLabel instructionLabel = new JLabel("Silakan login untuk melanjutkan");
        instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        instructionLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 40, 15);
        formPanel.add(instructionLabel, gbc);
        
        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 15, 5, 15);
        formPanel.add(userLabel, gbc);
        
        JPanel userPanel = new JPanel(new BorderLayout(15, 0));
        userPanel.setBackground(Color.WHITE);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        userIcon.setForeground(new Color(100, 100, 100));
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        userPanel.add(userIcon, BorderLayout.WEST);
        userPanel.add(usernameField, BorderLayout.CENTER);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(2, 15, 20, 15);
        formPanel.add(userPanel, gbc);
        
        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        passLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 15, 5, 15);
        formPanel.add(passLabel, gbc);
        
        JPanel passPanel = new JPanel(new BorderLayout(15, 0));
        passPanel.setBackground(Color.WHITE);
        
        JLabel passIcon = new JLabel("🔒");
        passIcon.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        passIcon.setForeground(new Color(100, 100, 100));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        passPanel.add(passIcon, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(2, 15, 15, 15);
        formPanel.add(passPanel, gbc);
        
        // Show password
        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setFocusPainted(false);
        showPasswordCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 15, 25, 15);
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(showPasswordCheck, gbc);
        
        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(450, 55));
        
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        loginButton.addActionListener(e -> login());
        
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);
        
        // Exit button
        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exitButton.setBackground(PRIMARY_COLOR);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setPreferredSize(new Dimension(450, 55));
        
        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin keluar dari aplikasi?",
                "Konfirmasi Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 15, 30, 15);
        formPanel.add(exitButton, gbc);
        
        // Atur formPanel agar berada di tengah rightPanel
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formPanel, gbcRight);
        
        // ========== GABUNGKAN PANEL KIRI DAN KANAN DENGAN PROPORSI 50:50 ==========
        gbcMain.gridx = 0;
        gbcMain.weightx = 0.5; // 50% lebar untuk panel kiri
        mainPanel.add(leftPanel, gbcMain);
        
        gbcMain.gridx = 1;
        gbcMain.weightx = 0.5; // 50% lebar untuk panel kanan
        mainPanel.add(rightPanel, gbcMain);
        
        add(mainPanel);
        
        // Enter key listener
        passwordField.addActionListener(e -> login());
        usernameField.addActionListener(e -> passwordField.requestFocus());
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan password harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Admin admin = controller.login(username, password);
        
        if (admin != null) {
            JOptionPane.showMessageDialog(this, 
                "Login berhasil! Selamat datang, " + admin.getNamaLengkap(), 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            new Dashboard().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Username atau password salah!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginForm().setVisible(true);
        });
    }
}