package entity;

public class TaiKhoan {
    private String tenTK;
    private String matKhau;
    private String loaiTK;
	public TaiKhoan(String tenTK, String matKhau, String loaiTK) {
		super();
		this.tenTK = tenTK;
		this.matKhau = matKhau;
		this.loaiTK = loaiTK;
	}
	public String getTenTK() {
		return tenTK;
	}
	public void setTenTK(String tenTK) {
		this.tenTK = tenTK;
	}
	public String getMatKhau() {
		return matKhau;
	}
	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	public String getLoaiTK() {
		return loaiTK;
	}
	public void setLoaiTK(String loaiTK) {
		this.loaiTK = loaiTK;
	}

    
}
