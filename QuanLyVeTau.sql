-- 1. Bảng TaiKhoan
CREATE TABLE TaiKhoan (
    tenTK      VARCHAR(50)   PRIMARY KEY,
    matKhau    VARCHAR(100)  NOT NULL,
    loaiTK     VARCHAR(20)   NOT NULL
);

-- Thêm tài khoản admin
INSERT INTO TaiKhoan (tenTK, matKhau, loaiTK)
VALUES ('admin', '123456', 'admin');

-- Thêm tài khoản nhân viên
INSERT INTO TaiKhoan (tenTK, matKhau, loaiTK)
VALUES ('nhanvien01', 'abc123', 'nhanvien');

SELECT *FROM TaiKhoan

-- 2. Bảng NhanVien
CREATE TABLE NhanVien (
    maNV         VARCHAR(10)   PRIMARY KEY,
    tenNV        NVARCHAR(100) NOT NULL,
    namSinh      DATE          NULL,
    phai         BIT           NULL,
    CCCD         VARCHAR(20)   NULL,
    chucVu       NVARCHAR(50)  NULL,
    tinhTrangNV  BIT           NULL,
    tenTK        VARCHAR(50)   NULL,
    CONSTRAINT FK_NhanVien_TaiKhoan
      FOREIGN KEY (tenTK) REFERENCES TaiKhoan(tenTK)
);

SELECT *FROM NhanVien
-- 3. Bảng HanhKhach
CREATE TABLE HanhKhach (
    maHK   VARCHAR(10)   PRIMARY KEY,
    ten    NVARCHAR(100) NOT NULL,
    CCCD   VARCHAR(20)   NULL,
    sdt    VARCHAR(15)   NULL
);

-- 4. Bảng Tau
CREATE TABLE Tau (
    maTau    VARCHAR(10)   PRIMARY KEY,
    tenTau   NVARCHAR(100) NOT NULL,
    loaiTau  NVARCHAR(50)  NULL
);

-- 5. Bảng LoaiToa
CREATE TABLE LoaiToa (
    maLoaiToa   VARCHAR(10)   PRIMARY KEY,
    tenLoaiToa  NVARCHAR(50)  NOT NULL,
    moTa        NVARCHAR(200) NULL
);

-- 6. Bảng Toa
CREATE TABLE Toa (
    maToa       VARCHAR(10) NOT NULL PRIMARY KEY,
    tenToa      NVARCHAR(50) NOT NULL,
    maTau       VARCHAR(10) NOT NULL,
    maLoaiToa   VARCHAR(10) NOT NULL,
    CONSTRAINT FK_Toa_Tau
      FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT FK_Toa_LoaiToa
      FOREIGN KEY (maLoaiToa) REFERENCES LoaiToa(maLoaiToa)
);

-- 7. Bảng LoaiGhe
CREATE TABLE LoaiGhe (
    maLoaiGhe   VARCHAR(10)   PRIMARY KEY,
    tenLoaiGhe  NVARCHAR(50)  NOT NULL,
    moTa        NVARCHAR(200) NULL
);

-- 8. Bảng Ghe
CREATE TABLE Ghe (
    maGhe     VARCHAR(10) NOT NULL PRIMARY KEY,
    cho       VARCHAR(10) NOT NULL,
    loaiGhe   VARCHAR(10) NOT NULL,
    maToa     VARCHAR(10) NOT NULL,
    CONSTRAINT FK_Ghe_LoaiGhe
      FOREIGN KEY (loaiGhe) REFERENCES LoaiGhe(maLoaiGhe),
    CONSTRAINT FK_Ghe_Toa
      FOREIGN KEY (maToa) REFERENCES Toa(maToa)
);

-- 9. Bảng LoaiVe
CREATE TABLE LoaiVe (
    maLoaiVe    VARCHAR(10)   PRIMARY KEY,
    tenLoaiVe   NVARCHAR(50)  NOT NULL,
    giaTien     DECIMAL(18,2) NOT NULL
);

-- 10. Bảng Ve
CREATE TABLE Ve (
    maVe       VARCHAR(10)   PRIMARY KEY,
    maTau      VARCHAR(10)   NOT NULL,
    gioDi      DATETIME      NOT NULL,
    ngayDi     DATE          NOT NULL,
    loaiVe     NVARCHAR(50)  NULL,
    cho        VARCHAR(10)   NOT NULL,
    maLoaiVe   VARCHAR(10)   NOT NULL,
    CONSTRAINT FK_Ve_Tau
      FOREIGN KEY (maTau) REFERENCES Tau(maTau),
    CONSTRAINT FK_Ve_LoaiVe
      FOREIGN KEY (maLoaiVe) REFERENCES LoaiVe(maLoaiVe),
    CONSTRAINT FK_Ve_Ghe
      FOREIGN KEY (cho) REFERENCES Ghe(maGhe)
);

