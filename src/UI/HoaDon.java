package UI;

import DAO.DAO_Ve;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HoaDon extends JPanel {
    private JTable tableHoaDon;
    private DefaultTableModel model;

    public HoaDon() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Quản lý Hóa Đơn", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder("Danh sách hóa đơn"));
        add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel(
            new String[]{"Mã HĐ", "Ngày lập", "Khách hàng", "Tổng tiền", "Chi tiết"}, 0
        );
        tableHoaDon = new JTable(model);
        scrollPane.setViewportView(tableHoaDon);

        loadHoaDonFromDB();

        tableHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableHoaDon.rowAtPoint(evt.getPoint());
                int col = tableHoaDon.columnAtPoint(evt.getPoint());
                if (col == 4 && row >= 0) {
                    String maHD = (String) model.getValueAt(row, 0);
                    // Mở chi tiết hóa đơn (có thể tái sử dụng ChiTietHoaDon)
                    ChiTietHoaDon chiTiet = new ChiTietHoaDon(maHD, HoaDon.this, row);
                    chiTiet.setVisible(true);
                }
            }
        });
    }

    private void loadHoaDonFromDB() {
        try {
            List<DAO_Ve.HoaDonInfo> list = DAO_Ve.getAllHoaDon();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (DAO_Ve.HoaDonInfo hd : list) {
                model.addRow(new Object[]{
                    hd.getMaVe(), df.format(hd.getNgayLap()), hd.getTenKhach(),
                    String.format("%.2f", hd.getTongTien()), "Xem chi tiết"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Lỗi tải hóa đơn: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE
            );
        }
    }
    /**
     * Cập nhật giá trị cột "Tổng tiền" tại dòng đã chọn.
     * @param rowIndex chỉ số dòng trong bảng
     * @param amount giá trị tổng tiền mới
     */
    public void updateTotalAmount(int rowIndex, double amount) {
        // cột Tổng tiền là cột index 3
        model.setValueAt(String.format("%.2f", amount), rowIndex, 3);
    }
}
