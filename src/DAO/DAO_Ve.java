package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
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
        private final String maHK;
        private final String maTau;
        private final Time gioDi;
        private final Date ngayDi;
        private final String loaiVeName;
        private Date ngayLap;
        private String tenKhach;
        private double tongTien;

        public HoaDonInfo(String maVe, Date ngayLap, String tenKhach, double tongTien, String maTau, String loaiVeName) {
            this.maVe = maVe;
            this.ngayLap = ngayLap;
            this.tenKhach = tenKhach;
            this.tongTien = tongTien;
            this.maHK = "";
            this.maTau = maTau;
            this.gioDi = null;
            this.ngayDi = new Date();
            this.loaiVeName = loaiVeName;
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

        public String getMaTau() { return maTau; }
        public String getGioDi() { return new SimpleDateFormat("HH:mm").format(gioDi); }
        public String getNgayDiString() { return new SimpleDateFormat("dd/MM/yyyy").format(ngayDi); }
        public String getLoaiVeName() { return loaiVeName; }
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
                list.add(new HoaDonInfo(maVe, ngayLap, tenKH, tongTien, tenKH, tenKH));
            }
        }
        return list;
    }

    /** Đóng gói kết quả đặt vé, giữ nguyên constructor cũ */
    public static class BookingResult {
        // --- các trường cũ ---
        private final String maVe;
        private final String maHK;
        private final String maTau;
        private final String gioDi;
        private final String ngayDi;
        private final String loaiVeName;

        // --- các trường về hành trình ---
        private final String gaDi;
        private final String gaDen;
        private final String loaiChuyen; // "Một chiều" / "Khứ hồi"
        private final String ngayVe;      // "" nếu một chiều

        // --- các trường khách hàng mới ---
        private final String tenKhach;
        private final String cccd;
        private final String sdt;

        /** 
         * Constructor cũ, để giữ tương thích với HoaDonThanhToan, UI.Tau, v.v.
         * Trường khách hàng và thông tin hành trình được khởi thành rỗng.
         */
        public BookingResult(
                String maVe, String maHK,
                String maTau, String gioDi,
                String ngayDi, String loaiVeName
        ) {
            this.maVe        = maVe;
            this.maHK        = maHK;
            this.maTau       = maTau;
            this.gioDi       = gioDi;
            this.ngayDi      = ngayDi;
            this.loaiVeName  = loaiVeName;

            this.gaDi        = "";
            this.gaDen       = "";
            this.loaiChuyen  = "";
            this.ngayVe      = "";

            this.tenKhach    = "";
            this.cccd        = "";
            this.sdt         = "";
        }

        /**
         * Constructor đầy đủ, gọi từ bookTicket(...) để UI.VeTau lấy hết
         */
        public BookingResult(
                String maVe, String maHK,
                String maTau, String gioDi,
                String ngayDi, String loaiVeName,
                String gaDi, String gaDen,
                String loaiChuyen, String ngayVe,
                String tenKhach, String cccd, String sdt
        ) {
            this.maVe        = maVe;
            this.maHK        = maHK;
            this.maTau       = maTau;
            this.gioDi       = gioDi;
            this.ngayDi      = ngayDi;
            this.loaiVeName  = loaiVeName;

            this.gaDi        = gaDi;
            this.gaDen       = gaDen;
            this.loaiChuyen  = loaiChuyen;
            this.ngayVe      = ngayVe;

            this.tenKhach    = tenKhach;
            this.cccd        = cccd;
            this.sdt         = sdt;
        }

        // --- getters cũ ---
        public String getMaVe()       { return maVe; }
        public String getMaHK()       { return maHK; }
        public String getMaTau()      { return maTau; }
        public String getGioDi()      { return gioDi; }
        public String getNgayDi()     { return ngayDi; }
        public String getLoaiVeName(){ return loaiVeName; }

        // --- getters hành trình ---
        public String getGaDi()       { return gaDi; }
        public String getGaDen()      { return gaDen; }
        public String getLoaiChuyen() { return loaiChuyen; }
        public String getNgayVe()     { return ngayVe; }

        // --- getters khách hàng mới ---
        public String getTenKhach()   { return tenKhach; }
        public String getCccd()       { return cccd; }
        public String getSdt()        { return sdt; }
    }
    
    


    /**
     * Phương thức chính, hỗ trợ một chiều/khứ hồi + thông tin khách.
     */
    public static BookingResult bookTicket(
            String tenKhach, String cccd, String sdt,
            String maTau, String gioDi, Date ngayDi,
            boolean isRoundTrip, Date ngayVeParam,
            String loaiVe, String soGhe,
            String maLoaiVe, int soLuongVe
    ) throws SQLException {
        String maHK = DAO_HanhKhach.insertOrGetId(tenKhach, cccd, sdt);
        String maVe = "VE" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // chuẩn bị các chuỗi hiển thị
        String loaiChuyen = isRoundTrip ? "Khứ hồi" : "Một chiều";
        String ngayDiStr  = new SimpleDateFormat("dd/MM/yyyy").format(ngayDi);
        String ngayVeStr  = isRoundTrip
                            ? new SimpleDateFormat("dd/MM/yyyy").format(ngayVeParam)
                            : "";

        try (Connection conn = ConnectDB.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String maGhe = lookupMaGhe(conn, maTau, soGhe);

                String sql = """
                    INSERT INTO Ve(
                      maVe, maTau, gioDi, ngayDi,
                      loaiVe, cho, maLoaiVe, soLuongVe, maHK
                    ) VALUES(?,?,?,?,?,?,?,?,?)
                """;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, maVe);
                    ps.setString(2, maTau);
                    ps.setString(3, gioDi);
                    ps.setDate(4, new java.sql.Date(ngayDi.getTime()));
                    ps.setString(5, loaiVe);
                    ps.setString(6, maGhe);
                    ps.setString(7, maLoaiVe);
                    ps.setInt   (8, soLuongVe);
                    ps.setString(9, maHK);
                    ps.executeUpdate();
                }

                String loaiVeName;
                String sql2 = "SELECT tenLoaiVe FROM LoaiVe WHERE maLoaiVe = ?";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, maLoaiVe);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        loaiVeName = rs2.next()
                             ? rs2.getString("tenLoaiVe")
                             : loaiVe;
                    }
                }

                // lấy ga đi / ga đến
                String gaDi = "", gaDen = "";
                String sql3 =
                    "SELECT g1.tenGa AS gaDi, g2.tenGa AS gaDen\n" +
                    "  FROM ChuyenTau ct\n" +
                    "  JOIN Ga g1 ON ct.maGaDi  = g1.maGa\n" +
                    "  JOIN Ga g2 ON ct.maGaDen = g2.maGa\n" +
                    " WHERE ct.maTau = ?";
                try (PreparedStatement ps3 = conn.prepareStatement(sql3)) {
                    ps3.setString(1, maTau);
                    try (ResultSet rs3 = ps3.executeQuery()) {
                        if (rs3.next()) {
                            gaDi  = rs3.getString("gaDi");
                            gaDen = rs3.getString("gaDen");
                        }
                    }
                }

                conn.commit();

                return new BookingResult(
                	    maVe, maHK,
                	    maTau, gioDi,
                	    ngayDiStr, loaiVeName,
                	    gaDi, gaDen,
                	    loaiChuyen, ngayVeStr,
                	    tenKhach, cccd, sdt
                	);

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    /**
     * Overload tương thích chữ ký cũ (10 params) — bọc vào bản mới.
     */
    public static BookingResult bookTicket(
    	    String tenKhach, String cccd, String sdt,
    	    String maTau, String gioDi, Date ngayDi,
    	    String loaiVe, String soGhe,
    	    String maLoaiVe, int soLuongVe
    	) throws SQLException {
    	    return bookTicket(
    	        tenKhach, cccd, sdt,
    	        maTau, gioDi, ngayDi,
    	        false,       // một chiều
    	        null,        // không có ngày về
    	        loaiVe, soGhe,
    	        maLoaiVe, soLuongVe
    	    );
    	}


    /** Tra cứu mã ghế trong bảng Ghe */
    private static String lookupMaGhe(Connection conn, String maTau, String soGhe) throws SQLException {
        String sql = """
                    SELECT g.maGhe
                      FROM Ghe g
                      JOIN Toa t ON g.maToa = t.maToa
                     WHERE t.maTau = ?
                       AND g.cho   = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            ps.setString(2, soGhe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("maGhe");
                } else {
                    throw new SQLException("Ghế không tồn tại: tàu=" + maTau + ", cho=" + soGhe);
                }
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
        try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tenGa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("maGa");
            }
        }
        return null;
    }
    
    /**
     * Trả về chuỗi ngày đi theo định dạng dd/MM/yyyy
     */
    public static String getNgayDi(String maTau) {
        String sql = "SELECT TOP 1 ngayDi FROM ChuyenTau WHERE maTau = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getDate("ngayDi") != null) {
                    return new SimpleDateFormat("dd/MM/yyyy")
                               .format(rs.getDate("ngayDi"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Trả về mảng [giờ đi, giờ đến] ở dạng HH:mm
     */
    public static String[] getGioDiGioDen(String maTau) {
        String sql = "SELECT TOP 1 gioKhoiHanh, gioDen FROM ChuyenTau WHERE maTau = ?";
        String[] r = { "??:??", "??:??" };
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                    if (rs.getTime("gioKhoiHanh") != null) {
                        r[0] = fmt.format(rs.getTime("gioKhoiHanh"));
                    }
                    if (rs.getTime("gioDen") != null) {
                        r[1] = fmt.format(rs.getTime("gioDen"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }

}