-- 11. Bảng HoaDon
CREATE TABLE HoaDon (
    maHD       VARCHAR(10)   PRIMARY KEY,
    ngayLapHD  DATETIME      NOT NULL,
    maNV       VARCHAR(10)   NOT NULL,
    maHK       VARCHAR(10)   NOT NULL,
    CONSTRAINT FK_HoaDon_NhanVien
      FOREIGN KEY (maNV) REFERENCES NhanVien(maNV),
    CONSTRAINT FK_HoaDon_HanhKhach
      FOREIGN KEY (maHK) REFERENCES HanhKhach(maHK)
);

-- 12. Bảng CT_HoaDon
CREATE TABLE CT_HoaDon (
    maHD     VARCHAR(10) NOT NULL,
    maVe     VARCHAR(10) NOT NULL,
    soLuong  INT         NOT NULL,
    PRIMARY KEY (maHD, maVe),
    CONSTRAINT FK_CTHoaDon_HoaDon
      FOREIGN KEY (maHD) REFERENCES HoaDon(maHD),
    CONSTRAINT FK_CTHoaDon_Ve
      FOREIGN KEY (maVe) REFERENCES Ve(maVe)
);

-- 13. Bảng ChuyenTau
CREATE TABLE ChuyenTau (
    maChuyenTau    VARCHAR(10)   PRIMARY KEY,
    tenChuyenTau   NVARCHAR(100) NOT NULL,
    gaDi           NVARCHAR(100) NULL,
    gaDen          NVARCHAR(100) NULL,
    ngayDi         DATE          NULL,
    gioKhoiHanh    TIME          NULL,
    maTau          VARCHAR(10)   NOT NULL,
    CONSTRAINT FK_ChuyenTau_Tau
      FOREIGN KEY (maTau) REFERENCES Tau(maTau)
);

-- Truy vấn thêm, xóa, cập nhật, tìm kiếm
-- Nhân viên
-- Thêm
SELECT * FROM TaiKhoan WHERE tenTK = 'nguyenvana';
IF NOT EXISTS (SELECT 1 FROM TaiKhoan WHERE tenTK = 'nguyenvana')
BEGIN
    INSERT INTO TaiKhoan (tenTK, matKhau, loaiTK)
    VALUES ('nguyenvana', 'matkhau123', 'admin'); -- Thay đổi thông tin theo yêu cầu
END
INSERT INTO NhanVien (maNV, tenNV, namSinh, phai, CCCD, chucVu, tinhTrangNV, tenTK)
VALUES 
('NV001', N'Nguyễn Văn A', '1990-05-10', 1, '123456789012', N'Quản lý', 1, 'nguyenvana');

-- Các query để tránh trường hợp lỗi khóa ngoại
ALTER TABLE NhanVien NOCHECK CONSTRAINT FK_NhanVien_TaiKhoan;
ALTER TABLE NhanVien CHECK CONSTRAINT FK_NhanVien_TaiKhoan;
ALTER TABLE NhanVien DROP CONSTRAINT FK_NhanVien_TaiKhoan;

-- Xem bảng
EXEC sp_columns NhanVien;
SELECT 
    c.name AS ColumnName, 
    t.name AS DataType, 
    c.max_length AS MaxLength,
    c.is_nullable AS IsNullable,
    c.is_identity AS IsIdentity
FROM sys.columns c
JOIN sys.types t ON c.user_type_id = t.user_type_id
WHERE c.object_id = OBJECT_ID('NhanVien');

-- Xóa
DELETE FROM NhanVien WHERE maNV = 'NV001';
DELETE FROM NhanVien;

-- Cập nhật
UPDATE NhanVien 
SET chucVu = N'Trưởng phòng' 
WHERE maNV = 'NV001';
UPDATE NhanVien 
SET tinhTrangNV = 0 
WHERE maNV = 'NV001';
SELECT * FROM NhanVien;
SELECT * FROM NhanVien WHERE maNV = 'NV001';
SELECT * FROM NhanVien WHERE tenNV LIKE N'%Nguyễn%';
SELECT * FROM NhanVien WHERE tinhTrangNV = 1;
SELECT * FROM NhanVien WHERE chucVu = N'Nhân viên';
SELECT * FROM NhanVien WHERE tenTK IS NOT NULL;



