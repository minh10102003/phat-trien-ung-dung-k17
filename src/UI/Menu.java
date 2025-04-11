package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class Menu extends JFrame {
	private JPanel sidebar;
	private JPanel mainContent; // Panel chứa các màn hình (cards)
	private CardLayout cardLayout; // Quản lý chuyển màn hình
	// Map chứa các nút điều hướng để dễ thay đổi trạng thái
	private Map<String, JButton> navButtons = new HashMap<>();

	public Menu() {
		super("Menu");
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

		// Các nút điều hướng
		String[] buttonNames = { "Trang chủ", "Vé tàu", "Tàu", "Chuyến tàu", "Hóa đơn", "Nhân viên", "Thống kê",
				"Đăng xuất" };
		String[] iconPaths = { "src/images/home.png", "src/images/train-ticket.png", "src/images/train.png",
				"src/images/railroad.png", "src/images/receipt.png", "src/images/employee.png",
				"src/images/statistics.png", "src/images/logout.png" };
		Color[] buttonColors = { new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
				new Color(217, 217, 217) };

		for (int i = 0; i < buttonNames.length; i++) {
			final String btnName = buttonNames[i];
			JButton button = new JButton(btnName);

			int btnW = 100, btnH = 40;
			button.setPreferredSize(new Dimension(btnW, btnH));
			button.setMinimumSize(new Dimension(btnW, btnH));
			button.setMaximumSize(new Dimension(btnW, btnH));

			// Thêm icon vào button
			ImageIcon iconButt = new ImageIcon(iconPaths[i]);
			Image imgButt = iconButt.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(imgButt));

			// Ban đầu chỉ hiển thị icon, căn giữa
			button.setText("");
			button.setHorizontalAlignment(SwingConstants.CENTER);
			button.setHorizontalTextPosition(SwingConstants.CENTER);
			// padding đều để icon không quá sát viền
			button.setMargin(new Insets(5, 5, 5, 5));

			// Ban đầu, disable nút "Trang chủ" để đánh dấu đây là màn hình hiện tại
			if (btnName.equals("Trang chủ")) {
				button.setEnabled(false);
			}
			button.setFont(new Font("Roboto", Font.BOLD, 13));
			button.setBackground(buttonColors[i]);

			// Hàm tiện ích để khởi động animation
			BiConsumer<JButton, Boolean> animateHover = (btn, entering) -> {
				final int startMargin = entering ? 5 : 15;
				final int endMargin = entering ? 15 : 5;
				final int startGap = entering ? 0 : 10;
				final int endGap = entering ? 10 : 0;
				final int fps = 180;
				final int delay = 2000 / fps;
				final int durationMs = 20;
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
								// sau khi ẩn text, đưa icon về giữa
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

			// MouseListener dùng animation mượt
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

			// Bắt sự kiện chuyển màn hình và cập nhật trạng thái nút
			button.addActionListener(e -> {
				if (btnName.equals("Trang chủ")) {
					cardLayout.show(mainContent, "TrangChu");
					navButtons.get("Trang chủ").setEnabled(false);
					navButtons.get("Nhân viên").setEnabled(true);
					navButtons.get("Chuyến tàu").setEnabled(true); // Bật lại nút "Chuyến tàu" khi về Trang chủ
					navButtons.get("Hóa đơn").setEnabled(true);
				} else if (btnName.equals("Nhân viên")) {
					cardLayout.show(mainContent, "NhanVien");
					navButtons.get("Trang chủ").setEnabled(true);
					navButtons.get("Nhân viên").setEnabled(false);
					navButtons.get("Chuyến tàu").setEnabled(true); // Bật lại nút "Chuyến tàu" khi vào Quản lý nhân viên
					navButtons.get("Hóa đơn").setEnabled(true);
				} else if (btnName.equals("Chuyến tàu")) { // Thêm sự kiện cho "Chuyến tàu"
					cardLayout.show(mainContent, "ChuyenTau");
					navButtons.get("Trang chủ").setEnabled(true);
					navButtons.get("Nhân viên").setEnabled(true);
					navButtons.get("Chuyến tàu").setEnabled(false); // Vô hiệu hóa nút "Chuyến tàu" khi đang ở màn hình
																	// này
					navButtons.get("Hóa đơn").setEnabled(true);
				} else if (btnName.equals("Hóa đơn")) {
					cardLayout.show(mainContent, "HoaDon");
					navButtons.get("Trang chủ").setEnabled(true);
					navButtons.get("Hóa đơn").setEnabled(false);
					navButtons.get("Nhân viên").setEnabled(true);
					navButtons.get("Chuyến tàu").setEnabled(true);
				}
			});

		}

		// Thêm sidebar vào frame: cột 0, chiếm toàn bộ chiều cao (gridheight = 2)
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0; // Sidebar cố định kích thước theo chiều ngang
		gbc.weighty = 1.0;
		add(sidebar, gbc);

		// ======================= MAIN CONTENT VỚI CARDLAYOUT =======================
		cardLayout = new CardLayout();
		mainContent = new JPanel(cardLayout);

		// Màn hình "Trang chủ" (gồm các panel cũ)
		JPanel homePanel = createHomePanel();
		mainContent.add(homePanel, "TrangChu");

		// Màn hình "Quản lý nhân viên" (sử dụng class QL_NhanVien)
		NhanVien NhanVienPanel = new NhanVien();
		mainContent.add(NhanVienPanel, "NhanVien");

		// Màn hình "Chuyến tàu"
		ChuyenTau chuyenTauPanel = new ChuyenTau(); // Thêm màn hình Chuyến tàu
		mainContent.add(chuyenTauPanel, "ChuyenTau");

		// Màn hình "Hoa don"
		HoaDon hoaDonPanel = new HoaDon();
		mainContent.add(hoaDonPanel, "HoaDon");

		// Thêm mainContent vào frame: cột 1, chiếm toàn bộ chiều cao
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.weightx = 1.0;
		add(mainContent, gbc);
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
	    JPanel bottomPanel = createSubPanel("Vùng Dưới", Color.WHITE);
	    
	    // Cấu hình GridBagConstraints
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.insets = new Insets(5, 5, 5, 5); // Thêm khoảng cách giữa các panel
	    
	    // Thêm panel trên bên trái
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1; // Trải rộng 1 cột
	    gbc.gridheight = 1;
	    gbc.weightx = 0.5; // 50% không gian ngang
	    gbc.weighty = 0.4; // 40% không gian dọc
	    homePanel.add(topLeftPanel, gbc);
	    
	    // Thêm panel trên bên phải
	    gbc.gridx = 1;
	    gbc.gridy = 0;
	    gbc.gridwidth = 1; // Trải rộng 1 cột
	    gbc.gridheight = 1;
	    gbc.weightx = 0.5; // 50% không gian ngang
	    gbc.weighty = 0.4; // 40% không gian dọc
	    homePanel.add(topRightPanel, gbc);
	    
	    // Thêm panel dưới - chiếm toàn bộ chiều rộng
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.gridwidth = 2; // Trải rộng 2 cột
	    gbc.gridheight = 1;
	    gbc.weightx = 1.0;
	    gbc.weighty = 0.6; // 60% không gian dọc
	    homePanel.add(bottomPanel, gbc);
	    
	    return homePanel;
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

	    String[] labels = {"Mã nhân viên", "Họ tên", "Ngày sinh", "Giới tính", "Email", "SĐT", "Chức vụ"};
	    String[] keys = {"maNV", "hoTen", "ngaySinh", "gioiTinh", "email", "sdt", "chucVu"};

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
	    mgbc.weightx = 0.7;
	    mgbc.weighty = 1.0;
	    panel.add(infoPanel, mgbc);
	    
	    // Thêm imagePanel (chiếm khoảng 30% chiều ngang)
	    mgbc.gridx = 1;
	    mgbc.gridy = 0;
	    mgbc.weightx = 0.3;
	    mgbc.weighty = 1.0;
	    panel.add(imagePanel, mgbc);
	    
	    return panel;
	}

	/**
	 * Phương thức tạo panel trên bên trái với thông tin hành trình
	 */
	private JPanel createTopLeftPanel() {
	    JPanel panel = new JPanel();
	    panel.setLayout(new GridBagLayout());
	    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    
	    // Tạo font lớn hơn
	    Font largeFont = new Font("Arial", Font.PLAIN, 14);
	    Font titleFont = new Font("Arial", Font.BOLD, 16);
	    Font buttonFont = new Font("Arial", Font.BOLD, 14);
	    Font smallFont = new Font("Arial", Font.PLAIN, 12);
	    
	    // Tạo panel "Thông tin hành trình" với kiểu header mới
	    JPanel infoPanel = new JPanel(new BorderLayout());
	    infoPanel.setBackground(Color.WHITE);
	    infoPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
	    
	    // Tạo header cho "Thông tin hành trình" giống với "Giỏ vé"
	    JPanel infoHeaderPanel = new JPanel(new BorderLayout());
	    infoHeaderPanel.setBackground(new Color(70, 130, 180)); // Steel Blue color
	    JLabel infoHeaderLabel = new JLabel(" Thông tin hành trình", JLabel.LEFT);
	    infoHeaderLabel.setFont(largeFont);
	    infoHeaderLabel.setForeground(Color.WHITE);
	    infoHeaderPanel.add(infoHeaderLabel, BorderLayout.CENTER);
	    
	    // Tạo panel nội dung cho thông tin hành trình
	    JPanel infoContentPanel = new JPanel(new GridBagLayout());
	    infoContentPanel.setBackground(Color.WHITE);
	    
	    GridBagConstraints ip = new GridBagConstraints();
	    ip.insets = new Insets(5, 5, 5, 5);
	    ip.fill = GridBagConstraints.HORIZONTAL;
	    ip.gridx = 0;
	    ip.weightx = 1.0; // Để các thành phần mở rộng theo chiều ngang

	    // Ga đi
	    ip.gridy = 0;
	    JLabel lblGaDi = new JLabel("Ga đi");
	    lblGaDi.setFont(largeFont);
	    infoContentPanel.add(lblGaDi, ip);
	    
	    ip.gridy = 1;
	    JTextField txtGaDi = new JTextField();
	    txtGaDi.setFont(largeFont);
	    infoContentPanel.add(txtGaDi, ip);

	    // Ga đến
	    ip.gridy = 2;
	    JLabel lblGaDen = new JLabel("Ga đến");
	    lblGaDen.setFont(largeFont);
	    infoContentPanel.add(lblGaDen, ip);
	    
	    ip.gridy = 3;
	    JTextField txtGaDen = new JTextField();
	    txtGaDen.setFont(largeFont);
	    infoContentPanel.add(txtGaDen, ip);

	    // Radio một chiều / khứ hồi
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

	    // Ngày đi
	    ip.gridy = 5;
	    JLabel lblNgayDi = new JLabel("Ngày đi");
	    lblNgayDi.setFont(largeFont);
	    infoContentPanel.add(lblNgayDi, ip);
	    
	    ip.gridy = 6;
	    JPanel pNgayDi = new JPanel(new BorderLayout());
	    pNgayDi.setBackground(Color.WHITE);
	    JTextField txtNgayDi = new JTextField();
	    txtNgayDi.setFont(largeFont);
	    JButton btnCal1 = new JButton(new ImageIcon("src/images/calendar.png"));
	    btnCal1.setPreferredSize(new Dimension(30, 30));
	    pNgayDi.add(txtNgayDi, BorderLayout.CENTER);
	    pNgayDi.add(btnCal1, BorderLayout.EAST);
	    infoContentPanel.add(pNgayDi, ip);

	    // Ngày về
	    ip.gridy = 7;
	    JLabel lblNgayVe = new JLabel("Ngày về");
	    lblNgayVe.setFont(largeFont);
	    infoContentPanel.add(lblNgayVe, ip);
	    
	    ip.gridy = 8;
	    JPanel pNgayVe = new JPanel(new BorderLayout());
	    pNgayVe.setBackground(Color.WHITE);
	    JTextField txtNgayVe = new JTextField();
	    txtNgayVe.setFont(largeFont);
	    JButton btnCal2 = new JButton(new ImageIcon("src/images/calendar.png"));
	    btnCal2.setPreferredSize(new Dimension(30, 30));
	    pNgayVe.add(txtNgayVe, BorderLayout.CENTER);
	    pNgayVe.add(btnCal2, BorderLayout.EAST);
	    infoContentPanel.add(pNgayVe, ip);

	    // Nút Tìm kiếm
	    ip.gridy = 9;
	    JButton btnTim = new JButton("Tìm kiếm");
	    btnTim.setFont(buttonFont);
	    btnTim.setBackground(new Color(70, 130, 180));
	    btnTim.setForeground(Color.WHITE);
	    infoContentPanel.add(btnTim, ip);
	    
	    // Thêm header và content vào infoPanel
	    infoPanel.add(infoHeaderPanel, BorderLayout.NORTH);
	    infoPanel.add(infoContentPanel, BorderLayout.CENTER);
	    
	    
	    
	    // Thêm các panel vào panel chính
	    GridBagConstraints mainGbc = new GridBagConstraints();
	    mainGbc.fill = GridBagConstraints.BOTH;
	    mainGbc.insets = new Insets(5, 5, 5, 5);
	    
	    // Thêm infoPanel (chiếm khoảng 60% chiều ngang)
	    mainGbc.gridx = 0;
	    mainGbc.gridy = 0;
	    mainGbc.weightx = 0.6;
	    mainGbc.weighty = 1.0;
	    panel.add(infoPanel, mainGbc);
	    
	    return panel;
	}

	/**
	 * Phương thức hỗ trợ để tạo các panel con khác
	 */
	private JPanel createSubPanel(String name, Color color) {
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.setBackground(color);
	    panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
	    
	    // Tạo font
	    Font titleFont = new Font("Roboto", Font.BOLD, 24);
	    Font subtitleFont = new Font("Roboto", Font.ITALIC, 14);
	    Font routeFont = new Font("Roboto", Font.BOLD, 16);
	    Font timeFont = new Font("Arial", Font.PLAIN, 14);
	    Font priceFont = new Font("Roboto", Font.BOLD, 18);
	    Font buttonFont = new Font("Roboto", Font.BOLD, 14);
	    
	    // Panel tiêu đề
	    JPanel titlePanel = new JPanel(new BorderLayout());
	    titlePanel.setBackground(new Color(240, 240, 240)); // Màu xám nhạt
	    
	    JLabel titleLabel = new JLabel("Các hành trình tàu hỏa phổ biến");
	    titleLabel.setFont(titleFont);
	    titleLabel.setForeground(new Color(60, 60, 60));
	    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));
	    
	    titlePanel.add(titleLabel, BorderLayout.NORTH);
	    
	    // Panel chứa các hành trình
	    JPanel routesPanel = new JPanel(new GridLayout(4, 3, 10, 10));
	    routesPanel.setBackground(new Color(240, 240, 240));
	    routesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    // Dữ liệu các hành trình
	    String[][] routeData = {
	        {"Sài Gòn", "Nha Trang", "8h20p", "SNT2", "385.000 đ"},
	        {"Hà Nội", "Lào Cai", "7h45p", "SP3", "375.000 đ"},
	        {"Sài Gòn", "Đà Nẵng", "17h42p", "SE22", "577.000 đ"},
	        {"Hà Nội", "Đà Nẵng", "16h18p", "SE19", "1.112.000 đ"},
	        {"Sài Gòn", "Phan Thiết", "4h35p", "SPT2", "200.000 đ"},
	        {"Hà Nội", "Hải Phòng", "2h25p", "HP1", "90.000 đ"},
	        {"Hà Nội", "Huế", "13h4p", "SE1", "571.000 đ"},
	        {"Đà Nẵng", "Huế", "3h20p", "HD2", "180.000 đ"},
	        {"Sài Gòn", "Hà Nội", "33h25p", "SE2", "1.000.000 đ"},
	        {"Hà Nội", "Đồng Hới", "9h58p", "SE1", "500.000 đ"},
	        {"Hà Nội", "Nha Trang", "25h40p", "SE1", "1.018.000 đ"},
	        {"Đà Lạt", "Trại Mát", "30p", "DL3", "100.000 đ"}
	    };
	    
	    // Tạo các card hành trình
	    for (String[] route : routeData) {
	        JPanel routeCard = createRouteCard(
	            route[0], // Ga đi
	            route[1], // Ga đến
	            route[2], // Thời gian
	            route[3], // Mã tàu
	            route[4], // Giá vé
	            routeFont,
	            timeFont,
	            priceFont,
	            buttonFont
	        );
	        routesPanel.add(routeCard);
	    }
	    
	    // Thêm các panel vào panel chính
	    panel.add(titlePanel, BorderLayout.NORTH);
	    panel.add(routesPanel, BorderLayout.CENTER);
	    
	    return panel;
	}

	/**
	 * Phương thức tạo card hiển thị thông tin hành trình
	 */
	private JPanel createRouteCard(String from, String to, String time, String trainCode, 
	                              String price, Font routeFont, Font timeFont, Font priceFont, Font buttonFont) {
	    JPanel card = new JPanel(new BorderLayout(10, 5));
	    card.setBackground(Color.WHITE);
	    card.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
	        BorderFactory.createEmptyBorder(10, 15, 10, 15)
	    ));
	    
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
}