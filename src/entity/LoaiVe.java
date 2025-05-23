package entity;

public class LoaiVe {
    private String maLoaiVe;
    private String tenLoaiVe;
    private double giaTien;
	public LoaiVe(String maLoaiVe, String tenLoaiVe, double giaTien) {
		super();
		this.maLoaiVe = maLoaiVe;
		this.tenLoaiVe = tenLoaiVe;
		this.giaTien = giaTien;
	}
	public String getMaLoaiVe() {
		return maLoaiVe;
	}
	public void setMaLoaiVe(String maLoaiVe) {
		this.maLoaiVe = maLoaiVe;
	}
	public String getTenLoaiVe() {
		return tenLoaiVe;
	}
	public void setTenLoaiVe(String tenLoaiVe) {
		this.tenLoaiVe = tenLoaiVe;
	}
	public double getGiaTien() {
		return giaTien;
	}
	public void setGiaTien(double giaTien) {
		this.giaTien = giaTien;
	}

	@Override
    public String toString() {
        return tenLoaiVe;  
    }
}
