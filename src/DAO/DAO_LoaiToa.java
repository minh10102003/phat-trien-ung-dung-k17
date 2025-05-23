package DAO;

import entity.LoaiToa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_LoaiToa {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8;";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    /**
     * Lấy tất cả các loại toa từ bảng LoaiToa.
     * @return danh sách các loại toa.
     */
    public static List<LoaiToa> getAllLoaiToa() {
        List<LoaiToa> loaiToaList = new ArrayList<>();
        String sql = "SELECT maLoaiToa, tenLoaiToa FROM LoaiToa";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String maLoaiToa = rs.getString("maLoaiToa");
                String tenLoaiToa = rs.getString("tenLoaiToa");

                // Tạo đối tượng LoaiToa và thêm vào danh sách
                LoaiToa loaiToa = new LoaiToa(maLoaiToa, tenLoaiToa);
                loaiToaList.add(loaiToa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loaiToaList;
    }

    /**
     * Lấy thông tin loại toa theo mã loại toa.
     * @param maLoaiToa mã loại toa
     * @return đối tượng LoaiToa tương ứng.
     */
    public static LoaiToa getLoaiToaById(String maLoaiToa) {
        LoaiToa loaiToa = null;
        String sql = "SELECT maLoaiToa, tenLoaiToa FROM LoaiToa WHERE maLoaiToa = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maLoaiToa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tenLoaiToa = rs.getString("tenLoaiToa");

                    // Tạo đối tượng LoaiToa
                    loaiToa = new LoaiToa(maLoaiToa, tenLoaiToa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return loaiToa;
    }
}
