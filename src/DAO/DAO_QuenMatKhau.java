package DAO;

import java.sql.*;

public class DAO_QuenMatKhau {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASS = "sapassword";

    public static boolean capNhatMatKhau(String tenTK, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE tenTK = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matKhauMoi);
            stmt.setString(2, tenTK);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
