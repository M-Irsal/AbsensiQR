package view;

import controller.SekolahController;
import model.Sekolah;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class FormKelolaSekolah extends JFrame {
    private JTextField txtKodeSekolah, txtNamaSekolah, txtAlamat, txtTelepon;
    private JComboBox<String> cmbStatus;
    private JTable tableSekolah;
    private DefaultTableModel tableModel;
    private SekolahController controller;
    private List<Sekolah> originalData;
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    
    public FormKelolaSekolah() {
        controller = new SekolahController();
        originalData = new ArrayList<>();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Kelola Sekolah - Sistem Absensi QR");
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
        
        JLabel titleLabel = new JLabel("KELOLA DATA SEKOLAH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center panel split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(null);
        
        // Left panel - Input form
        JPanel leftPanel = createInputPanel();
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel - Table
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
            BorderFactory.createEmptyBorder(30, 30, 30, 30)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel titleLabel = new JLabel("TAMBAH DATA SEKOLAH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Buat kolom label dengan lebar tetap
        gbc.weightx = 0.3; // Label mengambil 30% ruang
        gbc.anchor = GridBagConstraints.EAST; // Rata kanan untuk label
        
        // Kode Sekolah
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Kode Sekolah:"), gbc);
        
        gbc.weightx = 0.7; // Field mengambil 70% ruang
        gbc.anchor = GridBagConstraints.WEST; // Rata kiri untuk field
        
        txtKodeSekolah = new JTextField(15);
        txtKodeSekolah.setPreferredSize(new Dimension(250, 35));
        txtKodeSekolah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        panel.add(txtKodeSekolah, gbc);
        
        // Nama Sekolah
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nama Sekolah:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtNamaSekolah = new JTextField(15);
        txtNamaSekolah.setPreferredSize(new Dimension(250, 35));
        txtNamaSekolah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(txtNamaSekolah, gbc);
        
        // Alamat
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Alamat:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtAlamat = new JTextField(15);
        txtAlamat.setPreferredSize(new Dimension(250, 35));
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(txtAlamat, gbc);
        
        // Telepon
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Telepon:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtTelepon = new JTextField(15);
        txtTelepon.setPreferredSize(new Dimension(250, 35));
        txtTelepon.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(txtTelepon, gbc);
        
        // Status
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Status:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbStatus = new JComboBox<>();
        cmbStatus.addItem("active");
        cmbStatus.addItem("inactive");
        cmbStatus.setPreferredSize(new Dimension(250, 35));
        panel.add(cmbStatus, gbc);
        
        // Buttons Panel - Digeser ke kiri
        gbc.gridwidth = 2;
        gbc.gridy = 6;
        gbc.weightx = 1;
        gbc.insets = new Insets(30, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST; // Rata kiri
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); // Rata kiri
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = createButton("Simpan", SUCCESS_COLOR);
        btnSave.addActionListener(e -> saveSekolah());
        
        JButton btnEdit = createButton("Edit", INFO_COLOR);
        btnEdit.addActionListener(e -> editSekolah());
        
        JButton btnDelete = createButton("Hapus", DANGER_COLOR);
        btnDelete.addActionListener(e -> deleteSekolah());
        
        JButton btnClear = createButton("Bersihkan", WARNING_COLOR);
        btnClear.addActionListener(e -> clearForm());
        
        JButton btnRefresh = createButton("Refresh", new Color(155, 89, 182));
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);
        
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Title
        JLabel titleLabel = new JLabel("DAFTAR SEKOLAH");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Kode Sekolah", "Nama Sekolah", "Alamat", "Telepon", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSekolah = new JTable(tableModel);
        tableSekolah.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableSekolah.setRowHeight(30);
        
        // HEADER TABLE - Default putih dengan teks biru
        JTableHeader header = tableSekolah.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(PRIMARY_COLOR);
        header.setOpaque(true);
        
        // Custom renderer untuk header dengan efek hover
        header.setDefaultRenderer(new HeaderCellRenderer(PRIMARY_COLOR));
        
        // Set column widths
        tableSekolah.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableSekolah.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableSekolah.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableSekolah.getColumnModel().getColumn(3).setPreferredWidth(250);
        tableSekolah.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableSekolah.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        // Custom renderer for status
        tableSekolah.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
        
        // Selection listener
        tableSekolah.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedSekolah();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableSekolah);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(Color.WHITE);
        JLabel totalLabel = new JLabel("Total: 0 sekolah");
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(totalLabel);
        
        tableModel.addTableModelListener(e -> {
            totalLabel.setText("Total: " + tableModel.getRowCount() + " sekolah");
        });
        
        panel.add(infoPanel, BorderLayout.SOUTH);
        
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
            
            if ("active".equals(value)) {
                c.setForeground(SUCCESS_COLOR);
                setText("✓ Active");
            } else if ("inactive".equals(value)) {
                c.setForeground(DANGER_COLOR);
                setText("✗ Inactive");
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
    
    private void saveSekolah() {
        String kodeSekolah = txtKodeSekolah.getText().trim();
        String namaSekolah = txtNamaSekolah.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String telepon = txtTelepon.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        
        if (kodeSekolah.isEmpty() || namaSekolah.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Kode Sekolah dan Nama Sekolah harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Sekolah sekolah = new Sekolah();
        sekolah.setKodeSekolah(kodeSekolah);
        sekolah.setNamaSekolah(namaSekolah);
        sekolah.setAlamat(alamat);
        sekolah.setTelepon(telepon);
        sekolah.setStatus(status);
        
        if (controller.addSekolah(sekolah)) {
            JOptionPane.showMessageDialog(this, 
                "Data sekolah berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan data! Kode Sekolah mungkin sudah ada.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editSekolah() {
        int selectedRow = tableSekolah.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data sekolah yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String kodeSekolah = txtKodeSekolah.getText().trim();
        String namaSekolah = txtNamaSekolah.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String telepon = txtTelepon.getText().trim();
        String status = (String) cmbStatus.getSelectedItem();
        
        if (kodeSekolah.isEmpty() || namaSekolah.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Kode Sekolah dan Nama Sekolah harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin mengubah data sekolah " + namaSekolah + "?", 
            "Konfirmasi Edit", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            Sekolah sekolah = new Sekolah();
            sekolah.setId(id);
            sekolah.setKodeSekolah(kodeSekolah);
            sekolah.setNamaSekolah(namaSekolah);
            sekolah.setAlamat(alamat);
            sekolah.setTelepon(telepon);
            sekolah.setStatus(status);
            
            if (controller.updateSekolah(sekolah)) {
                JOptionPane.showMessageDialog(this, 
                    "Data sekolah berhasil diubah!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal mengubah data!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSekolah() {
        int selectedRow = tableSekolah.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data sekolah yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String namaSekolah = tableModel.getValueAt(selectedRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus sekolah:\n" + namaSekolah + 
            "\n\nPerhatian: Semua data siswa dan absensi di sekolah ini juga akan ikut terhapus!", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteSekolah(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Data sekolah berhasil dihapus!", 
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
    
    private void clearForm() {
        txtKodeSekolah.setText("");
        txtNamaSekolah.setText("");
        txtAlamat.setText("");
        txtTelepon.setText("");
        cmbStatus.setSelectedIndex(0);
        tableSekolah.clearSelection();
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        originalData = controller.getAllSekolah();
        
        for (Sekolah s : originalData) {
            Object[] row = {
                s.getId(),
                s.getKodeSekolah(),
                s.getNamaSekolah(),
                s.getAlamat() != null ? s.getAlamat() : "",
                s.getTelepon() != null ? s.getTelepon() : "",
                s.getStatus()
            };
            tableModel.addRow(row);
        }
    }
    
    private void showSelectedSekolah() {
        int row = tableSekolah.getSelectedRow();
        if (row >= 0) {
            txtKodeSekolah.setText(tableModel.getValueAt(row, 1).toString());
            txtNamaSekolah.setText(tableModel.getValueAt(row, 2).toString());
            txtAlamat.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
            txtTelepon.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
            
            String status = tableModel.getValueAt(row, 5).toString();
            if ("active".equals(status)) {
                cmbStatus.setSelectedIndex(0);
            } else {
                cmbStatus.setSelectedIndex(1);
            }
        }
    }
}