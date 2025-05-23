package entity;

import java.util.Date;

public class Ve {
    private String maVe;
    private String maTau;
    private String cho;
    private Date ngayDi;
    private String loaiVe;

    public Ve(String maVe, String maTau, String cho, Date ngayDi, String loaiVe) {
        this.maVe = maVe;
        this.maTau = maTau;
        this.cho = cho;
        this.ngayDi = ngayDi;
        this.loaiVe = loaiVe;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getCho() {
        return cho;
    }

    public void setCho(String cho) {
        this.cho = cho;
    }

    public Date getNgayDi() {
        return ngayDi;
    }

    public void setNgayDi(Date ngayDi) {
        this.ngayDi = ngayDi;
    }

    public String getLoaiVe() {
        return loaiVe;
    }

    public void setLoaiVe(String loaiVe) {
        this.loaiVe = loaiVe;
    }
}
