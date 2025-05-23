package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import connectDB.ConnectDB;

public class DAO_Ve {
    // Class để lưu thông tin Ga
    public static class Ga {
        private String maGa;
        private String tenGa;

        public Ga(String maGa, String tenGa) {
            this.maGa = maGa;
            this.tenGa = tenGa;
        }

        public String getMaGa() {
            return maGa;
        }

        public String getTenGa() {
            return tenGa;
        }

        @Override
        public String toString() {
            return tenGa;
        }
    }

    // Class chứa thông tin cho hóa đơn
    public static class HoaDonInfo {
        private String maVe;
        private Date ngayLap;
        private String tenKhach;
        private double tongTien;

        public HoaDonInfo(String maVe, Date ngayLap, String tenKhach, double tongTien) {
            this.maVe = maVe;
            this.ngayLap = ngayLap;
            this.tenKhach = tenKhach;
            this.tongTien = tongTien;
        }

        public String getMaVe() {
            return maVe;
        }

        public Date getNgayLap() {
            return ngayLap;
        }

        public String getTenKhach() {
            return tenKhach;
        }

        public double getTongTien() {
            return tongTien;
        }
    }

    /**
     * Trả về danh sách hóa đơn từ DB.
     */
    public static List<HoaDonInfo> getAllHoaDon() throws SQLException {
        List<HoaDonInfo> list = new ArrayList<>();
        String sql = """
            SELECT v.maVe,
                   v.ngayDi    AS ngayLap,
                   h.ten       AS tenKhach,
                   v.soLuongVe * lv.giaTien AS tongTien
              FROM Ve v
              JOIN HanhKhach h ON v.maHK = h.maHK
              JOIN LoaiVe    lv ON v.maLoaiVe = lv.maLoaiVe
             ORDER BY v.ngayDi DESC
        """;
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maVe = rs.getString("maVe");
                Date ngayLap = rs.getDate("ngayLap");
                String tenKH = rs.getString("tenKhach");
                double tongTien = rs.getDouble("tongTien");
                list.add(new HoaDonInfo(maVe, ngayLap, tenKH, tongTien));
            }
        }
        return list;
    }

    /**
     * Gộp logic: chèn hoặc lấy maHK (DAO_HanhKhach), sau đó chèn vé mới.
     * Bảng Ve có 9 cột: maVe, maTau, gioDi, ngayDi, loaiVe, cho, maLoaiVe, soLuongVe, maHK
     * Tham số soGhe có thể là số ghế ("11") hoặc định dạng DB ("2,5").
     */
    public static String bookTicket(
            String tenKhach,
            String cccd,
            String sdt,
            String maTau,
            String gioDi,
            Date ngayDi,
            String loaiVe,
            String soGhe,
            String maLoaiVe,
            int soLuongVe
    ) throws SQLException {
        String maHK = DAO_HanhKhach.insertOrGetId(tenKhach, cccd, sdt);
        String maVe = "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        try (Connection conn = ConnectDB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String maGhe = lookupMaGhe(conn, maTau, soGhe);
                String sql = """
                    INSERT INTO Ve(
                        maVe, maTau, gioDi, ngayDi,
                        loaiVe, cho, maLoaiVe, soLuongVe, maHK
                    ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, maVe);
                    ps.setString(2, maTau);
                    ps.setString(3, gioDi);
                    ps.setDate(4, new java.sql.Date(ngayDi.getTime()));
                    ps.setString(5, loaiVe);
                    ps.setString(6, maGhe);
                    ps.setString(7, maLoaiVe);
                    ps.setInt(8, soLuongVe);
                    ps.setString(9, maHK);
                    ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
        return maVe;
    }

    /**
     * Tra cứu maGhe từ bàn ghế dựa trên maTau (mã tàu) và số ghế.
     * Nếu soGhe là số ("11"), tự động chuyển thành "row,col" tương ứng.
     */
    private static String lookupMaGhe(Connection conn, String maTau, String soGhe) throws SQLException {
        // Chuyển số ghế thành định dạng row,col nếu cần
        String choValue = soGhe;
        if (!choValue.contains(",")) {
            try {
                int num = Integer.parseInt(choValue);
                int row = (num - 1) / 6 + 1;
                int col = (num - 1) % 6 + 1;
                choValue = row + "," + col;
            } catch (NumberFormatException ignore) {
            }
        }
        String sql = """
            SELECT g.maGhe
              FROM Ghe g
              JOIN Toa t ON g.maToa = t.maToa
             WHERE t.maTau = ? AND g.cho = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            ps.setString(2, choValue);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maGhe");
                }
                throw new SQLException("Ghế không tồn tại: tàu=" + maTau + ", cho=" + choValue);
            }
        }
    }

    /** Lấy danh sách Ga từ DB */
    public static List<Ga> getAllGa() throws SQLException {
        List<Ga> listGa = new ArrayList<>();
        String sql = "SELECT maGa, tenGa FROM Ga";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listGa.add(new Ga(rs.getString("maGa"), rs.getString("tenGa")));
            }
        }
        return listGa;
    }

    /** Lấy mã Ga dựa trên tên Ga */
    public static String getMaGaByTen(String tenGa) throws SQLException {
        String sql = "SELECT maGa FROM Ga WHERE tenGa = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenGa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("maGa");
            }
        }
        return null;
    }
}
