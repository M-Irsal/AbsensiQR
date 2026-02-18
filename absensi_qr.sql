-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 17 Feb 2026 pada 06.36
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Basis data: `absensi_qr`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `absensi`
--

CREATE TABLE `absensi` (
  `id` int(11) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `tanggal` date NOT NULL,
  `jam_masuk` time NOT NULL,
  `status` varchar(20) DEFAULT 'Hadir'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `absensi`
--

INSERT INTO `absensi` (`id`, `nis`, `tanggal`, `jam_masuk`, `status`) VALUES
(1, '263001', '2026-02-17', '11:32:01', 'Hadir'),
(2, '243001', '2026-02-17', '11:32:12', 'Hadir'),
(3, '253001', '2026-02-17', '11:32:19', 'Hadir'),
(4, '242001', '2026-02-17', '11:33:22', 'Hadir'),
(5, '252001', '2026-02-17', '11:33:28', 'Hadir'),
(6, '262001', '2026-02-17', '11:33:33', 'Hadir'),
(7, '241001', '2026-02-17', '11:33:59', 'Hadir'),
(8, '251001', '2026-02-17', '11:34:04', 'Hadir'),
(9, '261001', '2026-02-17', '11:34:09', 'Hadir');

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nama_lengkap` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `nama_lengkap`) VALUES
(1, 'admin', '0192023a7bbd73250516f069df18b500', 'Administrator');

-- --------------------------------------------------------

--
-- Struktur dari tabel `siswa`
--

CREATE TABLE `siswa` (
  `id` int(11) NOT NULL,
  `nis` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `kelas` varchar(20) NOT NULL,
  `jurusan` varchar(50) NOT NULL,
  `qr_code` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `siswa`
--

INSERT INTO `siswa` (`id`, `nis`, `nama`, `kelas`, `jurusan`, `qr_code`, `created_at`) VALUES
(1, '241001', 'Ahmad RPL 1', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(2, '241002', 'Budi RPL 2', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(3, '241003', 'Citra RPL 3', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(4, '241004', 'Dedi RPL 4', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(5, '241005', 'Eka RPL 5', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(6, '241006', 'Fajar RPL 6', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(7, '241007', 'Gita RPL 7', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(8, '241008', 'Hendra RPL 8', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(9, '241009', 'Indah RPL 9', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(10, '241010', 'Joko RPL 10', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(11, '241011', 'Kartika RPL 11', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(12, '241012', 'Lukman RPL 12', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(13, '241013', 'Maya RPL 13', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(14, '241014', 'Nanda RPL 14', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(15, '241015', 'Oscar RPL 15', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(16, '241016', 'Putri RPL 16', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(17, '241017', 'Qori RPL 17', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(18, '241018', 'Rama RPL 18', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(19, '241019', 'Sari RPL 19', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(20, '241020', 'Tono RPL 20', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(21, '241021', 'Umar RPL 21', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(22, '241022', 'Vina RPL 22', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(23, '241023', 'Wawan RPL 23', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(24, '241024', 'Xaverius RPL 24', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(25, '241025', 'Yanti RPL 25', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(26, '241026', 'Zainal RPL 26', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(27, '241027', 'Aulia RPL 27', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(28, '241028', 'Bayu RPL 28', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(29, '241029', 'Cynthia RPL 29', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(30, '241030', 'Denny RPL 30', '10', 'RPL', NULL, '2026-02-17 03:24:43'),
(31, '242001', 'Agus TKJ 1', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(32, '242002', 'Bella TKJ 2', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(33, '242003', 'Candra TKJ 3', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(34, '242004', 'Dewi TKJ 4', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(35, '242005', 'Eko TKJ 5', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(36, '242006', 'Farah TKJ 6', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(37, '242007', 'Galih TKJ 7', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(38, '242008', 'Hani TKJ 8', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(39, '242009', 'Irfan TKJ 9', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(40, '242010', 'Juwita TKJ 10', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(41, '242011', 'Kurnia TKJ 11', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(42, '242012', 'Lestari TKJ 12', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(43, '242013', 'Miftah TKJ 13', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(44, '242014', 'Nadia TKJ 14', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(45, '242015', 'Oki TKJ 15', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(46, '242016', 'Pramono TKJ 16', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(47, '242017', 'Qonita TKJ 17', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(48, '242018', 'Rizky TKJ 18', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(49, '242019', 'Sinta TKJ 19', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(50, '242020', 'Teguh TKJ 20', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(51, '242021', 'Ulfah TKJ 21', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(52, '242022', 'Vicky TKJ 22', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(53, '242023', 'Winda TKJ 23', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(54, '242024', 'Yusuf TKJ 24', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(55, '242025', 'Zahra TKJ 25', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(56, '242026', 'Aditya TKJ 26', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(57, '242027', 'Bunga TKJ 27', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(58, '242028', 'Cahyo TKJ 28', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(59, '242029', 'Dina TKJ 29', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(60, '242030', 'Eka TKJ 30', '10', 'TKJ', NULL, '2026-02-17 03:24:43'),
(61, '243001', 'Aris Multimedia 1', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(62, '243002', 'Bambang Multimedia 2', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(63, '243003', 'Chika Multimedia 3', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(64, '243004', 'Dodi Multimedia 4', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(65, '243005', 'Elsa Multimedia 5', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(66, '243006', 'Feri Multimedia 6', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(67, '243007', 'Gina Multimedia 7', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(68, '243008', 'Hadi Multimedia 8', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(69, '243009', 'Intan Multimedia 9', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(70, '243010', 'Jarwo Multimedia 10', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(71, '243011', 'Kiki Multimedia 11', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(72, '243012', 'Lina Multimedia 12', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(73, '243013', 'Miko Multimedia 13', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(74, '243014', 'Nina Multimedia 14', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(75, '243015', 'Oman Multimedia 15', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(76, '243016', 'Putu Multimedia 16', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(77, '243017', 'Rina Multimedia 17', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(78, '243018', 'Soni Multimedia 18', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(79, '243019', 'Tuti Multimedia 19', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(80, '243020', 'Ujang Multimedia 20', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(81, '243021', 'Vera Multimedia 21', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(82, '243022', 'Wati Multimedia 22', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(83, '243023', 'Yoga Multimedia 23', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(84, '243024', 'Zaki Multimedia 24', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(85, '243025', 'Ayu Multimedia 25', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(86, '243026', 'Bima Multimedia 26', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(87, '243027', 'Cici Multimedia 27', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(88, '243028', 'Dian Multimedia 28', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(89, '243029', 'Endah Multimedia 29', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(90, '243030', 'Fahri Multimedia 30', '10', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(91, '251001', 'Adi RPL 1', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(92, '251002', 'Boby RPL 2', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(93, '251003', 'Cici RPL 3', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(94, '251004', 'Doni RPL 4', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(95, '251005', 'Euis RPL 5', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(96, '251006', 'Firman RPL 6', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(97, '251007', 'Gita RPL 7', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(98, '251008', 'Herman RPL 8', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(99, '251009', 'Indra RPL 9', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(100, '251010', 'Juli RPL 10', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(101, '251011', 'Kartika RPL 11', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(102, '251012', 'Luki RPL 12', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(103, '251013', 'Mira RPL 13', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(104, '251014', 'Niko RPL 14', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(105, '251015', 'Ola RPL 15', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(106, '251016', 'Putra RPL 16', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(107, '251017', 'Rina RPL 17', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(108, '251018', 'Samsul RPL 18', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(109, '251019', 'Tina RPL 19', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(110, '251020', 'Umar RPL 20', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(111, '251021', 'Vina RPL 21', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(112, '251022', 'Wawan RPL 22', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(113, '251023', 'Xena RPL 23', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(114, '251024', 'Yudi RPL 24', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(115, '251025', 'Zara RPL 25', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(116, '251026', 'Ani RPL 26', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(117, '251027', 'Budi RPL 27', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(118, '251028', 'Caca RPL 28', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(119, '251029', 'Dedi RPL 29', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(120, '251030', 'Eka RPL 30', '11', 'RPL', NULL, '2026-02-17 03:24:43'),
(121, '252001', 'Agung TKJ 1', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(122, '252002', 'Bunga TKJ 2', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(123, '252003', 'Cakra TKJ 3', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(124, '252004', 'Dewi TKJ 4', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(125, '252005', 'Edo TKJ 5', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(126, '252006', 'Fani TKJ 6', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(127, '252007', 'Ganda TKJ 7', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(128, '252008', 'Hesti TKJ 8', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(129, '252009', 'Iman TKJ 9', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(130, '252010', 'Joko TKJ 10', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(131, '252011', 'Kiki TKJ 11', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(132, '252012', 'Lala TKJ 12', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(133, '252013', 'Momon TKJ 13', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(134, '252014', 'Nia TKJ 14', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(135, '252015', 'Opik TKJ 15', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(136, '252016', 'Pipit TKJ 16', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(137, '252017', 'Qori TKJ 17', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(138, '252018', 'Rara TKJ 18', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(139, '252019', 'Siska TKJ 19', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(140, '252020', 'Taufik TKJ 20', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(141, '252021', 'Uci TKJ 21', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(142, '252022', 'Vivi TKJ 22', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(143, '252023', 'Windi TKJ 23', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(144, '252024', 'Yayan TKJ 24', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(145, '252025', 'Zulfa TKJ 25', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(146, '252026', 'Akmal TKJ 26', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(147, '252027', 'Bela TKJ 27', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(148, '252028', 'Cici TKJ 28', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(149, '252029', 'Dani TKJ 29', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(150, '252030', 'Elok TKJ 30', '11', 'TKJ', NULL, '2026-02-17 03:24:43'),
(151, '253001', 'Alya Multimedia 1', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(152, '253002', 'Bagas Multimedia 2', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(153, '253003', 'Cindy Multimedia 3', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(154, '253004', 'Dimas Multimedia 4', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(155, '253005', 'Elma Multimedia 5', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(156, '253006', 'Fajar Multimedia 6', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(157, '253007', 'Gita Multimedia 7', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(158, '253008', 'Hendra Multimedia 8', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(159, '253009', 'Ika Multimedia 9', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(160, '253010', 'Jajang Multimedia 10', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(161, '253011', 'Kartika Multimedia 11', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(162, '253012', 'Luki Multimedia 12', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(163, '253013', 'Mila Multimedia 13', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(164, '253014', 'Nando Multimedia 14', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(165, '253015', 'Oka Multimedia 15', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(166, '253016', 'Putri Multimedia 16', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(167, '253017', 'Rama Multimedia 17', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(168, '253018', 'Sari Multimedia 18', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(169, '253019', 'Tono Multimedia 19', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(170, '253020', 'Umi Multimedia 20', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(171, '253021', 'Vina Multimedia 21', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(172, '253022', 'Wawan Multimedia 22', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(173, '253023', 'Yuni Multimedia 23', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(174, '253024', 'Zaki Multimedia 24', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(175, '253025', 'Ari Multimedia 25', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(176, '253026', 'Bima Multimedia 26', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(177, '253027', 'Caca Multimedia 27', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(178, '253028', 'Dodi Multimedia 28', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(179, '253029', 'Ega Multimedia 29', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(180, '253030', 'Fina Multimedia 30', '11', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(181, '261001', 'Abdul RPL 1', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(182, '261002', 'Beti RPL 2', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(183, '261003', 'Cecep RPL 3', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(184, '261004', 'Dina RPL 4', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(185, '261005', 'Eman RPL 5', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(186, '261006', 'Fitri RPL 6', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(187, '261007', 'Gugun RPL 7', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(188, '261008', 'Heni RPL 8', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(189, '261009', 'Iwan RPL 9', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(190, '261010', 'Juju RPL 10', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(191, '261011', 'Kurnia RPL 11', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(192, '261012', 'Lilis RPL 12', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(193, '261013', 'Maman RPL 13', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(194, '261014', 'Neni RPL 14', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(195, '261015', 'Otang RPL 15', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(196, '261016', 'Popon RPL 16', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(197, '261017', 'Rendi RPL 17', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(198, '261018', 'Siti RPL 18', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(199, '261019', 'Tata RPL 19', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(200, '261020', 'Usep RPL 20', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(201, '261021', 'Vita RPL 21', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(202, '261022', 'Wawan RPL 22', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(203, '261023', 'Yayat RPL 23', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(204, '261024', 'Zizi RPL 24', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(205, '261025', 'Asep RPL 25', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(206, '261026', 'Bambang RPL 26', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(207, '261027', 'Cucu RPL 27', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(208, '261028', 'Dede RPL 28', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(209, '261029', 'Endang RPL 29', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(210, '261030', 'Fikri RPL 30', '12', 'RPL', NULL, '2026-02-17 03:24:43'),
(211, '262001', 'Andi TKJ 1', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(212, '262002', 'Bella TKJ 2', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(213, '262003', 'Cepi TKJ 3', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(214, '262004', 'Desi TKJ 4', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(215, '262005', 'Eka TKJ 5', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(216, '262006', 'Feri TKJ 6', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(217, '262007', 'Gilang TKJ 7', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(218, '262008', 'Hana TKJ 8', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(219, '262009', 'Iis TKJ 9', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(220, '262010', 'Jajang TKJ 10', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(221, '262011', 'Kiki TKJ 11', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(222, '262012', 'Lia TKJ 12', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(223, '262013', 'Miko TKJ 13', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(224, '262014', 'Nina TKJ 14', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(225, '262015', 'Oman TKJ 15', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(226, '262016', 'Pipin TKJ 16', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(227, '262017', 'Rian TKJ 17', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(228, '262018', 'Siska TKJ 18', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(229, '262019', 'Tedi TKJ 19', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(230, '262020', 'Ujang TKJ 20', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(231, '262021', 'Vera TKJ 21', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(232, '262022', 'Winda TKJ 22', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(233, '262023', 'Yoga TKJ 23', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(234, '262024', 'Zulfa TKJ 24', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(235, '262025', 'Agus TKJ 25', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(236, '262026', 'Budi TKJ 26', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(237, '262027', 'Caca TKJ 27', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(238, '262028', 'Dodi TKJ 28', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(239, '262029', 'Eneng TKJ 29', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(240, '262030', 'Fahmi TKJ 30', '12', 'TKJ', NULL, '2026-02-17 03:24:43'),
(241, '263001', 'Aneu Multimedia 1', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(242, '263002', 'Beben Multimedia 2', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(243, '263003', 'Cinta Multimedia 3', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(244, '263004', 'Deni Multimedia 4', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(245, '263005', 'Euis Multimedia 5', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(246, '263006', 'Fadli Multimedia 6', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(247, '263007', 'Gina Multimedia 7', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(248, '263008', 'Husni Multimedia 8', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(249, '263009', 'Ida Multimedia 9', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(250, '263010', 'Jaja Multimedia 10', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(251, '263011', 'Kartika Multimedia 11', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(252, '263012', 'Leni Multimedia 12', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(253, '263013', 'Mumu Multimedia 13', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(254, '263014', 'Nana Multimedia 14', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(255, '263015', 'Opik Multimedia 15', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(256, '263016', 'Pipit Multimedia 16', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(257, '263017', 'Rizal Multimedia 17', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(258, '263018', 'Santi Multimedia 18', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(259, '263019', 'Tuti Multimedia 19', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(260, '263020', 'Urip Multimedia 20', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(261, '263021', 'Vivi Multimedia 21', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(262, '263022', 'Wawan Multimedia 22', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(263, '263023', 'Yanti Multimedia 23', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(264, '263024', 'Zaenal Multimedia 24', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(265, '263025', 'Anwar Multimedia 25', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(266, '263026', 'Bunga Multimedia 26', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(267, '263027', 'Candra Multimedia 27', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(268, '263028', 'Dewi Multimedia 28', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(269, '263029', 'Egi Multimedia 29', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43'),
(270, '263030', 'Farah Multimedia 30', '12', 'MULTIMEDIA', NULL, '2026-02-17 03:24:43');

--
-- Indeks untuk tabel yang dibuang
--

--
-- Indeks untuk tabel `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_absensi` (`nis`,`tanggal`);

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indeks untuk tabel `siswa`
--
ALTER TABLE `siswa`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nis` (`nis`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT untuk tabel `siswa`
--
ALTER TABLE `siswa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=271;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`nis`) REFERENCES `siswa` (`nis`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
