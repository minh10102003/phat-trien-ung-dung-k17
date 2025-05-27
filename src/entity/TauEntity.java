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
	// Mới: mã ga đi và mã ga đến
	private String maGaDi;
	private String maGaDen;

	/**
	 * Constructor đầy đủ, bao gồm cả mã ga đi và mã ga đến
	 */
	public TauEntity(String maTau, String tenTau, String loaiTau, Timestamp gioKhoiHanh, Timestamp gioDen, int soChoCon,
			String thoiGianDi, String thoiGianDen, String maGaDi, String maGaDen) {
		this.maTau = maTau;
		this.tenTau = tenTau;
		this.loaiTau = loaiTau;
		this.gioKhoiHanh = gioKhoiHanh;
		this.gioDen = gioDen;
		this.soChoCon = soChoCon;
		this.thoiGianDi = thoiGianDi;
		this.thoiGianDen = thoiGianDen;
		this.maGaDi = maGaDi;
		this.maGaDen = maGaDen;
	}

	/**
	 * Constructor rút gọn (không có station codes)
	 */
	public TauEntity(String maTau, String tenTau, String loaiTau, String thoiGianDi, String thoiGianDen, int soChoCon) {
		this(maTau, tenTau, loaiTau, null, null, soChoCon, thoiGianDi, thoiGianDen, null, null);
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

	public Timestamp getGioKhoiHanh() {
		return gioKhoiHanh;
	}

	public void setGioKhoiHanh(Timestamp gioKhoiHanh) {
		this.gioKhoiHanh = gioKhoiHanh;
	}

	public Timestamp getGioDen() {
		return gioDen;
	}

	public void setGioDen(Timestamp gioDen) {
		this.gioDen = gioDen;
	}

	public String getThoiGianDi() {
		return thoiGianDi;
	}

	public void setThoiGianDi(String thoiGianDi) {
		this.thoiGianDi = thoiGianDi;
	}

	public String getThoiGianDen() {
		return thoiGianDen;
	}

	public void setThoiGianDen(String thoiGianDen) {
		this.thoiGianDen = thoiGianDen;
	}

	public int getSoChoCon() {
		return soChoCon;
	}

	public void setSoChoCon(int soChoCon) {
		this.soChoCon = soChoCon;
	}

	// Mới: getters cho mã ga đi và ga đến
	public String getMaGaDi() {
		return maGaDi;
	}

	public void setMaGaDi(String maGaDi) {
		this.maGaDi = maGaDi;
	}

	public String getMaGaDen() {
		return maGaDen;
	}

	public void setMaGaDen(String maGaDen) {
		this.maGaDen = maGaDen;
	}
}
