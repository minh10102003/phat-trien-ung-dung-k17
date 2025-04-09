package entity;

public class NhanVien {
    private String username;
    private String password;

    public NhanVien(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void xacNhanNguoiDung() {
        System.out.println("Xác nhận người dùng...");
    }

    public void baoCaoThongKe() {
        System.out.println("Báo cáo thống kê...");
    }

    public boolean xacNhanVeMua() {
        System.out.println("Xác nhận vé mua...");
        return true;
    }

    @Override
    public String toString() {
        return "NhanVienQuanLy{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
