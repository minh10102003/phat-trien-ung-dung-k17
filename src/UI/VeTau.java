package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import DAO.DAO_Ga;
import DAO.DAO_Ve;
import DAO.DAO_Ve.BookingResult;
import connectDB.ConnectDB;
import entity.LoaiVe;

public class VeTau {

	/**
	 * Panel đặt vé: hiển thị tóm tắt vé + nhập info + nút Thanh toán
	 */
	public static class DatVePanel extends JPanel {
		private final TicketPaymentListener listener;
		private final String maNV, maTau, tenTau, ngayDiStr, defaultLoaiVe, maLoaiVe;
		private final int cho;
		private int soLuongVe;
		private final List<LoaiVe> listLoaiVe;
		private final DefaultTableModel tableModel;
		private final JTable table;
		private final JTextField tenKhachField = new JTextField();
		private final JTextField cccdField = new JTextField();
		private final JTextField sdtField = new JTextField();
		private final JTextField giaVeField = new JTextField();
		private final JButton btnLuu = new JButton("Thanh toán và lưu HĐ");

		public DatVePanel(String maNV, String maTau, String tenTau, String gioDiParam, String gioDenParam,
				String ngayDiParam, int cho, int soLuongVe, String loaiVe, String maLoaiVe, List<LoaiVe> listLoaiVe,
				TicketPaymentListener listener) {
			this.maNV = maNV;
			this.maTau = maTau;
			this.tenTau = tenTau;
			this.cho = cho;
			this.soLuongVe = soLuongVe;
			this.defaultLoaiVe = loaiVe;
			this.maLoaiVe = maLoaiVe;
			this.listLoaiVe = new ArrayList<>(listLoaiVe);
			this.listener = listener;

			String[] gio = DAO_Ve.getGioDiGioDen(maTau);
			String gioDi = (gioDiParam == null || gioDiParam.isEmpty() || "??:??".equals(gioDiParam)) ? gio[0]
					: gioDiParam;
			String gioDen = (gioDenParam == null || gioDenParam.isEmpty() || "??:??".equals(gioDenParam)) ? gio[1]
					: gioDenParam;
			this.ngayDiStr = (ngayDiParam == null || ngayDiParam.isEmpty()) ? DAO_Ve.getNgayDi(maTau) : ngayDiParam;

			setLayout(new BorderLayout(10, 10));
			setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

			// Table
			String[] cols = { "Tên tàu", "Giờ đi", "Giờ đến", "Loại vé", "Chỗ", "Số lượng vé" };
			tableModel = new DefaultTableModel(cols, 0) {
				@Override
				public boolean isCellEditable(int row, int col) {
					return col == 5;
				}

				@Override
				public Class<?> getColumnClass(int col) {
					return (col == 4 || col == 5) ? Integer.class : String.class;
				}
			};
			tableModel.addRow(new Object[] { tenTau, gioDi, gioDen, defaultLoaiVe, cho, soLuongVe });
			tableModel.addTableModelListener(e -> {
				if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
					this.soLuongVe = (Integer) tableModel.getValueAt(0, 5);
					recalcPrice();
				}
			});
			table = new JTable(tableModel);
			table.setRowHeight(28);
			JPanel pTab = new JPanel(new BorderLayout());
			pTab.setBorder(BorderFactory.createTitledBorder("Thông tin vé"));
			pTab.add(new JScrollPane(table), BorderLayout.CENTER);

			// Customer
			giaVeField.setEditable(false);
			JPanel pCus = new JPanel(new GridLayout(5, 2, 10, 10));
			pCus.setBorder(BorderFactory.createTitledBorder("Thông tin hành khách & giá vé"));
			pCus.add(new JLabel("Họ và tên:"));
			pCus.add(tenKhachField);
			pCus.add(new JLabel("CCCD:"));
			pCus.add(cccdField);
			pCus.add(new JLabel("SĐT:"));
			pCus.add(sdtField);
			pCus.add(new JLabel("Giá vé:"));
			pCus.add(giaVeField);
			pCus.add(new JLabel(""));
			pCus.add(btnLuu);
			recalcPrice();

			add(pTab, BorderLayout.CENTER);
			add(pCus, BorderLayout.SOUTH);

			btnLuu.addActionListener(e -> onSave());
		}

		private void recalcPrice() {
			double unitPrice = listLoaiVe.stream().filter(lv -> lv.getTenLoaiVe().equals(defaultLoaiVe))
					.mapToDouble(LoaiVe::getGiaTien).findFirst().orElse(0);
			giaVeField.setText(String.format("%.0f", unitPrice * soLuongVe));
		}

