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

public class FormRekapSemester extends JFrame {
    private JTable tableRekap;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbTahunAjaran;
    private JComboBox<String> cmbSemester;
    private JComboBox<String> cmbKelas;
    private JComboBox<String> cmbJurusan;
    private JButton btnBack, btnRefresh, btnExport;
    private AbsensiController controller;
    private List<Siswa> semuaSiswa;
    private JLabel infoLabel; // Untuk menampilkan info
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private final Color HADIR_COLOR = new Color(46, 204, 113);
    private final Color TIDAK_HADIR_COLOR = new Color(231, 76, 60);
    
    public FormRekapSemester() {
        controller = new AbsensiController();
        semuaSiswa = new ArrayList<>();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Rekap Absensi Semester - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
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
        
        JLabel titleLabel = new JLabel("REKAP ABSENSI PER SEMESTER", SwingConstants.CENTER);
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
        JLabel filterTitle = new JLabel("FILTER REKAP SEMESTER");
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
        
        // Baris 1: Tahun Ajaran dan Semester
        gbc.gridy = 1;
        gbc.gridx = 0;
        filterContainer.add(new JLabel("Tahun Ajaran:"), gbc);
        
        // Combo Tahun Ajaran
        cmbTahunAjaran = new JComboBox<>();
        int tahunIni = LocalDate.now().getYear();
        cmbTahunAjaran.addItem(tahunIni + "/" + (tahunIni + 1));
        cmbTahunAjaran.addItem((tahunIni - 1) + "/" + tahunIni);
        cmbTahunAjaran.setPreferredSize(new Dimension(120, 30));
        cmbTahunAjaran.addActionListener(e -> filterData());
        gbc.gridx = 1;
        filterContainer.add(cmbTahunAjaran, gbc);
        
        gbc.gridx = 2;
        filterContainer.add(new JLabel("Semester:"), gbc);
        
        // Combo Semester
        cmbSemester = new JComboBox<>();
        cmbSemester.addItem("Ganjil");
        cmbSemester.addItem("Genap");
        cmbSemester.setPreferredSize(new Dimension(100, 30));
        cmbSemester.addActionListener(e -> filterData());
        gbc.gridx = 3;
        filterContainer.add(cmbSemester, gbc);
        
        // Baris 2: Kelas dan Jurusan
        gbc.gridy = 2;
        gbc.gridx = 0;
        filterContainer.add(new JLabel("Kelas:"), gbc);
        
        // Combo Kelas
        cmbKelas = new JComboBox<>();
        cmbKelas.addItem("Semua Kelas");
        cmbKelas.addItem("10");
        cmbKelas.addItem("11");
        cmbKelas.addItem("12");
        cmbKelas.setPreferredSize(new Dimension(100, 30));
        cmbKelas.addActionListener(e -> filterData());
        gbc.gridx = 1;
        filterContainer.add(cmbKelas, gbc);
        
        gbc.gridx = 2;
        filterContainer.add(new JLabel("Jurusan:"), gbc);
        
        // Combo Jurusan
        cmbJurusan = new JComboBox<>();
        cmbJurusan.addItem("Semua Jurusan");
        cmbJurusan.addItem("RPL");
        cmbJurusan.addItem("TKJ");
        cmbJurusan.addItem("MULTIMEDIA");
        cmbJurusan.setPreferredSize(new Dimension(120, 30));
        cmbJurusan.addActionListener(e -> filterData());
        gbc.gridx = 3;
        filterContainer.add(cmbJurusan, gbc);
        
        // Baris 3: Export Button
        gbc.gridy = 3;
        gbc.gridx = 3;
        btnExport = createButton("Export Excel", new Color(46, 204, 113));
        btnExport.setPreferredSize(new Dimension(120, 35));
        btnExport.addActionListener(e -> exportToExcel());
        filterContainer.add(btnExport, gbc);
        
        panel.add(filterContainer, BorderLayout.NORTH);
        
        // Table dengan scroll pane
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Rekap Absensi per Semester ",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            PRIMARY_COLOR
        ));
        
        // Table Model
        String[] columns = {"No", "NIS", "Nama", "Kelas", "Jurusan", "Total Hadir", "Total Tidak Hadir", "Persentase"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 5) return Integer.class;
                if (columnIndex == 6) return Integer.class;
                return String.class;
            }
        };
        
        tableRekap = new JTable(tableModel);
        tableRekap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableRekap.setRowHeight(30);
        
        // HEADER TABLE - Default putih dengan teks biru
        JTableHeader header = tableRekap.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(PRIMARY_COLOR);
        header.setOpaque(true);
        
        // Custom renderer untuk header dengan efek hover
        header.setDefaultRenderer(new HeaderCellRenderer(PRIMARY_COLOR));
        
        // Set custom renderer untuk kolom persentase
        tableRekap.getColumnModel().getColumn(7).setCellRenderer(new PersentaseCellRenderer());
        
        // Atur lebar kolom
        tableRekap.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableRekap.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableRekap.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableRekap.getColumnModel().getColumn(3).setPreferredWidth(50);
        tableRekap.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableRekap.getColumnModel().getColumn(5).setPreferredWidth(80);
        tableRekap.getColumnModel().getColumn(6).setPreferredWidth(100);
        tableRekap.getColumnModel().getColumn(7).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableRekap);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(Color.WHITE);
        infoLabel = new JLabel("Periode: - | Total Siswa: 0 | Rata-rata Kehadiran: 0 kali/siswa");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        tablePanel.add(infoPanel, BorderLayout.SOUTH);
        
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
    
    // Custom cell renderer untuk kolom Persentase
    class PersentaseCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            if (value != null) {
                String persen = value.toString();
                setHorizontalAlignment(SwingConstants.CENTER);
                
                // Warna berdasarkan persentase
                try {
                    double p = Double.parseDouble(persen.replace("%", ""));
                    if (p >= 80) {
                        c.setForeground(HADIR_COLOR);
                    } else if (p >= 60) {
                        c.setForeground(new Color(241, 196, 15)); // Kuning
                    } else {
                        c.setForeground(TIDAK_HADIR_COLOR);
                    }
                } catch (Exception ex) {
                    c.setForeground(Color.BLACK);
                }
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
        filterData();
    }
    
    private void filterData() {
        String tahunAjaran = (String) cmbTahunAjaran.getSelectedItem();
        String semester = (String) cmbSemester.getSelectedItem();
        String filterKelas = (String) cmbKelas.getSelectedItem();
        String filterJurusan = (String) cmbJurusan.getSelectedItem();
        
        // Panggil controller untuk mendapatkan data rekap
        Map<String, Object> rekapData = controller.getRekapSemester(
            tahunAjaran, semester, filterKelas, filterJurusan);
        
        List<Object[]> rekapList = (List<Object[]>) rekapData.get("data");
        String tanggalMulai = (String) rekapData.get("tanggalMulai");
        String tanggalSelesai = (String) rekapData.get("tanggalSelesai");
        
        // Clear table
        tableModel.setRowCount(0);
        
        // Tampilkan data
        int no = 1;
        for (Object[] rowData : rekapList) {
            Object[] row = new Object[8];
            row[0] = no++;
            row[1] = rowData[0]; // NIS
            row[2] = rowData[1]; // Nama
            row[3] = rowData[2]; // Kelas
            row[4] = rowData[3]; // Jurusan
            row[5] = rowData[4]; // Total Hadir
            row[6] = rowData[5]; // Total Tidak Hadir
            row[7] = rowData[6]; // Persentase
            
            tableModel.addRow(row);
        }
        
        // Update info label
        if (infoLabel != null) {
            infoLabel.setText(String.format(
                "Periode: %s s/d %s | Total Siswa: %d | Rata-rata Kehadiran: %.1f kali/siswa",
                tanggalMulai, tanggalSelesai, tableModel.getRowCount(),
                hitungRataRataKehadiran(rekapList)));
        }
    }
    
    // Method bantu hitung rata-rata kehadiran
    private double hitungRataRataKehadiran(List<Object[]> rekapList) {
        if (rekapList == null || rekapList.isEmpty()) return 0;
        
        int totalHadir = 0;
        for (Object[] row : rekapList) {
            totalHadir += (Integer) row[4];
        }
        
        return (double) totalHadir / rekapList.size();
    }
    
    private void exportToExcel() {
        String tahunAjaran = (String) cmbTahunAjaran.getSelectedItem();
        String semester = (String) cmbSemester.getSelectedItem();
        String kelas = (String) cmbKelas.getSelectedItem();
        String jurusan = (String) cmbJurusan.getSelectedItem();
        
        String fileName = "rekap_semester_" + tahunAjaran.replace("/", "_") + "_" + semester;
        if (!kelas.equals("Semua Kelas")) {
            fileName += "_kelas" + kelas;
        }
        if (!jurusan.equals("Semua Jurusan")) {
            fileName += "_" + jurusan;
        }
        fileName += ".xlsx";
        
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
                
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Rekap Semester");
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                String[] columns = {"No", "NIS", "Nama", "Kelas", "Jurusan", "Total Hadir", "Total Tidak Hadir", "Persentase"};
                
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
                
                String info = String.format("Data berhasil diekspor!\n\nFilter:\n- Tahun Ajaran: %s\n- Semester: %s\n- Kelas: %s\n- Jurusan: %s\n\nTotal Siswa: %d",
                    tahunAjaran, semester, kelas, jurusan, tableModel.getRowCount());
                
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