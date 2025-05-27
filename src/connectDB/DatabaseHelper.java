package connectDB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import UI.Tau;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "sapassword";

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getGioDiVaGioDenByMaTau(String maTau) {
        String sql = "SELECT TOP 1 gioKhoiHanh, gioDen FROM ChuyenTau WHERE maTau = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maTau);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Time gioDi = rs.getTime("gioKhoiHanh");
                Time gioDen = rs.getTime("gioDen");

                String gioDiStr = (gioDi != null) ? format.format(gioDi) : "??:??";
                String gioDenStr = (gioDen != null) ? format.format(gioDen) : "??:??";
                return new String[]{gioDiStr, gioDenStr};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{"??:??", "??:??"};
    }

    /**
     * Lấy danh sách mã toa (String) theo mã tàu.
     */
    public static List<String> getCabinsByTrainId(String maTau) {
        List<String> danhSachToa = new ArrayList<>();
        String sql = "SELECT maToa FROM Toa WHERE maTau = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maTau.trim());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                danhSachToa.add(rs.getString("maToa"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn danh sách toa:");
            e.printStackTrace();
        }
        return danhSachToa;
    }

    /**
     * Lấy danh sách ghế của toa theo mã toa (String).
     * Đọc maGhe dưới dạng String, soGhe dưới dạng int.
     */
    public static List<Tau.GheInfo> getGheByToa(String maToa) {
        List<Tau.GheInfo> danhSach = new ArrayList<>();
        String sql = """
            SELECT g.maGhe, g.cho, lg.tenLoaiGhe
            FROM Ghe g
            JOIN LoaiGhe lg ON g.loaiGhe = lg.maLoaiGhe
            WHERE g.maToa = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maToa);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // maGhe dưới dạng String
                String maGhe = rs.getString("maGhe");

                // cho (số ghế) dưới dạng String, có thể chứa dấu phẩy
                String rawSoGhe = rs.getString("cho");
                int soGhe = 0;
                if (rawSoGhe != null) {
                    // loại bỏ dấu phẩy trước khi parse
                    String cleaned = rawSoGhe.replace(",", "");
                    try {
                        soGhe = Integer.parseInt(cleaned);
                    } catch (NumberFormatException ex) {
                        System.err.println("Không parse được số ghế từ: " + rawSoGhe);
                    }
                }

                String loaiGhe = rs.getString("tenLoaiGhe");
                danhSach.add(new Tau.GheInfo(maGhe, soGhe, loaiGhe));
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn ghế của toa " + maToa);
            e.printStackTrace();
        }

        return danhSach;
    }
    
    /**
     * Trả về tên loại toa (mô tả) theo mã toa.
     * Giả sử trong CSDL bạn có bảng Toa với cột loaiToa,
     * và bảng LoaiToa (maLoaiToa, tenLoaiToa).
     */
    public static String getCabinDescription(String maToa) {
        String sql = """
            SELECT lg.tenLoaiToa
            FROM Toa t
            JOIN LoaiToa lg
              ON t.maLoaiToa = lg.maLoaiToa
            WHERE t.maToa = ?
        """;
        try ( Connection conn = getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql) ) {

            stmt.setString(1, maToa.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("tenLoaiToa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";  // fallback nếu không tìm thấy
    }




    public static String getNgayDiByMaTau(String maTau) {
        // TODO: implement nếu cần
        return null;
    }
}
