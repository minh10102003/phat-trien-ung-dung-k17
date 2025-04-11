package entity;

import java.util.Date;
import java.util.List;

public class HoaDon {
    private String maHD;
    private Date ngayLapHD;
    private NhanVien nhanVien;
    private HanhKhach hanhKhach;
    private List<CT_HoaDon> chiTietHoaDon;
    
	public HoaDon(String maHD, Date ngayLapHD, NhanVien nhanVien, HanhKhach hanhKhach, List<CT_HoaDon> chiTietHoaDon) {
		super();
		this.maHD = maHD;
		this.ngayLapHD = ngayLapHD;
		this.nhanVien = nhanVien;
		this.hanhKhach = hanhKhach;
		this.chiTietHoaDon = chiTietHoaDon;
	}
	
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public Date getNgayLapHD() {
		return ngayLapHD;
	}
	public void setNgayLapHD(Date ngayLapHD) {
		this.ngayLapHD = ngayLapHD;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public HanhKhach getHanhKhach() {
		return hanhKhach;
	}
	public void setHanhKhach(HanhKhach hanhKhach) {
		this.hanhKhach = hanhKhach;
	}
	public List<CT_HoaDon> getChiTietHoaDon() {
		return chiTietHoaDon;
	}
	public void setChiTietHoaDon(List<CT_HoaDon> chiTietHoaDon) {
		this.chiTietHoaDon = chiTietHoaDon;
	}

    
}
