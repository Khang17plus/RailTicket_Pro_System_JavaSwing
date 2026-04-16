-- ========================================
-- 1. KHỞI TẠO CƠ SỞ DỮ LIỆU
-- ========================================
USE master;
GO
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'RailWayTicketDB')
    DROP DATABASE RailWayTicketDB;
GO
CREATE DATABASE RailWayTicketDB;
GO
USE RailWayTicketDB;
GO

-- ========================================
-- 2. PHÂN HỆ KHÁCH HÀNG & NHÂN SỰ
-- ========================================
CREATE TABLE KhachHang (
    maKH VARCHAR(20) PRIMARY KEY,
    tenKH NVARCHAR(100) NOT NULL,
    cccd VARCHAR(20) UNIQUE NOT NULL,
    soDienThoai VARCHAR(15) UNIQUE,
    email VARCHAR(100),
    ngayDangKy DATETIME DEFAULT GETDATE()
);

CREATE TABLE NhanVien (
    maNV VARCHAR(20) PRIMARY KEY,
    tenNV NVARCHAR(100) NOT NULL,
    chucVu NVARCHAR(50),
    soDienThoai VARCHAR(15),
    trangThai BIT DEFAULT 1
);

CREATE TABLE TaiKhoan (
    maNV VARCHAR(20) PRIMARY KEY,
    matKhau VARCHAR(255) NOT NULL,
    vaiTro VARCHAR(20) CHECK (vaiTro IN ('ADMIN', 'STAFF')),
    CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (maNV) REFERENCES NhanVien(maNV) ON DELETE CASCADE
);

-- ========================================
-- 3. PHÂN HỆ VẬN HÀNH (CƠ SỞ VẬT CHẤT)
-- ========================================
CREATE TABLE GaTau (
    maGa VARCHAR(20) PRIMARY KEY,
    tenGa NVARCHAR(100) NOT NULL,
    diaChi NVARCHAR(255)
);

CREATE TABLE Tau (
    maTau VARCHAR(20) PRIMARY KEY,
    tenTau NVARCHAR(100) NOT NULL,
    loaiTau NVARCHAR(50),
    trangThai NVARCHAR(50) DEFAULT N'Sẵn sàng'
);

CREATE TABLE ToaTau (
    maToa VARCHAR(20) PRIMARY KEY,
    maTau VARCHAR(20) NOT NULL,
    tenToa NVARCHAR(50),
    loaiToa NVARCHAR(50),
    sucChua INT CHECK (sucChua > 0),
    CONSTRAINT FK_ToaTau_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau) ON DELETE CASCADE
);

CREATE TABLE Ghe (
    maGhe VARCHAR(20) PRIMARY KEY,
    maToa VARCHAR(20) NOT NULL,
    soGhe INT NOT NULL,
    loaiGhe NVARCHAR(50),
    CONSTRAINT FK_Ghe_ToaTau FOREIGN KEY (maToa) REFERENCES ToaTau(maToa) ON DELETE CASCADE,
    CONSTRAINT UQ_Toa_SoGhe UNIQUE (maToa, soGhe)
);

-- ========================================
-- 4. PHÂN HỆ CHÍNH SÁCH
-- ========================================
CREATE TABLE Thue (
    maThue VARCHAR(20) PRIMARY KEY,
    tenThue NVARCHAR(100) NOT NULL,
    phanTram DECIMAL(5,2) CHECK (phanTram >= 0),
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    trangThai BIT DEFAULT 1
);

CREATE TABLE KhuyenMai (
    maKM VARCHAR(20) PRIMARY KEY,
    tenKM NVARCHAR(100) NOT NULL,
    loaiKM VARCHAR(20) CHECK (loaiKM IN ('PERCENT', 'FIXED')),
    giaTri DECIMAL(10,2) CHECK (giaTri >= 0),
    ngayBatDau DATETIME,
    ngayKetThuc DATETIME,
    trangThai BIT DEFAULT 1
);

