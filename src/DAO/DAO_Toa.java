package DAO;

import entity.LoaiToa;
import entity.ToaEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_Toa {

    public static List<ToaEntity> layToaTheoMaTau(String maTau) {
        List<ToaEntity> danhSach = new ArrayList<>();

        String sql = "SELECT t.maToa, t.tenToa, lt.maLoaiToa, lt.tenLoaiToa, lt.moTa " +
                     "FROM Toa t " +
                     "JOIN LoaiToa lt ON t.maLoaiToa = lt.maLoaiToa " +
                     "WHERE t.maTau = ?";

        try (Connection conn = connectDB.ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maTau);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maToa = rs.getString("maToa");
                    String tenToa = rs.getString("tenToa");

                    LoaiToa loaiToa = new LoaiToa(
                        rs.getString("maLoaiToa"),
                        rs.getString("tenLoaiToa"),
                        rs.getString("moTa")
                    );

                    ToaEntity toa = new ToaEntity(maToa, tenToa, loaiToa);
                    danhSach.add(toa);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }

    public static List<ToaEntity> getAllToa() {
        List<ToaEntity> toaList = new ArrayList<>();
        String sql = "SELECT t.maToa, t.tenToa, lt.maLoaiToa, lt.tenLoaiToa, lt.moTa " +
                     "FROM Toa t " +
                     "JOIN LoaiToa lt ON t.maLoaiToa = lt.maLoaiToa";

        try (Connection conn = connectDB.ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String maToa = rs.getString("maToa");
                String tenToa = rs.getString("tenToa");

                LoaiToa loaiToa = new LoaiToa(
                    rs.getString("maLoaiToa"),
                    rs.getString("tenLoaiToa"),
                    rs.getString("moTa")
                );

                ToaEntity toaEntity = new ToaEntity(maToa, tenToa, loaiToa);
                toaList.add(toaEntity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toaList;
    }
}
