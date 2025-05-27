package DAO;

import connectDB.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAO_ThongKe {

    /**
     * Thống kê số vé bán ra theo ngày/tuần/tháng.
     * periodType: "Ngày", "Tuần", hoặc "Tháng"
     */
    public static List<Object[]> countTickets(String periodType, Date from, Date to) throws SQLException {
        String periodExpr;
        switch (periodType) {
            case "Tuần":
                // YEAR-WEEK
                periodExpr = "CONCAT(DATEPART(YEAR, ngayDi), '-W', DATEPART(WEEK, ngayDi))";
                break;
            case "Tháng":
                // YYYY-MM
                periodExpr = "CONVERT(varchar(7), ngayDi, 23)";
                break;
            default:
                // Mặc định "Ngày"
                periodExpr = "CONVERT(varchar(10), ngayDi, 23)";
        }
        String sql = """
            SELECT
                %s AS period,
                SUM(soLuongVe) AS ticketCount
            FROM Ve
            WHERE ngayDi BETWEEN ? AND ?
            GROUP BY %s
            ORDER BY %s
        """.formatted(periodExpr, periodExpr, periodExpr);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("period"),
                        rs.getInt("ticketCount")
                    });
                }
                return list;
            }
        }
    }

    /**
     * Thống kê doanh thu theo ngày/tuần/tháng.
     */
    public static List<Object[]> totalRevenue(String periodType, Date from, Date to) throws SQLException {
        String periodExpr;
        switch (periodType) {
            case "Tuần":
                periodExpr = "CONCAT(DATEPART(YEAR, v.ngayDi), '-W', DATEPART(WEEK, v.ngayDi))";
                break;
            case "Tháng":
                periodExpr = "CONVERT(varchar(7), v.ngayDi, 23)";
                break;
            default:
                periodExpr = "CONVERT(varchar(10), v.ngayDi, 23)";
        }
        String sql = """
            SELECT
                %s AS period,
                SUM(v.soLuongVe * lv.giaTien) AS revenue
            FROM Ve v
            JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe
            WHERE v.ngayDi BETWEEN ? AND ?
            GROUP BY %s
            ORDER BY %s
        """.formatted(periodExpr, periodExpr, periodExpr);

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("period"),
                        rs.getDouble("revenue")
                    });
                }
                return list;
            }
        }
    }

    /**
     * Thống kê số vé theo tiêu chí: "Ngày","Tháng" hoặc "Tuyến".
     */
    public static List<Object[]> countTicketsByCriteria(String criteria, Date from, Date to) {
        List<Object[]> list = new ArrayList<>();
        String sql;
        if ("Tuyến".equals(criteria)) {
            // Lấy mã tàu và tên tàu từ bảng Tau
            sql = """
                SELECT v.maTau AS criteria, t.tenTau AS label, SUM(v.soLuongVe) AS ticketCount
                  FROM Ve v
                  JOIN Tau t ON v.maTau = t.maTau
                 WHERE v.ngayDi BETWEEN ? AND ?
                 GROUP BY v.maTau, t.tenTau
                 ORDER BY ticketCount DESC
            """;
        } else {
            // Dùng lại countTickets cho ngày/tuần/tháng
            try {
                return countTickets(criteria, from, to);
            } catch (SQLException ex) {
                ex.printStackTrace();
                return list;
            }
        }

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("criteria") + " - " + rs.getString("label"),
                        rs.getInt("ticketCount")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê vé trả lại. 
     * Giả sử bạn có bảng VeTraLai(maTra, maVe, ngayTra, lyDo, soLuong).
     * Nếu chưa có, bạn cần tạo bảng này để lưu log trả vé.
     */
    public static List<Object[]> refundedTickets(Date from, Date to, String reason) throws SQLException {
        String sql = """
            SELECT
                CONVERT(varchar(10), ngayTra, 23) AS date,
                SUM(soLuong) AS refundedCount,
                lyDo
            FROM VeTraLai
            WHERE ngayTra BETWEEN ? AND ?
              AND (? = '' OR lyDo LIKE ?)
            GROUP BY CONVERT(varchar(10), ngayTra, 23), lyDo
            ORDER BY date
        """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            ps.setString(3, reason);
            ps.setString(4, "%" + reason + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("date"),
                        rs.getInt("refundedCount"),
                        rs.getString("lyDo")
                    });
                }
                return list;
            }
        }
    }

    /**
     * Thống kê thông tin cá nhân cho một nhân viên (toàn bộ).
     * Trả về { tổngSốHDDaLập, tổngDoanhThu }.
     */
    public static Object[] personalSummary(String maNV) throws SQLException {
        String sqlCount = "SELECT COUNT(*) AS cnt FROM HoaDon WHERE maNV = ?";
        String sqlSum   = """
            SELECT SUM(v.soLuongVe * lv.giaTien) AS total
              FROM HoaDon h
              JOIN Ve v      ON h.maHK = v.maHK
              JOIN LoaiVe lv ON v.maLoaiVe = lv.maLoaiVe
             WHERE h.maNV = ?
        """;

        try (Connection conn = ConnectDB.getConnection()) {
            int cnt;
            try (PreparedStatement ps = conn.prepareStatement(sqlCount)) {
                ps.setString(1, maNV);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    cnt = rs.getInt("cnt");
                }
            }

            double total = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlSum)) {
                ps.setString(1, maNV);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) total = rs.getDouble("total");
                }
            }
            return new Object[]{ cnt, total };
        }
    }
}
