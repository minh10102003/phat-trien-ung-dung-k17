package entity;

public class HanhKhach {
    private String maKH;
    private String cccd;
    private String password;

    public HanhKhach(String maKH, String cccd, String password) {
        this.maKH = maKH;
        this.cccd = cccd;
        this.password = password;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getCCCD() {
        return cccd;
    }

    public void setCCCD(String cccd) {
        this.cccd = cccd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean datVe(String maVe, int soLuong) {
        // Logic đặt vé giả định
        System.out.println("Đặt " + soLuong + " vé với mã " + maVe);
        return true;
    }

    public void xemLichTrinh() {
        System.out.println("Xem lịch trình...");
    }

    @Override
    public String toString() {
        return "HanhKhach{" +
                "maKH='" + maKH + '\'' +
                ", cccd='" + cccd + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
