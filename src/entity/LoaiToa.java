package entity;

public class LoaiToa {
    private String maLoaiToa;
    private String tenLoaiToa;
    private String moTa;
	public LoaiToa(String maLoaiToa, String tenLoaiToa, String moTa) {
		super();
		this.maLoaiToa = maLoaiToa;
		this.tenLoaiToa = tenLoaiToa;
		this.moTa = moTa;
	}
	public String getMaLoaiToa() {
		return maLoaiToa;
	}
	public void setMaLoaiToa(String maLoaiToa) {
		this.maLoaiToa = maLoaiToa;
	}
	public String getTenLoaiToa() {
		return tenLoaiToa;
	}
	public void setTenLoaiToa(String tenLoaiToa) {
		this.tenLoaiToa = tenLoaiToa;
	}
	public String getMoTa() {
		return moTa;
	}
	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

    
}
