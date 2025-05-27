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
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.Collections;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import entity.Entity_NhanVien;
import entity.LoaiVe;
import DAO.DAO_LoaiVe;
import DAO.DAO_NhanVien;

import javax.swing.JComboBox;
import java.util.List;
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
import util.Session;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class Menu extends JFrame {
	private JPanel sidebar;
	private JPanel mainContent; // Panel chứa các màn hình (cards)
	private CardLayout cardLayout; // Quản lý chuyển màn hình
	// Map chứa các nút điều hướng để dễ thay đổi trạng thái
	private Map<String, JButton> navButtons = new HashMap<>();
//	private String selectedTicketType = "Một chiều";
	private String userRole;
	private String username;

	private final Entity_NhanVien currentUser;

	private List<LoaiVe> loaiVeList = new ArrayList<>();
	private String selectedTicketType;
	private VeTau.DanhSachVePanel danhSachVePanel;
	private Tau tauPanel;

	public Menu() {
		super("Quản lý vé tàu lửa");

		System.out.println("Session username = " + util.Session.currentUsername);

		// 1) Lấy username từ Session, rồi Entity_NhanVien
		String username = Session.currentUsername;
		Entity_NhanVien u = DAO_NhanVien.getByUsername(username);
		if (u == null) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy hồ sơ nhân viên: " + username, "Lỗi hệ thống",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		currentUser = u;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setSize(1280, 720);
		setLocationRelativeTo(null);
		// Sử dụng GridBagLayout cho frame chính
		setLayout(new GridBagLayout());

		// Tải danh sách loại vé từ CSDL
		loaiVeList = DAO_LoaiVe.getAllLoaiVe();
		if (loaiVeList != null && !loaiVeList.isEmpty()) {
			selectedTicketType = loaiVeList.get(0).getTenLoaiVe();
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets   = new Insets(5,5,5,5);
		gbc.fill     = GridBagConstraints.HORIZONTAL;
		gbc.gridx    = 0;
		gbc.weightx  = 1.0;
		gbc.weighty  = 1.0;

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

		// Tên NV (currentUser.getTenNV())
		JLabel lblAdmin = new JLabel(currentUser.getTenNV(), SwingConstants.CENTER);
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
		tauPanel = new Tau(Collections.emptyList(), // danh sách chuyến đi (rỗng)
				"", // originGa (ga đi) — để trống ban đầu
				"", // destGa (ga đến) — để trống ban đầu
				selectedTicketType, // defaultLoaiVe
				loaiVeList, // listLoaiVe
				"", // ngayDiStr (ngày đi rỗng)
				"Một chiều", // loaiChuyen
				"", // ngayVeStr (ngày về rỗng)
				danhSachVePanel // listener để callback lên DanhSachVePanel
		);
		mainContent.add(tauPanel, "Tau");

		// Màn hình "Hoa don"
		HoaDon hoaDonPanel = new HoaDon();
		mainContent.add(hoaDonPanel, "HoaDon");

		// Màn hình "Thống kê"
		ThongKe thongKePanel = new ThongKe();
		mainContent.add(thongKePanel, "ThongKe");

		// Màn hình "Vé tàu"
		danhSachVePanel = new VeTau.DanhSachVePanel();

		JPanel vePanel = new JPanel(new BorderLayout());
		vePanel.add(danhSachVePanel, BorderLayout.CENTER);

		mainContent.add(vePanel, "VeTau");
		
		HanhKhachPanel khachPanel = new HanhKhachPanel();
		mainContent.add(khachPanel, "HanhKhach");

		// 2. Định nghĩa font dùng chung cho popup menu
		Font menuFont = new Font("Roboto", Font.BOLD, 14);

		// 4. Các nút điều hướng bên sidebar
		String[] buttonNames = { "Trang chủ", "Vé tàu", "Tàu", "Hóa đơn", "Nhân viên", "Khách hàng", "Thống kê", "Đăng xuất" };
		String[] iconPaths = { "src/images/home.png", "src/images/train-ticket.png", "src/images/train.png",
				"src/images/receipt.png", "src/images/employee.png", "src/images/customer.png", "src/images/statistics.png",
				"src/images/logout.png" };
		Color[] buttonColors = { new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217), new Color(217, 217, 217) };

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
				// nếu user là nhân viên và đang bấm vào nút “Nhân viên”
			    if ("nhanvien".equals(util.Session.currentRole) && btnName.equals("Nhân viên")) {
			        JOptionPane.showMessageDialog(
			            this,
			            "Chức năng này chỉ dùng cho Quản lý",
			            "Truy cập bị từ chối",
			            JOptionPane.WARNING_MESSAGE
			        );
			        return;
			    }
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
				case "Khách hàng":
			        cardLayout.show(mainContent, "HanhKhach");
			        break;
				case "Vé tàu":
					cardLayout.show(mainContent, "VeTau");
					break;
				case "Thống kê":
					cardLayout.show(mainContent, "ThongKe");
					break;
				case "Đăng xuất":
					// TODO: xử lý logout tại đây
//					dispose();
//		            new DangNhap().setVisible(true);
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

	public VeTau.DanhSachVePanel getDanhSachVePanel() {
		return danhSachVePanel;
	}

	/**
	 * Loai ve
	 */
	/** Enable tất cả nút, sau đó disable đúng nút activeKey */
	public void updateNavButtons(String activeKey) {
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
		String currentUser = Session.currentUsername;
		Entity_NhanVien nv = DAO_NhanVien.getByUsername(currentUser);
		if (nv == null) {
			// fallback nếu cần
			nv = new Entity_NhanVien("N/A", "N/A", "01/01/1970", true, "N/A", "N/A", true, "N/A", null);
		}

		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

		Font largeFont = new Font("Roboto", Font.BOLD, 14);

		// Thông tin text
		String[] labels = { "Mã nhân viên", "Họ tên", "Ngày sinh", "Giới tính", "CCCD", "Chức vụ" };
		String[] values = { nv.getMaNV(), nv.getTenNV(), nv.getNamSinh(), nv.isPhai() ? "Nam" : "Nữ", nv.getCCCD(),
				nv.getChucVu() };

		JPanel infoPanel = new JPanel(new GridBagLayout());
		GridBagConstraints igbc = new GridBagConstraints();
		igbc.insets = new Insets(4, 4, 4, 4);
		igbc.fill = GridBagConstraints.HORIZONTAL;
		igbc.gridx = 0;

		for (int i = 0; i < labels.length; i++) {
			// label
			igbc.gridy = i;
			igbc.weightx = 0.0;
			igbc.weighty = 1.0; // dãn đều theo chiều dọc
			JLabel lbl = new JLabel(labels[i] + ":");
			lbl.setFont(largeFont);
			infoPanel.add(lbl, igbc);

			// value
			igbc.gridx = 1;
			igbc.weightx = 1.0;
			JLabel val = new JLabel(values[i]);
			val.setFont(largeFont);
			infoPanel.add(val, igbc);

			// reset to first column
			igbc.gridx = 0;
		}

		// Ảnh
		JPanel imagePanel = new JPanel(new BorderLayout());
		imagePanel.setPreferredSize(new Dimension(170, 260));
		byte[] photoBytes = nv.getPhoto();
		JLabel imageLabel;
		if (photoBytes != null) {
			ImageIcon icon = new ImageIcon(photoBytes);
			Image img = icon.getImage().getScaledInstance(170, 260, Image.SCALE_SMOOTH);
			imageLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
		} else {
			imageLabel = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
			imageLabel.setFont(largeFont);
		}
		imagePanel.add(imageLabel, BorderLayout.CENTER);

		// Ghép lại vào panel chính
		GridBagConstraints mgbc = new GridBagConstraints();
		mgbc.insets = new Insets(10, 10, 10, 10);
		mgbc.fill = GridBagConstraints.BOTH;

		// Info panel chiếm 70%
		mgbc.gridx = 0;
		mgbc.gridy = 0;
		mgbc.weightx = 0.7;
		mgbc.weighty = 1.0;
		panel.add(infoPanel, mgbc);

		// Image panel chiếm 30%
		mgbc.gridx = 1;
		mgbc.weightx = 0.3;
		panel.add(imagePanel, mgbc);

		return panel;
	}

	private JPanel createTopLeftPanel() {
		// 1) Lấy map từ DAO: key = tenGa, value = maGa
	    Map<String, String> nameToCode = DAO_Ga.getGaMap();

	    // 2) Panel chính
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

	    Font largeFont  = new Font("Roboto", Font.PLAIN, 14);
	    Font buttonFont = new Font("Roboto", Font.BOLD, 14);

	    // container trắng
	    JPanel infoPanel = new JPanel(new BorderLayout());
	    infoPanel.setBackground(Color.WHITE);
	    infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

	    // header
	    JPanel infoHeader = new JPanel(new BorderLayout());
	    infoHeader.setBackground(new Color(70, 130, 180));
	    JLabel lblHeader = new JLabel(" Thông tin hành trình");
	    lblHeader.setForeground(Color.WHITE);
	    lblHeader.setFont(largeFont);
	    infoHeader.add(lblHeader, BorderLayout.CENTER);

	    // nội dung
	    JPanel infoContent = new JPanel(new GridBagLayout());
	    infoContent.setBackground(Color.WHITE);
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 10, 5, 10);
	    gbc.gridx   = 0;
	    gbc.weightx = 0;
	    gbc.weighty = 1.0;                   // CHO MỖI HÀNG ĐƯỢC PHÂN BỔ KHÔNG GIAN DỌC
	    gbc.fill    = GridBagConstraints.NONE;

	    // --- Ga đi ---
	    gbc.gridy  = 0;
	    JLabel lblGaDi = new JLabel("Ga đi:");
	    lblGaDi.setFont(largeFont);
	    infoContent.add(lblGaDi, gbc);

	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JComboBox<String> comboGaDi = new JComboBox<>();
	    comboGaDi.setFont(largeFont);
	    nameToCode.keySet().forEach(comboGaDi::addItem);
	    infoContent.add(comboGaDi, gbc);

	    // --- Ga đến ---
	    gbc.gridy  = 1;
	    gbc.gridx   = 0;
	    gbc.fill    = GridBagConstraints.NONE;
	    gbc.weightx = 0;
	    JLabel lblGaDen = new JLabel("Ga đến:");
	    lblGaDen.setFont(largeFont);
	    infoContent.add(lblGaDen, gbc);

	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JComboBox<String> comboGaDen = new JComboBox<>();
	    comboGaDen.setFont(largeFont);
	    nameToCode.keySet().forEach(comboGaDen::addItem);
	    infoContent.add(comboGaDen, gbc);

	    // --- Loại vé ---
	    gbc.gridy  = 2;
	    gbc.gridx   = 0;
	    gbc.fill    = GridBagConstraints.NONE;
	    gbc.weightx = 0;
	    JLabel lblLoaiVe = new JLabel("Loại vé:");
	    lblLoaiVe.setFont(largeFont);
	    infoContent.add(lblLoaiVe, gbc);

	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JComboBox<LoaiVe> comboLoaiVe = new JComboBox<>();
	    comboLoaiVe.setFont(largeFont);
	    loaiVeList.forEach(comboLoaiVe::addItem);
	    comboLoaiVe.addActionListener(e -> {
	        LoaiVe lv = (LoaiVe)comboLoaiVe.getSelectedItem();
	        if (lv != null) selectedTicketType = lv.getTenLoaiVe();
	    });
	    infoContent.add(comboLoaiVe, gbc);

	    // --- Một chiều / Khứ hồi ---
	    gbc.gridy  = 3;
	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JPanel pRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
	    pRadio.setBackground(Color.WHITE);
	    JRadioButton rbOne    = new JRadioButton("Một chiều", true);
	    JRadioButton rbReturn = new JRadioButton("Khứ hồi");
	    rbOne.setFont(largeFont);
	    rbReturn.setFont(largeFont);
	    rbOne.setBackground(Color.WHITE);
	    rbReturn.setBackground(Color.WHITE);
	    ButtonGroup bg = new ButtonGroup();
	    bg.add(rbOne); bg.add(rbReturn);
	    pRadio.add(rbOne); pRadio.add(rbReturn);
	    infoContent.add(pRadio, gbc);

	    // --- Ngày đi ---
	    gbc.gridy  = 4;
	    gbc.gridx   = 0;
	    gbc.fill    = GridBagConstraints.NONE;
	    gbc.weightx = 0;
	    JLabel lblNgayDi = new JLabel("Ngày đi:");
	    lblNgayDi.setFont(largeFont);
	    infoContent.add(lblNgayDi, gbc);

	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JDateChooser dateChooserNgayDi = new JDateChooser();
	    dateChooserNgayDi.setFont(largeFont);
	    dateChooserNgayDi.setDateFormatString("dd/MM/yyyy");
	    infoContent.add(dateChooserNgayDi, gbc);

	    // --- Ngày về ---
	    gbc.gridy  = 5;
	    gbc.gridx   = 0;
	    gbc.fill    = GridBagConstraints.NONE;
	    gbc.weightx = 0;
	    JLabel lblNgayVe = new JLabel("Ngày về:");
	    lblNgayVe.setFont(largeFont);
	    infoContent.add(lblNgayVe, gbc);

	    gbc.gridx   = 1;
	    gbc.fill    = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
	    JDateChooser dateChooserNgayVe = new JDateChooser();
	    dateChooserNgayVe.setFont(largeFont);
	    dateChooserNgayVe.setDateFormatString("dd/MM/yyyy");
	    infoContent.add(dateChooserNgayVe, gbc);

	    // mặc định ẩn
	    lblNgayVe.setVisible(false);
	    dateChooserNgayVe.setVisible(false);
	    rbOne.addActionListener(e -> {
	        lblNgayVe.setVisible(false);
	        dateChooserNgayVe.setVisible(false);
	    });
	    rbReturn.addActionListener(e -> {
	        lblNgayVe.setVisible(true);
	        dateChooserNgayVe.setVisible(true);
	    });

	    // ----- Nút Tìm kiếm -----
	    gbc.gridy    = 11;
	    gbc.weighty  = 0.0;                        
	    gbc.fill     = GridBagConstraints.NONE;    
	    gbc.anchor   = GridBagConstraints.CENTER;  
	    JButton btnTim = new JButton("Tìm kiếm");
	    btnTim.setFont(buttonFont);
	    btnTim.setBackground(new Color(70, 130, 180));
	    btnTim.setForeground(Color.WHITE);
	    btnTim.setOpaque(true);
	    btnTim.setBorderPainted(false);
	    infoContent.add(btnTim, gbc);


		btnTim.addActionListener(e -> {
			// 1) Đọc đầu vào
			String tenDi = (String) comboGaDi.getSelectedItem();
			String tenDen = (String) comboGaDen.getSelectedItem();
			Date ngayDi = dateChooserNgayDi.getDate();
			Date ngayVe = dateChooserNgayVe.getDate();

			// 2) Validate bắt buộc
			if (tenDi == null || tenDen == null || ngayDi == null || selectedTicketType == null) {
				JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin bắt buộc.", "Thiếu dữ liệu",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			boolean isRound = rbReturn.isSelected();
			if (isRound && ngayVe == null) {
				JOptionPane.showMessageDialog(null, "Vui lòng chọn Ngày về cho vé khứ hồi.", "Thiếu dữ liệu",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			// 3) Lấy mã ga từ tên
			String maDi = nameToCode.get(tenDi);
			String maDen = nameToCode.get(tenDen);

			// 4) Format ngày đi & ngày về
			String ngayDiStr = new SimpleDateFormat("dd/MM/yyyy").format(ngayDi);
			String ngayVeStr = isRound ? new SimpleDateFormat("dd/MM/yyyy").format(ngayVe) : "";

			// 5) Xác định loại chuyến
			String loaiChuyen = isRound ? "Khứ hồi" : "Một chiều";

			// 6) Query chuyến chiều đi
			List<TauEntity> dsDi = DAO_Tau.layDanhSachChuyenTau(maDi, maDen, ngayDi);

			// 7) Nếu Khứ hồi, query chuyến chiều về (đảo ga & ngày)
			List<TauEntity> dsVe = new ArrayList<>();
			if (isRound) {
				dsVe = DAO_Tau.layDanhSachChuyenTau(maDen, maDi, ngayVe);
			}

			// 8) Kiểm tra kết quả
			if (dsDi.isEmpty() || (isRound && dsVe.isEmpty())) {
				String msg = isRound ? "Không tìm thấy chuyến phù hợp cho cả chiều đi và chiều về."
						: "Không tìm thấy chuyến phù hợp cho chiều đi.";
				JOptionPane.showMessageDialog(null, msg, "Kết quả", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			// Thông báo số chuyến tìm được
			String info = isRound ? String.format("Tìm thấy %d chuyến đi và %d chuyến về!", dsDi.size(), dsVe.size())
					: String.format("Tìm thấy %d chuyến!", dsDi.size());
			JOptionPane.showMessageDialog(null, info, "Kết quả", JOptionPane.INFORMATION_MESSAGE);

			// 9) Tạo mới và add Tau panel với 2 danh sách + tên ga đi/ga đến
			tauPanel = new Tau(dsDi, // List<TauEntity> chuyến đi
					dsVe, // List<TauEntity> chuyến về (empty nếu Một chiều)
					tenDi, // originGa
					tenDen, // destGa
					selectedTicketType, loaiVeList, ngayDiStr, loaiChuyen, ngayVeStr, danhSachVePanel // listener
			);
			mainContent.add(tauPanel, "Tau");

			// 10) Show và refresh UI
			cardLayout.show(mainContent, "Tau");
			mainContent.revalidate();
			mainContent.repaint();
		});

		// ghép lại
		infoPanel.add(infoHeader, BorderLayout.NORTH);
		infoPanel.add(infoContent, BorderLayout.CENTER);

		GridBagConstraints c2 = new GridBagConstraints();
	    c2.insets   = new Insets(5,5,5,5);
	    c2.gridx    = 0;
	    c2.gridy    = 0;
	    c2.weightx  = 1.0;
	    c2.weighty  = 1.0;
	    c2.fill     = GridBagConstraints.BOTH;
	    panel.add(infoPanel, c2);

	    return panel;
	}

	/**
	 * Phương thức tạo card hiển thị thông tin hành trình
	 */
	private JPanel createRouteCard(String from, String to, String time, String trainCode, String price,
	                               Font routeFont, Font timeFont, Font priceFont, Font buttonFont) {
	    JPanel card = new JPanel(new BorderLayout(10, 5));
	    card.setBackground(Color.WHITE);
	    card.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
	        BorderFactory.createEmptyBorder(10, 15, 10, 15)
	    ));
	    // cho phép card giãn ngang tối đa
	    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

	    // stationsPanel (Ga đi / Ga đến)
	    JPanel stationsPanel = new JPanel(new BorderLayout());
	    stationsPanel.setBackground(Color.WHITE);
	    JLabel fromLabel = new JLabel(from);
	    fromLabel.setFont(routeFont);
	    JLabel toLabel = new JLabel(to);
	    toLabel.setFont(routeFont);
	    toLabel.setHorizontalAlignment(JLabel.RIGHT);
	    stationsPanel.add(fromLabel, BorderLayout.WEST);
	    stationsPanel.add(toLabel, BorderLayout.EAST);

	    // timePanel (TG đi / arrow / Mã tàu)
	    JPanel arrowPanel = new JPanel(new BorderLayout());
	    arrowPanel.setBackground(Color.WHITE);
	    JLabel timeLabel = new JLabel(time, SwingConstants.CENTER);
	    timeLabel.setFont(timeFont);
	    JLabel arrowLabel = new JLabel(" → ", SwingConstants.CENTER);
	    arrowLabel.setFont(timeFont);
	    JLabel trainLabel = new JLabel(trainCode, SwingConstants.CENTER);
	    trainLabel.setFont(timeFont);
	    arrowPanel.add(timeLabel,   BorderLayout.NORTH);
	    arrowPanel.add(arrowLabel,  BorderLayout.CENTER);
	    arrowPanel.add(trainLabel,  BorderLayout.SOUTH);

	    // pricePanel (Giá + nút Xem)
	    JPanel pricePanel = new JPanel(new BorderLayout(5,5));
	    pricePanel.setBackground(Color.WHITE);
	    JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
	    priceLabel.setFont(priceFont);
	    priceLabel.setForeground(new Color(0,175,200));
	    JButton viewButton = new JButton("Xem");
	    viewButton.setFont(buttonFont);
	    viewButton.setBackground(new Color(70,130,180));
	    viewButton.setForeground(Color.WHITE);
	    viewButton.setFocusPainted(false);
	    pricePanel.add(priceLabel,    BorderLayout.NORTH);
	    pricePanel.add(viewButton,    BorderLayout.SOUTH);

	    // ghép lại
	    JPanel infoPanel = new JPanel(new BorderLayout());
	    infoPanel.setBackground(Color.WHITE);
	    infoPanel.add(stationsPanel, BorderLayout.NORTH);
	    infoPanel.add(arrowPanel,     BorderLayout.CENTER);

	    card.add(infoPanel,  BorderLayout.CENTER);
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
