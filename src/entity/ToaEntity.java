package entity;

public class ToaEntity {
    private String maToa;
    private String tenToa;
    private LoaiToa loaiToa;
	public ToaEntity(String maToa, String tenToa, LoaiToa loaiToa) {
		super();
		this.maToa = maToa;
		this.tenToa = tenToa;
		this.loaiToa = loaiToa;
	}
	public String getMaToa() {
		return maToa;
	}
	public void setMaToa(String maToa) {
		this.maToa = maToa;
	}
	public String getTenToa() {
		return tenToa;
	}
	public void setTenToa(String tenToa) {
		this.tenToa = tenToa;
	}
	public LoaiToa getLoaiToa() {
		return loaiToa;
	}
	public void setLoaiToa(LoaiToa loaiToa) {
		this.loaiToa = loaiToa;
	}

    
}
