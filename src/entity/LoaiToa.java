package entity;

public class LoaiToa {
    private String maLoaiToa;
    private String tenLoaiToa;
    private String moTa;

    /**
     * Constructor đầy đủ với mô tả
     */
    public LoaiToa(String maLoaiToa, String tenLoaiToa, String moTa) {
        this.maLoaiToa = maLoaiToa;
        this.tenLoaiToa = tenLoaiToa;
        this.moTa = moTa;
    }

    /**
     * Constructor tiện lợi chỉ với mã và tên, mô tả để trống
     */
    public LoaiToa(String maLoaiToa, String tenLoaiToa) {
        this(maLoaiToa, tenLoaiToa, "");
    }

    public String getMaLoaiToa() {
        return maLoaiToa;
    }

    public void setMaLoaiToa(String maLoaiToa) {
        this.maLoaiToa = maLoaiToa;
    }

    public String getTenLoaiToa() {
        return tenLoaiToa;
    }

    public void setTenLoaiToa(String tenLoaiToa) {
        this.tenLoaiToa = tenLoaiToa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}