-- ========================================
-- 5. PHÂN HỆ LỊCH TRÌNH & KINH DOANH
-- ========================================
CREATE TABLE ChuyenTau (
    maChuyen VARCHAR(20) PRIMARY KEY,
    maTau VARCHAR(20) NOT NULL,
    maGaDi VARCHAR(20) NOT NULL,
    maGaDen VARCHAR(20) NOT NULL,
    thoiGianDi DATETIME NOT NULL,
    thoiGianDen DATETIME NOT NULL,
    trangThai NVARCHAR(50) DEFAULT N'Sắp khởi hành',
    CONSTRAINT FK_Chuyen_Tau FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT FK_Chuyen_GaDi FOREIGN KEY (maGaDi) REFERENCES GaTau(maGa),
    CONSTRAINT FK_Chuyen_GaDen FOREIGN KEY (maGaDen) REFERENCES GaTau(maGa),
    CONSTRAINT CHK_ThoiGian CHECK (thoiGianDen > thoiGianDi)
);

CREATE TABLE LichTrinhDungDo (
    maChuyen VARCHAR(20) NOT NULL,
    maGa VARCHAR(20) NOT NULL,
    thuTuDung INT NOT NULL,
    thoiGianDen DATETIME,
    thoiGianDi DATETIME,
    PRIMARY KEY (maChuyen, maGa),
    CONSTRAINT FK_LichTrinh_Chuyen FOREIGN KEY (maChuyen) REFERENCES ChuyenTau(maChuyen) ON DELETE CASCADE,
    CONSTRAINT FK_LichTrinh_Ga FOREIGN KEY (maGa) REFERENCES GaTau(maGa)
);

CREATE TABLE PhanCong (
    maPhanCong VARCHAR(20) PRIMARY KEY,
    maNV VARCHAR(20) NOT NULL,
    maChuyen VARCHAR(20) NOT NULL,
    vaiTro NVARCHAR(50),
    CONSTRAINT FK_PhanCong_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_PhanCong_Chuyen FOREIGN KEY (maChuyen) REFERENCES ChuyenTau(maChuyen)
);

CREATE TABLE VeTau (
    maVe VARCHAR(20) PRIMARY KEY,
    maChuyen VARCHAR(20) NOT NULL,
    maGhe VARCHAR(20) NOT NULL,
    giaGoc DECIMAL(12, 2) CHECK (giaGoc >= 0),
    trangThai NVARCHAR(50) DEFAULT N'Trống',
    CONSTRAINT FK_Ve_Chuyen FOREIGN KEY (maChuyen) REFERENCES ChuyenTau(maChuyen),
    CONSTRAINT FK_Ve_Ghe FOREIGN KEY (maGhe) REFERENCES Ghe(maGhe),
    CONSTRAINT UQ_Chuyen_Ghe UNIQUE (maChuyen, maGhe)
);

CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    maKH VARCHAR(20) NOT NULL,
    maNV VARCHAR(20) NOT NULL,
    ngayLap DATETIME DEFAULT GETDATE(),
    tongTienHang DECIMAL(12, 2) DEFAULT 0,
    tongThue DECIMAL(12, 2) DEFAULT 0,
    tongGiamGia DECIMAL(12, 2) DEFAULT 0,
    tongThanhToan DECIMAL(12, 2) DEFAULT 0,
    phuongThucThanhToan NVARCHAR(50),
    CONSTRAINT FK_HoaDon_KH FOREIGN KEY (maKH) REFERENCES KhachHang(maKH),
    CONSTRAINT FK_HoaDon_NV FOREIGN KEY (maNV) REFERENCES NhanVien(maNV)
);

CREATE TABLE CT_HoaDon (
    maHoaDon VARCHAR(20) NOT NULL,
    maVe VARCHAR(20) NOT NULL,
    maThue VARCHAR(20),
    maKM VARCHAR(20),
    giaBanThucTe DECIMAL(12, 2) NOT NULL,
    tienThue DECIMAL(12, 2) DEFAULT 0,
    tienGiamGia DECIMAL(12, 2) DEFAULT 0,
    thanhTien DECIMAL(12, 2) NOT NULL,
    PRIMARY KEY (maHoaDon, maVe),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon) ON DELETE CASCADE,
    CONSTRAINT FK_CTHD_Ve FOREIGN KEY (maVe) REFERENCES VeTau(maVe),
    CONSTRAINT FK_CTHD_Thue FOREIGN KEY (maThue) REFERENCES Thue(maThue), -- ĐÃ THÊM FK
    CONSTRAINT FK_CTHD_KM FOREIGN KEY (maKM) REFERENCES KhuyenMai(maKM)   -- ĐÃ THÊM FK
);
GO

