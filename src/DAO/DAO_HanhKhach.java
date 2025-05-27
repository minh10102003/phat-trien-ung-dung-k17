package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import connectDB.ConnectDB;
import entity.HanhKhach;

public class DAO_HanhKhach {
	/**
     * Nếu đã tồn tại khách với cùng CCCD thì trả về maHK
     * và cập nhật lại tenHK, sdt. Ngược lại sinh maHK mới và chèn.
     */
	public static String insertOrGetId(String tenKhach, String cccd, String sdt) throws SQLException {
	    String existingMaHK = null;
	    try (Connection conn = ConnectDB.getConnection()) {
	        // 1) Tìm maHK theo CCCD
	        String sqlSelect = "SELECT maHK FROM HanhKhach WHERE cccd = ?";
	        try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
	            ps.setString(1, cccd);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    existingMaHK = rs.getString("maHK");
	                }
	            }
	        }

	        if (existingMaHK != null) {
	            // 2) Cập nhật lại tên và sđt nếu đã tồn tại
	            String sqlUpdate = "UPDATE HanhKhach SET ten = ?, sdt = ? WHERE cccd = ?";
	            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
	                ps.setString(1, tenKhach);  // đúng cột ten
	                ps.setString(2, sdt);
	                ps.setString(3, cccd);
	                ps.executeUpdate();
	            }
	            return existingMaHK;
	        } else {
	            // 3) Chèn khách mới
	            String newMaHK = "HK" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
	            String sqlInsert = """
	                INSERT INTO HanhKhach (
	                    maHK,
	                    ten,      -- đúng cột ten
	                    cccd,
	                    sdt
	                ) VALUES (?, ?, ?, ?)
	            """;
	            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
	                ps.setString(1, newMaHK);
	                ps.setString(2, tenKhach);  // đúng cột ten
	                ps.setString(3, cccd);
	                ps.setString(4, sdt);
	                ps.executeUpdate();
	            }
	            return newMaHK;
	        }
	    }
	}
	
	// (2) Lấy danh sách tất cả khách hàng
    public static List<HanhKhach> getAll() throws SQLException {
        List<HanhKhach> list = new ArrayList<>();
        String sql = "SELECT maHK, ten, cccd, sdt FROM HanhKhach";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new HanhKhach(
                    rs.getString("maHK"),
                    rs.getString("ten"),
                    rs.getString("cccd"),
                    rs.getString("sdt")
                ));
            }
        }
        return list;
    }

    // (3) Lấy khách theo ID
    public static HanhKhach getById(String maHK) throws SQLException {
        String sql = "SELECT maHK, ten, cccd, sdt FROM HanhKhach WHERE maHK = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HanhKhach(
                        rs.getString("maHK"),
                        rs.getString("ten"),
                        rs.getString("cccd"),
                        rs.getString("sdt")
                    );
                }
            }
        }
        return null;
    }

    public static void delete(String maHK) throws SQLException {
        String sqlDeleteVe    = "DELETE FROM Ve WHERE maHK = ?";
        String sqlDeleteHoaDon = "DELETE FROM HoaDon WHERE maHK = ?";
        String sqlDeleteHK    = "DELETE FROM HanhKhach WHERE maHK = ?";
        try (Connection conn = ConnectDB.getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement psVe = conn.prepareStatement(sqlDeleteVe);
                PreparedStatement psHd = conn.prepareStatement(sqlDeleteHoaDon);
                PreparedStatement psHk = conn.prepareStatement(sqlDeleteHK);
            ) {
                // 1) Xóa vé liên quan
                psVe.setString(1, maHK);
                psVe.executeUpdate();

                // 2) Xóa hóa đơn liên quan
                psHd.setString(1, maHK);
                psHd.executeUpdate();

                // 3) Xóa hành khách
                psHk.setString(1, maHK);
                int affected = psHk.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Không tìm thấy hành khách để xóa: " + maHK);
                }

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }


}
