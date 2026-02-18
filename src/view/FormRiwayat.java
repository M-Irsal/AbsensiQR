package view;

import controller.AbsensiController;
import model.Siswa;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormRiwayat extends JFrame {
    private JTable tableRiwayat;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilterTanggal;
    private JComboBox<String> cmbFilterKelas;
    private JComboBox<String> cmbFilterJurusan;
    private JButton btnBack, btnRefresh, btnExport;
    private AbsensiController controller;
    private List<Siswa> semuaSiswa;
    private Map<String, Boolean> absensiHariIni;
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color HADIR_COLOR = new Color(46, 204, 113);
    private final Color TIDAK_HADIR_COLOR = new Color(231, 76, 60);
    
    public FormRiwayat() {
        controller = new AbsensiController();
        semuaSiswa = new ArrayList<>();
        absensiHariIni = new HashMap<>();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Riwayat Absensi - Sistem Absensi QR");
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
        
        btnBack = createButton("Kembali", new Color(108, 117, 125));
        btnBack.addActionListener(e -> backToDashboard());
        topPanel.add(btnBack, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("RIWAYAT ABSENSI", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        btnRefresh = createButton("Refresh", new Color(52, 152, 219));
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        // Filter Container
        JPanel filterContainer = new JPanel(new GridBagLayout());
        filterContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title Filter
        JLabel filterTitle = new JLabel("FILTER DATA ABSENSI");
        filterTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        filterTitle.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 5, 15, 5);
        filterContainer.add(filterTitle, gbc);
        
        // Reset insets
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;
        
        // Label Tanggal
        gbc.gridy = 1;
        gbc.gridx = 0;
        filterContainer.add(new JLabel("Tanggal:"), gbc);
        
        // Combo Tanggal
        cmbFilterTanggal = new JComboBox<>();
        cmbFilterTanggal.addItem(LocalDate.now().toString());
        cmbFilterTanggal.setEditable(true);
        cmbFilterTanggal.setPreferredSize(new Dimension(120, 30));
        cmbFilterTanggal.addActionListener(e -> filterData());
        gbc.gridx = 1;
        filterContainer.add(cmbFilterTanggal, gbc);
        
        // Label Kelas
        gbc.gridx = 2;
        filterContainer.add(new JLabel("Kelas:"), gbc);
        
        // Combo Kelas
        cmbFilterKelas = new JComboBox<>();
        cmbFilterKelas.addItem("Semua Kelas");
        cmbFilterKelas.addItem("10");
        cmbFilterKelas.addItem("11");
        cmbFilterKelas.addItem("12");
        cmbFilterKelas.setPreferredSize(new Dimension(100, 30));
        cmbFilterKelas.addActionListener(e -> filterData());
        gbc.gridx = 3;
        filterContainer.add(cmbFilterKelas, gbc);
        
        // Baris 2: Jurusan
        gbc.gridy = 2;
        gbc.gridx = 0;
        filterContainer.add(new JLabel("Jurusan:"), gbc);
        
        // Combo Jurusan
        cmbFilterJurusan = new JComboBox<>();
        cmbFilterJurusan.addItem("Semua Jurusan");
        cmbFilterJurusan.addItem("RPL");
        cmbFilterJurusan.addItem("TKJ");
        cmbFilterJurusan.addItem("MULTIMEDIA");
        cmbFilterJurusan.setPreferredSize(new Dimension(120, 30));
        cmbFilterJurusan.addActionListener(e -> filterData());
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        filterContainer.add(cmbFilterJurusan, gbc);
        
        // Export Button
        btnExport = createButton("Export Excel", new Color(46, 204, 113));
        btnExport.setPreferredSize(new Dimension(120, 35));
        btnExport.addActionListener(e -> exportToExcel());
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        filterContainer.add(btnExport, gbc);
        
        panel.add(filterContainer, BorderLayout.NORTH);
        
        // Table dengan scroll pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Data Absensi ",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        // Table Model
        String[] columns = {"No", "Tanggal", "NIS", "Nama", "Kelas", "Jurusan", "Jam Masuk", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };
        
        tableRiwayat = new JTable(tableModel);
        tableRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableRiwayat.setRowHeight(30);
        
        // HEADER TABLE - Default putih dengan teks biru
        JTableHeader header = tableRiwayat.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(PRIMARY_COLOR);
        header.setOpaque(true);
        
        // Custom renderer untuk header dengan efek hover
        header.setDefaultRenderer(new HeaderCellRenderer(PRIMARY_COLOR));
        
        // Set custom renderer untuk kolom Status
        tableRiwayat.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());
        
        // Atur lebar kolom
        tableRiwayat.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableRiwayat.getColumnModel().getColumn(1).setPreferredWidth(90);
        tableRiwayat.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableRiwayat.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableRiwayat.getColumnModel().getColumn(4).setPreferredWidth(50);
        tableRiwayat.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableRiwayat.getColumnModel().getColumn(6).setPreferredWidth(80);
        tableRiwayat.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableRiwayat);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(Color.WHITE);
        JLabel infoLabel = new JLabel("Total: 0 data | Hadir: 0 | Tidak Hadir: 0");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        tablePanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Update info saat data berubah
        tableModel.addTableModelListener(e -> {
            int total = tableModel.getRowCount();
            int hadir = 0;
            for (int i = 0; i < total; i++) {
                if ("Hadir".equals(tableModel.getValueAt(i, 7))) {
                    hadir++;
                }
            }
            infoLabel.setText(String.format("Total: %d data | Hadir: %d | Tidak Hadir: %d", 
                total, hadir, total - hadir));
        });
        
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Custom cell renderer untuk header tabel dengan efek hover
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
            
            // Default: background putih, teks biru
            label.setBackground(Color.WHITE);
            label.setForeground(textColor);
            
            // Saat hover (hasFocus true), background biru, teks putih
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
    
    // Custom cell renderer untuk kolom Status
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            if ("Hadir".equals(value)) {
                c.setForeground(HADIR_COLOR);
                setText("✓ " + value);
            } else if ("Tidak Hadir".equals(value)) {
                c.setForeground(TIDAK_HADIR_COLOR);
                setText("✗ " + value);
            } else {
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
        button.setPreferredSize(new Dimension(100, 35));
        
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
    
    private void loadData() {
        semuaSiswa = controller.getAllSiswa();
        cmbFilterTanggal.setSelectedItem(LocalDate.now().toString());
        filterData();
    }
    
    private void filterData() {
        String tanggal = (String) cmbFilterTanggal.getSelectedItem();
        String filterKelas = (String) cmbFilterKelas.getSelectedItem();
        String filterJurusan = (String) cmbFilterJurusan.getSelectedItem();
        
        if (tanggal == null || tanggal.trim().isEmpty()) return;
        
        List<Object[]> absensiData = controller.getRiwayatAbsensi(tanggal);
        
        Map<String, Object[]> absensiMap = new HashMap<>();
        for (Object[] absen : absensiData) {
            absensiMap.put(absen[2].toString(), absen);
        }
        
        tableModel.setRowCount(0);
        
        int no = 1;
        for (Siswa siswa : semuaSiswa) {
            if (!filterKelas.equals("Semua Kelas") && !siswa.getKelas().equals(filterKelas)) {
                continue;
            }
            
            if (!filterJurusan.equals("Semua Jurusan") && !siswa.getJurusan().equalsIgnoreCase(filterJurusan)) {
                continue;
            }
            
            Object[] absen = absensiMap.get(siswa.getNis());
            String jamMasuk = "";
            String status = "Tidak Hadir";
            
            if (absen != null) {
                jamMasuk = absen[1].toString();
                status = absen[6].toString();
            }
            
            Object[] row = {
                no++,
                tanggal,
                siswa.getNis(),
                siswa.getNama(),
                siswa.getKelas(),
                siswa.getJurusan(),
                jamMasuk,
                status
            };
            tableModel.addRow(row);
        }
    }
    
    private void exportToExcel() {
        String tanggal = (String) cmbFilterTanggal.getSelectedItem();
        String kelas = (String) cmbFilterKelas.getSelectedItem();
        String jurusan = (String) cmbFilterJurusan.getSelectedItem();
        
        if (tanggal == null || tanggal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Pilih tanggal terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String fileName = "riwayat_absensi";
        if (!kelas.equals("Semua Kelas")) {
            fileName += "_kelas" + kelas;
        }
        if (!jurusan.equals("Semua Jurusan")) {
            fileName += "_" + jurusan;
        }
        fileName += "_" + tanggal.replace("-", "") + ".xlsx";
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(fileName));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            
            try (org.apache.poi.ss.usermodel.Workbook workbook = 
                    new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                 java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
                
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Riwayat Absensi");
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                String[] columns = {"No", "Tanggal", "NIS", "Nama", "Kelas", "Jurusan", "Jam Masuk", "Status"};
                
                org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < columns.length; i++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }
                
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                workbook.write(fileOut);
                
                String info = String.format("Data berhasil diekspor!\n\nFilter:\n- Tanggal: %s\n- Kelas: %s\n- Jurusan: %s\n\nTotal data: %d",
                    tanggal, kelas, jurusan, tableModel.getRowCount());
                
                JOptionPane.showMessageDialog(this, 
                    info, 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Gagal mengekspor data: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void backToDashboard() {
        new Dashboard().setVisible(true);
        dispose();
    }
}