-- ========================================
-- 6. DỮ LIỆU MẪU (15 DÒNG MỖI BẢNG)
-- ========================================

-- 1. Khách hàng
INSERT INTO KhachHang (maKH, tenKH, cccd, soDienThoai, email) VALUES
('KH01', N'Nguyễn Văn A', '123456781', '0901000001', 'a@gmail.com'),
('KH02', N'Trần Thị B', '123456782', '0901000002', 'b@gmail.com'),
('KH03', N'Lê Văn C', '123456783', '0901000003', 'c@gmail.com'),
('KH04', N'Phạm Thị D', '123456784', '0901000004', 'd@gmail.com'),
('KH05', N'Hoàng Văn E', '123456785', '0901000005', 'e@gmail.com'),
('KH06', N'Đỗ Thị F', '123456786', '0901000006', 'f@gmail.com'),
('KH07', N'Bùi Văn G', '123456787', '0901000007', 'g@gmail.com'),
('KH08', N'Lý Thị H', '123456788', '0901000008', 'h@gmail.com'),
('KH09', N'Chu Văn I', '123456789', '0901000009', 'i@gmail.com'),
('KH10', N'Đặng Thị J', '123456790', '0901000010', 'j@gmail.com'),
('KH11', N'Trịnh Văn K', '123456791', '0901000011', 'k@gmail.com'),
('KH12', N'Vũ Thị L', '123456792', '0901000012', 'l@gmail.com'),
('KH13', N'Mai Văn M', '123456793', '0901000013', 'm@gmail.com'),
('KH14', N'Đào Thị N', '123456794', '0901000014', 'n@gmail.com'),
('KH15', N'Hà Văn O', '123456795', '0901000015', 'o@gmail.com');

-- 2. Nhân viên
INSERT INTO NhanVien (maNV, tenNV, chucVu, soDienThoai) VALUES
('NV01', N'Nguyễn Quản Lý', N'ADMIN', '0981000001'),
('NV02', N'Lê Văn Staff', N'STAFF', '0981000002'),
('NV03', N'Trần Thị Staff', N'STAFF', '0981000003'),
('NV04', N'Phạm Lái Tàu', N'STAFF', '0981000004'),
('NV05', N'Hoàng Lái Tàu', N'STAFF', '0981000005'),
('NV06', N'Bùi Soát Vé', N'STAFF', '0981000006'),
('NV07', N'Lý Soát Vé', N'STAFF', '0981000007'),
('NV08', N'Đỗ Bảo Trì', N'STAFF', '0981000008'),
('NV09', N'Đặng Kỹ Thuật', N'STAFF', '0981000009'),
('NV10', N'Chu Tiếp Viên', N'STAFF', '0981000010'),
('NV11', N'Trịnh Tiếp Viên', N'STAFF', '0981000011'),
('NV12', N'Vũ An Ninh', N'STAFF', '0981000012'),
('NV13', N'Mai An Ninh', N'STAFF', '0981000013'),
('NV14', N'Đào Tạp Vụ', N'STAFF', '0981000014'),
('NV15', N'Hà Trưởng Ga', N'ADMIN', '0981000015');

-- 3. Tài khoản
INSERT INTO TaiKhoan (maNV, matKhau, vaiTro) VALUES
('NV01', 'pass1', 'ADMIN'), ('NV02', 'pass2', 'STAFF'), ('NV03', 'pass3', 'STAFF'),
('NV04', 'pass4', 'STAFF'), ('NV05', 'pass5', 'STAFF'), ('NV06', 'pass6', 'STAFF'),
('NV07', 'pass7', 'STAFF'), ('NV08', 'pass8', 'STAFF'), ('NV09', 'pass9', 'STAFF'),
('NV10', 'pass10', 'STAFF'), ('NV11', 'pass11', 'STAFF'), ('NV12', 'pass12', 'STAFF'),
('NV13', 'pass13', 'STAFF'), ('NV14', 'pass14', 'STAFF'), ('NV15', 'pass15', 'ADMIN');

