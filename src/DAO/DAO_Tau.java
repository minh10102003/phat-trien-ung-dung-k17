package DAO;

import entity.TauEntity;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;

public class DAO_Tau {
    public static List<TauEntity> layDanhSachChuyenTau(String maGaDi, String maGaDen, java.util.Date ngayDi) {
        List<TauEntity> danhSach = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectDB.getConnection();

            String sql = "SELECT ct.MaTau, t.TenTau, t.LoaiTau, ct.SoChoCon, ct.gioKhoiHanh, ct.gioDen " +
                         "FROM ChuyenTau ct " +
                         "JOIN Tau t ON ct.MaTau = t.MaTau " +
                         "WHERE ct.maGaDi = ? AND ct.maGaDen = ? AND CAST(ct.ngayDi AS DATE) = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maGaDi);
            stmt.setString(2, maGaDen);
            stmt.setDate(3, new java.sql.Date(ngayDi.getTime()));

            rs = stmt.executeQuery();

            // Định dạng giờ theo HH:mm
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                String maTau = rs.getString("MaTau");
                String tenTau = rs.getString("TenTau");
                String loaiTau = rs.getString("LoaiTau");
                int soChoCon = rs.getInt("SoChoCon");

                Timestamp gioKhoiHanh = rs.getTimestamp("gioKhoiHanh");
                Timestamp gioDen = rs.getTimestamp("gioDen");

                String thoiGianDi = gioKhoiHanh != null ? timeFormat.format(gioKhoiHanh) : "??:??";
                String thoiGianDen = gioDen != null ? timeFormat.format(gioDen) : "??:??";

                TauEntity tau = new TauEntity(maTau, tenTau, loaiTau, thoiGianDi, thoiGianDen, soChoCon);

                // 🔧 Gán thêm timestamp gốc để phần UI xử lý chuẩn
                tau.setGioKhoiHanh(gioKhoiHanh);
                tau.setGioDen(gioDen);

                danhSach.add(tau);
            }
        

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }

        return danhSach;
    }

    public static List<TauEntity> layTatCaChuyenTau() {
        // TODO Auto-generated method stub
        return null;
    }
} 
