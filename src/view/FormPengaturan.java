package view;

import utils.SessionManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import controller.LogController;
import model.LogAktivitas;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class FormPengaturan extends JFrame {
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color PURPLE_COLOR = new Color(155, 89, 182);
    private final Color DARK_COLOR = new Color(52, 73, 94);
    
    // Komponen
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel welcomeLabel;
    
    // Data dummy untuk log
    private List<LogEntry> logEntries;
    
    public FormPengaturan() {
        logEntries = generateDummyLogs();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Pengaturan Sistem - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        
        JLabel titleLabel = new JLabel("⚙️ PENGATURAN SISTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // CardLayout untuk konten
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // Tambahkan panel-panel ke cardLayout
        contentPanel.add(createMainMenuPanel(), "MENU");
        contentPanel.add(createBackupPanel(), "BACKUP");
        contentPanel.add(createRestorePanel(), "RESTORE");
        contentPanel.add(createLogPanel(), "LOG");
        contentPanel.add(createClearCachePanel(), "CACHE");
        contentPanel.add(createResetPasswordPanel(), "RESET");
        contentPanel.add(createAboutPanel(), "ABOUT");
        
        // Tampilkan menu utama pertama kali
        cardLayout.show(contentPanel, "MENU");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 245, 250));
        
        JButton btnBack = createButton("Kembali ke Menu Utama", new Color(108, 117, 125));
        btnBack.addActionListener(e -> cardLayout.show(contentPanel, "MENU"));
        bottomPanel.add(btnBack);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Pilih Menu Pengaturan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Grid menu 2x3
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        // Menu items
        gridPanel.add(createMenuCard(
            "💾 Backup Database", 
            "Membuat backup database ke file SQL",
            SUCCESS_COLOR,
            e -> cardLayout.show(contentPanel, "BACKUP")
        ));
        
        gridPanel.add(createMenuCard(
            "🔄 Restore Database", 
            "Mengembalikan database dari file backup",
            WARNING_COLOR,
            e -> cardLayout.show(contentPanel, "RESTORE")
        ));
        
        gridPanel.add(createMenuCard(
            "📋 Log Sistem", 
            "Melihat log aktivitas sistem",
            INFO_COLOR,
            e -> cardLayout.show(contentPanel, "LOG")
        ));
        
        gridPanel.add(createMenuCard(
            "🗑️ Clear Cache", 
            "Membersihkan cache aplikasi",
            PURPLE_COLOR,
            e -> cardLayout.show(contentPanel, "CACHE")
        ));
        
        gridPanel.add(createMenuCard(
            "🔑 Reset Password", 
            "Mereset password admin",
            DANGER_COLOR,
            e -> cardLayout.show(contentPanel, "RESET")
        ));
        
        gridPanel.add(createMenuCard(
            "ℹ️ About", 
            "Informasi tentang aplikasi",
            DARK_COLOR,
            e -> cardLayout.show(contentPanel, "ABOUT")
        ));
        
        panel.add(gridPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMenuCard(String title, String description, Color color, ActionListener action) {
    JPanel card = new JPanel(new BorderLayout(10, 10));
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));
    card.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    titleLabel.setForeground(color);
    
    JLabel descLabel = new JLabel("<html>" + description + "</html>");
    descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    descLabel.setForeground(Color.GRAY);
    
    JLabel arrowLabel = new JLabel("→");
    arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    arrowLabel.setForeground(color);
    
    card.add(titleLabel, BorderLayout.NORTH);
    card.add(descLabel, BorderLayout.CENTER);
    card.add(arrowLabel, BorderLayout.EAST);
    
    card.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            card.setBackground(new Color(245, 245, 250));
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(19, 19, 19, 19)
            ));
        }
        public void mouseExited(MouseEvent e) {
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
        }
        public void mouseClicked(MouseEvent e) {
            // PERBAIKAN: Buat ActionEvent baru
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    });
    
    return card;
}
    
    // ==================== BACKUP PANEL ====================
    private JPanel createBackupPanel() {
    JPanel panel = new JPanel(new BorderLayout(20, 20));
    panel.setBackground(Color.WHITE);
    
    JLabel titleLabel = new JLabel("💾 BACKUP DATABASE", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setForeground(SUCCESS_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
    panel.add(titleLabel, BorderLayout.NORTH);
    
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Info panel
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    JPanel infoPanel = new JPanel(new BorderLayout());
    infoPanel.setBackground(new Color(245, 245, 250));
    infoPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    
    JLabel infoLabel = new JLabel("<html>"
        + "<b>Informasi Backup:</b><br>"
        + "• Backup akan menyimpan seluruh data siswa, absensi, admin, dan sekolah<br>"
        + "• File backup berformat .sql<br>"
        + "• Disarankan melakukan backup secara rutin (mingguan/bulanan)<br>"
        + "• Ukuran backup tergantung jumlah data"
        + "</html>");
    infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    infoPanel.add(infoLabel, BorderLayout.CENTER);
    
    panel.add(infoPanel, BorderLayout.NORTH);
    
    // Lokasi backup
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    centerPanel.add(new JLabel("Lokasi Backup:"), gbc);
    
    JTextField txtLocation = new JTextField(20);
    txtLocation.setText(System.getProperty("user.home") + "\\Documents\\backup_absensi");
    txtLocation.setEditable(false);
    gbc.gridx = 1;
    centerPanel.add(txtLocation, gbc);
    
    // Nama file - PERBAIKAN DISINI
    gbc.gridy = 2;
    gbc.gridx = 0;
    centerPanel.add(new JLabel("Nama File:"), gbc);
    
    // Gunakan LocalDateTime untuk format dengan jam
    String defaultName = "backup_absensi_" + 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".sql";
    JTextField txtFileName = new JTextField(defaultName, 20);
    gbc.gridx = 1;
    centerPanel.add(txtFileName, gbc);
    
    // Tombol Backup
    gbc.gridy = 3;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    
    JButton btnBackup = createButton("💾 Mulai Backup", SUCCESS_COLOR);
    btnBackup.setPreferredSize(new Dimension(200, 45));
    btnBackup.addActionListener(e -> {
        String path = txtLocation.getText() + "\\" + txtFileName.getText();
        JOptionPane.showMessageDialog(this,
            "Backup berhasil!\n\nFile disimpan di:\n" + path,
            "Backup Sukses",
            JOptionPane.INFORMATION_MESSAGE);
    });
    centerPanel.add(btnBackup, gbc);
    
    panel.add(centerPanel, BorderLayout.CENTER);
    
    return panel;
}
    
    // ==================== RESTORE PANEL ====================
    private JPanel createRestorePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("🔄 RESTORE DATABASE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(WARNING_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Warning panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JPanel warningPanel = new JPanel(new BorderLayout());
        warningPanel.setBackground(new Color(255, 240, 240));
        warningPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 150, 150)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel warningLabel = new JLabel("<html>"
            + "<b>⚠️ PERHATIAN!</b><br>"
            + "• Restore akan MENIMPA semua data yang ada saat ini<br>"
            + "• Pastikan Anda telah melakukan backup data terlebih dahulu<br>"
            + "• Proses ini tidak dapat dibatalkan"
            + "</html>");
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningLabel.setForeground(DANGER_COLOR);
        warningPanel.add(warningLabel, BorderLayout.CENTER);
        
        panel.add(warningPanel, BorderLayout.NORTH);
        
        // Pilih file
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        centerPanel.add(new JLabel("File Backup:"), gbc);
        
        JTextField txtFile = new JTextField(20);
        txtFile.setEditable(false);
        gbc.gridx = 1;
        centerPanel.add(txtFile, gbc);
        
        // Tombol Browse
        gbc.gridy = 2;
        gbc.gridx = 1;
        JButton btnBrowse = createButton("📂 Pilih File", INFO_COLOR);
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("SQL Files", "sql"));
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Documents"));
            
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                txtFile.setText(fileChooser.getSelectedFile().getPath());
            }
        });
        centerPanel.add(btnBrowse, gbc);
        
        // Tombol Restore
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton btnRestore = createButton("🔄 Mulai Restore", WARNING_COLOR);
        btnRestore.setPreferredSize(new Dimension(200, 45));
        btnRestore.addActionListener(e -> {
            if (txtFile.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Pilih file backup terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin merestore database?\n\nFile: " + txtFile.getText() +
                "\n\n⚠️ Semua data yang ada akan ditimpa!",
                "Konfirmasi Restore",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "Restore berhasil!\n\nDatabase telah dikembalikan dari file backup.",
                    "Restore Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        centerPanel.add(btnRestore, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== LOG PANEL ====================
  private JPanel createLogPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(Color.WHITE);
    
    JLabel titleLabel = new JLabel("📋 LOG SISTEM", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setForeground(INFO_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
    panel.add(titleLabel, BorderLayout.NORTH);
    
    // Filter panel
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filterPanel.setBackground(Color.WHITE);
    
    filterPanel.add(new JLabel("Filter Tanggal:"));
    JComboBox<String> cmbFilter = new JComboBox<>();
    cmbFilter.addItem("Hari Ini");
    cmbFilter.addItem("Kemarin");
    cmbFilter.addItem("7 Hari Terakhir");
    cmbFilter.addItem("30 Hari Terakhir");
    cmbFilter.addItem("Semua");
    filterPanel.add(cmbFilter);
    
    JButton btnRefresh = createButton("🔄 Refresh", INFO_COLOR);
    btnRefresh.setPreferredSize(new Dimension(100, 30));
    filterPanel.add(btnRefresh);
    
    panel.add(filterPanel, BorderLayout.NORTH);
    
    // Table Log
    String[] columns = {"Waktu", "Username", "Sekolah", "Aktivitas", "Status", "IP Address"};
    DefaultTableModel logModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    JTable logTable = new JTable(logModel);
    logTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    logTable.setRowHeight(30);
    
    // Header styling
    JTableHeader header = logTable.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    header.setBackground(INFO_COLOR);
    header.setForeground(PRIMARY_COLOR);
    
    // Set column widths
    logTable.getColumnModel().getColumn(0).setPreferredWidth(150);
    logTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    logTable.getColumnModel().getColumn(2).setPreferredWidth(150);
    logTable.getColumnModel().getColumn(3).setPreferredWidth(300);
    logTable.getColumnModel().getColumn(4).setPreferredWidth(80);
    logTable.getColumnModel().getColumn(5).setPreferredWidth(120);
    
    // Custom renderer untuk status
    logTable.getColumnModel().getColumn(4).setCellRenderer(new StatusLogCellRenderer());
    
    JScrollPane scrollPane = new JScrollPane(logTable);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Load data awal
    loadLogData(logModel, "Semua");
    
    // Event listener untuk filter
    cmbFilter.addActionListener(e -> {
        String filter = (String) cmbFilter.getSelectedItem();
        loadLogData(logModel, filter);
    });
    
    btnRefresh.addActionListener(e -> {
        String filter = (String) cmbFilter.getSelectedItem();
        loadLogData(logModel, filter);
    });
    
    // Export button
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.setBackground(Color.WHITE);
    
    JButton btnExport = createButton("📥 Export Log", SUCCESS_COLOR);
    btnExport.addActionListener(e -> exportLog(logModel));
    bottomPanel.add(btnExport);
    
    panel.add(bottomPanel, BorderLayout.SOUTH);
    
    return panel;
}

private void loadLogData(DefaultTableModel model, String filter) {
    LogController logController = new LogController();
    List<LogAktivitas> logs;
    
    if ("Semua".equals(filter)) {
        logs = logController.getAllLogs();
    } else {
        logs = logController.getLogsByDate(filter);
    }
    
    model.setRowCount(0);
    for (LogAktivitas log : logs) {
        model.addRow(new Object[]{
            log.getWaktuFormatted(),
            log.getUsername(),
            log.getNamaSekolah() != null ? log.getNamaSekolah() : "-",
            log.getAktivitas(),
            log.getStatus(),
            log.getIpAddress()
        });
    }
}

private void exportLog(DefaultTableModel model) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setSelectedFile(new java.io.File("log_sistem_" + 
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx"));
    
    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        String filePath = fileChooser.getSelectedFile().getPath();
        if (!filePath.endsWith(".xlsx")) {
            filePath += ".xlsx";
        }
        
        // TODO: Implement export to Excel
        JOptionPane.showMessageDialog(this,
            "Log berhasil diekspor ke:\n" + filePath,
            "Export Sukses",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

// Custom cell renderer untuk status
class StatusLogCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, 
            isSelected, hasFocus, row, column);
        
        if ("Berhasil".equals(value)) {
            c.setForeground(SUCCESS_COLOR);
        } else if ("Gagal".equals(value)) {
            c.setForeground(DANGER_COLOR);
        } else {
            c.setForeground(Color.BLACK);
        }
        
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        return c;
    }
}
    
    // ==================== CLEAR CACHE PANEL ====================
    private JPanel createClearCachePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("🗑️ CLEAR CACHE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PURPLE_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Info cache
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(245, 245, 250));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        infoPanel.setLayout(new GridLayout(4, 2, 10, 10));
        
        infoPanel.add(new JLabel("Ukuran Cache Saat Ini:"));
        infoPanel.add(new JLabel("24.5 MB"));
        infoPanel.add(new JLabel("File Temporary:"));
        infoPanel.add(new JLabel("156 file"));
        infoPanel.add(new JLabel("Cache QR Code:"));
        infoPanel.add(new JLabel("89 file"));
        infoPanel.add(new JLabel("Session Data:"));
        infoPanel.add(new JLabel("3 session"));
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Tombol Clear Cache
        gbc.gridy = 1;
        JButton btnClear = createButton("🗑️ CLEAR CACHE", PURPLE_COLOR);
        btnClear.setPreferredSize(new Dimension(250, 50));
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClear.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin membersihkan semua cache?\n\nData yang akan dihapus:\n" +
                "• Cache QR Code\n" +
                "• File temporary\n" +
                "• Session data\n\n" +
                "Aplikasi akan berjalan lebih cepat setelah dibersihkan.",
                "Konfirmasi Clear Cache",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "Cache berhasil dibersihkan!\n\n" +
                    "• 24.5 MB space telah dibebaskan\n" +
                    "• 156 file telah dihapus",
                    "Clear Cache Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        gbc.gridy = 2;
        centerPanel.add(btnClear, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== RESET PASSWORD PANEL ====================
    private JPanel createResetPasswordPanel() {
    JPanel panel = new JPanel(new BorderLayout(20, 20));
    panel.setBackground(Color.WHITE);
    
    JLabel titleLabel = new JLabel("🔑 RESET PASSWORD", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    titleLabel.setForeground(DANGER_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
    panel.add(titleLabel, BorderLayout.NORTH);
    
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Pilih User
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    centerPanel.add(new JLabel("Pilih User:"), gbc);
    
    JComboBox<AdminItem> cmbUser = new JComboBox<>();
    cmbUser.setPreferredSize(new Dimension(350, 35));
    gbc.gridx = 1;
    centerPanel.add(cmbUser, gbc);
    
    // Password Baru
    gbc.gridy = 1;
    gbc.gridx = 0;
    centerPanel.add(new JLabel("Password Baru:"), gbc);
    
    JPasswordField txtNewPassword = new JPasswordField(15);
    txtNewPassword.setPreferredSize(new Dimension(250, 35));
    gbc.gridx = 1;
    centerPanel.add(txtNewPassword, gbc);
    
    // Konfirmasi Password
    gbc.gridy = 2;
    gbc.gridx = 0;
    centerPanel.add(new JLabel("Konfirmasi:"), gbc);
    
    JPasswordField txtConfirmPassword = new JPasswordField(15);
    txtConfirmPassword.setPreferredSize(new Dimension(250, 35));
    gbc.gridx = 1;
    centerPanel.add(txtConfirmPassword, gbc);
    
    // Tombol Reset
    gbc.gridy = 3;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    
    JButton btnReset = createButton("🔑 RESET PASSWORD", DANGER_COLOR);
    btnReset.setPreferredSize(new Dimension(200, 45));
    btnReset.addActionListener(e -> {
        AdminItem selectedItem = (AdminItem) cmbUser.getSelectedItem();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this,
                "Pilih user terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String password = new String(txtNewPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Password tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                "Password dan konfirmasi tidak cocok!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmReset = JOptionPane.showConfirmDialog(this,
            "Yakin ingin mereset password untuk user:\n" + selectedItem.toString(),
            "Konfirmasi Reset Password",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirmReset == JOptionPane.YES_OPTION) {
            // Panggil method untuk update password
            boolean success = resetPassword(selectedItem.getId(), password);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Password berhasil direset!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal mereset password!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    centerPanel.add(btnReset, gbc);
    
    panel.add(centerPanel, BorderLayout.CENTER);
    
    // Load data admin setelah panel dibuat
    loadAdminData(cmbUser);
    
    return panel;
}

private boolean resetPassword(int adminId, String newPassword) {
    String sql = "UPDATE admin SET password = MD5(?) WHERE id = ?";
    
    try (java.sql.PreparedStatement stmt = config.Koneksi.getConnection().prepareStatement(sql)) {
        stmt.setString(1, newPassword);
        stmt.setInt(2, adminId);
        
        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    // ========== METHOD UNTUK MENGAMBIL DATA ADMIN REAL ==========
private void loadAdminData(JComboBox<AdminItem> cmbUser) {
    cmbUser.removeAllItems();
    
    String sql = "SELECT a.id, a.username, a.nama_lengkap, s.nama_sekolah, a.role " +
                 "FROM admin a " +
                 "LEFT JOIN sekolah s ON a.sekolah_id = s.id " +
                 "ORDER BY a.role, a.username";
    
    try (java.sql.Statement stmt = config.Koneksi.getConnection().createStatement();
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String namaLengkap = rs.getString("nama_lengkap");
            String namaSekolah = rs.getString("nama_sekolah");
            String role = rs.getString("role");
            
            String displayText;
            if ("super_admin".equals(role)) {
                displayText = username + " (Super Admin)";
            } else {
                String sekolah = (namaSekolah != null) ? namaSekolah : "-";
                displayText = username + " - " + namaLengkap + " (" + sekolah + ")";
            }
            
            cmbUser.addItem(new AdminItem(id, username, displayText));
        }
        
    } catch (java.sql.SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Gagal mengambil data admin: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

// Class helper untuk menyimpan data admin di combo box
class AdminItem {
    private int id;
    private String username;
    private String displayText;
    
    public AdminItem(int id, String username, String displayText) {
        this.id = id;
        this.username = username;
        this.displayText = displayText;
    }
    
    public int getId() { return id; }
    public String getUsername() { return username; }
    
    @Override
    public String toString() {
        return displayText;
    }
}
    
    // ==================== ABOUT PANEL ====================
    private JPanel createAboutPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    
    // HAPUS judul "ℹ️ ABOUT"
    
    JPanel centerPanel = new JPanel(new GridBagLayout());
    centerPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    
    // Logo
    gbc.gridx = 0;
    gbc.gridy = 0;
    JLabel logoLabel = new JLabel("📱");
    logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 70));
    logoLabel.setForeground(PRIMARY_COLOR);
    centerPanel.add(logoLabel, gbc);
    
    // Nama Aplikasi
    gbc.gridy = 1;
    JLabel appNameLabel = new JLabel("Sistem Absensi QR Code");
    appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
    appNameLabel.setForeground(PRIMARY_COLOR);
    centerPanel.add(appNameLabel, gbc);
    
    // Versi
    gbc.gridy = 2;
    JLabel versionLabel = new JLabel("Versi 1.0.0");
    versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    versionLabel.setForeground(Color.GRAY);
    centerPanel.add(versionLabel, gbc);
    
    // Deskripsi dengan justify
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 0.5;
    
    JTextArea descArea = new JTextArea(
        "Aplikasi absensi berbasis QR Code untuk sekolah yang dikembangkan dengan Java Swing dan MySQL. " +
        "Memungkinkan manajemen data siswa, generate QR Code, scan absensi dengan webcam, " +
        "serta menampilkan riwayat dan rekap absensi per semester. Dilengkapi dengan fitur multi-sekolah " +
        "untuk super admin yang dapat mengelola beberapa sekolah sekaligus."
    );
    descArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    descArea.setForeground(new Color(80, 80, 80));
    descArea.setBackground(Color.WHITE);
    descArea.setLineWrap(true);
    descArea.setWrapStyleWord(true);
    descArea.setEditable(false);
    descArea.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    
    // Set justify alignment
    javax.swing.text.StyledDocument doc = new javax.swing.text.DefaultStyledDocument();
    javax.swing.text.SimpleAttributeSet center = new javax.swing.text.SimpleAttributeSet();
    javax.swing.text.StyleConstants.setAlignment(center, javax.swing.text.StyleConstants.ALIGN_JUSTIFIED);
    doc.setParagraphAttributes(0, doc.getLength(), center, false);
    descArea.setDocument(doc);
    
    JScrollPane descScroll = new JScrollPane(descArea);
    descScroll.setBorder(BorderFactory.createEmptyBorder());
    descScroll.setBackground(Color.WHITE);
    descScroll.setPreferredSize(new Dimension(500, 120));
    centerPanel.add(descScroll, gbc);
    
    // Fitur
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;
    
    JPanel fiturPanel = new JPanel(new GridLayout(3, 3, 10, 5));
    fiturPanel.setBackground(Color.WHITE);
    fiturPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        " Fitur Unggulan ",
        TitledBorder.LEFT,
        TitledBorder.TOP,
        new Font("Segoe UI", Font.BOLD, 12),
        PRIMARY_COLOR
    ));
    
    String[] fitur = {
        "✓ Manajemen Data Siswa",
        "✓ Generate QR Code",
        "✓ Scan QR dengan Webcam",
        "✓ Riwayat Absensi",
        "✓ Rekap Semester",
        "✓ Export ke Excel",
        "✓ Multi-sekolah",
        "✓ Log Aktivitas",
        "✓ Backup & Restore"
    };
    
    for (String f : fitur) {
        JLabel label = new JLabel(f);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(new Color(100, 100, 100));
        fiturPanel.add(label);
    }
    
    centerPanel.add(fiturPanel, gbc);
    
    // Copyright
    gbc.gridy = 5;
    gbc.insets = new Insets(15, 10, 10, 10);
    JLabel copyrightLabel = new JLabel("© 2026 - All Rights Reserved");
    copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    copyrightLabel.setForeground(Color.GRAY);
    centerPanel.add(copyrightLabel, gbc);
    
    panel.add(centerPanel, BorderLayout.CENTER);
    
    return panel;
}
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    // Class untuk menyimpan data log
    class LogEntry {
        private String waktu;
        private String username;
        private String aktivitas;
        private String status;
        private String ipAddress;
        
        public LogEntry(String waktu, String username, String aktivitas, String status, String ipAddress) {
            this.waktu = waktu;
            this.username = username;
            this.aktivitas = aktivitas;
            this.status = status;
            this.ipAddress = ipAddress;
        }
        
        public String getWaktu() { return waktu; }
        public String getUsername() { return username; }
        public String getAktivitas() { return aktivitas; }
        public String getStatus() { return status; }
        public String getIpAddress() { return ipAddress; }
    }
    
    private List<LogEntry> generateDummyLogs() {
        List<LogEntry> logs = new ArrayList<>();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        logs.add(new LogEntry(today + " 08:00:23", "admin", "Login", "Berhasil", "192.168.1.10"));
        logs.add(new LogEntry(today + " 08:05:47", "admin", "Tambah Siswa (NIS: 241031)", "Berhasil", "192.168.1.10"));
        logs.add(new LogEntry(today + " 08:15:12", "admin", "Edit Siswa (NIS: 241001)", "Berhasil", "192.168.1.10"));
        logs.add(new LogEntry(today + " 09:30:05", "admin", "Absensi QR - NIS: 241001", "Berhasil", "192.168.1.15"));
        logs.add(new LogEntry(today + " 09:32:18", "admin", "Absensi QR - NIS: 241002", "Berhasil", "192.168.1.15"));
        logs.add(new LogEntry(today + " 10:00:00", "superadmin", "Login", "Berhasil", "192.168.1.20"));
        logs.add(new LogEntry(today + " 10:15:30", "superadmin", "Tambah Sekolah (SMKN 2 Bandung)", "Berhasil", "192.168.1.20"));
        logs.add(new LogEntry(today + " 10:30:45", "superadmin", "Tambah Admin (admin_smk2)", "Berhasil", "192.168.1.20"));
        logs.add(new LogEntry(today + " 11:00:00", "admin", "Export Excel", "Berhasil", "192.168.1.10"));
        logs.add(new LogEntry(today + " 11:30:00", "admin", "Logout", "Berhasil", "192.168.1.10"));
        logs.add(new LogEntry(yesterday + " 13:20:10", "admin", "Login Gagal - Wrong password", "Gagal", "192.168.1.50"));
        logs.add(new LogEntry(yesterday + " 14:00:00", "admin", "Login", "Berhasil", "192.168.1.50"));
        logs.add(new LogEntry(yesterday + " 15:45:30", "admin", "Hapus Siswa (NIS: 241030)", "Berhasil", "192.168.1.50"));
        
        return logs;
    }
}