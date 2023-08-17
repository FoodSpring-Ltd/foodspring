--mst_divisi
INSERT INTO `mst_divisi` VALUES (1, 1, '2023-03-10 16:43:54', 'Divisi IT', 1, 'IT-01', NULL, NULL, 'IT');
INSERT INTO `mst_divisi` VALUES (2, 1, '2023-03-11 18:39:56', NULL, 1, 'MRK-002', 1, '2023-03-11 18:42:23', 'MARKETING');
INSERT INTO `mst_divisi` VALUES (3, 1, '2023-03-11 18:44:05', NULL, 1, 'OPS - 01', 1, '2023-03-11 18:44:23', 'OPERASIONAL');
INSERT INTO `mst_divisi` VALUES (4, 1, '2023-03-12 20:19:51', NULL, 1, 'HRD  - 01', NULL, NULL, 'HUMAN RESOURCE');
INSERT INTO `mst_divisi` VALUES (5, 1, '2023-03-13 01:27:38', NULL, 1, 'FIN-01', NULL, NULL, 'FINANCE');
INSERT INTO `mst_divisi` VALUES (6, 1, '2023-03-13 04:53:11', NULL, 1, 'SCRTY-01', 1, '2023-03-13 05:32:40', 'KEAMANAN');
INSERT INTO `mst_divisi` VALUES (7, 1, '2023-03-13 04:55:15', NULL, 0, 'SCRTY-02', 1, '2023-03-13 05:07:14', 'KEAMANAN');
INSERT INTO `mst_divisi` VALUES (8, 1, '2023-03-13 05:33:36', NULL, 0, 'SCRTY-02', 1, '2023-03-13 05:34:50', 'KEAMANAN DAN KESEHATAN');
INSERT INTO `mst_divisi` VALUES (9, 1, '2023-03-13 05:35:05', NULL, 0, 'SCRTY-02', 1, '2023-03-13 05:35:15', 'KEAMANAN DAN KESEHATAN');
INSERT INTO `mst_divisi` VALUES (10, 1, '2023-03-13 05:35:25', NULL, 1, 'SCRTY-02', NULL, NULL, 'KEAMANAN DAN KESEHATAN');
INSERT INTO `mst_divisi` VALUES (11, 1, '2023-03-15 00:56:19', NULL, 1, 'asdadasd', 1, '2023-03-15 00:56:28', 'adads');


--mst_employee
INSERT INTO `mst_employee` VALUES (1, '123123', 'aaaaa', '123');
INSERT INTO `mst_employee` VALUES (2, '123123123', 'ddddddd', '213132123');
INSERT INTO `mst_employee` VALUES (3, '123123123', 'bbbbbbbb', '12312313');
INSERT INTO `mst_employee` VALUES (4, '345345345345345', 'ccccccccc', '345345345345');
INSERT INTO `mst_employee` VALUES (5, '3453453453', 'eeeeeeeeeee', '34535445345345');
INSERT INTO `mst_employee` VALUES (6, '345345345345', 'iiiiiiiiiii', '34534534534');
INSERT INTO `mst_employee` VALUES (7, '345345345345', 'hhhhhhhh', '345345345345345');
INSERT INTO `mst_employee` VALUES (8, '65756756567345345', 'fffffffffff', '3457657567567');
INSERT INTO `mst_employee` VALUES (9, '345345345345', 'ggggggggggg', '345345345345');

