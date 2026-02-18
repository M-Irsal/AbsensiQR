# Aplikasi Absensi QR Code

Aplikasi desktop untuk sistem absensi menggunakan QR Code berbasis Java Swing.

## Fitur
- Login Admin
- Manajemen Data Siswa
- Generate QR Code per Siswa
- Scan QR Code menggunakan Webcam
- Absensi Otomatis (Real-time)
- Riwayat Absensi dengan Filter Tanggal
- Export ke Excel

## Library yang Digunakan
- ZXing (QR Code)
- Webcam Capture API
- MySQL Connector
- Apache POI (Excel)

## Cara Menjalankan

### 1. Setup Database
- Jalankan MySQL
- Execute script `database.sql`

### 2. Setup Library
- Buat folder `lib` di root project
- Download dan copy semua .jar ke folder `lib`:
  - zxing-core-3.4.1.jar
  - zxing-javase-3.4.1.jar
  - webcam-capture-0.3.12.jar
  - mysql-connector-java-8.0.33.jar
  - poi-5.2.3.jar
  - poi-ooxml-5.2.3.jar
  - poi-ooxml-schemas-5.2.3.jar
  - xmlbeans-5.1.1.jar
  - commons-collections4-4.4.jar
  - commons-compress-1.21.jar

### 3. Compile
```bash
javac -cp "lib/*;src" src/Main.java -d bin