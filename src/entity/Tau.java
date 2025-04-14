package entity;

public class Tau {
    private String maTau;
    private String tenTau;
    private LoaiToa loaiToa;
	public Tau(String maTau, String tenTau, LoaiToa loaiToa) {
		super();
		this.maTau = maTau;
		this.tenTau = tenTau;
		this.loaiToa = loaiToa;
	}
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
	public LoaiToa getLoaiToa() {
		return loaiToa;
	}
	public void setLoaiToa(LoaiToa loaiToa) {
		this.loaiToa = loaiToa;
	}

    
}
