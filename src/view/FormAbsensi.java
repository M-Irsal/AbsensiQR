package view;

import controller.AbsensiController;
import controller.LogController;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.GridLayout;

public class FormAbsensi extends JFrame {
    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private JTextArea txtResult;
    private JTable tableAbsensi;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilterTanggal;
    private JComboBox<String> cmbFilterKelas;
    private JComboBox<String> cmbFilterJurusan; 
    private JButton btnStart, btnStop, btnExport, btnBack, btnMode;
    private AbsensiController controller;
    private Timer timer;
    private boolean scanning = false;
    private boolean autoMode = true;
    private String lastScannedQR = "";
    private boolean isInitialized = false;
    private JPanel cameraContainer;
    private LogController logController;
    
    public FormAbsensi() {
        controller = new AbsensiController();
        logController = new LogController();
        initComponents();
        // Set initial data setelah komponen dibuat
        cmbFilterTanggal.setSelectedItem(LocalDate.now().toString());
        loadRiwayat(LocalDate.now().toString());
    }
    
    private void initComponents() {
        setTitle("Absensi QR - Sistem Absensi QR");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 250));
        
        btnBack = createButton("Kembali", new Color(108, 117, 125));
        btnBack.addActionListener(e -> backToDashboard());
        topPanel.add(btnBack, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("ABSENSI QR CODE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        btnMode = createButton("Mode: AUTO", new Color(52, 152, 219));
        btnMode.addActionListener(e -> toggleMode());
        topPanel.add(btnMode, BorderLayout.EAST);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(null);
        
        JPanel leftPanel = createCameraPanel();
        splitPane.setLeftComponent(leftPanel);
        
        JPanel rightPanel = createHistoryPanel();
        splitPane.setRightComponent(rightPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    public void startWebcam() {
        if (!isInitialized) {
            initWebcam();
            isInitialized = true;
        }
        
        if (webcam != null && !webcam.isOpen()) {
            try {
                webcam.open();
                if (webcamPanel != null) {
                    webcamPanel.start();
                }
                System.out.println("Webcam started");
                logController.logActivity("Memulai webcam", "Berhasil");
                
                if (autoMode) {
                    startScan();
                }
            } catch (Exception e) {
                e.printStackTrace();
                logController.logActivity("Gagal memulai webcam: " + e.getMessage(), "Error");
            }
        }
    }
    
    public void stopWebcam() {
        if (webcam != null && webcam.isOpen()) {
            stopScan();
            if (webcamPanel != null) {
                webcamPanel.stop();
            }
            webcam.close();
            System.out.println("Webcam stopped");
            logController.logActivity("Menghentikan webcam", "Berhasil");
        }
    }
    
    public void initializeForDisplay() {
        if (!isInitialized) {
            initWebcam();
            isInitialized = true;
        }
        // Refresh data dengan tanggal yang sedang dipilih
        String currentDate = (String) cmbFilterTanggal.getSelectedItem();
        loadRiwayat(currentDate);
        refreshCameraDisplay();
        startWebcam();
    }
    
    private void toggleMode() {
        autoMode = !autoMode;
        if (autoMode) {
            btnMode.setText("Mode: AUTO");
            btnMode.setBackground(new Color(46, 204, 113));
            startScan();
            logController.logActivity("Mode scan diubah ke AUTO", "Berhasil");
        } else {
            btnMode.setText("Mode: MANUAL");
            btnMode.setBackground(new Color(52, 152, 219));
            stopScan();
            logController.logActivity("Mode scan diubah ke MANUAL", "Berhasil");
        }
    }
    
    private JPanel createCameraPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(500, 600));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel camLabel = new JLabel("SCAN QR CODE", SwingConstants.CENTER);
        camLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        camLabel.setForeground(new Color(70, 130, 180));
        panel.add(camLabel, BorderLayout.NORTH);

        cameraContainer = new JPanel(new BorderLayout());
        cameraContainer.setPreferredSize(new Dimension(450, 400));
        cameraContainer.setBackground(Color.BLACK);
        cameraContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (webcamPanel != null) {
            cameraContainer.add(webcamPanel, BorderLayout.CENTER);
        } else {
            cameraContainer.add(new JLabel("Camera not ready", SwingConstants.CENTER), BorderLayout.CENTER);
        }

        panel.add(cameraContainer, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(Color.WHITE);

        btnStart = createButton("Start Scan", new Color(46, 204, 113));
        btnStop = createButton("Stop Scan", new Color(231, 76, 60));
        btnStop.setEnabled(false);

        btnStart.addActionListener(e -> {
            startScan();
            logController.logActivity("Memulai scan QR", "Berhasil");
        });
        
        btnStop.addActionListener(e -> {
            stopScan();
            logController.logActivity("Menghentikan scan QR", "Berhasil");
        });

        controlPanel.add(btnStart);
        controlPanel.add(btnStop);
        
        JButton btnManualScan = createButton("Manual Scan", new Color(155, 89, 182));
        btnManualScan.addActionListener(e -> {
            manualScan();
            logController.logActivity("Manual scan QR", "Berhasil");
        });
        btnManualScan.setEnabled(!autoMode);
        controlPanel.add(btnManualScan);
        
        panel.add(controlPanel, BorderLayout.SOUTH);

        txtResult = new JTextArea(3, 30);
        txtResult.setEditable(false);
        txtResult.setBackground(new Color(240, 240, 240));
        txtResult.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtResult);
        scrollPane.setPreferredSize(new Dimension(450, 60));
        panel.add(scrollPane, BorderLayout.NORTH);

        return panel;
    }
    
    private void manualScan() {
        if (!autoMode && webcam != null && webcam.isOpen()) {
            scanQRCode();
        }
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel title = new JLabel("RIWAYAT ABSENSI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(new Color(70, 130, 180));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        panel.add(title, BorderLayout.NORTH);

        // Filter Panel - dibuat 2 baris
        JPanel filterContainer = new JPanel(new GridLayout(2, 1, 0, 2));
        filterContainer.setBackground(Color.WHITE);
        filterContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Baris 1: Filter Tanggal dan Kelas
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row1Panel.setBackground(Color.WHITE);
        
        row1Panel.add(new JLabel("Tanggal:"));
        cmbFilterTanggal = new JComboBox<>();
        cmbFilterTanggal.addItem(LocalDate.now().toString());
        cmbFilterTanggal.setEditable(true);
        cmbFilterTanggal.setPreferredSize(new Dimension(100, 25));
        cmbFilterTanggal.addActionListener(e -> {
            filterData();
            logController.logActivity("Filter absensi berdasarkan tanggal: " + cmbFilterTanggal.getSelectedItem(), "Berhasil");
        });
        row1Panel.add(cmbFilterTanggal);
        
        row1Panel.add(new JLabel("  Kelas:"));
        cmbFilterKelas = new JComboBox<>();
        cmbFilterKelas.addItem("Semua");
        cmbFilterKelas.addItem("10");
        cmbFilterKelas.addItem("11");
        cmbFilterKelas.addItem("12");
        cmbFilterKelas.setPreferredSize(new Dimension(70, 25));
        cmbFilterKelas.addActionListener(e -> filterData());
        row1Panel.add(cmbFilterKelas);
        
        // Baris 2: Filter Jurusan dan Export
        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        row2Panel.setBackground(Color.WHITE);
        
        row2Panel.add(new JLabel("Jurusan:"));
        cmbFilterJurusan = new JComboBox<>();
        cmbFilterJurusan.addItem("Semua");
        cmbFilterJurusan.addItem("RPL");
        cmbFilterJurusan.addItem("TKJ");
        cmbFilterJurusan.addItem("MULTIMEDIA");
        cmbFilterJurusan.setPreferredSize(new Dimension(90, 25));
        cmbFilterJurusan.addActionListener(e -> filterData());
        row2Panel.add(cmbFilterJurusan);
        
        // Export Button di baris 2
        btnExport = createButton("Export Excel", new Color(52, 152, 219));
        btnExport.setPreferredSize(new Dimension(100, 25));
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExport.addActionListener(e -> exportToExcel());
        row2Panel.add(Box.createHorizontalStrut(10));
        row2Panel.add(btnExport);

        filterContainer.add(row1Panel);
        filterContainer.add(row2Panel);
        
        panel.add(filterContainer, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new String[]{"Tanggal", "Jam", "NIS", "Nama", "Kelas", "Jurusan", "Status"}, 0
        );
        tableAbsensi = new JTable(tableModel);
        tableAbsensi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableAbsensi.setRowHeight(25);
        tableAbsensi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableAbsensi.getTableHeader().setBackground(new Color(70, 130, 180));
        tableAbsensi.getTableHeader().setForeground(new Color(93, 173, 226));
        
        JScrollPane scrollPane = new JScrollPane(tableAbsensi);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    
    private void filterData() {
        String tanggal = (String) cmbFilterTanggal.getSelectedItem();
        String kelas = (String) cmbFilterKelas.getSelectedItem();
        String jurusan = (String) cmbFilterJurusan.getSelectedItem();
        
        if (tanggal == null || tanggal.trim().isEmpty()) return;
        
        tableModel.setRowCount(0);
        List<Object[]> allData = controller.getRiwayatAbsensi(tanggal);
        
        int beforeCount = allData.size();
        int afterCount = 0;
        
        for (Object[] row : allData) {
            boolean matchKelas = kelas.equals("Semua Kelas") || row[4].toString().equals(kelas);
            boolean matchJurusan = jurusan.equals("Semua Jurusan") || row[5].toString().equalsIgnoreCase(jurusan);
            
            if (matchKelas && matchJurusan) {
                tableModel.addRow(row);
                afterCount++;
            }
        }
        
        logController.logActivity("Filter absensi - Tanggal: " + tanggal + 
            ", Kelas: " + kelas + ", Jurusan: " + jurusan + 
            " (" + afterCount + "/" + beforeCount + " data)", "Berhasil");
    }
    
    public void loadRiwayat(String tanggal) {
        if (tableModel != null && cmbFilterKelas != null && cmbFilterJurusan != null) {
            tableModel.setRowCount(0);
            List<Object[]> list = controller.getRiwayatAbsensi(tanggal);
            for (Object[] row : list) {
                tableModel.addRow(row);
            }
            
            // Reset filter ke default
            cmbFilterKelas.setSelectedIndex(0);
            cmbFilterJurusan.setSelectedIndex(0);
            
            logController.logActivity("Memuat riwayat absensi tanggal: " + tanggal + " (" + list.size() + " data)", "Berhasil");
        }
    }
    
    private void initWebcam() {
        try {
            webcam = Webcam.getDefault();
            
            if (webcam == null) {
                java.util.List<Webcam> webcams = Webcam.getWebcams();
                if (!webcams.isEmpty()) {
                    webcam = webcams.get(0);
                    System.out.println("Menggunakan webcam: " + webcam.getName());
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Tidak ada webcam terdeteksi!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    logController.logActivity("Webcam tidak terdeteksi", "Gagal");
                    return;
                }
            }
            
            Dimension[] resolutions = webcam.getViewSizes();
            Dimension selectedRes = null;
            
            for (Dimension d : resolutions) {
                if (d.width == 640 && d.height == 480) {
                    selectedRes = d;
                    break;
                }
            }
            
            if (selectedRes == null && resolutions.length > 0) {
                selectedRes = resolutions[0];
            }
            
            if (selectedRes != null) {
                webcam.setViewSize(selectedRes);
                System.out.println("Resolusi: " + selectedRes.width + "x" + selectedRes.height);
            }
            
            webcamPanel = new WebcamPanel(webcam, false);
            webcamPanel.setFPSDisplayed(true);
            webcamPanel.setDisplayDebugInfo(true);
            webcamPanel.setImageSizeDisplayed(true);
            webcamPanel.setMirrored(true);
            webcamPanel.setPreferredSize(new Dimension(400, 300));
            
            // Update camera container dengan webcamPanel
            if (cameraContainer != null) {
                cameraContainer.removeAll();
                cameraContainer.add(webcamPanel, BorderLayout.CENTER);
                cameraContainer.revalidate();
                cameraContainer.repaint();
            }
            
            System.out.println("Webcam berhasil diinisialisasi (siap dibuka)");
            logController.logActivity("Inisialisasi webcam berhasil", "Berhasil");
            
        } catch (Exception e) {
            e.printStackTrace();
            logController.logActivity("Error inisialisasi webcam: " + e.getMessage(), "Error");
            JOptionPane.showMessageDialog(this, 
                "Error inisialisasi webcam: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshCameraDisplay() {
        if (webcamPanel != null) {
            SwingUtilities.invokeLater(() -> {
                webcamPanel.repaint();
                webcamPanel.revalidate();
            });
        }
    }
    
    private void startScan() {
        if (webcam != null && webcam.isOpen() && !scanning) {
            scanning = true;
            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            
            timer = new Timer(100, e -> {
                if (autoMode) {
                    scanQRCode();
                }
            });
            timer.start();
        }
    }
    
    private void stopScan() {
        if (scanning) {
            scanning = false;
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            
            if (timer != null) {
                timer.stop();
            }
        }
    }
    
    private void playBeepSound() {
        Toolkit.getDefaultToolkit().beep();
    }
    
    private void scanQRCode() {
        if (webcam != null && webcam.isOpen()) {
            BufferedImage image = webcam.getImage();
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                
                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    String qrData = result.getText();
                    
                    if (qrData.equals(lastScannedQR)) {
                        return;
                    }
                    
                    lastScannedQR = qrData;
                    
                    String status = controller.prosesAbsensi(qrData);
                    String message;
                    Color resultColor;
                    
                    playBeepSound();
                    
                    switch (status) {
                        case "Hadir":
                            message = "✓ Absensi berhasil untuk NIS: " + qrData;
                            resultColor = new Color(46, 204, 113);
                            // Refresh data dengan tanggal yang sedang dipilih
                            String currentDate = (String) cmbFilterTanggal.getSelectedItem();
                            loadRiwayat(currentDate);
                            logController.logActivity("Absensi berhasil NIS: " + qrData, "Berhasil");
                            break;
                        case "Sudah Absen":
                            message = "⚠ Siswa dengan NIS " + qrData + " sudah absen hari ini";
                            resultColor = new Color(241, 196, 15);
                            logController.logActivity("Absensi gagal - sudah absen NIS: " + qrData, "Gagal");
                            break;
                        case "Data Tidak Ditemukan":
                            message = "✗ Data dengan NIS " + qrData + " tidak ditemukan";
                            resultColor = new Color(231, 76, 60);
                            logController.logActivity("Absensi gagal - NIS tidak ditemukan: " + qrData, "Gagal");
                            break;
                        default:
                            message = "Terjadi kesalahan";
                            resultColor = Color.RED;
                            logController.logActivity("Absensi error: " + status, "Error");
                    }
                    
                    txtResult.setText(message);
                    txtResult.setForeground(resultColor);
                    
                    if (!autoMode) {
                        stopScan();
                    }
                    
                    Timer resetTimer = new Timer(2000, e -> lastScannedQR = "");
                    resetTimer.setRepeats(false);
                    resetTimer.start();
                    
                } catch (NotFoundException e) {
                    // No QR code found
                }
            }
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
        
        // Buat nama file berdasarkan filter
        String fileName = "absensi";
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
            
            try (Workbook workbook = new XSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(filePath)) {
                
                // Buat sheet dengan nama sesuai filter
                String sheetName = "Absensi";
                if (!kelas.equals("Semua Kelas")) {
                    sheetName += " Kelas " + kelas;
                }
                if (!jurusan.equals("Semua Jurusan")) {
                    sheetName += " " + jurusan;
                }
                Sheet sheet = workbook.createSheet(sheetName);
                
                // Header
                Row headerRow = sheet.createRow(0);
                String[] columns = {"Tanggal", "Jam", "NIS", "Nama", "Kelas", "Jurusan", "Status"};
                
                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Data dari tabel yang sudah difilter
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }
                
                // Auto-size columns
                for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                workbook.write(fileOut);
                
                // Tampilkan informasi filter yang digunakan
                String filterInfo = "Filter yang digunakan:\n";
                filterInfo += "- Tanggal: " + tanggal + "\n";
                filterInfo += "- Kelas: " + kelas + "\n";
                filterInfo += "- Jurusan: " + jurusan + "\n";
                filterInfo += "Total data: " + tableModel.getRowCount() + " baris";
                
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil diekspor ke:\n" + filePath + "\n\n" + filterInfo, 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                logController.logActivity("Export Excel absensi: " + fileName, "Berhasil");
                
            } catch (Exception e) {
                e.printStackTrace();
                logController.logActivity("Gagal export Excel: " + e.getMessage(), "Error");
                JOptionPane.showMessageDialog(this, 
                    "Gagal mengekspor data: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
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
        button.setPreferredSize(new Dimension(120, 35));
        
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
    
    private void backToDashboard() {
        stopScan();
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        logController.logActivity("Kembali ke dashboard dari form absensi", "Berhasil");
        new Dashboard().setVisible(true);
        dispose();
    }
    
    @Override
    public void dispose() {
        stopScan();
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        super.dispose();
    }
}