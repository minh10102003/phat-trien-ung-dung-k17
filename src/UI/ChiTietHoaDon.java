package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ChiTietHoaDon extends JFrame {
    private String maHoaDon;
    private JLabel lblKhachHang, lblNgayDatVe, lblThongTinChoNgoi, lblGiamGia, lblThanhTien;
    private JComboBox<String> cboGiamGia;
    private double tongTien;
    private HoaDon hoaDonPanel;
    private int rowIndex;

    public ChiTietHoaDon(String maHoaDon, HoaDon hoaDonPanel, int rowIndex) {
        this.maHoaDon = maHoaDon;
        this.hoaDonPanel = hoaDonPanel;
        this.rowIndex = rowIndex;
        setTitle("Chi tiết hóa đơn: " + maHoaDon);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        mainPanel.add(createLabel("Tên khách hàng:"));
        lblKhachHang = createLabel("");
        mainPanel.add(lblKhachHang);

        mainPanel.add(createLabel("Ngày đặt vé:"));
        lblNgayDatVe = createLabel("");
        mainPanel.add(lblNgayDatVe);

        mainPanel.add(createLabel("Thông tin chỗ ngồi:"));
        lblThongTinChoNgoi = createLabel("");
        mainPanel.add(lblThongTinChoNgoi);

        mainPanel.add(createLabel("Giảm giá:"));
        cboGiamGia = new JComboBox<>(new String[]{"Không giảm giá", "Người cao tuổi (>70) - 40%", "Trẻ nhỏ (<7) - 40%"});
        mainPanel.add(cboGiamGia);

        mainPanel.add(createLabel("Thành tiền:"));
        lblThanhTien = createLabel("");
        mainPanel.add(lblThanhTien);

        add(mainPanel, BorderLayout.CENTER);

        JButton btnClose = new JButton("Đóng");
        btnClose.setForeground(new Color(255, 255, 255));
        btnClose.setBackground(new Color(0, 64, 128));
        btnClose.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);

        cboGiamGia.addActionListener(e -> tinhThanhTien());
        loadHoaDonData();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 14));
        return label;
    }

    private void loadHoaDonData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/data/hoadon.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].trim().equals(maHoaDon)) {
                    lblKhachHang.setText(data[2]);
                    lblNgayDatVe.setText(data[1]);
                    lblThongTinChoNgoi.setText(data.length > 4 ? data[4] : "Không có thông tin");
                    tongTien = Double.parseDouble(data[3]);
                    tinhThanhTien();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tinhThanhTien() {
        double discount = 0;
        if (cboGiamGia.getSelectedIndex() == 1) {
            discount = 0.4;
        } else if (cboGiamGia.getSelectedIndex() == 2) {
            discount = 0.4;
        }
        double finalAmount = tongTien * (1 - discount);
        lblThanhTien.setText(String.format("%.2f VND", finalAmount));
        if (hoaDonPanel != null) {
            hoaDonPanel.updateTotalAmount(rowIndex, finalAmount);
        }
    }
}