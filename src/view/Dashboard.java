package view;

import controller.AbsensiController;
import controller.LaporanController;
import model.Siswa;
import utils.SessionManager;
import model.Admin;
import model.Sekolah;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Dashboard extends JFrame {
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Biru
    private final Color GRADIENT_START = new Color(52, 152, 219);     // Biru muda
    private final Color GRADIENT_END = new Color(41, 128, 185);       // Biru tua
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Abu-abu muda
    private final Color NOTIF_COLOR = new Color(231, 76, 60);         // Merah untuk notifikasi
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel welcomeLabel;
    private JLabel dateTimeLabel;
    private Timer clockTimer;
    private Timer notifTimer;
    private AbsensiController controller;
    private LaporanController laporanController;
    
    // Komponen untuk statistik
    private JLabel totalSiswaValue;
    private JLabel hadirHariIniValue;
    private JLabel belumAbsenValue;
    private JLabel totalAbsensiValue;
    
    // Referensi ke form
    private FormSiswa formSiswa;
    private FormAbsensi formAbsensi;
    private FormRiwayat formRiwayat;
    private FormRekapSemester formRekapSemester;
    private JFrame formPengaturan;
    private JFrame formLaporan;
    
    // Referensi untuk form super admin
    private JFrame formKelolaSekolah;
    private JFrame formKelolaAdmin;
    
    // Komponen sidebar
    private JButton btnLaporan;
    
    public Dashboard() {
        controller = new AbsensiController();
        laporanController = new LaporanController();
        initComponents();
        startClock();
        loadStatistics();
        startNotifChecker();
        setupWindowFocusListener();
    }
    
    private void initComponents() {
        setTitle("Dashboard - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Sidebar Kiri
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        // Content Panel Kanan
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, GRADIENT_START, 
                    0, getHeight(), GRADIENT_END
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(280, 750));
        sidebar.setLayout(new BorderLayout());
        
        // Header Sidebar
        JPanel headerPanel = createSidebarHeader();
        
        // Menu Buttons - Berbeda berdasarkan role
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 5, 15);
        
        // Cek role dari session
        boolean isSuperAdmin = SessionManager.getInstance().isSuperAdmin();
        
        if (isSuperAdmin) {
            // ========== MENU SUPER ADMIN ==========
            JButton btnDashboardGlobal = createSidebarButton("🏠 DASHBOARD GLOBAL", new Color(52, 152, 219));
            JButton btnKelolaSekolah = createSidebarButton("🏫 KELOLA SEKOLAH", new Color(46, 204, 113));
            JButton btnKelolaAdmin = createSidebarButton("👥 KELOLA ADMIN", new Color(155, 89, 182));
            
            // Button Laporan dengan notifikasi
            btnLaporan = createSidebarButton("📋 LAPORAN", new Color(241, 196, 15));
            
            JButton btnPengaturan = createSidebarButton("⚙️ PENGATURAN", new Color(52, 73, 94));
            
            btnDashboardGlobal.addActionListener(e -> showDashboardGlobal());
            btnKelolaSekolah.addActionListener(e -> showKelolaSekolah());
            btnKelolaAdmin.addActionListener(e -> showKelolaAdmin());
            btnLaporan.addActionListener(e -> showLaporan());
            btnPengaturan.addActionListener(e -> showPengaturan());
            
            gbc.gridy = 0; menuPanel.add(btnDashboardGlobal, gbc);
            gbc.gridy = 1; menuPanel.add(btnKelolaSekolah, gbc);
            gbc.gridy = 2; menuPanel.add(btnKelolaAdmin, gbc);
            gbc.gridy = 3; menuPanel.add(btnLaporan, gbc);
            gbc.gridy = 4; menuPanel.add(btnPengaturan, gbc);
            
        } else {
            // ========== MENU ADMIN SEKOLAH ==========
            JButton btnDashboard = createSidebarButton("🏠 DASHBOARD", new Color(52, 152, 219));
            JButton btnSiswa = createSidebarButton("👥 DATA SISWA", new Color(46, 204, 113));
            JButton btnAbsensi = createSidebarButton("📷 ABSENSI QR", new Color(155, 89, 182));
            
            // Button Laporan untuk admin sekolah
            btnLaporan = createSidebarButton("📋 LAPORAN", new Color(241, 196, 15));
            
            JButton btnRekap = createSidebarButton("📅 REKAP SEMESTER", new Color(230, 126, 34));
            
            btnDashboard.addActionListener(e -> showDashboardContent());
            btnSiswa.addActionListener(e -> showFormSiswa());
            btnAbsensi.addActionListener(e -> showFormAbsensi());
            btnLaporan.addActionListener(e -> showLaporan());
            btnRekap.addActionListener(e -> showRekapSemester());
            
            gbc.gridy = 0; menuPanel.add(btnDashboard, gbc);
            gbc.gridy = 1; menuPanel.add(btnSiswa, gbc);
            gbc.gridy = 2; menuPanel.add(btnAbsensi, gbc);
            gbc.gridy = 3; menuPanel.add(btnLaporan, gbc);
            gbc.gridy = 4; menuPanel.add(btnRekap, gbc);
        }
        
        // Footer dengan info user dan logout
        JPanel footerPanel = createSidebarFooter();
        
        sidebar.add(headerPanel, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(footerPanel, BorderLayout.SOUTH);
        
        return sidebar;
    }
    
    private JPanel createSidebarHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(280, 150));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel logoLabel = new JLabel("📱 QR ABSENSI");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(255, 255, 255, 180));
        
        // Info sekolah jika admin biasa
        String subtitle = "";
        if (!SessionManager.getInstance().isSuperAdmin()) {
            Sekolah sekolah = SessionManager.getInstance().getCurrentSekolah();
            if (sekolah != null) {
                subtitle = sekolah.getNamaSekolah();
            }
        } else {
            subtitle = "Super Administrator";
        }
        
        JLabel infoLabel = new JLabel(subtitle);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(255, 255, 255, 200));
        
        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setOpaque(false);
        textPanel.add(logoLabel);
        textPanel.add(versionLabel);
        textPanel.add(infoLabel);
        
        headerPanel.add(textPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createSidebarFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setPreferredSize(new Dimension(280, 120));
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        Admin admin = SessionManager.getInstance().getCurrentAdmin();
        String userName = (admin != null) ? admin.getNamaLengkap() : "Admin";
        String role = SessionManager.getInstance().isSuperAdmin() ? "Super Admin" : "Admin Sekolah";
        
        JLabel userLabel = new JLabel("👤 " + userName);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleLabel.setForeground(new Color(255, 255, 255, 200));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createVerticalStrut(3));
        userInfoPanel.add(roleLabel);
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoutPanel.setOpaque(false);
        
        JButton btnLogout = createSidebarButton("🚪 LOGOUT", new Color(192, 57, 43));
        btnLogout.setPreferredSize(new Dimension(250, 40));
        btnLogout.addActionListener(e -> logout());
        
        logoutPanel.add(btnLogout);
        
        footerPanel.add(userInfoPanel, BorderLayout.NORTH);
        footerPanel.add(logoutPanel, BorderLayout.CENTER);
        
        return footerPanel;
    }
    
    private JButton createSidebarButton(String text, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(250, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setOpaque(true);
                button.setBackground(hoverColor);
                button.setContentAreaFilled(true);
            }
            public void mouseExited(MouseEvent e) {
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        });
        
        return button;
    }
    
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        String welcomeText = "Selamat Datang";
        Admin admin = SessionManager.getInstance().getCurrentAdmin();
        if (admin != null) {
            welcomeText += ", " + admin.getNamaLengkap() + "!";
        } else {
            welcomeText += ", Admin!";
        }
        
        welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateTimeLabel.setForeground(Color.GRAY);
        
        topBar.add(welcomeLabel, BorderLayout.WEST);
        topBar.add(dateTimeLabel, BorderLayout.EAST);
        
        // Content panel dengan CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Buat instance form (tapi tidak ditampilkan sebagai JFrame)
        if (!SessionManager.getInstance().isSuperAdmin()) {
            formSiswa = new FormSiswa();
            formAbsensi = new FormAbsensi();
            formRiwayat = new FormRiwayat();
            formRekapSemester = new FormRekapSemester();
            
            // Ubah form dari JFrame menjadi JPanel
            JPanel siswaPanel = convertFormToPanel(formSiswa);
            JPanel absensiPanel = convertFormToPanel(formAbsensi);
            JPanel riwayatPanel = convertFormToPanel(formRiwayat);
            JPanel rekapPanel = convertFormToPanel(formRekapSemester);
            
            // Tambahkan panel konten
            contentPanel.add(createDashboardContent(), "DASHBOARD");
            contentPanel.add(siswaPanel, "SISWA");
            contentPanel.add(absensiPanel, "ABSENSI");
            contentPanel.add(riwayatPanel, "RIWAYAT");
            contentPanel.add(rekapPanel, "REKAP");
        } else {
            // Untuk super admin, kita akan menambahkan panel nanti saat menu diklik
            contentPanel.add(createDashboardGlobalContent(), "DASHBOARD_GLOBAL");
        }
        
        // Tampilkan dashboard pertama kali
        if (SessionManager.getInstance().isSuperAdmin()) {
            cardLayout.show(contentPanel, "DASHBOARD_GLOBAL");
        } else {
            cardLayout.show(contentPanel, "DASHBOARD");
        }
        
        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);
        
        return rightPanel;
    }
    
    private JPanel convertFormToPanel(JFrame form) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        Container contentPane = form.getContentPane();
        panel.add(contentPane, BorderLayout.CENTER);
        
        form.dispose();
        
        return panel;
    }
    
    private JPanel createDashboardContent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Statistic cards
        JPanel card1 = createStatCard("📊 Total Siswa", "0", new Color(52, 152, 219));
        JPanel card2 = createStatCard("✅ Hadir Hari Ini", "0", new Color(46, 204, 113));
        JPanel card3 = createStatCard("⏰ Belum Absen", "0", new Color(241, 196, 15));
        JPanel card4 = createStatCard("📅 Total Absensi", "0", new Color(155, 89, 182));
        
        // Simpan reference ke label value
        totalSiswaValue = (JLabel) ((BorderLayout) card1.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        hadirHariIniValue = (JLabel) ((BorderLayout) card2.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        belumAbsenValue = (JLabel) ((BorderLayout) card3.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        totalAbsensiValue = (JLabel) ((BorderLayout) card4.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(card1, gbc);
        gbc.gridx = 1;
        panel.add(card2, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(card3, gbc);
        gbc.gridx = 1;
        panel.add(card4, gbc);
        
        // Panel informasi
        JPanel infoCard = new JPanel(new BorderLayout());
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        infoCard.setPreferredSize(new Dimension(800, 300));
        
        Sekolah sekolah = SessionManager.getInstance().getCurrentSekolah();
        String infoText = "<html><h3 style='font-size:14px; margin-bottom:6px; text-align:center;'>Sistem Absensi QR Code</h3>";
        if (sekolah != null) {
            infoText += "<p><b>" + sekolah.getNamaSekolah() + "</b></p>";
        }
        infoText += "<p>Total siswa terdaftar: <b>" + controller.getAllSiswa().size() + "</b> orang</p>"
    + "<p>Gunakan menu di samping untuk mengakses fitur:</p>"
    + "<ul style='margin-left:0px; padding-left:0px; list-style-position:inside;'>"
    + "<li><b>Data Siswa</b> - Kelola data siswa</li>"
    + "<li><b>Absensi QR</b> - Scan QR Code</li>"
    + "<li><b>Laporan</b> - Kirim laporan/pengaduan</li>"
    + "</ul></html>";
        
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(80, 80, 80));
        
        infoCard.add(infoLabel, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(infoCard, gbc);
        
        return panel;
    }
    
    private JPanel createDashboardGlobalContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("DASHBOARD GLOBAL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        panel.add(title, BorderLayout.NORTH);
        
        // Statistik global
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        
        // Ambil data dari database untuk semua sekolah
        int totalSekolah = getTotalSekolah();
        int totalSiswa = getTotalSiswaAll();
        int totalAbsensi = getTotalAbsensiAll();
        int totalAdmin = getTotalAdmin();
        int totalLaporanBaru = laporanController.getJumlahLaporanBaru();
        
        statsPanel.add(createStatCard("🏫 TOTAL SEKOLAH", String.valueOf(totalSekolah), PRIMARY_COLOR));
        statsPanel.add(createStatCard("👥 TOTAL SISWA", String.valueOf(totalSiswa), new Color(46, 204, 113)));
        statsPanel.add(createStatCard("📊 TOTAL ABSENSI", String.valueOf(totalAbsensi), new Color(155, 89, 182)));
        statsPanel.add(createStatCard("📋 LAPORAN BARU", String.valueOf(totalLaporanBaru), NOTIF_COLOR));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadStatistics() {
        if (SessionManager.getInstance().isSuperAdmin()) return;
        
        try {
            List<Siswa> semuaSiswa = controller.getAllSiswa();
            int totalSiswa = semuaSiswa.size();
            
            String today = LocalDate.now().toString();
            List<Object[]> absensiHariIni = controller.getRiwayatAbsensi(today);
            int hadirHariIni = absensiHariIni.size();
            
            totalSiswaValue.setText(String.valueOf(totalSiswa));
            hadirHariIniValue.setText(String.valueOf(hadirHariIni));
            belumAbsenValue.setText(String.valueOf(totalSiswa - hadirHariIni));
            
            int totalAbsensi = 0;
            String[] sampleDates = {
                LocalDate.now().toString(),
                LocalDate.now().minusDays(1).toString(),
                LocalDate.now().minusDays(2).toString()
            };
            
            for (String date : sampleDates) {
                totalAbsensi += controller.getRiwayatAbsensi(date).size();
            }
            totalAbsensiValue.setText(String.valueOf(totalAbsensi));
            
        } catch (Exception e) {
            e.printStackTrace();
            totalSiswaValue.setText("0");
            hadirHariIniValue.setText("0");
            belumAbsenValue.setText("0");
            totalAbsensiValue.setText("0");
        }
    }
    
    // ========== METHOD UNTUK NOTIFIKASI LAPORAN ==========
    private void startNotifChecker() {
        if (SessionManager.getInstance().isSuperAdmin()) {
            // Cek setiap 5 detik
            notifTimer = new Timer(5000, e -> {
                updateNotifikasiLaporan();
            });
            notifTimer.start();
            
            // Cek langsung saat pertama kali login
            updateNotifikasiLaporan();
        }
    }

    // Method static untuk dipanggil dari controller lain
    public static void notifikasiLaporanBaru() {
        // Cari semua instance Dashboard yang sedang aktif
        for (Window window : Window.getWindows()) {
            if (window instanceof Dashboard) {
                Dashboard dashboard = (Dashboard) window;
                dashboard.updateNotifikasiLaporan();
            }
        }
    }

    private void setupWindowFocusListener() {
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (SessionManager.getInstance().isSuperAdmin()) {
                    updateNotifikasiLaporan();
                }
            }
        });
    }
    
    private void updateNotifikasiLaporan() {
        if (SessionManager.getInstance().isSuperAdmin()) {
            int jumlahBaru = laporanController.getJumlahLaporanBaru();
            
            // Update button text
            if (jumlahBaru > 0) {
                btnLaporan.setText("📋 LAPORAN (" + jumlahBaru + " baru) 🟥");
                
                // Efek blinking jika ada laporan baru
                Timer blinkTimer = new Timer(500, new ActionListener() {
                    int count = 0;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (count < 6) {
                            if (btnLaporan.getBackground().equals(NOTIF_COLOR)) {
                                btnLaporan.setBackground(new Color(241, 196, 15)); // Warna default
                            } else {
                                btnLaporan.setBackground(NOTIF_COLOR);
                            }
                            count++;
                        } else {
                            ((Timer)e.getSource()).stop();
                            btnLaporan.setBackground(new Color(241, 196, 15)); // Kembali ke default
                        }
                    }
                });
                blinkTimer.start();
            } else {
                btnLaporan.setText("📋 LAPORAN");
                btnLaporan.setBackground(new Color(241, 196, 15)); // Warna default
            }
        }
    }
    
    // ========== METHOD UNTUK SUPER ADMIN ==========
    private int getTotalSekolah() {
        // TODO: Implement method to get total sekolah from database
        return 1;
    }
    
    private int getTotalSiswaAll() {
        // TODO: Implement method to get total all siswa from database
        return 270;
    }
    
    private int getTotalAbsensiAll() {
        // TODO: Implement method to get total all absensi from database
        return 9;
    }
    
    private int getTotalAdmin() {
        // TODO: Implement method to get total admin from database
        return 2;
    }
    
    private void showDashboardGlobal() {
        cardLayout.show(contentPanel, "DASHBOARD_GLOBAL");
        welcomeLabel.setText("Dashboard Global - Super Admin");
    }
    
    private void showKelolaSekolah() {
        if (formKelolaSekolah == null) {
            formKelolaSekolah = new FormKelolaSekolah();
            JPanel sekolahPanel = convertFormToPanel(formKelolaSekolah);
            contentPanel.add(sekolahPanel, "KELOLA_SEKOLAH");
        }
        cardLayout.show(contentPanel, "KELOLA_SEKOLAH");
        welcomeLabel.setText("Kelola Data Sekolah");
    }
    
    private void showKelolaAdmin() {
        if (formKelolaAdmin == null) {
            formKelolaAdmin = new FormKelolaAdmin();
            JPanel adminPanel = convertFormToPanel(formKelolaAdmin);
            contentPanel.add(adminPanel, "KELOLA_ADMIN");
        }
        cardLayout.show(contentPanel, "KELOLA_ADMIN");
        welcomeLabel.setText("Kelola Data Admin");
    }
    
    private void showLaporan() {
        if (formLaporan == null) {
            formLaporan = new FormLaporan();
            JPanel laporanPanel = convertFormToPanel(formLaporan);
            contentPanel.add(laporanPanel, "LAPORAN");
        }
        cardLayout.show(contentPanel, "LAPORAN");
        welcomeLabel.setText("Laporan / Pengaduan");
        
        // Refresh notifikasi setelah melihat laporan
        if (SessionManager.getInstance().isSuperAdmin()) {
            updateNotifikasiLaporan();
        }
    }
    
    private void showPengaturan() {
        if (formPengaturan == null) {
            formPengaturan = new FormPengaturan();
            JPanel pengaturanPanel = convertFormToPanel(formPengaturan);
            contentPanel.add(pengaturanPanel, "PENGATURAN");
        }
        cardLayout.show(contentPanel, "PENGATURAN");
        welcomeLabel.setText("Pengaturan Sistem");
    }
    
    // ========== METHOD UNTUK ADMIN SEKOLAH ==========
    private void showDashboardContent() {
        cardLayout.show(contentPanel, "DASHBOARD");
        welcomeLabel.setText("Dashboard Utama");
        loadStatistics();
    }
    
    private void showFormSiswa() {
        cardLayout.show(contentPanel, "SISWA");
        welcomeLabel.setText("Manajemen Data Siswa");
        if (formSiswa != null) {
            formSiswa.loadData();
        }
    }
    
    private void showFormAbsensi() {
        cardLayout.show(contentPanel, "ABSENSI");
        welcomeLabel.setText("Absensi QR Code");
        
        if (formAbsensi != null) {
            formAbsensi.initializeForDisplay();
        }
    }
    
    private void showRiwayatContent() {
        cardLayout.show(contentPanel, "RIWAYAT");
        welcomeLabel.setText("Riwayat Absensi");
    }
    
    private void showRekapSemester() {
        cardLayout.show(contentPanel, "REKAP");
        welcomeLabel.setText("Rekap Absensi Semester");
    }
    
    private void startClock() {
        clockTimer = new Timer(1000, e -> {
            dateTimeLabel.setText(new java.util.Date().toLocaleString());
        });
        clockTimer.start();
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin logout?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            clockTimer.stop();
            if (notifTimer != null) {
                notifTimer.stop();
            }
            SessionManager.getInstance().logout();
            new LoginForm().setVisible(true);
            dispose();
        }
    }
}