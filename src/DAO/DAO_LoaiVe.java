package DAO;

import entity.LoaiVe;
import connectDB.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO xử lý bảng LoaiVe
 */
public class DAO_LoaiVe {
    /**
     * Lấy danh sách tất cả loại vé
     * @return List<LoaiVe>
     * @throws SQLException
     */
    public static List<LoaiVe> getAllLoaiVe() throws SQLException {
        List<LoaiVe> list = new ArrayList<>();
        String sql = "SELECT maLoaiVe, tenLoaiVe, giaTien FROM LoaiVe";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String ma   = rs.getString("maLoaiVe");
                String ten  = rs.getNString("tenLoaiVe");
                double gia  = rs.getDouble("giaTien");
                LoaiVe lv   = new LoaiVe(ma, ten, gia);
                list.add(lv);
            }
        }
        return list;
    }
}
