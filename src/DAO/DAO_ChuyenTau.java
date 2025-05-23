package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO_ChuyenTau {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    /**
     * Tìm danh sách tên chuyến tàu phù hợp với ga đi, ga đến và ngày đi/ngày về.
     * @param gaDi mã ga đi (maGa trong bảng Ga)
     * @param gaDen mã ga đến (maGa trong bảng Ga)
     * @param ngayDi ngày đi (java.util.Date)
     * @param ngayVe ngày về (java.util.Date)
     * @return danh sách tên chuyến tàu khớp
     */
    public static List<String> timChuyenTau(String gaDi, String gaDen, java.util.Date ngayDi, java.util.Date ngayVe) {
        List<String> chuyenTauList = new ArrayList<>();

        if (gaDi == null || gaDen == null || ngayDi == null || ngayVe == null) {
            return chuyenTauList;
        }

        gaDi = gaDi.trim();
        gaDen = gaDen.trim();

        String sql = "SELECT tenChuyenTau FROM ChuyenTau " +
                     "WHERE maGaDi = ? AND maGaDen = ? " +
                     "AND ngayDi = ? AND ngayVe = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.sql.Date sqlNgayDi = new java.sql.Date(ngayDi.getTime());
            java.sql.Date sqlNgayVe = new java.sql.Date(ngayVe.getTime());

            stmt.setString(1, gaDi);
            stmt.setString(2, gaDen);
            stmt.setDate(3, sqlNgayDi);
            stmt.setDate(4, sqlNgayVe);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    chuyenTauList.add(rs.getString("tenChuyenTau"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chuyenTauList;
    }
}
