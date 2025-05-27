package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DAO_Ga {
    private static final String URL      = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER     = "sa";
    private static final String PASSWORD = "sapassword";

    /** Trả về danh sách tất cả tên ga */
    public static List<String> getAllGa() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT tenGa FROM Ga";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(rs.getString("tenGa").trim());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Trả về map tên ga → mã ga, dùng để build combobox
     */
    public static Map<String, String> getGaMap() {
        Map<String, String> map = new LinkedHashMap<>();
        String sql = "SELECT maGa, tenGa FROM Ga";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maGa  = rs.getString("maGa").trim();
                String tenGa = rs.getString("tenGa").trim();
                map.put(tenGa, maGa);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * Lấy tên ga dựa trên mã ga.
     * @param maGa mã ga (ví dụ "G001")
     * @return tên ga tương ứng, hoặc chính maGa nếu không tìm thấy
     */
    public static String getTenGaByMa(String maGa) {
        String sql = "SELECT tenGa FROM Ga WHERE maGa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maGa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tenGa").trim();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // nếu không tìm thấy, trả về mã ga gốc
        return maGa;
    }
}
