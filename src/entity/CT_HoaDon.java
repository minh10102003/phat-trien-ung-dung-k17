package entity;

public class CT_HoaDon {
    private HoaDon hoaDon;
    private Ve ve;
    private int soLuong;

	public CT_HoaDon(HoaDon hoaDon, Ve ve, int soLuong) {
		super();
		this.hoaDon = hoaDon;
		this.ve = ve;
		this.soLuong = soLuong;
	}
    
	public HoaDon getHoaDon() {
		return hoaDon;
	}
	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}
	public Ve getVe() {
		return ve;
	}
	public void setVe(Ve ve) {
		this.ve = ve;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
}