-- 4. Ga Tàu
INSERT INTO GaTau (maGa, tenGa, diaChi) VALUES
('G01', N'Ga Hà Nội', N'Hà Nội'), ('G02', N'Ga Phủ Lý', N'Hà Nam'), ('G03', N'Ga Nam Định', N'Nam Định'),
('G04', N'Ga Ninh Bình', N'Ninh Bình'), ('G05', N'Ga Thanh Hóa', N'Thanh Hóa'), ('G06', N'Ga Vinh', N'Nghệ An'),
('G07', N'Ga Đồng Hới', N'Quảng Bình'), ('G08', N'Ga Huế', N'Thừa Thiên Huế'), ('G09', N'Ga Đà Nẵng', N'Đà Nẵng'),
('G10', N'Ga Quảng Ngãi', N'Quảng Ngãi'), ('G11', N'Ga Quy Nhơn', N'Bình Định'), ('G12', N'Ga Nha Trang', N'Khánh Hòa'),
('G13', N'Ga Tháp Chàm', N'Ninh Thuận'), ('G14', N'Ga Biên Hòa', N'Đồng Nai'), ('G15', N'Ga Sài Gòn', N'TP.HCM');

-- 5. Tàu
INSERT INTO Tau (maTau, tenTau, loaiTau) VALUES
('T01', 'SE1', 'Express'), ('T02', 'SE2', 'Express'), ('T03', 'SE3', 'Express'),
('T04', 'SE4', 'Express'), ('T05', 'SE5', 'Express'), ('T06', 'SE6', 'Express'),
('T07', 'SE7', 'Express'), ('T08', 'SE8', 'Express'), ('T09', 'TN1', 'Normal'),
('T10', 'TN2', 'Normal'), ('T11', 'SPT1', 'Tourist'), ('T12', 'SPT2', 'Tourist'),
('T13', 'SNT1', 'Quality'), ('T14', 'SNT2', 'Quality'), ('T15', 'LVP1', 'VIP');

-- 6. Toa Tàu
INSERT INTO ToaTau (maToa, maTau, tenToa, loaiToa, sucChua) VALUES
('TOA01', 'T01', 'Toa 1', N'Ngồi mềm', 60), ('TOA02', 'T01', 'Toa 2', N'Giường nằm', 30),
('TOA03', 'T02', 'Toa 1', N'Ngồi mềm', 60), ('TOA04', 'T03', 'Toa 1', N'Ngồi mềm', 60),
('TOA05', 'T04', 'Toa 1', N'Ngồi mềm', 60), ('TOA06', 'T05', 'Toa 1', N'Ngồi mềm', 60),
('TOA07', 'T06', 'Toa 1', N'Ngồi mềm', 60), ('TOA08', 'T07', 'Toa 1', N'Ngồi mềm', 60),
('TOA09', 'T08', 'Toa 1', N'Ngồi mềm', 60), ('TOA10', 'T09', 'Toa 1', N'Ngồi cứng', 80),
('TOA11', 'T10', 'Toa 1', N'Ngồi cứng', 80), ('TOA12', 'T11', 'Toa 1', N'Ngồi mềm', 60),
('TOA13', 'T12', 'Toa 1', N'Ngồi mềm', 60), ('TOA14', 'T13', 'Toa 1', N'Giường nằm', 30),
('TOA15', 'T15', 'Toa VIP', N'Phòng VIP', 10);

