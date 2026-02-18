package view;

import controller.AbsensiController;
import model.Siswa;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class FormSiswa extends JFrame {
    private JTextField txtNIS, txtNama, txtKelas, txtJurusan;
    private JTable tableSiswa;
    private DefaultTableModel tableModel;
    private JLabel lblQRCode;
    private AbsensiController controller;
    private JComboBox<String> cmbFilterKelas;
    private JComboBox<String> cmbFilterJurusan;
    private List<Siswa> originalData; // Menyimpan data asli untuk filter
    
    public FormSiswa() {
        controller = new AbsensiController();
        originalData = new ArrayList<>();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Data Siswa - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel with back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        
        JButton btnBack = createButton("Kembali", new Color(108, 117, 125));
        btnBack.addActionListener(e -> backToDashboard());
        topPanel.add(btnBack, BorderLayout.WEST);
        
        // Title
        JLabel titleLabel = new JLabel("DATA SISWA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setBorder(null);
        
        // Left panel - Input form
        JPanel leftPanel = createInputPanel();
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel - Table with filter
        JPanel rightPanel = createTablePanel();
        splitPane.setRightComponent(rightPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createInputPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Title
    JLabel titleLabel = new JLabel("INPUT DATA SISWA");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    titleLabel.setForeground(new Color(70, 130, 180));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 5, 15, 5); // Bottom padding 15
    panel.add(titleLabel, gbc);
    
    // Reset insets untuk field-field
    gbc.insets = new Insets(5, 5, 5, 5);
    
    // NIS
    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    panel.add(new JLabel("NIS:"), gbc);
    
    txtNIS = new JTextField(15);
    txtNIS.setPreferredSize(new Dimension(200, 30));
    txtNIS.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridx = 1;
    panel.add(txtNIS, gbc);
    
    // Nama
    gbc.gridy = 2;
    gbc.gridx = 0;
    panel.add(new JLabel("Nama:"), gbc);
    
    txtNama = new JTextField(15);
    txtNama.setPreferredSize(new Dimension(200, 30));
    txtNama.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridx = 1;
    panel.add(txtNama, gbc);
    
    // Kelas
    gbc.gridy = 3;
    gbc.gridx = 0;
    panel.add(new JLabel("Kelas:"), gbc);
    
    txtKelas = new JTextField(15);
    txtKelas.setPreferredSize(new Dimension(200, 30));
    txtKelas.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridx = 1;
    panel.add(txtKelas, gbc);
    
    // Jurusan
    gbc.gridy = 4;
    gbc.gridx = 0;
    panel.add(new JLabel("Jurusan:"), gbc);
    
    txtJurusan = new JTextField(15);
    txtJurusan.setPreferredSize(new Dimension(200, 30));
    txtJurusan.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    gbc.gridx = 1;
    panel.add(txtJurusan, gbc);
    
    // Buttons Panel
JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
buttonPanel.setBackground(Color.WHITE);

JButton btnSave = createButton("Simpan", new Color(46, 204, 113));
btnSave.addActionListener(e -> saveSiswa());

JButton btnEdit = createButton("Edit", new Color(52, 152, 219));
btnEdit.addActionListener(e -> editSiswa());

JButton btnDelete = createButton("Hapus", new Color(231, 76, 60));
btnDelete.addActionListener(e -> deleteSiswa());

JButton btnClear = createButton("Bersihkan", new Color(241, 196, 15));
btnClear.addActionListener(e -> clearForm());

JButton btnRefresh = createButton("Refresh", new Color(155, 89, 182));
btnRefresh.addActionListener(e -> loadData());

buttonPanel.add(btnSave);
buttonPanel.add(btnEdit);
buttonPanel.add(btnDelete);
buttonPanel.add(btnClear);
buttonPanel.add(btnRefresh);

gbc.gridy = 5;
gbc.gridx = 0;
gbc.gridwidth = 2;
// Top 15, Bottom 40 (ditambah lebih besar)
gbc.insets = new Insets(15, 5, 40, 5); 
panel.add(buttonPanel, gbc);

// QR Code preview
JLabel qrLabel = new JLabel("QR CODE PREVIEW");
qrLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
qrLabel.setForeground(new Color(70, 130, 180));
qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
gbc.gridy = 6;
// Top 0, Bottom 10 (beri jarak ke QR code)
gbc.insets = new Insets(0, 5, 10, 5); 
panel.add(qrLabel, gbc);

lblQRCode = new JLabel();
lblQRCode.setPreferredSize(new Dimension(150, 150));
lblQRCode.setHorizontalAlignment(SwingConstants.CENTER);
lblQRCode.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
gbc.gridy = 7;
// Top 0, Bottom 15 (jarak ke download button)
gbc.insets = new Insets(0, 5, 15, 5);
panel.add(lblQRCode, gbc);

// Download QR button
JButton btnDownloadQR = createButton("Download QR", new Color(52, 152, 219));
btnDownloadQR.addActionListener(e -> downloadQRCode());
gbc.gridy = 8;
// Top 0, Bottom 5
gbc.insets = new Insets(0, 5, 5, 5);
panel.add(btnDownloadQR, gbc);
    
    return panel;
}
    
   private JPanel createTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220)),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    
    // Top panel with filter
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    filterPanel.setBackground(Color.WHITE);
    filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    
    // Filter Kelas
    JLabel filterLabel = new JLabel("Filter Kelas:");
    filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    
    cmbFilterKelas = new JComboBox<>();
    cmbFilterKelas.addItem("Semua Kelas");
    cmbFilterKelas.addItem("Kelas 10");
    cmbFilterKelas.addItem("Kelas 11");
    cmbFilterKelas.addItem("Kelas 12");
    cmbFilterKelas.setPreferredSize(new Dimension(120, 30));
    cmbFilterKelas.addActionListener(e -> filterData());
    
    filterPanel.add(filterLabel);
    filterPanel.add(cmbFilterKelas);
    
    // Filter Jurusan
    JLabel filterJurusanLabel = new JLabel("Filter Jurusan:");
    filterJurusanLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
    filterJurusanLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 5));
    
    cmbFilterJurusan = new JComboBox<>();
    cmbFilterJurusan.addItem("Semua Jurusan");
    cmbFilterJurusan.addItem("RPL");
    cmbFilterJurusan.addItem("TKJ");
    cmbFilterJurusan.addItem("MULTIMEDIA");
    cmbFilterJurusan.setPreferredSize(new Dimension(120, 30));
    cmbFilterJurusan.addActionListener(e -> filterData());
    
    filterPanel.add(filterJurusanLabel);
    filterPanel.add(cmbFilterJurusan);
    
    panel.add(filterPanel, BorderLayout.NORTH);
    
    // Table
    String[] columns = {"NIS", "Nama", "Kelas", "Jurusan"};
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    tableSiswa = new JTable(tableModel);
    tableSiswa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    tableSiswa.setRowHeight(25);
    tableSiswa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    tableSiswa.getTableHeader().setBackground(new Color(70, 130, 180));
    tableSiswa.getTableHeader().setForeground(Color.WHITE);
    tableSiswa.setSelectionBackground(new Color(184, 207, 229));
    
    // Add selection listener
    tableSiswa.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            showSelectedSiswa();
        }
    });
    
    JScrollPane scrollPane = new JScrollPane(tableSiswa);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    panel.add(scrollPane, BorderLayout.CENTER);
    
    // Info panel
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    infoPanel.setBackground(Color.WHITE);
    JLabel totalLabel = new JLabel("Total: 0 siswa");
    totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    infoPanel.add(totalLabel);
    
    // Update total saat data berubah
    tableModel.addTableModelListener(e -> {
        totalLabel.setText("Total: " + tableModel.getRowCount() + " siswa");
    });
    
    panel.add(infoPanel, BorderLayout.SOUTH);
    
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
        button.setPreferredSize(new Dimension(90, 35));
          button.setVerticalAlignment(SwingConstants.CENTER);
    button.setHorizontalAlignment(SwingConstants.CENTER);
        
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
    
    private void filterData() {
    String selectedKelas = (String) cmbFilterKelas.getSelectedItem();
    String selectedJurusan = (String) cmbFilterJurusan.getSelectedItem();
    tableModel.setRowCount(0);
    
    for (Siswa s : originalData) {
        boolean matchKelas = selectedKelas.equals("Semua Kelas") ||
            (selectedKelas.equals("Kelas 10") && s.getKelas().equals("10")) ||
            (selectedKelas.equals("Kelas 11") && s.getKelas().equals("11")) ||
            (selectedKelas.equals("Kelas 12") && s.getKelas().equals("12"));
        
        boolean matchJurusan = selectedJurusan.equals("Semua Jurusan") ||
            (selectedJurusan.equals("RPL") && s.getJurusan().equalsIgnoreCase("RPL")) ||
            (selectedJurusan.equals("TKJ") && s.getJurusan().equalsIgnoreCase("TKJ")) ||
            (selectedJurusan.equals("MULTIMEDIA") && s.getJurusan().equalsIgnoreCase("MULTIMEDIA"));
        
        if (matchKelas && matchJurusan) {
            addRowToTable(s);
        }
    }
}
    
    private void addRowToTable(Siswa s) {
        Object[] row = {
            s.getNis(),
            s.getNama(),
            s.getKelas(),
            s.getJurusan()
        };
        tableModel.addRow(row);
    }
    
    private void editSiswa() {
    int selectedRow = tableSiswa.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, 
            "Pilih data siswa yang akan diedit!", 
            "Peringatan", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String nis = txtNIS.getText().trim();
    String nama = txtNama.getText().trim();
    String kelas = txtKelas.getText().trim();
    String jurusan = txtJurusan.getText().trim();
    
    if (nis.isEmpty() || nama.isEmpty() || kelas.isEmpty() || jurusan.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Semua field harus diisi!", 
            "Peringatan", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Yakin ingin mengubah data siswa dengan NIS " + nis + "?", 
        "Konfirmasi Edit", 
        JOptionPane.YES_NO_OPTION);
        
    if (confirm == JOptionPane.YES_OPTION) {
        Siswa siswa = new Siswa(nis, nama, kelas, jurusan);
        boolean success = controller.updateSiswa(siswa);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Data siswa berhasil diubah!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            generateQRCode(nis);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal mengubah data! NIS mungkin tidak ditemukan.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private void deleteSiswa() {
    int selectedRow = tableSiswa.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, 
            "Pilih data siswa yang akan dihapus!", 
            "Peringatan", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String nis = tableModel.getValueAt(selectedRow, 0).toString();
    String nama = tableModel.getValueAt(selectedRow, 1).toString();
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Yakin ingin menghapus data siswa:\nNIS: " + nis + "\nNama: " + nama + 
        "\n\nPerhatian: Semua riwayat absensi siswa ini juga akan ikut terhapus!", 
        "Konfirmasi Hapus", 
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
        
    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = controller.deleteSiswa(nis);
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Data siswa berhasil dihapus!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menghapus data!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
    private void downloadQRCode() {
        if (lblQRCode.getIcon() == null) {
            JOptionPane.showMessageDialog(this, 
                "Tidak ada QR Code untuk diunduh!\nGenerate QR Code terlebih dahulu.", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nis = txtNIS.getText().trim();
        if (nis.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Pilih atau input data siswa terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("QR_Code_" + nis + ".png"));
        fileChooser.setDialogTitle("Simpan QR Code");
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".png")) {
                filePath += ".png";
            }
            
            try {
                Icon icon = lblQRCode.getIcon();
                BufferedImage bufferedImage = new BufferedImage(
                    icon.getIconWidth(), 
                    icon.getIconHeight(), 
                    BufferedImage.TYPE_INT_ARGB
                );
                
                Graphics g = bufferedImage.createGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();
                
                File outputFile = new File(filePath);
                ImageIO.write(bufferedImage, "png", outputFile);
                
                JOptionPane.showMessageDialog(this, 
                    "QR Code berhasil disimpan di:\n" + filePath, 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan QR Code: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveSiswa() {
        String nis = txtNIS.getText().trim();
        String nama = txtNama.getText().trim();
        String kelas = txtKelas.getText().trim();
        String jurusan = txtJurusan.getText().trim();
        
        if (nis.isEmpty() || nama.isEmpty() || kelas.isEmpty() || jurusan.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua field harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Siswa siswa = new Siswa(nis, nama, kelas, jurusan);
        
        if (controller.addSiswa(siswa)) {
            JOptionPane.showMessageDialog(this, 
                "Data siswa berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            generateQRCode(nis);
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan data! NIS mungkin sudah ada.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateQRCode(String nis) {
        try {
            QRCodeWriter qrWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            
            BitMatrix bitMatrix = qrWriter.encode(nis, BarcodeFormat.QR_CODE, 150, 150, hints);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            
            ImageIcon icon = new ImageIcon(pngData);
            lblQRCode.setIcon(icon);
            
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Gagal generate QR Code!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        txtNIS.setText("");
        txtNama.setText("");
        txtKelas.setText("");
        txtJurusan.setText("");
        lblQRCode.setIcon(null);
        tableSiswa.clearSelection();
    }
    
   public void loadData() {
    tableModel.setRowCount(0);
    originalData = controller.getAllSiswa();
    
    for (Siswa s : originalData) {
        Object[] row = {
            s.getNis(),
            s.getNama(),
            s.getKelas(),
            s.getJurusan()
        };
        tableModel.addRow(row);
    }
    
    // Reset filter ke default
    cmbFilterKelas.setSelectedIndex(0);
    cmbFilterJurusan.setSelectedIndex(0); // TAMBAHKAN INI
}
    
    private void showSelectedSiswa() {
        int row = tableSiswa.getSelectedRow();
        if (row >= 0) {
            txtNIS.setText(tableModel.getValueAt(row, 0).toString());
            txtNama.setText(tableModel.getValueAt(row, 1).toString());
            txtKelas.setText(tableModel.getValueAt(row, 2).toString());
            txtJurusan.setText(tableModel.getValueAt(row, 3).toString());
            
            generateQRCode(tableModel.getValueAt(row, 0).toString());
        }
    }
    
    private void backToDashboard() {
        new Dashboard().setVisible(true);
        dispose();
    }
}