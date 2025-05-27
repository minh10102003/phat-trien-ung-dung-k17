package entity;

import java.io.Serializable;

/**
 * Entity lớp Hành Khách.
 * Cập nhật:
 * - Thêm no-args constructor để hỗ trợ frameworks cần khởi tạo không tham số.
 * - Triển khai Serializable.
 * - Đổi trường CCCD về lowercase cccd, vẫn giữ getter/setter là getCCCD()/setCCCD() để tương thích.
 * - Override toString() tiện debug.
 */
public class HanhKhach implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maHK;
    private String ten;
    private String cccd;
    private String sdt;
    
    // No-args constructor
    public HanhKhach() {
    }

    // Full-args constructor
    public HanhKhach(String maHK, String ten, String cccd, String sdt) {
        this.maHK = maHK;
        this.ten = ten;
        this.cccd = cccd;
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

    // vẫn giữ tên method để tương thích DAO/UI
    public String getCCCD() {
        return cccd;
    }

    public void setCCCD(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (CCCD: %s)", maHK, ten, cccd);
    }
}
