package view;

import controller.AdminController;
import controller.SekolahController;
import model.Admin;
import model.Sekolah;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class FormKelolaAdmin extends JFrame {
    private JTextField txtUsername, txtNamaLengkap, txtPassword;
    private JComboBox<String> cmbRole;
    private JComboBox<SekolahItem> cmbSekolah;
    private JTable tableAdmin;
    private DefaultTableModel tableModel;
    private AdminController adminController;
    private SekolahController sekolahController;
    private List<Admin> originalData;
    private List<Sekolah> daftarSekolah;
    
    // Warna tema
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    
    // Class helper untuk combo box sekolah
    class SekolahItem {
        private int id;
        private String nama;
        
        public SekolahItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }
        
        public int getId() { return id; }
        public String getNama() { return nama; }
        
        @Override
        public String toString() {
            return nama;
        }
    }
    
    public FormKelolaAdmin() {
    adminController = new AdminController();
    sekolahController = new SekolahController();
    originalData = new ArrayList<>();
    daftarSekolah = new ArrayList<>();
    initComponents();
    loadSekolah();   // Panggil DULU untuk mengisi daftarSekolah
    loadData();      // Baru panggil loadData
}
    
    private void initComponents() {
        setTitle("Kelola Admin - Sistem Absensi QR");
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
        
        JLabel titleLabel = new JLabel("KELOLA DATA ADMIN", SwingConstants.CENTER);
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
        JLabel titleLabel = new JLabel("TAMBAH DATA ADMIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Buat kolom label dengan lebar tetap
        gbc.weightx = 0.3; // Label mengambil 30% ruang
        gbc.anchor = GridBagConstraints.EAST; // Rata kanan untuk label
        
        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);
        
        gbc.weightx = 0.7; // Field mengambil 70% ruang
        gbc.anchor = GridBagConstraints.WEST; // Rata kiri untuk field
        
        txtUsername = new JTextField(15);
        txtUsername.setPreferredSize(new Dimension(250, 35));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        panel.add(txtUsername, gbc);
        
        // Nama Lengkap
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nama Lengkap:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtNamaLengkap = new JTextField(15);
        txtNamaLengkap.setPreferredSize(new Dimension(250, 35));
        txtNamaLengkap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(txtNamaLengkap, gbc);
        
        // Password
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        txtPassword = new JPasswordField(15);
        txtPassword.setPreferredSize(new Dimension(250, 35));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(txtPassword, gbc);
        
        // Role
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Role:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbRole = new JComboBox<>();
        cmbRole.addItem("admin_sekolah");
        cmbRole.addItem("super_admin");
        cmbRole.setPreferredSize(new Dimension(250, 35));
        cmbRole.addActionListener(e -> {
            // Jika super admin, disable combo sekolah
            boolean isSuperAdmin = "super_admin".equals(cmbRole.getSelectedItem());
            cmbSekolah.setEnabled(!isSuperAdmin);
            if (isSuperAdmin) {
                cmbSekolah.setSelectedIndex(-1);
            }
        });
        panel.add(cmbRole, gbc);
        
        // Sekolah
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Sekolah:"), gbc);
        
        gbc.weightx = 0.7;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        cmbSekolah = new JComboBox<>();
        cmbSekolah.setPreferredSize(new Dimension(250, 35));
        cmbSekolah.setEnabled(true);
        panel.add(cmbSekolah, gbc);
        
        // Buttons Panel - Digeser ke kiri
        gbc.gridwidth = 2;
        gbc.gridy = 6;
        gbc.weightx = 1;
        gbc.insets = new Insets(30, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST; // Rata kiri
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSave = createButton("Simpan", SUCCESS_COLOR);
        btnSave.addActionListener(e -> saveAdmin());
        
        JButton btnEdit = createButton("Edit", INFO_COLOR);
        btnEdit.addActionListener(e -> editAdmin());
        
        JButton btnDelete = createButton("Hapus", DANGER_COLOR);
        btnDelete.addActionListener(e -> deleteAdmin());
        
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
        JLabel titleLabel = new JLabel("DAFTAR ADMIN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Username", "Nama Lengkap", "Role", "Sekolah"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableAdmin = new JTable(tableModel);
        tableAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableAdmin.setRowHeight(30);
        
        // HEADER TABLE - Default putih dengan teks biru
        JTableHeader header = tableAdmin.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Color.WHITE);
        header.setForeground(PRIMARY_COLOR);
        header.setOpaque(true);
        
        // Custom renderer untuk header dengan efek hover
        header.setDefaultRenderer(new HeaderCellRenderer(PRIMARY_COLOR));
        
        // Custom renderer untuk kolom Role
        tableAdmin.getColumnModel().getColumn(3).setCellRenderer(new RoleCellRenderer());
        
        // Set column widths
        tableAdmin.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableAdmin.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableAdmin.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableAdmin.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableAdmin.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        // Selection listener
        tableAdmin.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedAdmin();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableAdmin);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(Color.WHITE);
        JLabel totalLabel = new JLabel("Total: 0 admin");
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(totalLabel);
        
        tableModel.addTableModelListener(e -> {
            totalLabel.setText("Total: " + tableModel.getRowCount() + " admin");
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
    
    // Custom cell renderer untuk kolom Role
    class RoleCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            if ("super_admin".equals(value)) {
                c.setForeground(new Color(155, 89, 182)); // Ungu
                setText("Super Admin");
            } else if ("admin_sekolah".equals(value)) {
                c.setForeground(INFO_COLOR);
                setText("Admin Sekolah");
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
    
    private void loadSekolah() {
        cmbSekolah.removeAllItems();
        daftarSekolah = sekolahController.getAllSekolah();
        
        // Tambahkan opsi kosong
        cmbSekolah.addItem(null);
        
        for (Sekolah s : daftarSekolah) {
            if ("active".equals(s.getStatus())) {
                cmbSekolah.addItem(new SekolahItem(s.getId(), s.getNamaSekolah()));
            }
        }
    }
    
    private void saveAdmin() {
        String username = txtUsername.getText().trim();
        String namaLengkap = txtNamaLengkap.getText().trim();
        String password = txtPassword.getText().trim();
        String role = (String) cmbRole.getSelectedItem();
        SekolahItem selectedSekolah = (SekolahItem) cmbSekolah.getSelectedItem();
        
        if (username.isEmpty() || namaLengkap.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username, Nama Lengkap, dan Password harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi: admin_sekolah harus pilih sekolah
        if ("admin_sekolah".equals(role) && (selectedSekolah == null)) {
            JOptionPane.showMessageDialog(this, 
                "Untuk role Admin Sekolah, harus memilih sekolah!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setNamaLengkap(namaLengkap);
        admin.setPassword(password);
        admin.setRole(role);
        admin.setSekolahId(selectedSekolah != null ? selectedSekolah.getId() : 0);
        
        if (adminController.addAdmin(admin)) {
            JOptionPane.showMessageDialog(this, 
                "Data admin berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan data! Username mungkin sudah ada.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editAdmin() {
        int selectedRow = tableAdmin.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data admin yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username = txtUsername.getText().trim();
        String namaLengkap = txtNamaLengkap.getText().trim();
        String password = txtPassword.getText().trim();
        String role = (String) cmbRole.getSelectedItem();
        SekolahItem selectedSekolah = (SekolahItem) cmbSekolah.getSelectedItem();
        
        if (username.isEmpty() || namaLengkap.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Username dan Nama Lengkap harus diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi: admin_sekolah harus pilih sekolah
        if ("admin_sekolah".equals(role) && (selectedSekolah == null)) {
            JOptionPane.showMessageDialog(this, 
                "Untuk role Admin Sekolah, harus memilih sekolah!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin mengubah data admin " + username + "?", 
            "Konfirmasi Edit", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            Admin admin = new Admin();
            admin.setId(id);
            admin.setUsername(username);
            admin.setNamaLengkap(namaLengkap);
            admin.setPassword(password.isEmpty() ? null : password); // Jika password kosong, tidak diubah
            admin.setRole(role);
            admin.setSekolahId(selectedSekolah != null ? selectedSekolah.getId() : 0);
            
            if (adminController.updateAdmin(admin)) {
                JOptionPane.showMessageDialog(this, 
                    "Data admin berhasil diubah!", 
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
    
    private void deleteAdmin() {
        int selectedRow = tableAdmin.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data admin yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String username = tableModel.getValueAt(selectedRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin menghapus admin:\n" + username + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminController.deleteAdmin(id)) {
                JOptionPane.showMessageDialog(this, 
                    "Data admin berhasil dihapus!", 
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
        txtUsername.setText("");
        txtNamaLengkap.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        cmbSekolah.setSelectedIndex(-1);
        tableAdmin.clearSelection();
    }
    
    private void loadData() {
    tableModel.setRowCount(0);
    originalData = adminController.getAllAdmin();
    
    for (Admin a : originalData) {
        String namaSekolah = "";
        if (a.getSekolahId() > 0) {
            // Cari nama sekolah dari daftarSekolah
            for (Sekolah s : daftarSekolah) {
                if (s.getId() == a.getSekolahId()) {
                    namaSekolah = s.getNamaSekolah();
                    break;
                }
            }
        } else {
            namaSekolah = "-";
        }
        
        Object[] row = {
            a.getId(),
            a.getUsername(),
            a.getNamaLengkap(),
            a.getRole(),
            namaSekolah
        };
        tableModel.addRow(row);
    }
}
    private void showSelectedAdmin() {
        int row = tableAdmin.getSelectedRow();
        if (row >= 0) {
            String username = tableModel.getValueAt(row, 1).toString();
            String namaLengkap = tableModel.getValueAt(row, 2).toString();
            String role = tableModel.getValueAt(row, 3).toString();
            String namaSekolah = tableModel.getValueAt(row, 4).toString();
            
            txtUsername.setText(username);
            txtNamaLengkap.setText(namaLengkap);
            txtPassword.setText(""); // Password dikosongkan untuk keamanan
            cmbRole.setSelectedItem(role);
            
            // Cari sekolah berdasarkan nama
            boolean found = false;
            for (int i = 0; i < cmbSekolah.getItemCount(); i++) {
                SekolahItem item = cmbSekolah.getItemAt(i);
                if (item != null && item.getNama().equals(namaSekolah)) {
                    cmbSekolah.setSelectedIndex(i);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cmbSekolah.setSelectedIndex(-1);
            }
            
            // Jika super admin, disable combo sekolah
            cmbSekolah.setEnabled(!"super_admin".equals(role));
        }
    }
}