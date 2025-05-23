package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import DAO.DAO_ChuyenTau;
import DAO.DAO_Ga;
import DAO.DAO_Tau;
import entity.TauEntity;
import UI.Tau;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

public class Menu extends JFrame {
	private JPanel sidebar;
	private JPanel mainContent; // Panel chứa các màn hình (cards)
	private CardLayout cardLayout; // Quản lý chuyển màn hình
	// Map chứa các nút điều hướng để dễ thay đổi trạng thái
	private Map<String, JButton> navButtons = new HashMap<>();
	private String userRole;
	private String username;

	public Menu() {
		super("Quản lý vé tàu lửa");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		// Sử dụng GridBagLayout cho frame chính
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		// ======================= SIDEBAR PANEL =======================
		sidebar = new JPanel(new GridBagLayout());
		sidebar.setPreferredSize(new Dimension(180, 0));
		sidebar.setBackground(new Color(70, 130, 180));
		GridBagConstraints sgbc = new GridBagConstraints();
		sgbc.gridx = 0;
		sgbc.insets = new Insets(10, 10, 10, 10);
		sgbc.fill = GridBagConstraints.HORIZONTAL;
		sgbc.weightx = 1.0;

		// Admin Panel với Icon (trong sidebar)
		JPanel adminPanel = new JPanel(new GridBagLayout());
		adminPanel.setBackground(new Color(70, 130, 180));
		GridBagConstraints agbc = new GridBagConstraints();
		agbc.gridx = 0;
		agbc.fill = GridBagConstraints.HORIZONTAL;
		agbc.insets = new Insets(5, 5, 5, 5);

		// Icon
		ImageIcon icon = new ImageIcon("src/images/profile.png"); // Đường dẫn tới ảnh của bạn
		Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		JLabel lblIcon = new JLabel(new ImageIcon(img));
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		agbc.gridy = 0;
		adminPanel.add(lblIcon, agbc);

		// Label Admin
		JLabel lblAdmin = new JLabel("Admin", SwingConstants.CENTER);
		lblAdmin.setForeground(Color.WHITE);
		lblAdmin.setFont(new Font("Roboto", Font.BOLD, 14));
		agbc.gridy = 1;
		adminPanel.add(lblAdmin, agbc);

		// Label Welcome
		JLabel lblWelcome = new JLabel("Chào mừng bạn trở lại", SwingConstants.CENTER);
		lblWelcome.setForeground(Color.WHITE);
		lblWelcome.setFont(new Font("Roboto", Font.PLAIN, 12));
		agbc.gridy = 2;
		adminPanel.add(lblWelcome, agbc);

		// Thêm adminPanel vào sidebar
		sgbc.gridy = 0;
		sidebar.add(adminPanel, sgbc);

		// ======================= MAIN CONTENT VỚI CARDLAYOUT =======================
		cardLayout = new CardLayout();
		mainContent = new JPanel(cardLayout);

		// Màn hình "Trang chủ"
		JPanel homePanel = createHomePanel();
		mainContent.add(homePanel, "TrangChu");

		// Màn hình "Quản lý nhân viên"
		NhanVien NhanVienPanel = new NhanVien();
		mainContent.add(NhanVienPanel, "NhanVien");

		// Màn hình "Chuyến tàu"
		Tau tauPanel = new Tau();
		mainContent.add(tauPanel, "Tau");

		// Màn hình "Hoa don"
		HoaDon hoaDonPanel = new HoaDon();
		mainContent.add(hoaDonPanel, "HoaDon");
		ThongKe thongKePanel = new ThongKe();
		mainContent.add(thongKePanel, "ThongKe");

		// Màn hình "Vé tàu"
		JPanel vePanel = new JPanel(new BorderLayout());

		// Form đặt vé lên toàn bộ vùng chính (khởi tạo với 7 tham số)
		vePanel.add(new VeTau.DatVePanel(null, // maTau chưa chọn
				null, // gioDi chưa có
				null, // gioDen chưa có
				null, // ngayDi chưa có
				"", // cho (chuỗi trống)
				1, // soLuongVe mặc định
				"Một chiều" // loaiVe mặc định
		), BorderLayout.CENTER);

		mainContent.add(vePanel, "VeTau");

		// 2. Định nghĩa font dùng chung cho popup menu
		Font menuFont = new Font("Roboto", Font.BOLD, 14);

		// 4. Các nút điều hướng bên sidebar
		String[] buttonNames = { "Trang chủ", "Vé tàu", "Tàu", "Hóa đơn", "Nhân viên", "Thống kê", "Đăng xuất" };
		String[] iconPaths = { "src/images/home.png", "src/images/train-ticket.png", "src/images/train.png",
				"src/images/receipt.png", "src/images/employee.png", "src/images/statistics.png",
				"src/images/logout.png" };
		Color[] buttonColors = { new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217) };