--mst_menu_header
INSERT INTO `mst_menu_header` VALUES (1, 1, '2023-03-05 06:27:25', 'YANG BERHUBUNGAN DENGAN MODUL SALES', 1, 1, '2023-03-19 15:55:13', 'SALES');
INSERT INTO `mst_menu_header` VALUES (2, 1, '2023-03-05 06:27:52', 'YANG BERHUBUNGAN DENGAN MODUL HUMAN RESOURCE', 1, NULL, NULL, 'HRD');
INSERT INTO `mst_menu_header` VALUES (3, 1, '2023-03-05 06:28:00', 'YANG BERHUBUNGAN DENGAN MODUL USER MANAGEMENT', 1, NULL, NULL, 'USER MANAGEMENT');
INSERT INTO `mst_menu_header` VALUES (4, 1, '2023-03-05 06:28:50', 'UNTUK SAMPLE SAJA', 1, NULL, NULL, 'EXTRA');
INSERT INTO `mst_menu_header` VALUES (5, 1, '2023-03-05 06:29:11', 'YANG BERHUBUNGAN DENGAN MODUL FINANCE', 1, NULL, NULL, 'FINANCE');
INSERT INTO `mst_menu_header` VALUES (6, 1, '2023-03-05 06:29:26', 'UNTUK SEMUA USER', 1, NULL, NULL, 'GLOBAL');

--mst_paket_layanan
INSERT INTO `mst_paket_layanan` VALUES (1, 1, '2023-03-11 22:09:00', 10000, 1, NULL, NULL, 'Cuci Bersih', 'Reguler');
INSERT INTO `mst_paket_layanan` VALUES (2, 1, '2023-03-11 22:09:24', 15000, 1, NULL, NULL, 'Cuci Mengkilap', 'Premium');
INSERT INTO `mst_paket_layanan` VALUES (3, 1, '2023-03-11 22:09:51', 20000, 1, NULL, NULL, 'Cuci ++', 'Reguler');
INSERT INTO `mst_paket_layanan` VALUES (4, 1, '2023-03-11 22:09:56', 30000, 1, NULL, NULL, 'Cuci ++', 'Premium');

--mst_pelanggan
INSERT INTO `mst_pelanggan` VALUES (1, 1, '2023-03-11 22:07:45', 1, NULL, NULL, 'Syafril');
INSERT INTO `mst_pelanggan` VALUES (2, 1, '2023-03-11 22:08:08', 1, NULL, NULL, 'Syarifudin');
INSERT INTO `mst_pelanggan` VALUES (3, 1, '2023-03-11 22:08:15', 1, NULL, NULL, 'Deby');
INSERT INTO `mst_pelanggan` VALUES (4, 1, '2023-03-11 22:08:26', 1, NULL, NULL, 'Fadhil');
INSERT INTO `mst_pelanggan` VALUES (5, 1, '2023-03-11 22:08:33', 1, NULL, NULL, 'Adi');

--mst_pembayaran
INSERT INTO `mst_pembayaran` VALUES (1, 1, '2023-03-11 22:10:34', 1, NULL, NULL, 'Cash');
INSERT INTO `mst_pembayaran` VALUES (2, 1, '2023-03-11 22:10:47', 1, NULL, NULL, 'Kartu Kredit');
INSERT INTO `mst_pembayaran` VALUES (3, 1, '2023-03-11 22:10:56', 1, NULL, NULL, 'Debit');

--mst_student
INSERT INTO `mst_student` VALUES (7, 'raja.mobile.office@gmail.com', 'Paul', 'Christian');
INSERT INTO `mst_student` VALUES (8, 'poll.chihuy@gmail.com', 'Paul5', 'ChristianZZZ');
INSERT INTO `mst_student` VALUES (11, '123123123123123123123123123123123123', '345345345345', 'hahahazzzzzz');
INSERT INTO `mst_student` VALUES (12, 'raja.mobile.office@gmail.com', '123', '123123');

