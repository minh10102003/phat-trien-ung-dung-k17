package DAO;

import java.sql.*;
import java.util.UUID;
import connectDB.ConnectDB;

public class DAO_HanhKhach {
    /**
     * Nếu đã có khách với CCCD thì trả về maHK cũ,
     * ngược lại tạo maHK mới, chèn vào và trả về.
     */
    public static String insertOrGetId(String ten, String cccd, String sdt) throws SQLException {
        String sqlSelect = "SELECT maHK FROM HanhKhach WHERE CCCD = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
            ps.setString(1, cccd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maHK");
                }
            }
        }

        // Chưa có → tạo mới
        String maHK = "HK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String sqlInsert = "INSERT INTO HanhKhach (maHK, ten, CCCD, sdt) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
            ps.setString(1, maHK);
            ps.setString(2, ten);
            ps.setString(3, cccd);
            ps.setString(4, sdt);
            ps.executeUpdate();
        }
        return maHK;
    }
}
