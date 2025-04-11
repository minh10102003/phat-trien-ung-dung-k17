package entity;

public class Ghe {
    private String maGhe;
    private String cho;
    private LoaiGhe loaiGhe;
    private Toa toa;
    
	public Ghe(String maGhe, String cho, LoaiGhe loaiGhe, Toa toa) {
		super();
		this.maGhe = maGhe;
		this.cho = cho;
		this.loaiGhe = loaiGhe;
		this.toa = toa;
	}
	
	public String getMaGhe() {
		return maGhe;
	}
	public void setMaGhe(String maGhe) {
		this.maGhe = maGhe;
	}
	public String getCho() {
		return cho;
	}
	public void setCho(String cho) {
		this.cho = cho;
	}
	public LoaiGhe getLoaiGhe() {
		return loaiGhe;
	}
	public void setLoaiGhe(LoaiGhe loaiGhe) {
		this.loaiGhe = loaiGhe;
	}
	public Toa getToa() {
		return toa;
	}
	public void setToa(Toa toa) {
		this.toa = toa;
	}
}