		private void onSave() {
			String ten = tenKhachField.getText().trim(), cccd = cccdField.getText().trim(),
					sdt = sdtField.getText().trim();
			if (ten.isEmpty() || cccd.isEmpty() || sdt.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khách.", "Thiếu dữ liệu",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			Date d;
			try {
				d = new SimpleDateFormat("dd/MM/yyyy").parse(ngayDiStr);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Định dạng Ngày đi không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String gioDi = tableModel.getValueAt(0, 1).toString(), gioDen = tableModel.getValueAt(0, 2).toString();
			String loai = tableModel.getValueAt(0, 3).toString();
			int choC = (Integer) tableModel.getValueAt(0, 4);
			int sl = (Integer) tableModel.getValueAt(0, 5);
			double tt = Double.parseDouble(giaVeField.getText().trim());
			// Dialog đặt vé + thanh toán
			JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Đặt vé & Thanh toán", true);
			DatVePanel dat = new DatVePanel(maNV, maTau, tenTau, gioDi, gioDen, ngayDiStr, choC, sl, loai, maLoaiVe,
					listLoaiVe, listener);
			dlg.setContentPane(dat);
			dlg.pack();
			dlg.setLocationRelativeTo(this);
			dlg.setVisible(true);
		}
	}

	/**
	 * Panel danh sách vé + in vé
	 */
	public static class DanhSachVePanel extends JPanel implements TicketPaymentListener {
		private final DefaultTableModel newVeModel;
		private final JTable newVeTable;
		private final DefaultTableModel allVeModel;
		private final JTable allVeTable;

		private final JTextField tfCCCD = new JTextField();
		private final JTextField tfHoTen = new JTextField();
		private final JTextField tfSDT = new JTextField();
		private final JTextField tfGaDi = new JTextField();
		private final JTextField tfGaDen = new JTextField();
		private final JTextField tfLoaiChuyen = new JTextField();
		private final JTextField tfNgayDi = new JTextField();
		private final JTextField tfNgayVe = new JTextField();

		public DanhSachVePanel() {
			setLayout(new BorderLayout(10, 10));
			// Vé mới
			newVeModel = new DefaultTableModel(new String[] { "Mã vé", "Tên tàu", "Giờ đi", "Ngày đi", "Loại vé",
					"Loại chuyến", "Chỗ", "SL vé", "Tổng tiền" }, 0);
			newVeTable = new JTable(newVeModel);
			newVeTable.setRowHeight(28);
			JScrollPane spNew = new JScrollPane(newVeTable);
			JButton btnPrint = new JButton("In vé");
			btnPrint.setFont(btnPrint.getFont().deriveFont(Font.BOLD, 16f));
			btnPrint.setPreferredSize(new Dimension(120, 40));
			btnPrint.addActionListener(e -> printTicket());

			JPanel pInfo = new JPanel();
			pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
			// ép pInfo rộng 350px (chiều ngang)
			pInfo.setPreferredSize(new Dimension(350, 0));
			JPanel pCus = new JPanel(new GridLayout(3, 2, 5, 5));
			pCus.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
			pCus.add(new JLabel("CCCD:"));
			pCus.add(tfCCCD);
			tfCCCD.setEditable(false);
			pCus.add(new JLabel("Họ tên:"));
			pCus.add(tfHoTen);
			tfHoTen.setEditable(false);
			pCus.add(new JLabel("SĐT:"));
			pCus.add(tfSDT);
			tfSDT.setEditable(false);
			JPanel pIt = new JPanel(new GridLayout(5, 2, 5, 5));
			pIt.setBorder(BorderFactory.createTitledBorder("Thông tin hành trình"));
			pIt.add(new JLabel("Ga đi:"));
			pIt.add(tfGaDi);
			tfGaDi.setEditable(false);
			pIt.add(new JLabel("Ga đến:"));
			pIt.add(tfGaDen);
			tfGaDen.setEditable(false);
			pIt.add(new JLabel("Loại chuyến:"));
			pIt.add(tfLoaiChuyen);
			tfLoaiChuyen.setEditable(false);
			pIt.add(new JLabel("Ngày đi:"));
			pIt.add(tfNgayDi);
			tfNgayDi.setEditable(false);
			pIt.add(new JLabel("Ngày về:"));
			pIt.add(tfNgayVe);
			tfNgayVe.setEditable(false);
			pInfo.add(pCus);
			pInfo.add(Box.createVerticalStrut(8));
			pInfo.add(pIt);
			pInfo.add(Box.createVerticalGlue());
			pInfo.add(btnPrint);

			JPanel pTop = new JPanel(new BorderLayout());
			pTop.setBorder(BorderFactory.createTitledBorder("Vé mới"));
			pTop.add(spNew, BorderLayout.CENTER);
			pTop.add(pInfo, BorderLayout.EAST);

			// Vé đã thanh toán
			allVeModel = new DefaultTableModel(new String[] { "Mã vé", "Mã tàu", "Giờ đi", "Ngày đi", "Loại vé", "Chỗ",
					"Mã loại vé", "Số lượng vé" }, 0);
			allVeTable = new JTable(allVeModel);
			allVeTable.setRowHeight(28);
			JButton btnRef = new JButton("Làm mới");
			btnRef.addActionListener(e -> loadVeData());
			JPanel pAll = new JPanel(new BorderLayout());
			pAll.setBorder(BorderFactory.createTitledBorder("Danh sách vé đã thanh toán"));
			JPanel topAll = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			topAll.add(btnRef);
			pAll.add(topAll, BorderLayout.NORTH);
			pAll.add(new JScrollPane(allVeTable), BorderLayout.CENTER);

			add(pTop, BorderLayout.NORTH);
			add(pAll, BorderLayout.CENTER);
			loadVeData();
		}

		@Override
		public void onTicketPaid(BookingResult br, String maHD, String tenTau, String maLoaiVe, int cho, int soLuongVe,
				double tongTien, String gaDi, // ở đây gaDi đã là **tên** ga, không phải mã
				String gaDen, // tương tự cho gaDen
				String loaiChuyen, String ngayVe) {
			// 1) Làm mới bảng vé mới đặt
			newVeModel.setRowCount(0);
			newVeModel.addRow(new Object[] { br.getMaVe(), tenTau, br.getGioDi(), br.getNgayDi(), br.getLoaiVeName(),
					loaiChuyen, cho, soLuongVe, String.format("%.0f", tongTien) });
			newVeTable.revalidate();
			newVeTable.repaint();
			if (newVeTable.getRowCount() > 0) {
				newVeTable.setRowSelectionInterval(0, 0);
			}

			// 2) Điền thông tin khách
			tfCCCD.setText(br.getCccd());
			tfHoTen.setText(br.getTenKhach());
			tfSDT.setText(br.getSdt());

			// 3) **Thay vì convert lại mã → tên**, ta đã truyền thẳng tên ga vào gaDi/gaDen
			tfGaDi.setText(gaDi);
			tfGaDen.setText(gaDen);

			// 4) Các thông tin còn lại
			tfLoaiChuyen.setText(loaiChuyen);
			tfNgayDi.setText(br.getNgayDi());
			tfNgayVe.setText(ngayVe);

			// 5) Tải lại toàn bộ dữ liệu vé
			loadVeData();
		}

		private void printTicket() {
			int row = newVeTable.getSelectedRow();
			if (row < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn vé để in.", "Chú ý", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 1) Lấy dữ liệu
			String maVe = newVeModel.getValueAt(row, 0).toString();
			String tenTau = newVeModel.getValueAt(row, 1).toString();
			String gioDi = newVeModel.getValueAt(row, 2).toString();
			String ngayDi = newVeModel.getValueAt(row, 3).toString();
			String loaiVe = newVeModel.getValueAt(row, 4).toString();
			String loaiChuyen = newVeModel.getValueAt(row, 5).toString();
			String cho = newVeModel.getValueAt(row, 6).toString();
			String slVe = newVeModel.getValueAt(row, 7).toString();
			String tongTien = newVeModel.getValueAt(row, 8).toString();

			// Chuỗi QR
			String qrText = String.format("Ticket:%s|Train:%s|Date:%s|Time:%s", maVe, tenTau, ngayDi, gioDi);

			// 2) Chọn nơi lưu file
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Chọn nơi lưu file PDF vé");
			chooser.setSelectedFile(new File("boardingpass_" + maVe + ".pdf"));
			if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
				return;
			File outFile = chooser.getSelectedFile();
			if (!outFile.getName().toLowerCase().endsWith(".pdf"))
				outFile = new File(outFile.getParent(), outFile.getName() + ".pdf");

			try (PDDocument doc = new PDDocument()) {
				PDPage page = new PDPage(PDRectangle.A4);
				doc.addPage(page);

				// Tạo QR code
				QRCodeWriter qrWriter = new QRCodeWriter();
				BitMatrix matrix = qrWriter.encode(qrText, BarcodeFormat.QR_CODE, 150, 150);
				BufferedImage qrImg = MatrixToImageWriter.toBufferedImage(matrix);
				PDImageXObject qrXObj = LosslessFactory.createFromImage(doc, qrImg);

				// Load font
				PDType0Font fontBold = PDType0Font.load(doc, new FileInputStream("src/fonts/DejaVuSans-Bold.ttf"),
						true);
				PDType0Font font = PDType0Font.load(doc, new FileInputStream("src/fonts/DejaVuSans.ttf"), true);

				try (PDPageContentStream cs = new PDPageContentStream(doc, page, AppendMode.OVERWRITE, true, true)) {
					float pageW = page.getMediaBox().getWidth();
					float pageH = page.getMediaBox().getHeight();

					// --- Khung header ---
					cs.setStrokingColor(Color.DARK_GRAY);
					cs.addRect(20, pageH - 200, pageW - 40, 180);
					cs.stroke();

					// Tiêu đề
					cs.beginText();
					cs.setFont(fontBold, 26);
					cs.newLineAtOffset((pageW - 40) / 2 - 80, pageH - 70);
					cs.showText("THẺ LÊN TÀU HỎA");
					cs.endText();

					// QR code
					cs.drawImage(qrXObj, pageW - 180, pageH - 355, 150, 150);

					// --- Khung chi tiết ---
					cs.setStrokingColor(Color.GRAY);
					cs.addRect(20, pageH - 500, pageW - 40, 280);
					cs.stroke();

					// Vẽ bảng 2 cột
					float leftX = 30, rightX = pageW / 2 + 10;
					float startY = pageH - 230;
					float leading = 20;

					cs.beginText();
					cs.setFont(fontBold, 12);
					cs.newLineAtOffset(leftX, startY);
					cs.showText("MÃ VÉ:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("GA ĐI:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("GA ĐẾN:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("NGÀY ĐI:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("GIỜ ĐI:");
					cs.endText();

					cs.beginText();
					cs.setFont(font, 12);
					cs.newLineAtOffset(rightX, startY);
					cs.showText(maVe);
					cs.newLineAtOffset(0, -leading);
					cs.showText(tfGaDi.getText().trim());
					cs.newLineAtOffset(0, -leading);
					cs.showText(tfGaDen.getText().trim());
					cs.newLineAtOffset(0, -leading);
					cs.showText(ngayDi);
					cs.newLineAtOffset(0, -leading);
					cs.showText(gioDi);
					cs.endText();

					// Bảng thứ hai (Loại vé, chuyến, chỗ...)
					float secondColY = startY - 5 * leading - 20;

					cs.beginText();
					cs.setFont(fontBold, 12);
					cs.newLineAtOffset(leftX, secondColY);
					cs.showText("LOẠI VÉ:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("LOẠI CHUYẾN:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("CHỖ:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("SL VÉ:");
					cs.newLineAtOffset(0, -leading);
					cs.showText("TỔNG TIỀN:");
					cs.endText();

					cs.beginText();
					cs.setFont(font, 12);
					cs.newLineAtOffset(rightX, secondColY);
					cs.showText(loaiVe);
					cs.newLineAtOffset(0, -leading);
					cs.showText(loaiChuyen);
					cs.newLineAtOffset(0, -leading);
					cs.showText(cho);
					cs.newLineAtOffset(0, -leading);
					cs.showText(slVe);
					cs.newLineAtOffset(0, -leading);
					cs.showText(String.format("%s VND", tongTien));
					cs.endText();
				}

				// Lưu file
				doc.save(outFile);
				JOptionPane.showMessageDialog(this, "Đã lưu vé boarding pass:\n" + outFile.getAbsolutePath(), "In vé",
						JOptionPane.INFORMATION_MESSAGE);

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi in vé:\n" + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		}

		private void loadVeData() {
			allVeModel.setRowCount(0);
			String sql = "SELECT maVe,maTau,gioDi,ngayDi,loaiVe,cho,maLoaiVe,soLuongVe FROM Ve";
			try (Connection conn = ConnectDB.getConnection();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				while (rs.next()) {
					allVeModel.addRow(new Object[] { rs.getString("maVe"), rs.getString("maTau"),
							rs.getTime("gioDi") != null ? tf.format(rs.getTime("gioDi")) : "",
							rs.getDate("ngayDi") != null ? df.format(rs.getDate("ngayDi")) : "", rs.getString("loaiVe"),
							rs.getString("cho"), rs.getString("maLoaiVe"), rs.getInt("soLuongVe") });
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách vé: " + ex.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}
}
