package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.Entity_NhanVien;

public class DAO_NhanVien {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Entity_NhanVien> getAllNhanVien() {
        List<Entity_NhanVien> list = new ArrayList<>();
        String query = "SELECT * FROM NhanVien";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Entity_NhanVien(
                    rs.getString("maNV"),
                    rs.getString("tenNV"),
                    rs.getString("namSinh"),
                    rs.getBoolean("phai"),
                    rs.getString("CCCD"),
                    rs.getString("chucVu"),
                    rs.getBoolean("tinhTrangNV"),
                    rs.getString("tenTK")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean themNhanVien(Entity_NhanVien nv) {
        String sql = "INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setDate(3, Date.valueOf(nv.getNamSinh()));
            ps.setBoolean(4, nv.isPhai());
            ps.setString(5, nv.getCCCD());
            ps.setString(6, nv.getChucVu());
            ps.setBoolean(7, nv.isTinhTrangNV());
            ps.setString(8, nv.getTenTK());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean capNhatNhanVien(Entity_NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNV=?, namSinh=?, phai=?, CCCD=?, chucVu=?, tinhTrangNV=?, tenTK=? WHERE maNV=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setDate(2, Date.valueOf(nv.getNamSinh()));
            ps.setBoolean(3, nv.isPhai());
            ps.setString(4, nv.getCCCD());
            ps.setString(5, nv.getChucVu());
            ps.setBoolean(6, nv.isTinhTrangNV());
            ps.setString(7, nv.getTenTK());
            ps.setString(8, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
