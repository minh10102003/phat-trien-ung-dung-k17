package entity;

public class LoaiGhe {
    private String maLoaiGhe;
    private String tenLoaiGhe;
    private String moTa;
    
	public LoaiGhe(String maLoaiGhe, String tenLoaiGhe, String moTa) {
		super();
		this.maLoaiGhe = maLoaiGhe;
		this.tenLoaiGhe = tenLoaiGhe;
		this.moTa = moTa;
	}

	public String getMaLoaiGhe() {
		return maLoaiGhe;
	}

	public void setMaLoaiGhe(String maLoaiGhe) {
		this.maLoaiGhe = maLoaiGhe;
	}

	public String getTenLoaiGhe() {
		return tenLoaiGhe;
	}

	public void setTenLoaiGhe(String tenLoaiGhe) {
		this.tenLoaiGhe = tenLoaiGhe;
	}

	public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

    
}
