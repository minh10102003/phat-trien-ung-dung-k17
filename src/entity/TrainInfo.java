package entity;

public class TrainInfo {
    private String maTau;
    private String tenTau;
    private String gioKhoiHanh;
    private String gioDen;
    private int soChoTrong;

    public TrainInfo(String maTau, String tenTau, String gioKhoiHanh, String gioDen, int soChoTrong) {
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.gioKhoiHanh = gioKhoiHanh;
        this.gioDen = gioDen;
        this.soChoTrong = soChoTrong;
    }

    public String getMaTau() {
        return maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public String getGioKhoiHanh() {
        return gioKhoiHanh;
    }

    public String getGioDen() {
        return gioDen;
    }

    public int getSoChoTrong() {
        return soChoTrong;
    }

    public int getAvailableSeats() {
        return soChoTrong; 
    }

    public String getDeparture() {
        return gioKhoiHanh;
    }

    public String getArrival() {
        return gioDen;
    }
}
