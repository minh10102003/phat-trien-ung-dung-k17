package UI;

import DAO.DAO_NhanVien;
import entity.Entity_NhanVien;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class NhanVien extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public NhanVien() {
        setLayout(new BorderLayout());

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnIn = new JButton("In");

        btnThem.addActionListener(this::handleThem);
        btnSua.addActionListener(this::handleSua);
        btnXoa.addActionListener(this::handleXoa);
        btnIn.addActionListener(this::handleIn);

        toolBar.add(btnThem);
        toolBar.add(btnSua);
        toolBar.add(btnXoa);
        toolBar.add(btnIn);

        add(toolBar, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{
                "Mã nhân viên", "Tên nhân viên", "Năm sinh", "Giới tính", "CCCD", "Chức vụ", "Tình trạng", "Tên tài khoản"
        }, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<Entity_NhanVien> list = DAO_NhanVien.getAllNhanVien();
        for (Entity_NhanVien nv : list) {
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getNamSinh(), nv.isPhai() ? "Nam" : "Nữ",
                    nv.getCCCD(), nv.getChucVu(), nv.isTinhTrangNV() ? "Hoạt động" : "Nghỉ", nv.getTenTK()
            });
        }
    }

    private void handleThem(ActionEvent e) {
        Entity_NhanVien nv = showForm(null);
        if (nv != null && DAO_NhanVien.themNhanVien(nv)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Thêm thành công");
        } else if (nv != null) {
            JOptionPane.showMessageDialog(this, "Thêm thất bại");
        }
    }

    private void handleSua(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên");
            return;
        }
        Entity_NhanVien nv = getFromRow(row);
        Entity_NhanVien nvNew = showForm(nv);
        if (nvNew != null && DAO_NhanVien.capNhatNhanVien(nvNew)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Cập nhật thành công");
        } else if (nvNew != null) {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại");
        }
    }

    private void handleXoa(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên");
            return;
        }
        String maNV = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && DAO_NhanVien.xoaNhanVien(maNV)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại");
        }
    }
    
    private void handleIn(ActionEvent e) {
    	
    }

    private Entity_NhanVien showForm(Entity_NhanVien nvEdit) {
        JTextField txtMa = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtNgay = new JTextField();
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        JTextField txtCCCD = new JTextField();
        JTextField txtChucVu = new JTextField();
        JComboBox<String> cbTinhTrang = new JComboBox<>(new String[]{"Hoạt động", "Nghỉ"});
        JTextField txtTK = new JTextField();

        if (nvEdit != null) {
            txtMa.setText(nvEdit.getMaNV()); txtMa.setEnabled(false);
            txtTen.setText(nvEdit.getTenNV());
            txtNgay.setText(nvEdit.getNamSinh());
            cbGioiTinh.setSelectedItem(nvEdit.isPhai() ? "Nam" : "Nữ");
            txtCCCD.setText(nvEdit.getCCCD());
            txtChucVu.setText(nvEdit.getChucVu());
            cbTinhTrang.setSelectedItem(nvEdit.isTinhTrangNV() ? "Hoạt động" : "Nghỉ");
            txtTK.setText(nvEdit.getTenTK());
        }

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Mã NV:")); panel.add(txtMa);
        panel.add(new JLabel("Tên NV:")); panel.add(txtTen);
        panel.add(new JLabel("Năm sinh (yyyy-MM-dd):")); panel.add(txtNgay);
        panel.add(new JLabel("Giới tính:")); panel.add(cbGioiTinh);
        panel.add(new JLabel("CCCD:")); panel.add(txtCCCD);
        panel.add(new JLabel("Chức vụ:")); panel.add(txtChucVu);
        panel.add(new JLabel("Tình trạng:")); panel.add(cbTinhTrang);
        panel.add(new JLabel("Tên TK:")); panel.add(txtTK);

        int result = JOptionPane.showConfirmDialog(this, panel, nvEdit == null ? "Thêm" : "Cập nhật", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return new Entity_NhanVien(
                txtMa.getText().trim(),
                txtTen.getText().trim(),
                txtNgay.getText().trim(),
                cbGioiTinh.getSelectedItem().equals("Nam"),
                txtCCCD.getText().trim(),
                txtChucVu.getText().trim(),
                cbTinhTrang.getSelectedItem().equals("Hoạt động"),
                txtTK.getText().trim()
            );
        }
        return null;
    }

    private Entity_NhanVien getFromRow(int row) {
        return new Entity_NhanVien(
            model.getValueAt(row, 0).toString(),
            model.getValueAt(row, 1).toString(),
            model.getValueAt(row, 2).toString(),
            model.getValueAt(row, 3).toString().equals("Nam"),
            model.getValueAt(row, 4).toString(),
            model.getValueAt(row, 5).toString(),
            model.getValueAt(row, 6).toString().equals("Hoạt động"),
            model.getValueAt(row, 7).toString()
        );
    }
}
