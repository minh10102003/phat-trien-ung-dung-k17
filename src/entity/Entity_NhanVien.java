package entity;

public class Entity_NhanVien {
    private String maNV;
    private String tenNV;
    private String namSinh;
    private boolean phai;
    private String CCCD;
    private String chucVu;
    private boolean tinhTrangNV;
    private String tenTK;

    public Entity_NhanVien(String maNV, String tenNV, String namSinh, boolean phai, String CCCD, String chucVu, boolean tinhTrangNV, String tenTK) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.namSinh = namSinh;
        this.phai = phai;
        this.CCCD = CCCD;
        this.chucVu = chucVu;
        this.tinhTrangNV = tinhTrangNV;
        this.tenTK = tenTK;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getNamSinh() { return namSinh; }
    public void setNamSinh(String namSinh) { this.namSinh = namSinh; }

    public boolean isPhai() { return phai; }
    public void setPhai(boolean phai) { this.phai = phai; }

    public String getCCCD() { return CCCD; }
    public void setCCCD(String CCCD) { this.CCCD = CCCD; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public boolean isTinhTrangNV() { return tinhTrangNV; }
    public void setTinhTrangNV(boolean tinhTrangNV) { this.tinhTrangNV = tinhTrangNV; }

    public String getTenTK() { return tenTK; }
    public void setTenTK(String tenTK) { this.tenTK = tenTK; }
}