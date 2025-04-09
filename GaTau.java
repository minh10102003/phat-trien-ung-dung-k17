package entity;

public class GaTau {
    private String maGa;
    private String tenGa;
    private String diaChi;
    private String moTa;

    public GaTau(String maGa, String tenGa, String diaChi, String moTa) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.diaChi = diaChi;
        this.moTa = moTa;
    }

    public String getMaGa() {
        return maGa;
    }

    public void setMaGa(String maGa) {
        this.maGa = maGa;
    }

    public String getTenGa() {
        return tenGa;
    }

    public void setTenGa(String tenGa) {
        this.tenGa = tenGa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "GaTau{" +
                "maGa='" + maGa + '\'' +
                ", tenGa='" + tenGa + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", moTa='" + moTa + '\'' +
                '}';
    }
}

