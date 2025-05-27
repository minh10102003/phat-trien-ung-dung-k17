package entity;

public class Entity_NhanVien {
	private String maNV;
	private String tenNV;
	private String namSinh;
	private boolean phai;
	private String CCCD;
	private String chucVu;
	private boolean tinhTrangNV;
	private String tenTK;
	private byte[] photo; // hình ảnh nhân viên

	/**
	 * Constructor đầy đủ, bao gồm ảnh (photo) dưới dạng byte array. photo có thể
	 * null nếu không có ảnh.
	 */
	public Entity_NhanVien(String maNV, String tenNV, String namSinh, boolean phai, String CCCD, String chucVu,
			boolean tinhTrangNV, String tenTK, byte[] photo) {
		this.maNV = maNV;
		this.tenNV = tenNV;
		this.namSinh = namSinh;
		this.phai = phai;
		this.CCCD = CCCD;
		this.chucVu = chucVu;
		this.tinhTrangNV = tinhTrangNV;
		this.tenTK = tenTK;
		this.photo = photo;
	}

	// Nếu cần constructor cũ không ảnh, vẫn giữ để tương thích
	public Entity_NhanVien(String maNV, String tenNV, String namSinh, boolean phai, String CCCD, String chucVu,
			boolean tinhTrangNV, String tenTK) {
		this(maNV, tenNV, namSinh, phai, CCCD, chucVu, tinhTrangNV, tenTK, null);
	}

	// getters và setters
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

	public String getNamSinh() {
		return namSinh;
	}

	public void setNamSinh(String namSinh) {
		this.namSinh = namSinh;
	}

	public boolean isPhai() {
		return phai;
	}

	public void setPhai(boolean phai) {
		this.phai = phai;
	}

	public String getCCCD() {
		return CCCD;
	}

	public void setCCCD(String CCCD) {
		this.CCCD = CCCD;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public boolean isTinhTrangNV() {
		return tinhTrangNV;
	}

	public void setTinhTrangNV(boolean tinhTrangNV) {
		this.tinhTrangNV = tinhTrangNV;
	}

	public String getTenTK() {
		return tenTK;
	}

	public void setTenTK(String tenTK) {
		this.tenTK = tenTK;
	}

	/**
	 * Lấy mảng byte của ảnh (VARBINARY từ DB), có thể null nếu không có ảnh.
	 */
	public byte[] getPhoto() {
		return photo;
	}

	/**
	 * Set mảng byte của ảnh (để lưu vào DB).
	 */
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
}
