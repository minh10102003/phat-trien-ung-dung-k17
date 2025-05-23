// entity/SeatInfo.java
package entity;

public class SeatInfo {
    private String maGhe;
    private int cho; // số ghế
    private String maLoaiGhe;
    private String tenLoaiGhe;
    private String moTaLoaiGhe;

    public SeatInfo(String maGhe, int cho, String maLoaiGhe, String tenLoaiGhe, String moTaLoaiGhe) {
        this.maGhe = maGhe;
        this.cho = cho;
        this.maLoaiGhe = maLoaiGhe;
        this.tenLoaiGhe = tenLoaiGhe;
        this.moTaLoaiGhe = moTaLoaiGhe;
    }

    public String getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(String maGhe) {
        this.maGhe = maGhe;
    }

    public int getCho() {
        return cho;
    }

    public void setCho(int cho) {
        this.cho = cho;
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

    public String getMoTaLoaiGhe() {
        return moTaLoaiGhe;
    }

    public void setMoTaLoaiGhe(String moTaLoaiGhe) {
        this.moTaLoaiGhe = moTaLoaiGhe;
    }

    @Override
    public String toString() {
        return "Ghế số " + cho + " - Loại: " + tenLoaiGhe;
    }
}