-- 7. Ghế
INSERT INTO Ghe (maGhe, maToa, soGhe, loaiGhe) VALUES
('G001', 'TOA01', 1, N'Cửa sổ'), ('G002', 'TOA01', 2, N'Lối đi'), ('G003', 'TOA01', 3, N'Cửa sổ'),
('G004', 'TOA02', 1, N'Tầng 1'), ('G005', 'TOA02', 2, N'Tầng 2'), ('G006', 'TOA03', 1, N'Lối đi'),
('G007', 'TOA04', 1, N'Cửa sổ'), ('G008', 'TOA05', 1, N'Lối đi'), ('G009', 'TOA06', 1, N'Cửa sổ'),
('G010', 'TOA07', 1, N'Lối đi'), ('G011', 'TOA08', 1, N'Cửa sổ'), ('G012', 'TOA09', 1, N'Lối đi'),
('G013', 'TOA10', 1, N'Cửa sổ'), ('G014', 'TOA14', 1, N'Tầng 1'), ('G015', 'TOA15', 1, N'VIP');

-- 8. Thuế
INSERT INTO Thue VALUES
('TH01', 'VAT 5%', 5, '2024-01-01', '2026-01-01', 1),
('TH02', 'VAT 8%', 8, '2024-01-01', '2026-01-01', 1),
('TH03', 'VAT 10%', 10, '2024-01-01', '2026-01-01', 1),
('TH04', 'Special 2%', 2, '2024-01-01', '2026-01-01', 1),
('TH05', 'Region 3%', 3, '2024-01-01', '2026-01-01', 1),
('TH06', 'Fast 1%', 1, '2024-01-01', '2026-01-01', 1),
('TH07', 'High 12%', 12, '2024-01-01', '2026-01-01', 1),
('TH08', 'VIP 15%', 15, '2024-01-01', '2026-01-01', 1),
('TH09', 'No Tax', 0, '2024-01-01', '2026-01-01', 1),
('TH10', 'Local 7%', 7, '2024-01-01', '2026-01-01', 1),
('TH11', 'Foreign 9%', 9, '2024-01-01', '2026-01-01', 1),
('TH12', 'Long 6%', 6, '2024-01-01', '2026-01-01', 1),
('TH13', 'Short 4%', 4, '2024-01-01', '2026-01-01', 1),
('TH14', 'Night 2%', 2, '2024-01-01', '2026-01-01', 1),
('TH15', 'Holiday 11%', 11, '2024-01-01', '2026-01-01', 1);

-- 9. Khuyến mãi
INSERT INTO KhuyenMai VALUES
('KM01', 'Promo 10%', 'PERCENT', 10, '2024-01-01', '2025-01-01', 1),
('KM02', 'Promo 20%', 'PERCENT', 20, '2024-01-01', '2025-01-01', 1),
('KM03', 'Minus 50k', 'FIXED', 50000, '2024-01-01', '2025-01-01', 1),
('KM04', 'Minus 100k', 'FIXED', 100000, '2024-01-01', '2025-01-01', 1),
('KM05', 'Summer 15%', 'PERCENT', 15, '2024-05-01', '2024-08-01', 1),
('KM06', 'Tet 25%', 'PERCENT', 25, '2024-01-01', '2024-02-01', 1),
('KM07', 'VIP 30%', 'PERCENT', 30, '2024-01-01', '2025-01-01', 1),
('KM08', 'Student 5%', 'PERCENT', 5, '2024-01-01', '2025-01-01', 1),
('KM09', 'Old 10%', 'PERCENT', 10, '2024-01-01', '2025-01-01', 1),
('KM10', 'Flash 40%', 'PERCENT', 40, '2024-06-01', '2024-06-30', 1),
('KM11', 'Minus 200k', 'FIXED', 200000, '2024-01-01', '2025-01-01', 1),
('KM12', 'Minus 300k', 'FIXED', 300000, '2024-01-01', '2025-01-01', 1),
('KM13', 'Weekend 12%', 'PERCENT', 12, '2024-01-01', '2025-01-01', 1),
('KM14', 'Combo 18%', 'PERCENT', 18, '2024-01-01', '2025-01-01', 1),
('KM15', 'Special 35%', 'PERCENT', 35, '2024-01-01', '2025-01-01', 1);

