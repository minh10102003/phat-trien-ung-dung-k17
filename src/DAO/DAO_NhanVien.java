package DAO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import DAO.DAO_TaiKhoan;
import entity.Entity_NhanVien;

public class DAO_NhanVien {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Lấy danh sách nhân viên, bao gồm cả ảnh (photo)
     */
    public static List<Entity_NhanVien> getAllNhanVien() {
        List<Entity_NhanVien> list = new ArrayList<>();
        String query = "SELECT * FROM NhanVien";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                byte[] photo = rs.getBytes("photo");
                list.add(new Entity_NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("namSinh"),
                    rs.getBoolean("phai"),
                    rs.getString("CCCD"),
                    rs.getString("chucVu"),
                    rs.getBoolean("tinhTrangNV"),
                    rs.getString("tenTK"),
                    photo
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Sinh tự động mã NV: NV001, NV002, ...
    private static String generateMaNV() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maNV, 3, LEN(maNV)-2) AS INT)) FROM NhanVien";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int max = 0;
            if (rs.next() && rs.getObject(1) != null) {
                max = rs.getInt(1);
            }
            return String.format("NV%03d", max + 1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi sinh maNV", e);
        }
    }

    // Sinh tên TK: admin01/admin02 hoặc nhanvien01/nhanvien02
    private static String generateUsername(String chucVu) {
        String prefix = chucVu.equalsIgnoreCase("Quản lý") ? "admin" : "nhanvien";
        // Tìm số thứ tự lớn nhất trong TaiKhoan
        String sql = "SELECT MAX(CAST(SUBSTRING(tenTK, ?, LEN(tenTK)-?+1) AS INT)) "
                   + "FROM TaiKhoan WHERE tenTK LIKE ?";
        int baseLen = prefix.length();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // SUBSTRING bắt đầu từ vị trí baseLen+1
            ps.setInt(1, baseLen+1);
            ps.setInt(2, baseLen+1);
            ps.setString(3, prefix + "%");
            ResultSet rs = ps.executeQuery();
            int max = 0;
            if (rs.next() && rs.getObject(1) != null) {
                max = rs.getInt(1);
            }
            return String.format("%s%02d", prefix, max + 1);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi sinh tenTK", e);
        }
    }

    /**
     * Insert nhân viên, bao gồm ảnh
     */
    private static boolean insertNhanVien(Entity_NhanVien nv) {
        String sql = """
            INSERT INTO NhanVien 
              (maNV,tenNV,namSinh,phai,CCCD,chucVu,tinhTrangNV,tenTK,photo)
            VALUES(?,?,?,?,?,?,?,?,?)
        """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());

            // ----- sửa tại đây -----
            java.sql.Date sqlDate;
            try {
                java.util.Date utilDate =
                    new SimpleDateFormat("dd/MM/yyyy").parse(nv.getNamSinh());
                sqlDate = new java.sql.Date(utilDate.getTime());
            } catch (Exception ex) {
                // nếu parse lỗi thì để ngày hiện tại hoặc NULL tuỳ bạn
                sqlDate = new java.sql.Date(System.currentTimeMillis());
            }
            ps.setDate(3, sqlDate);
            // -----------------------

            ps.setBoolean(4, nv.isPhai());
            ps.setString(5, nv.getCCCD());
            ps.setString(6, nv.getChucVu());
            ps.setBoolean(7, nv.isTinhTrangNV());
            ps.setString(8, nv.getTenTK());

            if (nv.getPhoto() != null) {
                ps.setBytes(9, nv.getPhoto());
            } else {
                ps.setNull(9, Types.VARBINARY);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm nhân viên mới: tự sinh maNV, tenTK;
     * gọi DAO_TaiKhoan.dangKyTaiKhoan trước để tạo account;
     * rồi insert vào NhanVien.
     */
    public static boolean themNhanVien(Entity_NhanVien nv) {
        String maNV  = generateMaNV();
        String tenTK = generateUsername(nv.getChucVu());
        nv.setMaNV(maNV);
        nv.setTenTK(tenTK);

        String defaultPass = "123456";
        String role = nv.getChucVu().equalsIgnoreCase("Quản lý") ? "admin" : "nhanvien";
        DAO_TaiKhoan daoTK = new DAO_TaiKhoan();
        if (!daoTK.dangKyTaiKhoan(tenTK, defaultPass, role)) return false;

        return insertNhanVien(nv);
    }

    /**
     * Cập nhật nhân viên, bao gồm ảnh
     */
    public static boolean capNhatNhanVien(Entity_NhanVien nv) {
        String sql = "UPDATE NhanVien SET " +
                     "tenNV=?, namSinh=?, phai=?, CCCD=?, chucVu=?, tinhTrangNV=?, tenTK=?, photo=? " +
                     "WHERE maNV=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setDate(2, Date.valueOf(nv.getNamSinh()));
            ps.setBoolean(3, nv.isPhai());
            ps.setString(4, nv.getCCCD());
            ps.setString(5, nv.getChucVu());
            ps.setBoolean(6, nv.isTinhTrangNV());
            ps.setString(7, nv.getTenTK());
            ps.setBytes(8, nv.getPhoto()); // ảnh
            ps.setString(9, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa nhân viên và tài khoản
     */
    /**
     * Xóa nhân viên + xóa luôn tài khoản trong 1 transaction.
     */
    public static boolean xoaNhanVien(String maNV) {
        String sqlGetTK = "SELECT tenTK FROM NhanVien WHERE maNV = ?";
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            // 1) Lấy tên tài khoản của NV
            String tenTK;
            try (PreparedStatement ps1 = conn.prepareStatement(sqlGetTK)) {
                ps1.setString(1, maNV);
                try (ResultSet rs = ps1.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false; // không tìm thấy NV
                    }
                    tenTK = rs.getString("tenTK");
                }
            }

            // 2) Xóa nhân viên
            try (PreparedStatement ps2 = conn.prepareStatement(
                        "DELETE FROM NhanVien WHERE maNV = ?")) {
                ps2.setString(1, maNV);
                ps2.executeUpdate();
            }

            // 3) Xóa tài khoản
            boolean okTK = DAO_TaiKhoan.xoaTaiKhoan(conn, tenTK);
            if (!okTK) throw new SQLException("Không xóa được TaiKhoan");

            // 4) Commit cả 2
            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignore) {}
            }
        }
    }

    /** lấy mã NV từ username */
    public static String getMaNVByUsername(String tenTK) {
        String sql = "SELECT maNV FROM NhanVien WHERE tenTK = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("maNV");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Trả về Entity_NhanVien theo tenTK, bao gồm cả ảnh
     */
    public static Entity_NhanVien getByUsername(String tenTK) {
        String sql = "SELECT * FROM NhanVien WHERE tenTK = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    byte[] photo = rs.getBytes("photo");
                    return new Entity_NhanVien(
                        rs.getString("maNV"),
                        rs.getString("tenNV"),
                        rs.getString("namSinh"),
                        rs.getBoolean("phai"),
                        rs.getString("CCCD"),
                        rs.getString("chucVu"),
                        rs.getBoolean("tinhTrangNV"),
                        rs.getString("tenTK"),
                        photo
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