--trx_pesanan
INSERT INTO `trx_pesanan` VALUES (1, 3, 1, '2023-02-01 22:17:45', 1, 1, '2023-03-11 22:39:24', 2, 1, 3);
INSERT INTO `trx_pesanan` VALUES (2, 4, 1, '2023-03-11 22:59:05', 1, 1, '2023-03-11 23:02:27', 1, 2, 2);
INSERT INTO `trx_pesanan` VALUES (3, 4, 1, '2023-03-11 22:59:23', 1, NULL, NULL, 2, 3, 1);
INSERT INTO `trx_pesanan` VALUES (4, 4, 11, '2023-03-14 17:05:56', 1, NULL, NULL, 2, 1, 3);
INSERT INTO `trx_pesanan` VALUES (5, 5, 11, '2023-03-14 17:06:17', 1, NULL, NULL, 1, 4, 1);
INSERT INTO `trx_pesanan` VALUES (6, 6, 1, '2023-03-16 19:35:06', 1, NULL, NULL, 1, 1, 1);
INSERT INTO `trx_pesanan` VALUES (7, 6, 1, '2023-03-16 19:35:14', 1, NULL, NULL, 1, 3, 1);
INSERT INTO `trx_pesanan` VALUES (8, 6, 1, '2023-03-16 19:35:21', 1, NULL, NULL, 1, 5, 1);

--mst_akses
INSERT INTO `mst_akses` VALUES (1, 1, '2023-03-05 07:02:58', 1, 1, '2023-03-13 23:18:22', 'CUSTOMER', 2);
INSERT INTO `mst_akses` VALUES (2, 1, '2023-03-05 07:03:27', 1, NULL, NULL, 'SALES STAFF', NULL);
INSERT INTO `mst_akses` VALUES (3, 1, '2023-03-05 07:03:47', 1, NULL, NULL, 'SALES MANAGER', NULL);
INSERT INTO `mst_akses` VALUES (4, 1, '2023-03-05 20:07:59', 1, NULL, NULL, 'FINANCE STAFF', NULL);
INSERT INTO `mst_akses` VALUES (5, 1, '2023-03-05 20:21:51', 1, NULL, NULL, 'FINANCE SPV', NULL);
INSERT INTO `mst_akses` VALUES (6, 1, '2023-03-06 20:19:16', 1, 11, '2023-03-14 16:58:18', 'ADMIN', 1);
INSERT INTO `mst_akses` VALUES (7, 1, '2023-03-11 07:07:02', 0, 1, '2023-03-11 07:59:53', 'HUMAN RESOURCE', 1);
INSERT INTO `mst_akses` VALUES (8, 1, '2023-03-11 07:21:28', 0, 1, '2023-03-11 07:59:45', 'HUMAN RESOURCE STAFF', 1);
INSERT INTO `mst_akses` VALUES (9, 1, '2023-03-11 07:23:17', 0, 1, '2023-03-11 07:57:30', 'HUMAN RESOURCE STAFF', 1);
INSERT INTO `mst_akses` VALUES (10, 1, '2023-03-11 08:30:06', 1, NULL, NULL, 'SALES KOMPOR', 1);
INSERT INTO `mst_akses` VALUES (11, 1, '2023-03-11 08:34:36', 1, 1, '2023-03-12 04:04:52', 'SALES PISO', 1);
INSERT INTO `mst_akses` VALUES (12, 1, '2023-03-11 08:35:32', 1, 1, '2023-03-13 01:05:16', 'SALES PIRING', 1);
INSERT INTO `mst_akses` VALUES (13, 1, '2023-03-11 08:36:40', 1, 1, '2023-03-11 08:54:20', 'SALES PIRING KACA', 1);
INSERT INTO `mst_akses` VALUES (14, 1, '2023-03-11 19:35:41', 1, 1, '2023-03-15 00:55:54', 'HUMAN RESOURCE MANAGER', 4);
INSERT INTO `mst_akses` VALUES (15, 1, '2023-03-12 20:21:59', 1, NULL, NULL, 'HUMAN RESOURCE SPV', 4);
INSERT INTO `mst_akses` VALUES (16, 1, '2023-03-15 00:55:26', 1, NULL, NULL, 'TESTING TESTING TESTING', 1);