		for (int i = 0; i < buttonNames.length; i++) {
			final String btnName = buttonNames[i];
			JButton button = new JButton(btnName);

			// kích thước cố định
			int btnW = 100, btnH = 40;
			button.setPreferredSize(new Dimension(btnW, btnH));
			button.setMinimumSize(new Dimension(btnW, btnH));
			button.setMaximumSize(new Dimension(btnW, btnH));

			// icon
			ImageIcon iconButt = new ImageIcon(iconPaths[i]);
			Image imgButt = iconButt.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(imgButt));

			// chỉ hiển thị icon, ẩn text
			button.setText("");
			button.setHorizontalAlignment(SwingConstants.CENTER);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			button.setMargin(new Insets(5, 5, 5, 5));

			// disable "Trang chủ" ban đầu
			if (btnName.equals("Trang chủ")) {
				button.setEnabled(false);
			}

			button.setFont(new Font("Roboto", Font.BOLD, 13));
			button.setBackground(buttonColors[i]);

			// hover animation (giữ nguyên như bạn đã có, hoặc điều chỉnh duration)
			BiConsumer<JButton, Boolean> animateHover = (btn, entering) -> {
				final int startMargin = entering ? 5 : 15;
				final int endMargin = entering ? 15 : 5;
				final int startGap = entering ? 0 : 10;
				final int endGap = entering ? 10 : 0;
				final int fps = 180;
				final int delay = 2000 / fps;
				final int durationMs = 40;
				final int steps = Math.max(1, durationMs / delay);

				Timer timer = new Timer(delay, null);
				timer.addActionListener(new ActionListener() {
					int step = 0;

					@Override
					public void actionPerformed(ActionEvent e) {
						step++;
						float t = step / (float) steps;
						int marginLeft = startMargin + Math.round((endMargin - startMargin) * t);
						int gap = startGap + Math.round((endGap - startGap) * t);
						btn.setMargin(new Insets(5, marginLeft, 5, 5));
						btn.setIconTextGap(gap);
						if (step >= steps) {
							timer.stop();
							if (!entering) {
								btn.setText("");
								btn.setHorizontalAlignment(SwingConstants.CENTER);
								btn.setHorizontalTextPosition(SwingConstants.CENTER);
							}
						}
					}
				});

				if (entering) {
					btn.setText(btnName);
					btn.setHorizontalAlignment(SwingConstants.LEFT);
					btn.setHorizontalTextPosition(SwingConstants.RIGHT);
				}
				timer.start();
			};
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					animateHover.accept(button, true);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					animateHover.accept(button, false);
				}
			});

			sgbc.ipadx = 60;
			sgbc.ipady = 10;
			sgbc.gridy = i + 1; // đặt nút từ dòng thứ 1 trở đi
			sidebar.add(button, sgbc);

			sgbc.ipadx = 0;
			sgbc.ipady = 0;

			// Lưu button vào map
			navButtons.put(btnName, button);

			if (btnName.equals("Đăng xuất")) {
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
						new DangNhap().setVisible(true);
					}
				});
			}

			// Xử lý click
			button.addActionListener(e -> {
				// 1) Show đúng card
				switch (btnName) {
				case "Trang chủ":
					cardLayout.show(mainContent, "TrangChu");
					break;
				case "Tàu":
					cardLayout.show(mainContent, "Tau");
					break;
				case "Hóa đơn":
					cardLayout.show(mainContent, "HoaDon");
					break;
				case "Nhân viên":
					cardLayout.show(mainContent, "NhanVien");
					break;
				case "Vé tàu":
					cardLayout.show(mainContent, "VeTau");
					break;
				case "Thống kê":
					cardLayout.show(mainContent, "ThongKe");
					break;
				case "Đăng xuất":
					// TODO: xử lý logout tại đây
					return;
				}
				// 2) Update navButtons
				updateNavButtons(btnName);
			});

			// Thêm sidebar vào frame: cột 0, chiếm toàn bộ chiều cao (gridheight = 2)
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 2;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 0; // Sidebar cố định kích thước theo chiều ngang
			gbc.weighty = 1.0;
			add(sidebar, gbc);

			// Thêm mainContent vào frame: cột 1, chiếm toàn bộ chiều cao
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.gridheight = 2;
			gbc.weightx = 1.0;
			add(mainContent, gbc);
		}
	}

	/**
	 * Loai ve
	 */
	/** Enable tất cả nút, sau đó disable đúng nút activeKey */
	private void updateNavButtons(String activeKey) {
		for (Map.Entry<String, JButton> e : navButtons.entrySet()) {
			e.getValue().setEnabled(!e.getKey().equals(activeKey));
		}
	}

	/**
	 * Phương thức tạo màn hình "Trang chủ"
	 */
	private JPanel createHomePanel() {
		// Tạo panel chính với GridBagLayout
		JPanel homePanel = new JPanel();
		homePanel.setLayout(new GridBagLayout());
		homePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Tạo các panel con
		JPanel topLeftPanel = createTopLeftPanel(); // Panel chứa thông tin hành trình
		JPanel topRightPanel = createTopRightPanel(); // Panel chứa thông tin nhân viên
		JPanel Bottom = createBottom();
		// Cấu hình GridBagConstraints
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5); // Thêm khoảng cách giữa các panel

		// Thêm panel trên bên trái
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1; // Trải rộng 1 cột
		gbc.gridheight = 1;
		gbc.weightx = 0.3; // 50% không gian ngang
		gbc.weighty = 0.2; // 40% không gian dọc
		homePanel.add(topLeftPanel, gbc);

		// Thêm panel trên bên phải
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1; // Trải rộng 1 cột
		gbc.gridheight = 1;
		gbc.weightx = 0.5; // 50% không gian ngang
		gbc.weighty = 0.2; // 40% không gian dọc
		homePanel.add(topRightPanel, gbc);

		// Thêm panel dưới - chiếm toàn bộ chiều rộng
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2; // Trải rộng 2 cột
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.8; // 60% không gian dọc

		// Panel dưới (GIF), chiếm cả 2 cột
		JPanel bottomPanel = createBottom();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.8;
		homePanel.add(bottomPanel, gbc);

		return homePanel;
	}

	private JPanel createBottom() {
		// Load GIF
		ImageIcon gifIcon = new ImageIcon("src/images/background.gif");
		Image gifImage = gifIcon.getImage();

		// Custom panel để vẽ GIF full size
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Vẽ GIF, scale để vừa kích thước panel
				g.drawImage(gifImage, 0, 0, getWidth(), getHeight(), // vẽ full panel
						this);
			}
		};
		panel.setLayout(new BorderLayout());
		return panel;
	}

	/**
	 * Phương thức tạo panel trên bên phải với thông tin nhân viên
	 */
	private JPanel createTopRightPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		// Tạo font lớn hơn
		Font largeFont = new Font("Roboto", Font.BOLD, 14);

		// Đọc file employee.txt
		Properties employeeProps = new Properties();
		try (InputStreamReader fis = new InputStreamReader(new FileInputStream("src/data/employee.txt"), "UTF-8")) {
			employeeProps.load(fis);
		} catch (IOException ex) {
			ex.printStackTrace();
			// Tạo dữ liệu mẫu nếu không đọc được file
			employeeProps.setProperty("maNV", "NV001");
			employeeProps.setProperty("hoTen", "Nguyễn Văn A");
			employeeProps.setProperty("ngaySinh", "01/01/1990");
			employeeProps.setProperty("gioiTinh", "Nam");
			employeeProps.setProperty("email", "nguyenvana@example.com");
			employeeProps.setProperty("sdt", "0123456789");
			employeeProps.setProperty("chucVu", "Nhân viên bán vé");
		}

		// Employee Info Panel
		JPanel infoPanel = new JPanel(new GridBagLayout());

		GridBagConstraints igbc = new GridBagConstraints();
		igbc.insets = new Insets(5, 5, 5, 5);
		igbc.fill = GridBagConstraints.BOTH;
		igbc.weightx = 1.0;
		igbc.weighty = 1.0;

		String[] labels = { "Mã nhân viên", "Họ tên", "Ngày sinh", "Giới tính", "Email", "SĐT", "Chức vụ" };
		String[] keys = { "maNV", "hoTen", "ngaySinh", "gioiTinh", "email", "sdt", "chucVu" };

		for (int i = 0; i < labels.length; i++) {
			igbc.gridx = 0;
			igbc.gridy = i;
			JLabel labelComponent = new JLabel(labels[i] + ":");
			labelComponent.setFont(largeFont);
			infoPanel.add(labelComponent, igbc);

			igbc.gridx = 1;
			String info = employeeProps.getProperty(keys[i], "N/A");
			JLabel valueComponent = new JLabel(info);
			valueComponent.setFont(largeFont);
			infoPanel.add(valueComponent, igbc);
		}

		// Image Panel: hiển thị ảnh nhân viên
		JPanel imagePanel = new JPanel(new BorderLayout());

		// Tạo một label hình ảnh mặc định trong trường hợp không tìm thấy file ảnh
		JLabel imageLabel = new JLabel("Không tìm thấy ảnh", SwingConstants.CENTER);
		imageLabel.setFont(largeFont);
		imageLabel.setPreferredSize(new Dimension(170, 260));

		try {
			ImageIcon icon = new ImageIcon("src/images/profilenhanvien.jpg");
			Image scaledImage = icon.getImage().getScaledInstance(170, 260, Image.SCALE_SMOOTH);
			imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}

		imagePanel.add(imageLabel, BorderLayout.CENTER);

		// Thêm các panel vào panel chính
		GridBagConstraints mgbc = new GridBagConstraints();
		mgbc.fill = GridBagConstraints.BOTH;
		mgbc.insets = new Insets(5, 5, 5, 5);

		// Thêm infoPanel (chiếm khoảng 70% chiều ngang)
		mgbc.gridx = 0;
		mgbc.gridy = 0;
		mgbc.weightx = 0.5;
		mgbc.weighty = 1.0;
		panel.add(infoPanel, mgbc);

		// Thêm imagePanel (chiếm khoảng 30% chiều ngang)
		mgbc.gridx = 1;
		mgbc.gridy = 0;
		mgbc.weightx = 0.2;
		mgbc.weighty = 1.0;
		panel.add(imagePanel, mgbc);

		return panel;
	}

	/**
	 * Phương thức tạo panel trên bên trái với thông tin hành trình
	 */
	// Đảm bảo bạn đã thêm phương thức getGaMap() trong DAO_Ga
	// public static Map<String, String> getGaMap() { ... }

	private JPanel createTopLeftPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		Font largeFont = new Font("Roboto", Font.PLAIN, 14);
		Font buttonFont = new Font("Roboto", Font.BOLD, 14);

		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

		JPanel infoHeaderPanel = new JPanel(new BorderLayout());
		infoHeaderPanel.setBackground(new Color(70, 130, 180));
		JLabel infoHeaderLabel = new JLabel(" Thông tin hành trình", JLabel.LEFT);
		infoHeaderLabel.setFont(largeFont);
		infoHeaderLabel.setForeground(Color.WHITE);
		infoHeaderPanel.add(infoHeaderLabel, BorderLayout.CENTER);

		JPanel infoContentPanel = new JPanel(new GridBagLayout());
		infoContentPanel.setBackground(Color.WHITE);

		GridBagConstraints ip = new GridBagConstraints();
		ip.insets = new Insets(5, 5, 5, 5);
		ip.fill = GridBagConstraints.HORIZONTAL;
		ip.gridx = 0;
		ip.weightx = 1.0;

		Map<String, String> gaMap = DAO_Ga.getGaMap();
		if (gaMap == null || gaMap.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Không thể tải danh sách ga từ cơ sở dữ liệu!");
			return new JPanel();
		}

		ip.gridy = 0;
		JLabel lblGaDi = new JLabel("Ga đi");
		lblGaDi.setFont(largeFont);
		infoContentPanel.add(lblGaDi, ip);

		ip.gridy = 1;
		JComboBox<String> comboGaDi = new JComboBox<>();
		comboGaDi.setFont(largeFont);
		for (String tenGa : gaMap.keySet()) {
			comboGaDi.addItem(tenGa);
		}
		infoContentPanel.add(comboGaDi, ip);

		ip.gridy = 2;
		JLabel lblGaDen = new JLabel("Ga đến");
		lblGaDen.setFont(largeFont);
		infoContentPanel.add(lblGaDen, ip);

		ip.gridy = 3;
		JComboBox<String> comboGaDen = new JComboBox<>();
		comboGaDen.setFont(largeFont);
		for (String tenGa : gaMap.keySet()) {
			comboGaDen.addItem(tenGa);
		}
		infoContentPanel.add(comboGaDen, ip);

		ip.gridy = 4;
		JPanel pRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pRadio.setBackground(Color.WHITE);
		JRadioButton rbOne = new JRadioButton("Một chiều", true);
		JRadioButton rbReturn = new JRadioButton("Khứ hồi");
		rbOne.setFont(largeFont);
		rbReturn.setFont(largeFont);
		rbOne.setBackground(Color.WHITE);
		rbReturn.setBackground(Color.WHITE);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbOne);
		bg.add(rbReturn);
		pRadio.add(rbOne);
		pRadio.add(Box.createHorizontalStrut(10));
		pRadio.add(rbReturn);
		infoContentPanel.add(pRadio, ip);

		ip.gridy = 6;
		JLabel lblNgayDi = new JLabel("Ngày đi");
		lblNgayDi.setFont(largeFont);
		infoContentPanel.add(lblNgayDi, ip);

		ip.gridy = 7;
		JDateChooser dateChooserNgayDi = new JDateChooser();
		dateChooserNgayDi.setFont(largeFont);
		dateChooserNgayDi.setDateFormatString("dd/MM/yyyy");
		infoContentPanel.add(dateChooserNgayDi, ip);

		ip.gridy = 8;
		JLabel lblNgayVe = new JLabel("Ngày về");
		lblNgayVe.setFont(largeFont);
		infoContentPanel.add(lblNgayVe, ip);

		ip.gridy = 9;
		JDateChooser dateChooserNgayVe = new JDateChooser();
		dateChooserNgayVe.setFont(largeFont);
		dateChooserNgayVe.setDateFormatString("dd/MM/yyyy");
		infoContentPanel.add(dateChooserNgayVe, ip);

		// Thiết lập ẩn mặc định khi chọn "Một chiều"
		lblNgayVe.setVisible(false);
		dateChooserNgayVe.setVisible(false);

		// Xử lý sự kiện RadioButton
		rbOne.addActionListener(e -> {
			lblNgayVe.setVisible(false);
			dateChooserNgayVe.setVisible(false);
			infoContentPanel.revalidate();
			infoContentPanel.repaint();
		});

		rbReturn.addActionListener(e -> {
			lblNgayVe.setVisible(true);
			dateChooserNgayVe.setVisible(true);
			infoContentPanel.revalidate();
			infoContentPanel.repaint();
		});

		ip.gridy = 10;
		JButton btnTim = new JButton("Tìm kiếm");
		btnTim.setFont(buttonFont);
		btnTim.setBackground(new Color(70, 130, 180));
		btnTim.setForeground(Color.WHITE);
		infoContentPanel.add(btnTim, ip);
		btnTim.setOpaque(true);
		btnTim.setContentAreaFilled(true);
		btnTim.setBorderPainted(false);

		btnTim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tenGaDi = (String) comboGaDi.getSelectedItem();
				String tenGaDen = (String) comboGaDen.getSelectedItem();
				String maGaDi = gaMap.get(tenGaDi);
				String maGaDen = gaMap.get(tenGaDen);
				java.util.Date ngayDi = dateChooserNgayDi.getDate();
				java.util.Date ngayVe = dateChooserNgayVe.getDate();

				if (maGaDi == null || maGaDen == null || ngayDi == null || ngayVe == null) {
					JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin.");
					return;
				}

				List<TauEntity> danhSach = DAO_Tau.layDanhSachChuyenTau(maGaDi, maGaDen, ngayDi);

				if (!danhSach.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Tìm thấy " + danhSach.size() + " chuyến tàu!");

					// Tạo panel Tau mới với dữ liệu thực tế
					Tau tauPanel = new Tau(danhSach);

					// Xóa panel cũ nếu cần, hoặc ghi đè
					mainContent.add(tauPanel, "Tau");
					cardLayout.show(mainContent, "Tau");
				} else {
					JOptionPane.showMessageDialog(null, "Không tìm thấy chuyến tàu phù hợp.");
				}

			}
		});

		infoPanel.add(infoHeaderPanel, BorderLayout.NORTH);
		infoPanel.add(infoContentPanel, BorderLayout.CENTER);

		GridBagConstraints mainGbc = new GridBagConstraints();
		mainGbc.fill = GridBagConstraints.BOTH;
		mainGbc.insets = new Insets(5, 5, 5, 5);
		mainGbc.gridx = 0;
		mainGbc.gridy = 0;
		mainGbc.weightx = 0.5;
		mainGbc.weighty = 1.0;
		panel.add(infoPanel, mainGbc);

		return panel;
	}

	/**
	 * Phương thức tạo card hiển thị thông tin hành trình
	 */
	private JPanel createRouteCard(String from, String to, String time, String trainCode, String price, Font routeFont,
			Font timeFont, Font priceFont, Font buttonFont) {
		JPanel card = new JPanel(new BorderLayout(10, 5));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)));

		// Panel chứa thông tin hành trình
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBackground(Color.WHITE);

		// Panel chứa ga đi, ga đến
		JPanel stationsPanel = new JPanel(new BorderLayout());
		stationsPanel.setBackground(Color.WHITE);

		JLabel fromLabel = new JLabel(from);
		fromLabel.setFont(routeFont);

		JLabel toLabel = new JLabel(to);
		toLabel.setFont(routeFont);
		toLabel.setHorizontalAlignment(JLabel.RIGHT);

		stationsPanel.add(fromLabel, BorderLayout.WEST);
		stationsPanel.add(toLabel, BorderLayout.EAST);

		// Panel chứa thời gian và mã tàu
		JPanel timePanel = new JPanel(new BorderLayout());
		timePanel.setBackground(Color.WHITE);

		JPanel arrowPanel = new JPanel(new BorderLayout());
		arrowPanel.setBackground(Color.WHITE);

		JLabel timeLabel = new JLabel(time);
		timeLabel.setFont(timeFont);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);

		JLabel arrowLabel = new JLabel(" → ");
		arrowLabel.setFont(timeFont);
		arrowLabel.setHorizontalAlignment(JLabel.CENTER);

		JLabel trainLabel = new JLabel(trainCode);
		trainLabel.setFont(timeFont);
		trainLabel.setHorizontalAlignment(JLabel.CENTER);

		arrowPanel.add(timeLabel, BorderLayout.NORTH);
		arrowPanel.add(arrowLabel, BorderLayout.CENTER);
		arrowPanel.add(trainLabel, BorderLayout.SOUTH);

		timePanel.add(arrowPanel, BorderLayout.CENTER);

		// Panel chứa giá và nút xem
		JPanel pricePanel = new JPanel(new BorderLayout(5, 5));
		pricePanel.setBackground(Color.WHITE);

		JLabel priceLabel = new JLabel(price);
		priceLabel.setFont(priceFont);
		priceLabel.setForeground(new Color(0, 175, 200)); // Màu xanh dương nhạt

		JButton viewButton = new JButton("Xem");
		viewButton.setFont(buttonFont);
		viewButton.setBackground(new Color(70, 130, 180)); // Màu cam
		viewButton.setForeground(Color.WHITE);
		viewButton.setFocusPainted(false);
		viewButton.setBorderPainted(false);

		pricePanel.add(priceLabel, BorderLayout.NORTH);
		pricePanel.add(viewButton, BorderLayout.SOUTH);

		// Thêm các panel vào card
		infoPanel.add(stationsPanel, BorderLayout.NORTH);
		infoPanel.add(timePanel, BorderLayout.CENTER);

		card.add(infoPanel, BorderLayout.CENTER);
		card.add(pricePanel, BorderLayout.EAST);

		return card;
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public JPanel getMainContentPanel() {
		return mainContent;
	}

}