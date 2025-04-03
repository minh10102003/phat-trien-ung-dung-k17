package UI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class HoaDon extends JPanel {
    private JTable tableHoaDon;
    private DefaultTableModel model;

    public HoaDon() {
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Quản lý Hóa Đơn", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panelButtons = new JPanel();
        JButton btnThem = new JButton("Thêm");
        btnThem.setForeground(new Color(255, 255, 255));
        btnThem.setBackground(new Color(0, 64, 128));
        JButton btnXoa = new JButton("Xóa");
        btnXoa.setForeground(new Color(255, 255, 255));
        btnXoa.setBackground(new Color(0, 64, 128));
        JButton btnSua = new JButton("Sửa");
        btnSua.setForeground(new Color(255, 255, 255));
        btnSua.setBackground(new Color(0, 64, 128));
        panelButtons.add(btnThem);
        panelButtons.add(btnXoa);
        panelButtons.add(btnSua);
        add(panelButtons, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder("Danh sách hóa đơn"));
        add(scrollPane, BorderLayout.CENTER);

        model = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Mã HĐ", "Ngày lập", "Khách hàng", "Tổng tiền", "Chi tiết" }
        );

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("src/data/hoadon.txt"), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rowData = line.split(",");
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = rowData[i].trim();
                }
                model.addRow(new Object[]{ rowData[0], rowData[1], rowData[2], rowData[3], "Xem chi tiết" });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        tableHoaDon = new JTable(model);
        scrollPane.setViewportView(tableHoaDon);

        tableHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableHoaDon.rowAtPoint(evt.getPoint());
                int col = tableHoaDon.columnAtPoint(evt.getPoint());
                if (col == 4 && row >= 0) {
                    String maHD = (String) model.getValueAt(row, 0);
                    ChiTietHoaDon chiTiet = new ChiTietHoaDon(maHD, HoaDon.this, row); // Truyền HoaDon và chỉ số hàng
                    chiTiet.setVisible(true);
                }
            }
        });

        btnThem.addActionListener(e -> themHoaDon());
        btnXoa.addActionListener(e -> xoaHoaDon());
        btnSua.addActionListener(e -> suaHoaDon());
    }

    private void themHoaDon() {
        model.addRow(new Object[]{ "HDXXXX", "Ngày mới", "Khách hàng mới", "0", "Xem chi tiết" });
    }

    private void xoaHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow >= 0) {
            model.removeRow(selectedRow);
        }
    }

    private void suaHoaDon() {
        int selectedRow = tableHoaDon.getSelectedRow();
        if (selectedRow >= 0) {
            model.setValueAt("Sửa đổi", selectedRow, 1);
        }
    }

    // Phương thức để cập nhật tổng tiền trong bảng
    public void updateTotalAmount(int row, double amount) {
        model.setValueAt(String.format("%.2f", amount), row, 3);
    }
}