package UI;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class NhanVien extends JPanel {
    private JTextField txtTimKiem;
    private JTable tableNhanVien;
    
    public NhanVien() {
        setLayout(new BorderLayout(10, 10));

        //Thanh công cụ (các nút chức năng) phía trên
        JPanel panelToolBar = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panelToolBar.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        add(panelToolBar, BorderLayout.NORTH);

        JButton btnThem = new JButton("Thêm nhân viên");
        btnThem.setForeground(new Color(255, 255, 255));
        btnThem.setBackground(new Color(0, 64, 128));
        panelToolBar.add(btnThem);
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Thêm Nhân Viên");
                dialog.setSize(400, 300);
                dialog.setLayout(new FlowLayout());

                JLabel lblMaNV = new JLabel("Mã NV:");
                JTextField txtMaNV = new JTextField(10);
                JLabel lblHoTen = new JLabel("Họ tên:");
                JTextField txtHoTen = new JTextField(15);
                JLabel lblNgaySinh = new JLabel("Ngày sinh:");
                JTextField txtNgaySinh = new JTextField(10);
                JLabel lblGioiTinh = new JLabel("Giới tính:");
                JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
                JLabel lblSDT = new JLabel("SĐT:");
                JTextField txtSDT = new JTextField(10);
                JLabel lblViTri = new JLabel("Vị trí:");
                JTextField txtViTri = new JTextField(15);

                JButton btnLuu = new JButton("Lưu");
                JButton btnHuy = new JButton("Hủy");

                dialog.add(lblMaNV);
                dialog.add(txtMaNV);
                dialog.add(lblHoTen);
                dialog.add(txtHoTen);
                dialog.add(lblNgaySinh);
                dialog.add(txtNgaySinh);
                dialog.add(lblGioiTinh);
                dialog.add(cbGioiTinh);
                dialog.add(lblSDT);
                dialog.add(txtSDT);
                dialog.add(lblViTri);
                dialog.add(txtViTri);
                dialog.add(btnLuu);
                dialog.add(btnHuy);

                btnLuu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String maNV = txtMaNV.getText().trim();
                        String hoTen = txtHoTen.getText().trim();
                        String ngaySinh = txtNgaySinh.getText().trim();
                        String gioiTinh = cbGioiTinh.getSelectedItem().toString();
                        String sdt = txtSDT.getText().trim();
                        String viTri = txtViTri.getText().trim();

                        if (maNV.isEmpty() || hoTen.isEmpty() || ngaySinh.isEmpty() || sdt.isEmpty() || viTri.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/nhanvien.txt", true))) {
                            writer.write(maNV + "," + hoTen + "," + ngaySinh + "," + gioiTinh + "," + sdt + "," + viTri);
                            writer.newLine();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(dialog, "Lỗi khi ghi vào file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        DefaultTableModel model = (DefaultTableModel) tableNhanVien.getModel();
                        model.addRow(new Object[]{maNV, hoTen, ngaySinh, gioiTinh, sdt, viTri});
                        JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!");
                        dialog.dispose();
                    }
                });

                btnHuy.addActionListener(e1 -> dialog.dispose());

                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setForeground(new Color(255, 255, 255));
        btnXoa.setBackground(new Color(0, 64, 128));
        panelToolBar.add(btnXoa);
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableNhanVien.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) tableNhanVien.getModel();
                String maNV = model.getValueAt(selectedRow, 0).toString(); // Lấy mã nhân viên

                // Xóa khỏi bảng
                model.removeRow(selectedRow);

                // Xóa khỏi file employee_detail.txt
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/data/nhanvien.txt"), "UTF-8"));
                    StringBuilder newContent = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (!data[0].trim().equals(maNV)) { // Chỉ giữ lại nhân viên không bị xóa
                            newContent.append(line).append("\n");
                        }
                    }
                    reader.close();

                    // Ghi lại file sau khi xóa
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/nhanvien.txt"));
                    writer.write(newContent.toString());
                    writer.close();

                    JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnSua = new JButton("Sửa");
        btnSua.setForeground(new Color(255, 255, 255));
        btnSua.setBackground(new Color(0, 64, 128));
        panelToolBar.add(btnSua);
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableNhanVien.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) tableNhanVien.getModel();
                
                // Lấy thông tin nhân viên từ bảng
                String maNV = model.getValueAt(selectedRow, 0).toString();
                String hoTen = model.getValueAt(selectedRow, 1).toString();
                String ngaySinh = model.getValueAt(selectedRow, 2).toString();
                String gioiTinh = model.getValueAt(selectedRow, 3).toString();
                String sdt = model.getValueAt(selectedRow, 4).toString();
                String viTri = model.getValueAt(selectedRow, 5).toString();

                // Hiển thị Dialog sửa thông tin
                JDialog dialog = new JDialog();
                dialog.setTitle("Sửa Thông Tin Nhân Viên");
                dialog.setSize(400, 300);
                dialog.setLayout(new FlowLayout());

                JLabel lblMaNV = new JLabel("Mã NV:");
                JTextField txtMaNV = new JTextField(maNV, 10);
                txtMaNV.setEnabled(false); // Không cho sửa mã nhân viên
                JLabel lblHoTen = new JLabel("Họ tên:");
                JTextField txtHoTen = new JTextField(hoTen, 15);
                JLabel lblNgaySinh = new JLabel("Ngày sinh:");
                JTextField txtNgaySinh = new JTextField(ngaySinh, 10);
                JLabel lblGioiTinh = new JLabel("Giới tính:");
                JComboBox<String> cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
                cbGioiTinh.setSelectedItem(gioiTinh);
                JLabel lblSDT = new JLabel("SĐT:");
                JTextField txtSDT = new JTextField(sdt, 10);
                JLabel lblViTri = new JLabel("Vị trí:");
                JTextField txtViTri = new JTextField(viTri, 15);

                JButton btnCapNhat = new JButton("Cập nhật");
                JButton btnHuy = new JButton("Hủy");

                dialog.add(lblMaNV);
                dialog.add(txtMaNV);
                dialog.add(lblHoTen);
                dialog.add(txtHoTen);
                dialog.add(lblNgaySinh);
                dialog.add(txtNgaySinh);
                dialog.add(lblGioiTinh);
                dialog.add(cbGioiTinh);
                dialog.add(lblSDT);
                dialog.add(txtSDT);
                dialog.add(lblViTri);
                dialog.add(txtViTri);
                dialog.add(btnCapNhat);
                dialog.add(btnHuy);

                // Sự kiện cập nhật thông tin
                btnCapNhat.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newHoTen = txtHoTen.getText().trim();
                        String newNgaySinh = txtNgaySinh.getText().trim();
                        String newGioiTinh = cbGioiTinh.getSelectedItem().toString();
                        String newSDT = txtSDT.getText().trim();
                        String newViTri = txtViTri.getText().trim();

                        if (newHoTen.isEmpty() || newNgaySinh.isEmpty() || newSDT.isEmpty() || newViTri.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Cập nhật dữ liệu trong bảng
                        model.setValueAt(newHoTen, selectedRow, 1);
                        model.setValueAt(newNgaySinh, selectedRow, 2);
                        model.setValueAt(newGioiTinh, selectedRow, 3);
                        model.setValueAt(newSDT, selectedRow, 4);
                        model.setValueAt(newViTri, selectedRow, 5);

                        // Cập nhật vào file dữ liệu
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/data/nhanvien.txt"), "UTF-8"));
                            StringBuilder newContent = new StringBuilder();
                            String line;
                            
                            while ((line = reader.readLine()) != null) {
                                String[] data = line.split(",");
                                if (data[0].trim().equals(maNV)) {
                                    // Ghi dữ liệu mới thay vì dòng cũ
                                    newContent.append(maNV).append(",").append(newHoTen).append(",").append(newNgaySinh).append(",")
                                            .append(newGioiTinh).append(",").append(newSDT).append(",").append(newViTri).append("\n");
                                } else {
                                    newContent.append(line).append("\n");
                                }
                            }
                            reader.close();

                            // Ghi lại file sau khi cập nhật
                            BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/nhanvien.txt"));
                            writer.write(newContent.toString());
                            writer.close();

                            JOptionPane.showMessageDialog(dialog, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                        dialog.dispose();
                    }
                });

                // Sự kiện hủy sửa
                btnHuy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn chắc chắn muốn hủy?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            dialog.dispose();
                        }
                    }
                });

                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });


        JButton btnIn = new JButton("In");
        btnIn.setForeground(new Color(255, 255, 255));
        btnIn.setBackground(new Color(0, 64, 128));
        panelToolBar.add(btnIn);
        btnIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tableNhanVien.getModel();
                int rowCount = model.getRowCount();
                int columnCount = model.getColumnCount();

                if (rowCount == 0) {
                    JOptionPane.showMessageDialog(null, "Không có dữ liệu để in!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Đặt tên file
                String filePath = "src/data/employee_list.csv";

                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
                    // Thêm BOM để Excel nhận diện UTF-8 (nếu mở file bằng Excel)
                    writer.write("\uFEFF");

                    // Ghi tiêu đề cột
                    for (int i = 0; i < columnCount; i++) {
                        writer.write(model.getColumnName(i) + (i < columnCount - 1 ? "," : "\n"));
                    }

                    // Ghi dữ liệu từ bảng
                    for (int i = 0; i < rowCount; i++) {
                        for (int j = 0; j < columnCount; j++) {
                            writer.write(model.getValueAt(i, j).toString() + (j < columnCount - 1 ? "," : "\n"));
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Xuất dữ liệu thành công!\nFile lưu tại: " + filePath, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Lỗi khi xuất file!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Panel tìm kiếm (phía trên, bên phải)
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        panelSearch.add(lblTimKiem);

        txtTimKiem = new JTextField();
        panelSearch.add(txtTimKiem);
        txtTimKiem.setColumns(15);
        


        // Thêm panelSearch vào ngay dưới thanh công cụ
        add(panelSearch, BorderLayout.SOUTH);

        //Bảng hiển thị nhân viên (center)
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder("Danh sách nhân viên"));
        add(scrollPane, BorderLayout.CENTER);

        // Tạo model cho bảng với tiêu đề cột
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "Mã NV", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Vị trí"
            }
        );
        // Đọc dữ liệu từ file employee.txt
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream("src/data/nhanvien.txt"), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Giả sử dữ liệu được phân cách bởi dấu phẩy
                String[] rowData = line.split(",");
                // Loại bỏ khoảng trắng dư nếu cần
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = rowData[i].trim();
                }
                model.addRow(rowData);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        tableNhanVien = new JTable(model);
        scrollPane.setViewportView(tableNhanVien);

        // Tùy chỉnh thêm cho bảng nếu muốn...
        // Ví dụ: tableNhanVien.getColumnModel().getColumn(1).setPreferredWidth(80);
    }
    
    protected void searchEmployee(String keyword) {
		// TODO Auto-generated method stub
		
	}

	protected void loadData() {
		// TODO Auto-generated method stub
		
	}

	// Getter/Setter nếu cần
    public JTable getTableNhanVien() {
        return tableNhanVien;
    }

    public JTextField getTxtTimKiem() {
        return txtTimKiem;
    }
}
