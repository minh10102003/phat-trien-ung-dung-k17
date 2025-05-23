package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import DAO.DAO_HanhKhach;
import DAO.DAO_LoaiVe;
import DAO.DAO_Ve;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connectDB.ConnectDB;
import entity.LoaiVe;

import java.util.List;


public class VeTau {

	/**
	 * Panel hiển thị danh sách các vé đã có trong CSDL
	 */
	public static class DanhSachVePanel extends JPanel {
		private DefaultTableModel model;
		private JTable table;

		public DanhSachVePanel() {
			setLayout(new BorderLayout());
			setBorder(BorderFactory.createTitledBorder("Danh sách vé có sẵn"));

			String[] columns = { "Mã vé", "Mã tàu", "Giờ đi", "Ngày đi", "Loại vé", "Chỗ", "Mã loại vé",
					"Số lượng vé" };
			model = new DefaultTableModel(columns, 0);
			table = new JTable(model);
			table.setRowHeight(28);

			add(new JScrollPane(table), BorderLayout.CENTER);
			loadVeData();
		}

		public void loadVeData() {
			model.setRowCount(0);
			String sql = "SELECT maVe, maTau, gioDi, ngayDi, loaiVe, cho, maLoaiVe, soLuongVe FROM Ve";
			try (Connection conn = ConnectDB.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {

				SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm");
				SimpleDateFormat dateFmt = new SimpleDateFormat("dd/MM/yyyy");

				while (rs.next()) {
					String maVe = rs.getString("maVe");
					String maTau = rs.getString("maTau");
					Time gioDi = rs.getTime("gioDi");
					Date ngayDi = rs.getDate("ngayDi");
					String loaiVe = rs.getString("loaiVe");
					String cho = rs.getString("cho");
					String maLoaiVe = rs.getString("maLoaiVe");
					int soLuongVe = rs.getInt("soLuongVe");

					model.addRow(new Object[] { maVe, maTau, timeFmt.format(gioDi), dateFmt.format(ngayDi), loaiVe, cho,
							maLoaiVe, soLuongVe });
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu vé: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Panel để đặt vé mới: chọn chuyến, toa, ghế rồi nhập thông tin khách
	 */

	public static class DatVePanel extends JPanel {
		private DefaultTableModel tableModel;
		private JTable table;
		private JTextField tenKhachField, cccdField, sdtField;
		private JComboBox<LoaiVe> cboLoaiVe; // ← thêm combo
		private JButton btnLuu;
		private final String ngayDiStr;

		public DatVePanel(String maTau, String gioDiParam, String gioDenParam, String ngayDiParam, String cho,
				int soLuongVe, String loaiVe) {

			// 1) Xác định giờ đi / giờ đến / ngày đi
			String[] gio = getGioDiGioDenFromDatabase(maTau);
			String gioDi = (gioDiParam == null || "??:??".equals(gioDiParam)) ? gio[0] : gioDiParam;
			String gioDen = (gioDenParam == null || "??:??".equals(gioDenParam)) ? gio[1] : gioDenParam;
			String ngayDi = (ngayDiParam == null || ngayDiParam.isEmpty()) ? getNgayDiFromDatabase(maTau) : ngayDiParam;
			this.ngayDiStr = ngayDi;

			// 2) Layout chính
			setLayout(new BorderLayout(10, 10));
			setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

			add(createTablePanel(maTau, gioDi, gioDen, ngayDi, cho, soLuongVe), BorderLayout.CENTER);
			add(createCustomerPanel(), BorderLayout.SOUTH);
		}

		private JPanel createTablePanel(String maTau, String gioDi, String gioDen, String ngayDi, String cho,
				int soLuongVe) {
			String[] cols = { "Mã tàu", "Giờ đi", "Giờ đến", "Ngày đi", "Loại vé", "Chỗ", "Mã loại vé", "Số lượng vé" };
			tableModel = new DefaultTableModel(cols, 0);
			// để trống cột 4 (Loại vé) và cột 6 (Mã loại vé)
			tableModel.addRow(new Object[] { maTau, gioDi, gioDen, ngayDi, "", // sẽ cập nhật từ combo
					cho, "", // sẽ cập nhật từ combo
					soLuongVe });
			table = new JTable(tableModel);
			table.setRowHeight(28);

			JPanel p = new JPanel(new BorderLayout());
			p.setBorder(BorderFactory.createTitledBorder("Thông tin vé"));
			p.add(new JScrollPane(table), BorderLayout.CENTER);
			return p;
		}

		private JPanel createCustomerPanel() {
		    JPanel p = new JPanel(new GridLayout(5, 2, 10, 10));
		    p.setBorder(BorderFactory.createTitledBorder("Thông tin hành khách & loại vé"));

		    p.add(new JLabel("Họ và tên:"));
		    tenKhachField = new JTextField(); p.add(tenKhachField);

		    p.add(new JLabel("CCCD:"));
		    cccdField = new JTextField(); p.add(cccdField);

		    p.add(new JLabel("Số điện thoại:"));
		    sdtField = new JTextField(); p.add(sdtField);

		    // --- Combo chọn loại vé ---
		    p.add(new JLabel("Loại vé:"));
		    cboLoaiVe = new JComboBox<>();
		    try {
		        List<LoaiVe> list = DAO_LoaiVe.getAllLoaiVe();
		        for (LoaiVe lv : list) {
		            cboLoaiVe.addItem(lv);
		        }
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		    p.add(cboLoaiVe);

		    // Nút Lưu
		    p.add(new JLabel());
		    btnLuu = new JButton("Lưu thông tin");
		    btnLuu.addActionListener(e -> onSave());
		    p.add(btnLuu);

		    return p;
		}


		private void onSave() {
		    // 1) Validate
		    String ten  = tenKhachField.getText().trim();
		    String cccd = cccdField.getText().trim();
		    String sdt  = sdtField.getText().trim();
		    if (!ten.matches("([A-ZÀ-Ỵ][a-zà-ỹ]+\\s?)+")) {
		        JOptionPane.showMessageDialog(this, "Tên không hợp lệ");
		        return;
		    }
		    if (!sdt.matches("^0\\d{9}$")) {
		        JOptionPane.showMessageDialog(this, "SĐT không hợp lệ");
		        return;
		    }
		    if (ngayDiStr == null || ngayDiStr.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "Không xác định được Ngày đi");
		        return;
		    }

		    // 2) Parse ngày
		    Date ngayDi;
		    try {
		        ngayDi = new SimpleDateFormat("dd/MM/yyyy").parse(ngayDiStr);
		    } catch (ParseException ex) {
		        JOptionPane.showMessageDialog(this, "Định dạng Ngày đi không hợp lệ");
		        return;
		    }

		    // 3) Lấy loại vé đã chọn từ JComboBox
		    LoaiVe selected = (LoaiVe) cboLoaiVe.getSelectedItem();
		    String tenLoaiVe = selected.getTenLoaiVe();
		    String maLoaiVe  = selected.getMaLoaiVe();

		    // 4) Cập nhật lại bảng tạm (để hiển thị trước khi lưu)
		    tableModel.setValueAt(tenLoaiVe, 0, 4);
		    tableModel.setValueAt(maLoaiVe,  0, 6);

		    // 5) Đọc lại các trường khác trong bảng
		    String maTau = (String) tableModel.getValueAt(0, 0);
		    String gioDi = (String) tableModel.getValueAt(0, 1);
		    String cho   = (String) tableModel.getValueAt(0, 5);
		    int    sl    = (int)    tableModel.getValueAt(0, 7);

		    // 6) Gọi DAO để chèn vé vào CSDL
		    try {
		        String maVeMoi = DAO_Ve.bookTicket(
		            ten, cccd, sdt,
		            maTau, gioDi, ngayDi,
		            tenLoaiVe, cho, maLoaiVe,
		            sl
		        );

		        // 7) Mở form Hóa Đơn
		        JFrame f = new JFrame("Hóa Đơn Vé");
		        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        f.setContentPane(new HoaDon());
		        f.pack();
		        f.setLocationRelativeTo(this);
		        f.setVisible(true);

		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(this,
		            "Lỗi khi lưu vé: " + ex.getMessage(),
		            "Lỗi", JOptionPane.ERROR_MESSAGE
		        );
		    }
		}


		// Helper lấy Ngày đi và Giờ đi/Giờ đến từ DB (giữ nguyên logic cũ)
		private String getNgayDiFromDatabase(String maTau) {
			String sql = "SELECT TOP 1 ngayDi FROM ChuyenTau WHERE maTau = ?";
			try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTau);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Date d = rs.getDate("ngayDi");
						return new SimpleDateFormat("dd/MM/yyyy").format(d);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Nếu không tìm thấy vẫn trả về giá trị mặc định, ví dụ "??:??" hoặc null
			return "";
		}

		private String[] getGioDiGioDenFromDatabase(String maTau) {
			String[] result = { "??:??", "??:??" };
			String sql = "SELECT TOP 1 gioKhoiHanh, gioDen FROM ChuyenTau WHERE maTau = ?";
			try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, maTau);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Time gioDi = rs.getTime("gioKhoiHanh");
						Time gioDen = rs.getTime("gioDen");
						SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
						result[0] = (gioDi != null) ? fmt.format(gioDi) : "??:??";
						result[1] = (gioDen != null) ? fmt.format(gioDen) : "??:??";
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}

	}
}