--mst_menu
INSERT INTO `mst_menu` VALUES (1, 1, '2023-03-05 06:11:12', 'localhost:8080', 1, 1, '2023-03-09 04:47:29', 'SALES ONE', '/api/menu/salesone', 1);
INSERT INTO `mst_menu` VALUES (2, 1, '2023-03-05 06:11:30', 'localhost:8080', 1, NULL, NULL, 'SALES TWO', '/api/menu/salestwo', 1);
INSERT INTO `mst_menu` VALUES (3, 1, '2023-03-05 06:12:27', 'localhost:8080', 1, NULL, NULL, 'SALES THREE', '/api/menu/salesthree', 1);
INSERT INTO `mst_menu` VALUES (4, 1, '2023-03-05 06:12:37', 'localhost:8080', 1, NULL, NULL, 'SALES FOUR', '/api/menu/salesfour', 1);
INSERT INTO `mst_menu` VALUES (5, 1, '2023-03-05 06:13:23', 'localhost:8080', 1, 1, '2023-03-14 15:53:45', 'HR ONE', '/api/menu/hrone', 2);
INSERT INTO `mst_menu` VALUES (6, 1, '2023-03-05 06:13:31', 'localhost:8080', 1, 1, '2023-03-14 15:51:57', 'HR TWO', '/api/menu/hrtwo', 2);
INSERT INTO `mst_menu` VALUES (7, 1, '2023-03-05 06:14:09', 'localhost:8080', 1, NULL, NULL, 'HR THREE', '/api/menu/hrthree', 2);
INSERT INTO `mst_menu` VALUES (8, 1, '2023-03-05 06:14:16', 'localhost:8080', 1, NULL, NULL, 'HR FOUR', '/api/menu/hrfour', 2);
INSERT INTO `mst_menu` VALUES (9, 1, '2023-03-05 06:14:56', 'localhost:8080', 1, NULL, NULL, 'FINANCE ONE', '/api/menu/financeone', 5);
INSERT INTO `mst_menu` VALUES (11, 1, '2023-03-05 06:15:07', 'localhost:8080', 1, NULL, NULL, 'FINANCE TWO', '/api/menu/financetwo', 5);
INSERT INTO `mst_menu` VALUES (12, 1, '2023-03-05 06:15:41', 'localhost:8080', 1, NULL, NULL, 'FINANCE THREE', '/api/menu/financethree', 5);
INSERT INTO `mst_menu` VALUES (13, 1, '2023-03-05 06:16:07', 'localhost:8080', 1, NULL, NULL, 'FINANCE FOUR', '/api/menu/financefour', 5);
INSERT INTO `mst_menu` VALUES (14, 1, '2023-03-05 06:17:07', 'localhost:8080', 1, NULL, NULL, 'STUDENTS', '/api/school/v1/students', 4);
INSERT INTO `mst_menu` VALUES (15, 1, '2023-03-05 06:17:37', 'localhost:8080', 1, NULL, NULL, 'EMPLOYEE', '/api/', 4);
INSERT INTO `mst_menu` VALUES (17, 1, '2023-03-05 06:18:12', 'localhost:8080', 1, NULL, NULL, 'AKSES', '/api/usrmgmnt/v1/akses/default', 3);
INSERT INTO `mst_menu` VALUES (18, 1, '2023-03-05 06:18:55', 'localhost:8080', 1, 1, '2023-03-14 19:15:58', 'USER', '/api/authz/v1/userman/default', 3);
INSERT INTO `mst_menu` VALUES (19, 1, '2023-03-05 06:19:29', 'localhost:8080', 1, 1, '2023-03-13 01:15:49', 'DIVISI', '/api/usrmgmnt/v1/divisi/default', 3);
INSERT INTO `mst_menu` VALUES (20, 1, '2023-03-05 07:05:10', 'localhost:8080', 1, 1, '2023-03-13 01:18:52', 'ABSEN', '/api/absen/default', NULL);
INSERT INTO `mst_menu` VALUES (21, 1, '2023-03-07 17:53:04', 'localhost:8080', 1, NULL, NULL, 'MENU', '/api/usrmgmnt/v1/menu/default', 3);
INSERT INTO `mst_menu` VALUES (60, 1, '2023-03-09 21:33:17', 'localhost:8080', 1, NULL, NULL, 'DEMO', '/api/usrmgmnt/v1/demo/default', 3);
INSERT INTO `mst_menu` VALUES (61, 1, '2023-03-11 22:12:22', 'localhost:8080', 1, 1, '2023-03-13 19:00:23', 'PESANAN', '/api/usrmgmnt/v1/pesanan/default', 4);
INSERT INTO `mst_menu` VALUES (62, 1, '2023-03-12 03:15:44', 'localhost:8080', 1, NULL, NULL, 'MENU HEADER', '/api/usrmgmnt/v1/menuheader/default', 3);
INSERT INTO `mst_menu` VALUES (63, 1, '2023-03-13 19:03:40', 'localhost:8080', 1, NULL, NULL, 'Menu Spesial', '/api/api/api/bakar', 1);
INSERT INTO `mst_menu` VALUES (64, 11, '2023-03-14 16:53:37', 'localhost:8080', 1, NULL, NULL, 'MENU TEST - 1', '/api/api/api/bakar', 6);
INSERT INTO `mst_menu` VALUES (65, 1, '2023-03-15 00:57:08', 'localhost:8080', 1, 1, '2023-03-15 00:57:32', 'asdadddddd', '/api/api/apiiiiiiiiiiiiiiiiiiii', 1);
INSERT INTO `mst_menu` VALUES (66, 1, '2023-03-18 19:13:33', 'localhost:8080', 1, 1, '2023-03-18 19:43:21', 'Menu Spesial 12', '/api/api/api', 1);

