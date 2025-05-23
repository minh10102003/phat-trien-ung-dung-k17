package UI;

import entity.TauEntity;
import entity.TrainInfo;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import connectDB.DatabaseHelper;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tau extends JPanel {
	private JPanel trainPanel;
	private JPanel cabinPanel;
	private JPanel seatPanel;
	private GheInfo selectedSeat;
	private String selectedTrain = "";
	private String selectedCabin = "";

	private Dimension trainButtonSize = new Dimension(250, 200);
	private Dimension cabinButtonSize = new Dimension(80, 80);
	private Dimension seatButtonSize = new Dimension(80, 80);

	private Dimension trainIconSize = new Dimension(120, 120);
	private Map<String, TrainInfo> trains = new HashMap<>();

	public Tau() {
		initUI();
	}

	public Tau(List<TauEntity> danhSachTau) {
		for (TauEntity tau : danhSachTau) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			String gioDi = tau.getGioKhoiHanh() != null ? timeFormat.format(tau.getGioKhoiHanh()) : "??:??";
			String gioDen = tau.getGioDen() != null ? timeFormat.format(tau.getGioDen()) : "??:??";
			trains.put(tau.getMaTau(),
					new TrainInfo(tau.getMaTau(), tau.getTenTau(), gioDi, gioDen, tau.getSoChoCon()));
		}
		initUI();
	}

	private void initUI() {
		trainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		trainPanel.setBorder(BorderFactory.createTitledBorder("Chọn chuyến tàu"));

		cabinPanel = new JPanel();
		cabinPanel.setLayout(new BoxLayout(cabinPanel, BoxLayout.Y_AXIS));
		cabinPanel.setBorder(BorderFactory.createTitledBorder("Chọn toa tàu"));

		seatPanel = new JPanel();
		seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
		seatPanel.setBorder(BorderFactory.createTitledBorder("Chọn ghế"));

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		createTrainPanel();
		centerPanel.add(trainPanel);
		centerPanel.add(cabinPanel);
		centerPanel.add(seatPanel);

		add(centerPanel, BorderLayout.CENTER);
	}

	private void createTrainPanel() {
		trainPanel.removeAll();
		for (TrainInfo train : trains.values()) {
			JPanel trainButton = createTrainButton(train);
			trainPanel.add(trainButton);
		}
		trainPanel.revalidate();
		trainPanel.repaint();
	}

	// 1. Ở lớp bao ngoài (ví dụ: trong class chứa createTrainButton), thêm:
	private List<JPanel> trainButtons = new ArrayList<>();
	private String selectTrain = null;

	// 2. Hàm tạo nút giờ xe lửa:
	private JPanel createTrainButton(TrainInfo train) {
		JPanel trainButton = new JPanel();
		trainButton.setLayout(new BoxLayout(trainButton, BoxLayout.Y_AXIS));
		trainButton.setPreferredSize(trainButtonSize);
		trainButton.setBorder(null);
		trainButton.setOpaque(true);

		// Gán mã tàu vào client property để sau này dễ lookup
		trainButton.putClientProperty("trainId", train.getMaTau());

		// Thiết lập màu mặc định
		Color defaultColor = new Color(240, 240, 240);
		Color hoverColor = new Color(200, 200, 200);
		Color selectedColor = new Color(211, 211, 211);
		trainButton.setBackground(defaultColor);

		// Icon + label như bạn đang làm
		ImageIcon icon = new ImageIcon("src/images/train_1.png");
		Image scaledIcon = icon.getImage().getScaledInstance(trainIconSize.width, trainIconSize.height,
				Image.SCALE_SMOOTH);
		JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel idLabel = new JLabel(train.getMaTau());
		idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		idLabel.setFont(new Font("Arial", Font.BOLD, 14));

		JLabel deparLabel = new JLabel("TG đi: " + train.getGioKhoiHanh());
		deparLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel arrLabel = new JLabel("TG đến: " + train.getGioDen());
		arrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		trainButton.add(iconLabel);
		trainButton.add(Box.createVerticalStrut(5));
		trainButton.add(idLabel);
		trainButton.add(Box.createVerticalStrut(5));
		trainButton.add(deparLabel);
		trainButton.add(arrLabel);

		// 3. Mouse listener chung cho hover + click
		trainButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				String id = (String) trainButton.getClientProperty("trainId");
				// chỉ hover khi chưa được chọn
				if (!id.equals(selectedTrain)) {
					trainButton.setBackground(hoverColor);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				String id = (String) trainButton.getClientProperty("trainId");
				// trả về màu mặc định nếu không phải nút đã chọn
				if (!id.equals(selectedTrain)) {
					trainButton.setBackground(defaultColor);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// đánh dấu nút này là nút được chọn
				selectedTrain = train.getMaTau();
				// cập nhật màu cho tất cả nút
				for (JPanel btn : trainButtons) {
					String id = (String) btn.getClientProperty("trainId");
					if (id.equals(selectedTrain)) {
						btn.setBackground(selectedColor);
					} else {
						btn.setBackground(defaultColor);
					}
				}
				// rồi bạn làm tiếp: hiển thị cabin, etc.
				createCabinPanel();
			}
		});

		// 4. Lưu panel vào list để dễ quản lý khi click
		trainButtons.add(trainButton);

		return trainButton;
	}

	private void createCabinPanel() {
		cabinPanel.removeAll();
		final JPanel cabinsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		cabinsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Đầu tàu (không selectable)
		JButton headBtn = createCabinButton("Đầu tàu", false);
		cabinsContainer.add(headBtn);

		// Lấy danh sách mã toa
		List<String> danhSachToa = DatabaseHelper.getCabinsByTrainId(selectedTrain);
		if (danhSachToa == null || danhSachToa.isEmpty()) {
			cabinPanel.add(new JLabel("Không có toa nào cho tàu này."));
		} else {
			for (String maToa : danhSachToa) {
				String text = "Toa " + maToa;
				JButton cabinButton = createCabinButton(text, true);

				cabinButton.addActionListener(e -> {
					// 1) Cập nhật selectedCabin
					selectedCabin = maToa;

					// 2) Loop qua tất cả button để set icon normal/selected
					for (Component c : cabinsContainer.getComponents()) {
						if (c instanceof JButton) {
							JButton btn = (JButton) c;
							String id = (String) btn.getClientProperty("cabinId");
							// chỉ đổi cho các nút selectable
							if (btn.isEnabled()) {
								ImageIcon iconToSet = id.equals(text)
										? (ImageIcon) btn.getClientProperty("selectedIcon")
										: (ImageIcon) btn.getClientProperty("normalIcon");
								btn.setIcon(iconToSet);
							}
						}
					}

					// 3) load seats như cũ
					loadSeatsForCabin(maToa);
				});

				cabinsContainer.add(cabinButton);
			}
			cabinPanel.add(cabinsContainer);
		}

		cabinPanel.revalidate();
		cabinPanel.repaint();
	}

	private JButton createCabinButton(String text, boolean isSelectable) {
		JButton button = new JButton();
		button.setPreferredSize(cabinButtonSize);
		button.setEnabled(isSelectable);

		// --- Chuẩn bị 3 đường dẫn icon ---
		String normalPath = text.equals("Đầu tàu") ? "/images/train-engine.png" : "/images/train-cargo.png";
		String hoverPath = text.equals("Đầu tàu") ? "/images/train-engine.png" : "/images/vietnam.png";
		String selectedPath = text.equals("Đầu tàu") ? "/images/train-engine.png" : "/images/ticket.png";

		// --- Load & scale normal icon ---
		URL normalURL = getClass().getResource(normalPath);
		ImageIcon rawNormal = normalURL != null ? new ImageIcon(normalURL) : new ImageIcon("src/images" + normalPath); // fallback
																														// dev
		Image normalImg = rawNormal.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
				Image.SCALE_SMOOTH);
		ImageIcon normalScaled = new ImageIcon(normalImg);
		button.setIcon(normalScaled);
		button.putClientProperty("normalIcon", normalScaled);

		// --- Load & scale hover icon ---
		URL hoverURL = getClass().getResource(hoverPath);
		if (hoverURL != null) {
			ImageIcon rawHover = new ImageIcon(hoverURL);
			Image hoverImg = rawHover.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
					Image.SCALE_SMOOTH);
			button.setRolloverIcon(new ImageIcon(hoverImg));
			button.setRolloverEnabled(true);
		}

		// --- Load & scale selected icon ---
		URL selURL = getClass().getResource(selectedPath);
		if (selURL != null) {
			ImageIcon rawSel = new ImageIcon(selURL);
			Image selImg = rawSel.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
					Image.SCALE_SMOOTH);
			ImageIcon selScaled = new ImageIcon(selImg);
			button.putClientProperty("selectedIcon", selScaled);
		}

		// Gắn luôn id để tiện so sánh khi click
		button.putClientProperty("cabinId", text);

		// Ẩn border/background để chỉ thấy icon
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setOpaque(false);

		return button;
	}

	// field ở đầu class
	private JButton lastSelectedSeatButton = null;

	// load lại seats
	private void loadSeatsForCabin(String maToa) {
	    seatPanel.removeAll();
	    seatPanel.setLayout(new BorderLayout(10, 10));

	    // 1) Tạo border title
	    String cabinType = DatabaseHelper.getCabinDescription(maToa);
	    seatPanel.setBorder(BorderFactory.createTitledBorder(
	        BorderFactory.createLineBorder(new Color(100, 100, 200), 2, true),
	        "Toa " + maToa + ": " + cabinType,
	        TitledBorder.CENTER,
	        TitledBorder.TOP,
	        new Font("Arial", Font.BOLD, 14)
	    ));

	    // 2) Lấy danh sách ghế
	    List<GheInfo> danhSachGhe = getSeatsForCabin(maToa);
	    if (danhSachGhe == null || danhSachGhe.isEmpty()) {
	        seatPanel.add(new JLabel("Không có thông tin ghế cho toa này", JLabel.CENTER),
	                      BorderLayout.CENTER);
	    } else {
	        // 3) Tính số hàng, cột, khoảng cách
	        int rows = 4;
	        int total = danhSachGhe.size();
	        int cols = (int) Math.ceil((double) total / rows);
	        int hgap = 8, vgap = 8;

	        // 4) Kích thước mỗi nút ghế (ép vuông)
	        int side = Math.min(seatButtonSize.width, seatButtonSize.height);

	        // 5) Panel map ghế với GridLayout + custom paint nền bo góc
	        JPanel seatMap = new JPanel(new GridLayout(rows, cols, hgap, vgap)) {
	            @Override protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D)g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                                    RenderingHints.VALUE_ANTIALIAS_ON);
	                g2.setColor(new Color(240, 240, 240));
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
	                g2.dispose();
	                super.paintComponent(g);
	            }
	        };
	        seatMap.setOpaque(false);

	        // 6) Thêm nút ghế
	        for (GheInfo ghe : danhSachGhe) {
	            seatMap.add(createSeatButton(ghe, side));
	        }
	        // 7) Placeholder nếu thiếu cell
	        for (int i = total; i < rows * cols; i++) {
	            JLabel empty = new JLabel();
	            empty.setPreferredSize(new Dimension(side, side));
	            seatMap.add(empty);
	        }

	        // 8) Đóng vào JScrollPane và ẩn border
	        JScrollPane sp = new JScrollPane(seatMap);
	        sp.setBorder(null);

	        // 9) Tính preferredSize để scrollPane không giãn
	        int panelW = cols * side + (cols - 1) * hgap;
	        int panelH = rows * side + (rows - 1) * vgap;
	        sp.setPreferredSize(new Dimension(panelW, panelH));

	        // 10) Bọc scrollPane và 2 arrow buttons bằng FlowLayout
	        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	        wrapper.setOpaque(false);
	        wrapper.add(createArrowButton("‹"));
	        wrapper.add(sp);
	        wrapper.add(createArrowButton("›"));

	        // 11) Thêm vào seatPanel
	        seatPanel.add(wrapper, BorderLayout.CENTER);
	    }

	    seatPanel.revalidate();
	    seatPanel.repaint();
	}

	// helper: tạo nút mũi tên
	private JButton createArrowButton(String arrow) {
	    JButton b = new JButton(arrow);
	    b.setFont(b.getFont().deriveFont(24f));
	    b.setBorderPainted(false);
	    b.setContentAreaFilled(false);
	    b.setFocusPainted(false);
	    // TODO: addActionListener nếu cần xử lý chuyển trang
	    return b;
	}

	// helper: tạo nút ghế tròn, hover, chọn đỏ, với kích thước vuông = side
	private JButton createSeatButton(GheInfo ghe, int side) {
	    JButton b = new JButton(String.valueOf(ghe.soGhe)) {
	        @Override public Dimension getPreferredSize() {
	            return new Dimension(side, side);
	        }
	        @Override protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D)g.create();
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                                RenderingHints.VALUE_ANTIALIAS_ON);
	            g2.setColor(getBackground());
	            g2.fillOval(0, 0, getWidth(), getHeight());
	            g2.dispose();
	            super.paintComponent(g);
	        }
	    };
	    b.setMargin(new Insets(0, 0, 0, 0));
	    b.setContentAreaFilled(false);
	    b.setBorderPainted(false);
	    b.setFocusPainted(false);
	    b.setForeground(Color.BLACK);

	    // hover màu sáng hơn
	    Color normal = new Color(200, 200, 200);
	    Color hover  = normal.brighter();
	    b.setBackground(normal);
	    b.addMouseListener(new MouseAdapter() {
	        @Override public void mouseEntered(MouseEvent e) {
	            if (b != lastSelectedSeatButton) b.setBackground(hover);
	        }
	        @Override public void mouseExited(MouseEvent e) {
	            if (b != lastSelectedSeatButton) b.setBackground(normal);
	        }
	    });

	    // click: đổi đỏ, reset nút cũ, show dialog
	    b.addActionListener(e -> {
	        if (lastSelectedSeatButton != null) {
	            lastSelectedSeatButton.setBackground(normal);
	        }
	        b.setBackground(Color.RED);
	        lastSelectedSeatButton = b;

	        selectedSeat = ghe;
	        JOptionPane.showMessageDialog(this,
	            "Bạn đã chọn ghế số " + ghe.soGhe + " (" + ghe.loaiGhe + ")");
	        int confirm = JOptionPane.showConfirmDialog(this,
	            "Bạn có muốn đặt vé với ghế này?", "Đặt vé",
	            JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            openDatVePanel();
	        }
	    });

	    return b;
	}



	private void openDatVePanel() {
		String maTau = selectedTrain;
		String gioDi = trains.get(maTau).getGioKhoiHanh();
		String gioDen = trains.get(maTau).getGioDen();
		String ngayDi = DatabaseHelper.getNgayDiByMaTau(maTau);
		String cho = String.valueOf(selectedSeat.soGhe);
		int soLuongVe = 1;

		JFrame datVeFrame = new JFrame("Đặt Vé Tàu");
		datVeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		datVeFrame.setSize(1000, 700);

		VeTau.DatVePanel datVePanel = new VeTau.DatVePanel(maTau, gioDi, gioDen, ngayDi, cho, soLuongVe, null);

		datVeFrame.setContentPane(datVePanel);
		datVeFrame.setLocationRelativeTo(null);
		datVeFrame.setVisible(true);
	}

	private List<GheInfo> getSeatsForCabin(String maToa) {
		return DatabaseHelper.getGheByToa(maToa);
	}

	public static class GheInfo {
		public String maGhe;
		public int soGhe;
		public String loaiGhe;

		public GheInfo(String maGhe, int soGhe, String loaiGhe) {
			this.maGhe = maGhe;
			this.soGhe = soGhe;
			this.loaiGhe = loaiGhe;
		}
	}

	public static void setGioKhoiHanh(Timestamp timestamp) {
		// TODO Auto-generated method stub

	}

	public static void setGioDen(Timestamp timestamp) {
		// TODO Auto-generated method stub

	}
}
