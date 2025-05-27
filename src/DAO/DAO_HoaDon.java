package DAO;

import connectDB.ConnectDB;
import entity.LoaiVe;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class DAO_HoaDon {
	/**
     * Tạo một hóa đơn mới, trả về mã hóa đơn sinh tự động.
     * Chỉ chèn vào các cột: maHD, ngayLapHD, maNV, maHK
     */
	public static String createInvoice(String maNV, String maHK) throws SQLException {
	    String maHD = "HD" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
	    String sql = "INSERT INTO HoaDon(maHD, ngayLapHD, maNV, maHK) VALUES(?, GETDATE(), ?, ?)";
	    try (Connection conn = ConnectDB.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, maHD);
	        ps.setString(2, maNV);
	        ps.setString(3, maHK);
	        ps.executeUpdate();
	        return maHD;
	    }
	}


	/**
	 * Lấy toàn bộ danh sách hóa đơn để hiển thị bảng.
	 */
	public static ResultSet getAllInvoices() throws SQLException {
		Connection conn = ConnectDB.getConnection();
		String sql = "SELECT maHD, ngayLapHD, maNV, maHK FROM HoaDon";
		PreparedStatement ps = conn.prepareStatement(sql);
		return ps.executeQuery();
	}

	public static double getPriceByLoai(String maLoaiVe) throws SQLException {
		String sql = "SELECT giaTien FROM LoaiVe WHERE maLoaiVe = ?";
		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, maLoaiVe);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("giaTien");
				}
			}
		}
		return 0;
	}

	// trong DAO.DAO_HoaDon
	public static List<LoaiVe> getAllLoaiVeStatic() {
		return DAO.DAO_LoaiVe.getAllLoaiVe();
	}

}
