package entity;

import java.util.Date;

public class ChuyenTau {
    private String maChuyenTau;
    private String tenChuyenTau;
    private String gaDi;
    private String gaDen;
    private Date ngayDi;
    private String gioKhoiHanh;
    private Tau tau;
    
	public ChuyenTau(String maChuyenTau, String tenChuyenTau, String gaDi, String gaDen, Date ngayDi,
			String gioKhoiHanh, Tau tau) {
		super();
		this.maChuyenTau = maChuyenTau;
		this.tenChuyenTau = tenChuyenTau;
		this.gaDi = gaDi;
		this.gaDen = gaDen;
		this.ngayDi = ngayDi;
		this.gioKhoiHanh = gioKhoiHanh;
		this.tau = tau;
	}
	
	public String getMaChuyenTau() {
		return maChuyenTau;
	}
	public void setMaChuyenTau(String maChuyenTau) {
		this.maChuyenTau = maChuyenTau;
	}
	public String getTenChuyenTau() {
		return tenChuyenTau;
	}
	public void setTenChuyenTau(String tenChuyenTau) {
		this.tenChuyenTau = tenChuyenTau;
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
	public Date getNgayDi() {
		return ngayDi;
	}
	public void setNgayDi(Date ngayDi) {
		this.ngayDi = ngayDi;
	}
	public String getGioKhoiHanh() {
		return gioKhoiHanh;
	}
	public void setGioKhoiHanh(String gioKhoiHanh) {
		this.gioKhoiHanh = gioKhoiHanh;
	}
	public Tau getTau() {
		return tau;
	}
	public void setTau(Tau tau) {
		this.tau = tau;
	}

	@Override
	public String toString() {
		return "ChuyenTau [maChuyenTau=" + maChuyenTau + ", tenChuyenTau=" + tenChuyenTau + ", gaDi=" + gaDi
				+ ", gaDen=" + gaDen + ", ngayDi=" + ngayDi + ", gioKhoiHanh=" + gioKhoiHanh + ", tau=" + tau + "]";
	}
	
	
}
