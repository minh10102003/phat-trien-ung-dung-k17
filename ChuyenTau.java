package entity;

import java.time.LocalDateTime;

public class ChuyenTau {
    private String maChuyen;
    private LocalDateTime thoiGianKhoiHanh;
    private String gaDi;
    private String gaDen;

    public ChuyenTau(String maChuyen, LocalDateTime thoiGianKhoiHanh, String gaDi, String gaDen) {
        this.maChuyen = maChuyen;
        this.thoiGianKhoiHanh = thoiGianKhoiHanh;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
    }

    public String getMaChuyen() {
        return maChuyen;
    }

    public void setMaChuyen(String maChuyen) {
        this.maChuyen = maChuyen;
    }

    public LocalDateTime getThoiGianKhoiHanh() {
        return thoiGianKhoiHanh;
    }

    public void setThoiGianKhoiHanh(LocalDateTime thoiGianKhoiHanh) {
        this.thoiGianKhoiHanh = thoiGianKhoiHanh;
    }

    public String getGaDi() {
        return gaDi;
    }

    public void setGaDi(String gaDi) {
        this.gaDi = gaDi;
    }

    public String getGaDen() {
        return gaDen;
    }

    public void setGaDen(String gaDen) {
        this.gaDen = gaDen;
    }

    public int kiemTraSoVeConLai() {
        // Tạm trả về 100 vé còn lại để demo
        return 100;
    }

    @Override
    public String toString() {
        return "ChuyenTau{" +
                "maChuyen='" + maChuyen + '\'' +
                ", thoiGianKhoiHanh=" + thoiGianKhoiHanh +
                ", gaDi='" + gaDi + '\'' +
                ", gaDen='" + gaDen + '\'' +
                '}';
    }
}

