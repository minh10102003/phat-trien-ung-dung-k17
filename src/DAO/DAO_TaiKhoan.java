package DAO;

import java.sql.*;

public class DAO_TaiKhoan {
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private final String dbUser = "sa";
    private final String dbPass = "sapassword";

    public boolean kiemTraDangNhap(String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT * FROM TaiKhoan WHERE tenTK = ? AND matKhau = ? AND loaiTK = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dangKyTaiKhoan(String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "INSERT INTO TaiKhoan (tenTK, matKhau, loaiTK) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}