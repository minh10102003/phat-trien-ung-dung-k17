package entity;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private int namSinh;
    private String phai;
    private String CCCD;
    private String chucVu;
    private String tinhTrangNV;
    private TaiKhoan taiKhoan;
	public NhanVien(String maNV, String tenNV, int namSinh, String phai, String cCCD, String chucVu, String tinhTrangNV,
			TaiKhoan taiKhoan) {
		super();
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.namSinh = namSinh;
		this.phai = phai;
		CCCD = cCCD;
		this.chucVu = chucVu;
		this.tinhTrangNV = tinhTrangNV;
		this.taiKhoan = taiKhoan;
	}
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getTenNV() {
		return tenNV;
	}
	public void setTenNV(String tenNV) {
		this.tenNV = tenNV;
	}
	public int getNamSinh() {
		return namSinh;
	}
	public void setNamSinh(int namSinh) {
		this.namSinh = namSinh;
	}
	public String getPhai() {
		return phai;
	}
	public void setPhai(String phai) {
		this.phai = phai;
	}
	public String getCCCD() {
		return CCCD;
	}
	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}
	public String getChucVu() {
		return chucVu;
	}
	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}
	public String getTinhTrangNV() {
		return tinhTrangNV;
	}
	public void setTinhTrangNV(String tinhTrangNV) {
		this.tinhTrangNV = tinhTrangNV;
	}
	public TaiKhoan getTaiKhoan() {
		return taiKhoan;
	}
	public void setTaiKhoan(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

    
}
