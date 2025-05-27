package DAO;

import entity.LoaiVe;
import connectDB.ConnectDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_LoaiVe {
    public static List<LoaiVe> getAllLoaiVe() {
        List<LoaiVe> danhSach = new ArrayList<>();
        String sql = "SELECT maLoaiVe, tenLoaiVe, giaTien FROM LoaiVe";

        try (Connection conn = ConnectDB.getConnection(); // <-- Sửa tại đây
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ma = rs.getString("maLoaiVe");
                String ten = rs.getString("tenLoaiVe");
                double gia = rs.getDouble("giaTien");

                LoaiVe lv = new LoaiVe(ma, ten, gia);
                danhSach.add(lv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }


}