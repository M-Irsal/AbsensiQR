package view;

import controller.LaporanController;
import model.Laporan;
import utils.SessionManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FormLaporan extends JFrame {
    
    private JTable tableLaporan;
    private DefaultTableModel tableModel;
    private JTextArea txtDeskripsi;
    private JTextField txtJudul;
    private JComboBox<String> cmbKategori;
    private JLabel lblLampiran;
    private String selectedLampiran;
    private LaporanController controller;
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color BARU_COLOR = new Color(231, 76, 60);      // Merah
    private final Color DIPROSES_COLOR = new Color(241, 196, 15); // Kuning
    private final Color SELESAI_COLOR = new Color(46, 204, 113);  // Hijau
    
    public FormLaporan() {
        controller = new LaporanController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
    setTitle("Laporan / Pengaduan - Sistem Absensi QR");
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
    
    String judul = SessionManager.getInstance().isSuperAdmin() 
        ? "📋 DAFTAR LAPORAN MASUK" 
        : "📋 LAPORAN / PENGADUAN";
    
    JLabel titleLabel = new JLabel(judul, SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    titleLabel.setForeground(PRIMARY_COLOR);
    topPanel.add(titleLabel, BorderLayout.CENTER);
    
    mainPanel.add(topPanel, BorderLayout.NORTH);
    
    // Cek role
    boolean isSuperAdmin = SessionManager.getInstance().isSuperAdmin();
    
    if (isSuperAdmin) {
        // Untuk super admin: hanya tabel (full width)
        JPanel rightPanel = createTablePanel(true); // true = mode super admin
        rightPanel.setPreferredSize(new Dimension(1050, 550));
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    } else {
        // Untuk admin sekolah: split pane (form input + tabel)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setBorder(null);
        
        // Left panel - Form input
        JPanel leftPanel = createInputPanel();
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel - Table
        JPanel rightPanel = createTablePanel(false);
        splitPane.setRightComponent(rightPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }
    
    add(mainPanel);
}
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel titleLabel = new JLabel("BUAT LAPORAN BARU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Judul
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Judul:"), gbc);
        
        txtJudul = new JTextField();
        txtJudul.setPreferredSize(new Dimension(250, 35));
        txtJudul.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        panel.add(txtJudul, gbc);
        
        // Kategori
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Kategori:"), gbc);
        
        cmbKategori = new JComboBox<>();
        cmbKategori.addItem("Bug");           // Sesuai dengan ENUM
        cmbKategori.addItem("Reset Password"); // Sesuai dengan ENUM
        cmbKategori.addItem("Permintaan Fitur"); // Sesuai dengan ENUM
        cmbKategori.addItem("Lainnya");        // Sesuai dengan ENUM
        cmbKategori.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        panel.add(cmbKategori, gbc);
        
        // Deskripsi
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Deskripsi:"), gbc);
        
        txtDeskripsi = new JTextArea(5, 20);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        
        JScrollPane scrollDesk = new JScrollPane(txtDeskripsi);
        scrollDesk.setPreferredSize(new Dimension(250, 100));
        gbc.gridx = 1;
        panel.add(scrollDesk, gbc);
        
        // Lampiran
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Lampiran:"), gbc);
        
        JPanel lampiranPanel = new JPanel(new BorderLayout(5, 0));
        lampiranPanel.setBackground(Color.WHITE);
        
        lblLampiran = new JLabel("Tidak ada file");
        lblLampiran.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLampiran.setForeground(Color.GRAY);
        
        JButton btnBrowse = createButton("Pilih File", INFO_COLOR);
        btnBrowse.setPreferredSize(new Dimension(100, 30));
        btnBrowse.addActionListener(e -> pilihLampiran());
        
        lampiranPanel.add(lblLampiran, BorderLayout.CENTER);
        lampiranPanel.add(btnBrowse, BorderLayout.EAST);
        
        gbc.gridx = 1;
        panel.add(lampiranPanel, gbc);
        
        // Tombol Kirim
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        JButton btnKirim = createButton("📤 KIRIM LAPORAN", SUCCESS_COLOR);
        btnKirim.setPreferredSize(new Dimension(200, 40));
        btnKirim.addActionListener(e -> kirimLaporan());
        panel.add(btnKirim, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel(boolean isSuperAdmin) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    
    // Title berbeda berdasarkan role
    String titleText = isSuperAdmin ? "DAFTAR LAPORAN MASUK" : "DAFTAR LAPORAN SAYA";
    JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    titleLabel.setForeground(PRIMARY_COLOR);
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    panel.add(titleLabel, BorderLayout.NORTH);
    
    // Kolom berbeda untuk super admin
    String[] columns;
    if (isSuperAdmin) {
        columns = new String[]{"ID", "Tanggal", "Dari Sekolah", "Pengirim", "Judul", "Kategori", "Status"};
    } else {
        columns = new String[]{"ID", "Tanggal", "Judul", "Kategori", "Status", "Respon"};
    }
    
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    tableLaporan = new JTable(tableModel);
    tableLaporan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tableLaporan.setRowHeight(35);
    
    // Header styling
    JTableHeader header = tableLaporan.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    header.setBackground(Color.WHITE);
    header.setForeground(PRIMARY_COLOR);
    header.setDefaultRenderer(new HeaderCellRenderer(PRIMARY_COLOR));
    
    // Set column widths berdasarkan role
    if (isSuperAdmin) {
        tableLaporan.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tableLaporan.getColumnModel().getColumn(1).setPreferredWidth(120);  // Tanggal
        tableLaporan.getColumnModel().getColumn(2).setPreferredWidth(150);  // Dari Sekolah
        tableLaporan.getColumnModel().getColumn(3).setPreferredWidth(100);  // Pengirim
        tableLaporan.getColumnModel().getColumn(4).setPreferredWidth(200);  // Judul
        tableLaporan.getColumnModel().getColumn(5).setPreferredWidth(100);  // Kategori
        tableLaporan.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status
    } else {
        tableLaporan.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableLaporan.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableLaporan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableLaporan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableLaporan.getColumnModel().getColumn(4).setPreferredWidth(80);
        tableLaporan.getColumnModel().getColumn(5).setPreferredWidth(150);
    }
    
    // Custom renderer untuk status
    tableLaporan.getColumnModel().getColumn(isSuperAdmin ? 6 : 4).setCellRenderer(new StatusLaporanCellRenderer());
    
    // Double click untuk lihat detail (khusus super admin bisa update status)
    tableLaporan.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                lihatDetailLaporan(isSuperAdmin);
            }
        }
    });
    
    JScrollPane scrollPane = new JScrollPane(tableLaporan);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Info panel
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    infoPanel.setBackground(Color.WHITE);
    
    String infoText = isSuperAdmin 
        ? "Klik 2x untuk memproses laporan" 
        : "Klik 2x untuk lihat detail";
    
    JLabel infoLabel = new JLabel(infoText);
    infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    infoLabel.setForeground(Color.GRAY);
    infoPanel.add(infoLabel);
    panel.add(infoPanel, BorderLayout.SOUTH);
    
    return panel;
}
    
    // Custom cell renderer untuk header
    class HeaderCellRenderer implements TableCellRenderer {
        private final Color textColor;
        private final DefaultTableCellRenderer renderer;
        
        public HeaderCellRenderer(Color textColor) {
            this.textColor = textColor;
            this.renderer = new DefaultTableCellRenderer();
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel label = (JLabel) renderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            label.setBackground(Color.WHITE);
            label.setForeground(textColor);
            
            if (hasFocus) {
                label.setBackground(textColor);
                label.setForeground(Color.WHITE);
            }
            
            label.setOpaque(true);
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            
            return label;
        }
    }
    
    // Custom renderer untuk status laporan
    class StatusLaporanCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            String status = value != null ? value.toString() : "";
            
            switch (status) {
                case "Baru":
                    setText("🆕 Baru");
                    c.setForeground(BARU_COLOR);
                    break;
                case "Diproses":
                    setText("⏳ Diproses");
                    c.setForeground(DIPROSES_COLOR);
                    break;
                case "Selesai":
                    setText("✅ Selesai");
                    c.setForeground(SELESAI_COLOR);
                    break;
                default:
                    setText(status);
                    c.setForeground(Color.BLACK);
            }
            
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            return c;
        }
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
    
    private void pilihLampiran() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Pilih File Lampiran");
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedLampiran = file.getAbsolutePath();
            lblLampiran.setText(file.getName());
            lblLampiran.setForeground(PRIMARY_COLOR);
        }
    }
    
    private void kirimLaporan() {
    String judul = txtJudul.getText().trim();
    String deskripsi = txtDeskripsi.getText().trim();
    String kategori = (String) cmbKategori.getSelectedItem();
    
    // Validasi input
    if (judul.isEmpty() || deskripsi.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Judul dan deskripsi harus diisi!",
            "Peringatan",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Validasi session
    if (SessionManager.getInstance().getCurrentAdmin() == null) {
        JOptionPane.showMessageDialog(this,
            "Session tidak ditemukan! Silakan login ulang.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    try {
        Laporan laporan = new Laporan();
        laporan.setJudul(judul);
        laporan.setDeskripsi(deskripsi);
        laporan.setKategori(kategori);
        laporan.setCreatedBy(SessionManager.getInstance().getCurrentAdmin().getUsername());
        laporan.setSekolahId(SessionManager.getInstance().getSekolahId());
        laporan.setLampiran(selectedLampiran);
        
        System.out.println("Mengirim laporan:");
        System.out.println("Judul: " + judul);
        System.out.println("Deskripsi: " + deskripsi);
        System.out.println("Kategori: " + kategori);
        System.out.println("Created By: " + laporan.getCreatedBy());
        System.out.println("Sekolah ID: " + laporan.getSekolahId());
        
        boolean success = controller.buatLaporan(laporan);
        
          
    if (success) {
        JOptionPane.showMessageDialog(this,
            "Laporan berhasil dikirim!",
            "Sukses",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Beri notifikasi ke dashboard
        Dashboard.notifikasiLaporanBaru();
        
        // Reset form
        txtJudul.setText("");
        txtDeskripsi.setText("");
        cmbKategori.setSelectedIndex(0);
        selectedLampiran = null;
        lblLampiran.setText("Tidak ada file");
        lblLampiran.setForeground(Color.GRAY);
        
        loadData();
    } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengirim laporan! Periksa koneksi database.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void loadData() {
    tableModel.setRowCount(0);
    
    List<Laporan> list;
    boolean isSuperAdmin = SessionManager.getInstance().isSuperAdmin();
    
    if (isSuperAdmin) {
        // Super admin lihat semua laporan
        list = controller.getAllLaporan();
        
        for (Laporan l : list) {
            String namaSekolah = l.getNamaSekolah() != null ? l.getNamaSekolah() : "-";
            
            tableModel.addRow(new Object[]{
                l.getId(),
                l.getTanggalLaporFormatted(),
                namaSekolah,
                l.getCreatedBy(),
                l.getJudul(),
                l.getKategori(),
                l.getStatus()
            });
        }
    } else {
        // Admin sekolah lihat laporannya sendiri
        list = controller.getLaporanBySekolah(SessionManager.getInstance().getSekolahId());
        
        for (Laporan l : list) {
            tableModel.addRow(new Object[]{
                l.getId(),
                l.getTanggalLaporFormatted(),
                l.getJudul(),
                l.getKategori(),
                l.getStatus(),
                l.getStatusIcon()
            });
        }
    }
}
    
   private void lihatDetailLaporan(boolean isSuperAdmin) {
    int row = tableLaporan.getSelectedRow();
    if (row < 0) return;
    
    int id = (int) tableModel.getValueAt(row, 0);
    
    // Cari laporan berdasarkan ID dari list atau database
    Laporan laporanDetail = null;
    List<Laporan> list;
    
    if (isSuperAdmin) {
        list = controller.getAllLaporan();
    } else {
        list = controller.getLaporanBySekolah(SessionManager.getInstance().getSekolahId());
    }
    
    for (Laporan l : list) {
        if (l.getId() == id) {
            laporanDetail = l;
            break;
        }
    }
    
    if (laporanDetail == null) return;
    
    if (isSuperAdmin) {
        // Tampilkan dialog detail lengkap dengan deskripsi
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        infoPanel.add(new JLabel("ID:"));
        infoPanel.add(new JLabel(String.valueOf(id)));
        
        infoPanel.add(new JLabel("Tanggal:"));
        infoPanel.add(new JLabel(laporanDetail.getTanggalLaporFormatted()));
        
        infoPanel.add(new JLabel("Dari Sekolah:"));
        infoPanel.add(new JLabel(laporanDetail.getNamaSekolah() != null ? laporanDetail.getNamaSekolah() : "-"));
        
        infoPanel.add(new JLabel("Pengirim:"));
        infoPanel.add(new JLabel(laporanDetail.getCreatedBy()));
        
        infoPanel.add(new JLabel("Kategori:"));
        infoPanel.add(new JLabel(laporanDetail.getKategori()));
        
        infoPanel.add(new JLabel("Judul:"));
        infoPanel.add(new JLabel(laporanDetail.getJudul()));
        
        infoPanel.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel(laporanDetail.getStatus());
        switch (laporanDetail.getStatus()) {
            case "Baru":
                statusLabel.setForeground(BARU_COLOR);
                break;
            case "Diproses":
                statusLabel.setForeground(DIPROSES_COLOR);
                break;
            case "Selesai":
                statusLabel.setForeground(SELESAI_COLOR);
                break;
        }
        infoPanel.add(statusLabel);
        
        detailPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Deskripsi panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(Color.WHITE);
        descPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Deskripsi Laporan ",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        JTextArea txtDeskripsi = new JTextArea(laporanDetail.getDeskripsi());
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDeskripsi.setEditable(false);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBackground(new Color(250, 250, 250));
        txtDeskripsi.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        scrollDesc.setPreferredSize(new Dimension(400, 150));
        descPanel.add(scrollDesc, BorderLayout.CENTER);
        
        detailPanel.add(descPanel, BorderLayout.CENTER);
        
        // Lampiran jika ada
        if (laporanDetail.getLampiran() != null) {
            JPanel lampiranPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lampiranPanel.setBackground(Color.WHITE);
            lampiranPanel.setBorder(BorderFactory.createTitledBorder(" Lampiran "));
            
            JLabel lampiranLabel = new JLabel(laporanDetail.getLampiran());
            lampiranLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lampiranPanel.add(lampiranLabel);
            
            detailPanel.add(lampiranPanel, BorderLayout.SOUTH);
        }
        
        // Dialog dengan opsi update status
        int option = JOptionPane.showOptionDialog(
            this,
            detailPanel,
            "Detail Laporan #" + id,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Tutup", "Ubah Status"},
            "Tutup"
        );
        
        if (option == 1) { // Ubah Status
            String[] options = {"Baru", "Diproses", "Selesai"};
            String currentStatus = laporanDetail.getStatus();
            
            String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru:",
                "Update Status Laporan",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                currentStatus
            );
            
            if (newStatus != null && !newStatus.equals(currentStatus)) {
                if (controller.updateStatusLaporan(id, newStatus)) {
                    JOptionPane.showMessageDialog(this,
                        "Status laporan berhasil diupdate!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Gagal mengupdate status!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
    } else {
        // Admin sekolah lihat detail
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        infoPanel.setBackground(Color.WHITE);
        
        infoPanel.add(new JLabel("ID:"));
        infoPanel.add(new JLabel(String.valueOf(id)));
        
        infoPanel.add(new JLabel("Tanggal:"));
        infoPanel.add(new JLabel(laporanDetail.getTanggalLaporFormatted()));
        
        infoPanel.add(new JLabel("Kategori:"));
        infoPanel.add(new JLabel(laporanDetail.getKategori()));
        
        infoPanel.add(new JLabel("Judul:"));
        infoPanel.add(new JLabel(laporanDetail.getJudul()));
        
        infoPanel.add(new JLabel("Status:"));
        JLabel statusLabel = new JLabel(laporanDetail.getStatus());
        switch (laporanDetail.getStatus()) {
            case "Baru":
                statusLabel.setForeground(BARU_COLOR);
                break;
            case "Diproses":
                statusLabel.setForeground(DIPROSES_COLOR);
                break;
            case "Selesai":
                statusLabel.setForeground(SELESAI_COLOR);
                break;
        }
        infoPanel.add(statusLabel);
        
        detailPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Deskripsi panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(Color.WHITE);
        descPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Deskripsi Laporan ",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        JTextArea txtDeskripsi = new JTextArea(laporanDetail.getDeskripsi());
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDeskripsi.setEditable(false);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setBackground(new Color(250, 250, 250));
        txtDeskripsi.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        scrollDesc.setPreferredSize(new Dimension(400, 150));
        descPanel.add(scrollDesc, BorderLayout.CENTER);
        
        detailPanel.add(descPanel, BorderLayout.CENTER);
        
        // Lampiran jika ada
        if (laporanDetail.getLampiran() != null) {
            JPanel lampiranPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lampiranPanel.setBackground(Color.WHITE);
            lampiranPanel.setBorder(BorderFactory.createTitledBorder(" Lampiran "));
            
            JLabel lampiranLabel = new JLabel(laporanDetail.getLampiran());
            lampiranLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lampiranPanel.add(lampiranLabel);
            
            detailPanel.add(lampiranPanel, BorderLayout.SOUTH);
        }
        
        JOptionPane.showMessageDialog(
            this,
            detailPanel,
            "Detail Laporan #" + id,
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
}