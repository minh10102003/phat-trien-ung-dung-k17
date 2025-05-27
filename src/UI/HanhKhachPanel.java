package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import entity.HanhKhach;
import DAO.DAO_HanhKhach;

/**
 * Panel quản lý danh sách hành khách với các chức năng CRUD.
 */
public class HanhKhachPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnUpdate, btnDelete;

    public HanhKhachPanel() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
        loadData();
    }

    private void initComponents() {
        // Model và Table
        String[] columns = {"Mã HK", "Họ tên", "CCCD", "SĐT"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel chứa các nút hành động
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnAdd = new JButton("Thêm khách hàng");
        btnUpdate = new JButton("Cập nhật khách hàng");
        btnDelete = new JButton("Xóa khách hàng");
        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        add(panelButtons, BorderLayout.SOUTH);

        // Đăng ký sự kiện
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());

        // Double-click để cập nhật nhanh
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    onUpdate();
                }
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<HanhKhach> list = DAO_HanhKhach.getAll();
            for (HanhKhach hk : list) {
                model.addRow(new Object[]{
                    hk.getMaHK(), hk.getTen(), hk.getCCCD(), hk.getSdt()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi load dữ liệu:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAdd() {
        HanhKhach result = showDialog(null);
        if (result != null) {
            try {
                DAO_HanhKhach.insertOrGetId(
                        result.getTen(), result.getCCCD(), result.getSdt());
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi thêm khách hàng:\n" + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onUpdate() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng để cập nhật",
                    "Chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maHK = (String) model.getValueAt(row, 0);
        try {
            HanhKhach existing = DAO_HanhKhach.getById(maHK);
            if (existing == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy hành khách với mã: " + maHK,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            HanhKhach updated = showDialog(existing);
            if (updated != null) {
                DAO_HanhKhach.insertOrGetId(
                        updated.getTen(), updated.getCCCD(), updated.getSdt());
                loadData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật khách hàng:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng để xóa",
                    "Chọn dòng", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maHK = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa khách hàng " + maHK + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                DAO_HanhKhach.delete(maHK);
                loadData();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi xóa khách hàng:\n" + ex.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Hiển thị dialog để nhập hoặc chỉnh sửa thông tin khách hàng.
     * @param hkEntity null để thêm mới, không null để cập nhật.
     * @return HanhKhach mới hoặc cập nhật, hoặc null nếu hủy.
     */
        /**
     * Hiển thị dialog để nhập hoặc chỉnh sửa thông tin khách hàng.
     * @param hkEntity null để thêm mới, không null để cập nhật.
     * @return HanhKhach mới hoặc cập nhật, hoặc null nếu hủy.
     */
    private HanhKhach showDialog(HanhKhach hkEntity) {
        JTextField txtMaHK = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtCCCD = new JTextField();
        JTextField txtSDT = new JTextField();

        if (hkEntity != null) {
            // Cập nhật: hiển thị và khóa mã
            txtMaHK.setText(hkEntity.getMaHK());
            txtMaHK.setEditable(false);
            txtTen.setText(hkEntity.getTen());
            txtCCCD.setText(hkEntity.getCCCD());
            txtSDT.setText(hkEntity.getSdt());
        }

        // Tạo panel với số dòng tùy theo add/update
        JPanel panel;
        if (hkEntity == null) {
            // Thêm mới: không hiển thị nhập Mã HK
            panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.add(new JLabel("Họ tên:"));  panel.add(txtTen);
            panel.add(new JLabel("CCCD:"));  panel.add(txtCCCD);
            panel.add(new JLabel("SĐT:"));   panel.add(txtSDT);
        } else {
            // Cập nhật: hiển thị đầy đủ
            panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Mã HK:")); panel.add(txtMaHK);
            panel.add(new JLabel("Họ tên:"));  panel.add(txtTen);
            panel.add(new JLabel("CCCD:"));  panel.add(txtCCCD);
            panel.add(new JLabel("SĐT:"));   panel.add(txtSDT);
        }

        String title = (hkEntity == null ? "Thêm khách hàng" : "Cập nhật khách hàng");
        int option = JOptionPane.showConfirmDialog(
                this, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            // Đọc dữ liệu
            String ten = txtTen.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String sdt = txtSDT.getText().trim();
            if (ten.isEmpty() || cccd.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin",
                        "Thiếu dữ liệu", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            // Trả về entity với hoặc không có mã (DAO sẽ sinh mã)
            String maHK = hkEntity != null ? hkEntity.getMaHK() : null;
            return new HanhKhach(maHK, ten, cccd, sdt);
        }
        return null;
    }
}

