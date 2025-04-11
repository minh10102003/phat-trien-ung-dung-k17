package entity;

public class HanhKhach {
    private String maHK;
    private String ten;
    private String CCCD;
    private String sdt;
    
	public HanhKhach(String maHK, String ten, String cCCD, String sdt) {
		super();
		this.maHK = maHK;
		this.ten = ten;
		CCCD = cCCD;
		this.sdt = sdt;
	}
	public String getMaHK() {
		return maHK;
	}
	public void setMaHK(String maHK) {
		this.maHK = maHK;
	}
	public String getTen() {
		return ten;
	}
	public void setTen(String ten) {
		this.ten = ten;
	}
	public String getCCCD() {
		return CCCD;
	}
	public void setCCCD(String cCCD) {
		CCCD = cCCD;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

    
}