--mst_user
INSERT INTO `mst_user` VALUES (1, 1, '2023-03-05 20:16:04', 'poll.chihuy@gmail.com', 1, '2023-03-20 21:34:25', 1, '2023-03-12 20:24:55', 'Paul C.M', '12121313441', '$2a$11$Rd9t1krU5G8DmWrd6MjWpejZhqdpKcHNYFeq3YuhuT0LxjwCqamDG', 0, '1992-12-25', '$2a$11$waC2wiqeOA2zX9Eqhzahv.M3SPubsaFsssZCYWgdkHKYxOCrWOmBK', 0, 'juaracoding1', 6);
INSERT INTO `mst_user` VALUES (2, 1, '2023-03-05 21:37:04', 'raja.mobile.office@gmail.com', 1, '2023-03-05 21:50:55', NULL, NULL, 'AAAAAqweqwe123', '12121313440', '$2a$11$auAFpzHgacsd2c4vINWLlOobQRDd0WVrGPhJPetzgj93LBEHf4R82', 0, '1992-12-25', '$2a$11$9agNZojnokeKX4sKPafOw.tCK.xX7Nk6iiSgbKxDR0qScif1pxQQ.', 0, 'AAAAAasdadqweqeqweq', 2);
INSERT INTO `mst_user` VALUES (3, 1, '2023-03-07 20:17:51', '21312323123213123@yahoo.com', 1, '2023-03-07 20:20:24', NULL, NULL, 'Paul C.MI', '12313123123', '$2a$11$sHUMkL9vZmfxbhklZq0kBuu6OfUZpwtbfe3qNSXJtChuJHnSIt8Be', 0, '1919-12-25', '$2a$11$MZnZGrOW2UAv1DDs57lPoOs8KNzD7rqDNa00ZSy06OslwlX7vqWFG', 0, 'paulpaulpaulpaul', 1);
INSERT INTO `mst_user` VALUES (9, 1, '2023-03-14 14:31:41', 'fitriyani07071@gmail.com', 1, '2023-03-14 14:34:25', 9, '2023-03-14 14:34:19', 'fitriyani', '+6281324171', '$2a$11$T7wTmByz3OQDVDM36SX2h.9itSaWZ/h9pbCC1b8Cp3gTmE45xkWqq', 0, '1999-12-12', '$2a$11$sTOtScUih/.q1IEdAtujq.soJvkusGlagdb5a3bFjHkckvR0ej.Fa', 0, 'fitriyani07071', 1);
INSERT INTO `mst_user` VALUES (11, 1, '2023-03-14 16:47:05', 'koswara.dev92@gmail.com', 1, '2023-03-14 16:58:28', 11, '2023-03-14 16:49:52', 'Koswaraaaaaaa', '+62813241718', '$2a$11$cZkcfFRcz6fJn.Jdn1wpD.EQxFZGL8uO.NFD8EfzlA6G6jeBfJvrW', 0, '1992-12-12', '$2a$11$dptK.onHPvSGJQHzi3rHyOdPaEuw76.7./lATWcJECvyqZKW.BAZu', 0, 'Koswara123', 6);
INSERT INTO `mst_user` VALUES (26, 1, '2023-03-15 01:35:17', 'jefrisaputra989@gmail.com', 1, '2023-03-15 01:36:37', 26, '2023-03-15 01:35:31', 'Jefri Saputra', '+628132417134', '$2a$11$Au1VuOZ25jwFyL3SupCk0.AIstqIUfKjecee.R/75D5ANEgRao4b.', 0, '1992-12-12', '$2a$11$3N2wtV36h6bSoXnQF6mr/uY8QP/rvAlNyZF2k4V62Z5CQ8VQeKbcO', 0, 'jefrisaputra989', 1);
INSERT INTO `mst_user` VALUES (28, 1, '2023-03-15 21:06:38', 'mauizatul92@gmail.com', 1, '2023-03-15 21:08:17', 28, '2023-03-15 21:08:06', 'mauizatul Fadhil', '+62813241712', '$2a$11$Nrx3XU6TnhtL2W2VA1VFb./h0kcZfXf/QkX/jHOfoNgkt77uGnD1W', 0, '1992-12-12', '835659', 0, 'mauizatul92', 6);
INSERT INTO `mst_user` VALUES (32, 1, '2023-03-17 05:11:33', 'yanwarsolah@gmail.com', 0, NULL, NULL, NULL, 'yanwarsolah', '+62813241719', '$2a$11$m3EQkS4Frj7Ms7jhEqJIre3I3lCGd92neDIyxZEGYh9KHpbBHNRx2', 0, '1992-12-12', '148151', 0, 'yanwarsolah123', 1);