-- 10. Chuyến tàu
INSERT INTO ChuyenTau VALUES
('CH01', 'T01', 'G01', 'G15', '2024-12-01 06:00', '2024-12-02 12:00', N'Sẵn sàng'),
('CH02', 'T02', 'G15', 'G01', '2024-12-01 19:00', '2024-12-03 01:00', N'Sẵn sàng'),
('CH03', 'T03', 'G01', 'G09', '2024-12-02 08:00', '2024-12-02 22:00', N'Sẵn sàng'),
('CH04', 'T04', 'G09', 'G01', '2024-12-02 10:00', '2024-12-03 00:00', N'Sẵn sàng'),
('CH05', 'T05', 'G01', 'G06', '2024-12-03 07:00', '2024-12-03 13:00', N'Sẵn sàng'),
('CH06', 'T06', 'G06', 'G01', '2024-12-03 15:00', '2024-12-03 21:00', N'Sẵn sàng'),
('CH07', 'T07', 'G01', 'G15', '2024-12-04 10:00', '2024-12-05 16:00', N'Sẵn sàng'),
('CH08', 'T08', 'G15', 'G01', '2024-12-04 20:00', '2024-12-06 04:00', N'Sẵn sàng'),
('CH09', 'T11', 'G15', 'G12', '2024-12-05 08:00', '2024-12-05 16:00', N'Sẵn sàng'),
('CH10', 'T12', 'G12', 'G15', '2024-12-05 18:00', '2024-12-06 02:00', N'Sẵn sàng'),
('CH11', 'T13', 'G15', 'G12', '2024-12-06 21:00', '2024-12-07 05:00', N'Sẵn sàng'),
('CH12', 'T14', 'G12', 'G15', '2024-12-07 07:00', '2024-12-07 15:00', N'Sẵn sàng'),
('CH13', 'T15', 'G01', 'G15', '2024-12-08 22:00', '2024-12-10 04:00', N'Sẵn sàng'),
('CH14', 'T09', 'G01', 'G15', '2024-12-09 13:00', '2024-12-11 02:00', N'Sẵn sàng'),
('CH15', 'T10', 'G15', 'G01', '2024-12-09 14:00', '2024-12-11 03:00', N'Sẵn sàng');

-- 11. Vé tàu
INSERT INTO VeTau (maVe, maChuyen, maGhe, giaGoc) VALUES
('V01', 'CH01', 'G001', 500000), ('V02', 'CH01', 'G002', 500000), ('V03', 'CH02', 'G006', 500000),
('V04', 'CH03', 'G007', 300000), ('V05', 'CH04', 'G008', 300000), ('V06', 'CH05', 'G009', 200000),
('V07', 'CH07', 'G011', 500000), ('V08', 'CH08', 'G012', 500000), ('V09', 'CH13', 'G015', 1500000),
('V10', 'CH14', 'G013', 400000), ('V11', 'CH01', 'G003', 500000), ('V12', 'CH11', 'G014', 600000),
('V13', 'CH09', 'G010', 400000), ('V14', 'CH10', 'G004', 450000), ('V15', 'CH12', 'G005', 450000);

-- 12. Hóa đơn
INSERT INTO HoaDon (maHoaDon, maKH, maNV, phuongThucThanhToan) VALUES
('HD01', 'KH01', 'NV02', N'Tiền mặt'), ('HD02', 'KH02', 'NV02', N'Chuyển khoản'),
('HD03', 'KH03', 'NV03', N'Thẻ'), ('HD04', 'KH04', 'NV03', N'Tiền mặt'),
('HD05', 'KH05', 'NV02', N'Ví điện tử'), ('HD06', 'KH06', 'NV02', N'Tiền mặt'),
('HD07', 'KH07', 'NV03', N'Chuyển khoản'), ('HD08', 'KH08', 'NV03', N'Thẻ'),
('HD09', 'KH09', 'NV02', N'Tiền mặt'), ('HD10', 'KH10', 'NV02', N'Ví điện tử'),
('HD11', 'KH11', 'NV03', N'Tiền mặt'), ('HD12', 'KH12', 'NV03', N'Chuyển khoản'),
('HD13', 'KH13', 'NV02', N'Thẻ'), ('HD14', 'KH14', 'NV02', N'Tiền mặt'),
('HD15', 'KH15', 'NV03', N'Ví điện tử');

