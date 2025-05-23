package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Ghe;

public class DAO_Ghe {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    public static List<Integer> layDanhSachGheTheoMaToa(String maToa) {
        List<Integer> danhSach = new ArrayList<>();
        String sql = "SELECT cho FROM Ghe WHERE maToa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maToa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String raw = rs.getString("cho");          // đọc chuỗi gốc
                    if (raw != null) {
                        // loại bỏ dấu phẩy
                        String cleaned = raw.replace(",", "");
                        try {
                            int soCho = Integer.parseInt(cleaned);
                            danhSach.add(soCho);
                        } catch (NumberFormatException ex) {
                            // nếu vẫn lỗi, bạn có thể log hoặc gán mặc định
                            System.err.println("Không parse được số từ: " + raw);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSach;
    }


	public static List<Ghe> getGheByMaToa(String maToa) {
		// TODO Auto-generated method stub
		return null;
	}





}