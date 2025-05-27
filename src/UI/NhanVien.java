package UI;

import DAO.DAO_NhanVien;
import entity.Entity_NhanVien;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
                "Mã NV","Tên NV","Năm sinh","Giới tính","CCCD","Chức vụ","Tình trạng","Tên TK","Ảnh"
        }, 0) {
            @Override public Class<?> getColumnClass(int col) {
                if (col == 8) return ImageIcon.class;
                return super.getColumnClass(col);
            }
        };
        table = new JTable(model);
        table.setRowHeight(60);
        // Renderer cho ảnh
        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                if (isSelected) lbl.setBackground(table.getSelectionBackground());
                if (value instanceof byte[]) {
                    try {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream((byte[]) value));
                        ImageIcon icon = new ImageIcon(img.getScaledInstance(50,50,Image.SCALE_SMOOTH));
                        lbl.setIcon(icon);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return lbl;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<Entity_NhanVien> list = DAO_NhanVien.getAllNhanVien();
        for (Entity_NhanVien nv : list) {
            ImageIcon imgIcon = null;
            // we store raw bytes in model
            model.addRow(new Object[]{
                    nv.getMaNV(), nv.getTenNV(), nv.getNamSinh(), nv.isPhai() ? "Nam" : "Nữ",
                    nv.getCCCD(), nv.getChucVu(), nv.isTinhTrangNV() ? "Hoạt động" : "Nghỉ",
                    nv.getTenTK(), nv.getPhoto()
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
        Entity_NhanVien old = getFromRow(row);
        Entity_NhanVien nvNew = showForm(old);
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
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên để xóa.", "Chú ý", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maNV = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa nhân viên " + maNV + " không?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean ok = DAO_NhanVien.xoaNhanVien(maNV);
            if (ok) {
                loadData();
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại. Có thể nhân viên đang có ràng buộc dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Đã có lỗi xảy ra khi xóa: " + ex.getMessage(),
                "Lỗi hệ thống",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void handleIn(ActionEvent e) {}

    private Entity_NhanVien showForm(Entity_NhanVien nvEdit) {
        // 1. Tạo các input
        JTextField txtMa       = new JTextField(20);
        txtMa.setEnabled(false);
        JTextField txtTen      = new JTextField(20);
        JTextField txtNgay     = new JTextField(20);
        JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam","Nữ"});
        JTextField txtCCCD     = new JTextField(20);
        JTextField txtChucVu   = new JTextField(20);
        JComboBox<String> cbTinhTrang = new JComboBox<>(new String[]{"Hoạt động","Nghỉ"});
        JTextField txtTK       = new JTextField(20);
        txtTK.setEnabled(false);

        // 2. Tải dữ liệu cũ nếu đang sửa
        byte[][] photoHolder = new byte[1][];
        JLabel lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(100,100));
        if (nvEdit != null) {
            txtMa.setText(nvEdit.getMaNV());
            txtTen.setText(nvEdit.getTenNV());
            txtNgay.setText(nvEdit.getNamSinh());
            cbGioiTinh.setSelectedItem(nvEdit.isPhai() ? "Nam" : "Nữ");
            txtCCCD.setText(nvEdit.getCCCD());
            txtChucVu.setText(nvEdit.getChucVu());
            cbTinhTrang.setSelectedItem(nvEdit.isTinhTrangNV() ? "Hoạt động" : "Nghỉ");
            txtTK.setText(nvEdit.getTenTK());
            byte[] old = nvEdit.getPhoto();
            if (old != null) {
                try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(old));
                    lblPhoto.setIcon(new ImageIcon(img.getScaledInstance(100,100,Image.SCALE_SMOOTH)));
                    photoHolder[0] = old;
                } catch (IOException ignored) {}
            }
        }

        // 3. Nút chọn ảnh
        JButton btnUpload = new JButton("Chọn ảnh");
        btnUpload.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File f = chooser.getSelectedFile();
                    byte[] data = Files.readAllBytes(f.toPath());
                    photoHolder[0] = data;
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
                    lblPhoto.setIcon(new ImageIcon(img.getScaledInstance(100,100,Image.SCALE_SMOOTH)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 4. Build panel với GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "Mã NV:", "Tên NV:", "Năm sinh:", "Giới tính:",
            "CCCD:", "Chức vụ:", "Tình trạng:", "Tên TK:"
        };
        Component[] fields = {
            txtMa, txtTen, txtNgay, cbGioiTinh,
            txtCCCD, txtChucVu, cbTinhTrang, txtTK
        };

        for (int i = 0; i < labels.length; i++) {
            c.gridx = 0;
            c.gridy = i;
            c.gridwidth = 1;
            panel.add(new JLabel(labels[i]), c);

            c.gridx = 1;
            panel.add(fields[i], c);
        }

        // Ảnh ở cột 2, spanning 2 rows để hiển thị thumbnail
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        panel.add(lblPhoto, c);

        // Nút upload ngay dưới thumbnail
        c.gridy = 2;
        c.gridheight = 1;
        panel.add(btnUpload, c);

        // 5. Hiển thị dialog
        int result = JOptionPane.showConfirmDialog(
            this, panel,
            nvEdit == null ? "Thêm nhân viên" : "Cập nhật nhân viên",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            return new Entity_NhanVien(
                txtMa.getText().trim(),
                txtTen.getText().trim(),
                txtNgay.getText().trim(),
                cbGioiTinh.getSelectedItem().equals("Nam"),
                txtCCCD.getText().trim(),
                txtChucVu.getText().trim(),
                cbTinhTrang.getSelectedItem().equals("Hoạt động"),
                txtTK.getText().trim(),
                photoHolder[0]  // có thể null
            );
        }
        return null;
    }


    private Entity_NhanVien getFromRow(int row) {
        return new Entity_NhanVien(
            model.getValueAt(row,0).toString(),
            model.getValueAt(row,1).toString(),
            model.getValueAt(row,2).toString(),
            model.getValueAt(row,3).toString().equals("Nam"),
            model.getValueAt(row,4).toString(),
            model.getValueAt(row,5).toString(),
            model.getValueAt(row,6).toString().equals("Hoạt động"),
            model.getValueAt(row,7).toString(),
            (byte[])model.getValueAt(row,8)
        );
    }
}