-- 13. Chi tiết hóa đơn (Với đầy đủ Foreign Key)
INSERT INTO CT_HoaDon VALUES
('HD01', 'V01', 'TH02', 'KM01', 500000, 40000, 50000, 490000),
('HD02', 'V02', 'TH02', NULL, 500000, 40000, 0, 540000),
('HD03', 'V03', 'TH02', 'KM08', 500000, 40000, 25000, 515000),
('HD04', 'V04', 'TH01', NULL, 300000, 15000, 0, 315000),
('HD05', 'V05', 'TH01', 'KM10', 300000, 15000, 120000, 195000),
('HD06', 'V06', 'TH01', NULL, 200000, 10000, 0, 210000),
('HD07', 'V07', 'TH02', 'KM02', 500000, 40000, 100000, 440000),
('HD08', 'V08', 'TH02', NULL, 500000, 40000, 0, 540000),
('HD09', 'V09', 'TH08', 'KM07', 1500000, 225000, 450000, 1275000),
('HD10', 'V10', 'TH01', NULL, 400000, 20000, 0, 420000),
('HD11', 'V11', 'TH02', 'KM05', 500000, 40000, 75000, 465000),
('HD12', 'V12', 'TH10', NULL, 600000, 42000, 0, 642000),
('HD13', 'V13', 'TH01', 'KM08', 400000, 20000, 20000, 400000),
('HD14', 'V14', 'TH01', 'KM03', 450000, 22500, 50000, 422500),
('HD15', 'V15', 'TH01', NULL, 450000, 22500, 0, 472500);

-- 14. Lịch trình dừng đỗ
INSERT INTO LichTrinhDungDo VALUES
('CH01', 'G01', 1, NULL, '2024-12-01 06:00'), ('CH01', 'G05', 2, '2024-12-01 10:00', '2024-12-01 10:15'),
('CH01', 'G15', 3, '2024-12-02 12:00', NULL), ('CH02', 'G15', 1, NULL, '2024-12-01 19:00'),
('CH03', 'G01', 1, NULL, '2024-12-02 08:00'), ('CH04', 'G09', 1, NULL, '2024-12-02 10:00'),
('CH05', 'G01', 1, NULL, '2024-12-03 07:00'), ('CH06', 'G06', 1, NULL, '2024-12-03 15:00'),
('CH07', 'G01', 1, NULL, '2024-12-04 10:00'), ('CH08', 'G15', 1, NULL, '2024-12-04 20:00'),
('CH09', 'G15', 1, NULL, '2024-12-05 08:00'), ('CH10', 'G12', 1, NULL, '2024-12-05 18:00'),
('CH11', 'G15', 1, NULL, '2024-12-06 21:00'), ('CH12', 'G12', 1, NULL, '2024-12-07 07:00'),
('CH13', 'G01', 1, NULL, '2024-12-08 22:00');

-- 15. PhanCong
INSERT INTO PhanCong VALUES
('PC01', 'NV04', 'CH01', N'Lái chính'), ('PC02', 'NV05', 'CH01', N'Lái phụ'),
('PC03', 'NV06', 'CH01', N'Soát vé'), ('PC04', 'NV04', 'CH02', N'Lái chính'),
('PC05', 'NV05', 'CH02', N'Lái phụ'), ('PC06', 'NV07', 'CH02', N'Soát vé'),
('PC07', 'NV04', 'CH03', N'Lái chính'), ('PC08', 'NV05', 'CH04', N'Lái chính'),
('PC09', 'NV06', 'CH05', N'Soát vé'), ('PC10', 'NV04', 'CH06', N'Lái chính'),
('PC11', 'NV05', 'CH07', N'Lái chính'), ('PC12', 'NV06', 'CH08', N'Soát vé'),
('PC13', 'NV04', 'CH09', N'Lái chính'), ('PC14', 'NV05', 'CH10', N'Lái chính'),
('PC15', 'NV07', 'CH13', N'Trưởng tàu');