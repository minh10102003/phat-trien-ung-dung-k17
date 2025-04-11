package entity;

import java.util.Date;

public class Ve {
    private String maVe;
    private ChuyenTau chuyenTau;
    private String gioDi;
    private Date ngayDi;
    private LoaiVe loaiVe;
    private String cho;
    private Toa toa;
	public Ve(String maVe, ChuyenTau chuyenTau, String gioDi, Date ngayDi, LoaiVe loaiVe, String cho, Toa toa) {
		super();
		this.maVe = maVe;
		this.chuyenTau = chuyenTau;
		this.gioDi = gioDi;
		this.ngayDi = ngayDi;
		this.loaiVe = loaiVe;
		this.cho = cho;
		this.toa = toa;
	}
	public String getMaVe() {
		return maVe;
	}
	public void setMaVe(String maVe) {
		this.maVe = maVe;
	}
	public ChuyenTau getChuyenTau() {
		return chuyenTau;
	}
	public void setChuyenTau(ChuyenTau chuyenTau) {
		this.chuyenTau = chuyenTau;
	}
	public String getGioDi() {
		return gioDi;
	}
	public void setGioDi(String gioDi) {
		this.gioDi = gioDi;
	}
	public Date getNgayDi() {
		return ngayDi;
	}
	public void setNgayDi(Date ngayDi) {
		this.ngayDi = ngayDi;
	}
	public LoaiVe getLoaiVe() {
		return loaiVe;
	}
	public void setLoaiVe(LoaiVe loaiVe) {
		this.loaiVe = loaiVe;
	}
	public String getCho() {
		return cho;
	}
	public void setCho(String cho) {
		this.cho = cho;
	}
	public Toa getToa() {
		return toa;
	}
	public void setToa(Toa toa) {
		this.toa = toa;
	}

    
}