INSERT INTO `map_akses_menu` VALUES (1, 1);
INSERT INTO `map_akses_menu` VALUES (1, 2);
INSERT INTO `map_akses_menu` VALUES (1, 14);
INSERT INTO `map_akses_menu` VALUES (1, 15);
INSERT INTO `map_akses_menu` VALUES (2, 1);
INSERT INTO `map_akses_menu` VALUES (2, 2);
INSERT INTO `map_akses_menu` VALUES (3, 1);
INSERT INTO `map_akses_menu` VALUES (3, 2);
INSERT INTO `map_akses_menu` VALUES (3, 3);
INSERT INTO `map_akses_menu` VALUES (4, 9);
INSERT INTO `map_akses_menu` VALUES (4, 11);
INSERT INTO `map_akses_menu` VALUES (4, 20);
INSERT INTO `map_akses_menu` VALUES (5, 9);
INSERT INTO `map_akses_menu` VALUES (5, 11);
INSERT INTO `map_akses_menu` VALUES (5, 12);
INSERT INTO `map_akses_menu` VALUES (5, 14);
INSERT INTO `map_akses_menu` VALUES (5, 15);
INSERT INTO `map_akses_menu` VALUES (5, 20);
INSERT INTO `map_akses_menu` VALUES (6, 1);
INSERT INTO `map_akses_menu` VALUES (6, 2);
INSERT INTO `map_akses_menu` VALUES (6, 3);
INSERT INTO `map_akses_menu` VALUES (6, 4);
INSERT INTO `map_akses_menu` VALUES (6, 5);
INSERT INTO `map_akses_menu` VALUES (6, 6);
INSERT INTO `map_akses_menu` VALUES (6, 7);
INSERT INTO `map_akses_menu` VALUES (6, 8);
INSERT INTO `map_akses_menu` VALUES (6, 9);
INSERT INTO `map_akses_menu` VALUES (6, 11);
INSERT INTO `map_akses_menu` VALUES (6, 12);
INSERT INTO `map_akses_menu` VALUES (6, 13);
INSERT INTO `map_akses_menu` VALUES (6, 14);
INSERT INTO `map_akses_menu` VALUES (6, 15);
INSERT INTO `map_akses_menu` VALUES (6, 17);
INSERT INTO `map_akses_menu` VALUES (6, 18);
INSERT INTO `map_akses_menu` VALUES (6, 19);
INSERT INTO `map_akses_menu` VALUES (6, 20);
INSERT INTO `map_akses_menu` VALUES (6, 21);
INSERT INTO `map_akses_menu` VALUES (6, 60);
INSERT INTO `map_akses_menu` VALUES (6, 61);
INSERT INTO `map_akses_menu` VALUES (6, 62);
INSERT INTO `map_akses_menu` VALUES (6, 63);
INSERT INTO `map_akses_menu` VALUES (6, 64);
INSERT INTO `map_akses_menu` VALUES (11, 1);
INSERT INTO `map_akses_menu` VALUES (11, 2);
INSERT INTO `map_akses_menu` VALUES (11, 3);
INSERT INTO `map_akses_menu` VALUES (11, 4);
INSERT INTO `map_akses_menu` VALUES (11, 5);
INSERT INTO `map_akses_menu` VALUES (11, 6);
INSERT INTO `map_akses_menu` VALUES (11, 7);
INSERT INTO `map_akses_menu` VALUES (11, 8);
INSERT INTO `map_akses_menu` VALUES (11, 9);
INSERT INTO `map_akses_menu` VALUES (11, 11);
INSERT INTO `map_akses_menu` VALUES (11, 12);
INSERT INTO `map_akses_menu` VALUES (11, 13);
INSERT INTO `map_akses_menu` VALUES (11, 14);
INSERT INTO `map_akses_menu` VALUES (11, 15);
INSERT INTO `map_akses_menu` VALUES (11, 17);
INSERT INTO `map_akses_menu` VALUES (11, 18);
INSERT INTO `map_akses_menu` VALUES (11, 19);
INSERT INTO `map_akses_menu` VALUES (11, 20);
INSERT INTO `map_akses_menu` VALUES (11, 21);
INSERT INTO `map_akses_menu` VALUES (11, 60);
INSERT INTO `map_akses_menu` VALUES (11, 61);
INSERT INTO `map_akses_menu` VALUES (11, 62);
INSERT INTO `map_akses_menu` VALUES (12, 3);
INSERT INTO `map_akses_menu` VALUES (12, 4);
INSERT INTO `map_akses_menu` VALUES (13, 14);
INSERT INTO `map_akses_menu` VALUES (13, 15);
INSERT INTO `map_akses_menu` VALUES (13, 17);
INSERT INTO `map_akses_menu` VALUES (13, 60);
INSERT INTO `map_akses_menu` VALUES (14, 14);
INSERT INTO `map_akses_menu` VALUES (15, 5);
INSERT INTO `map_akses_menu` VALUES (15, 6);
INSERT INTO `map_akses_menu` VALUES (15, 7);
INSERT INTO `map_akses_menu` VALUES (15, 8);
INSERT INTO `map_akses_menu` VALUES (15, 14);
INSERT INTO `map_akses_menu` VALUES (15, 15);
INSERT INTO `map_akses_menu` VALUES (15, 60);
INSERT INTO `map_akses_menu` VALUES (16, 1);