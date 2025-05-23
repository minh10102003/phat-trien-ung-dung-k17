package entity;

import java.sql.Timestamp;
import java.sql.Time;

public class TauEntity {
    private String maTau;
    private String tenTau;
    private String loaiTau;
    private Timestamp gioKhoiHanh;
    private Timestamp gioDen;
    private int soChoCon;
	private String thoiGianDi;
	private String thoiGianDen;

    public TauEntity(String maTau, String tenTau, String loaiTau, String thoiGianDi, String thoiGianDen, int soChoCon) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.loaiTau = loaiTau;
        this.thoiGianDi = thoiGianDi;
        this.thoiGianDen = thoiGianDen;
        this.soChoCon = soChoCon;
    }

    // Constructor cũ (nếu cần giữ)
    public TauEntity(String maTau, String tenTau, String loaiTau) {
        this(maTau, tenTau, loaiTau, null, null, 0);
    }

    // Getters & Setters
    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

    public String getLoaiTau() {
        return loaiTau;
    }

    public void setLoaiTau(String loaiTau) {
        this.loaiTau = loaiTau;
    }
    public void setGioKhoiHanh(Timestamp gioKhoiHanh) {
        this.gioKhoiHanh = gioKhoiHanh;
    }

    public void setGioDen(Timestamp gioDen) {
        this.gioDen = gioDen;
    }

    public String getThoiGianDi() { return thoiGianDi; }
    public void setThoiGianDi(String thoiGianDi) { this.thoiGianDi = thoiGianDi; }

    public String getThoiGianDen() { return thoiGianDen; }
    public void setThoiGianDen(String thoiGianDen) { this.thoiGianDen = thoiGianDen; }

    public Timestamp getGioKhoiHanh() { return gioKhoiHanh; }
    public Timestamp getGioDen() { return gioDen; }
    
    public int getSoChoCon() {
        return soChoCon;
    }

    public void setSoChoCon(int soChoCon) {
        this.soChoCon = soChoCon;
    }

	
}