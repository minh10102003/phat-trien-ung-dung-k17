package UI;

import entity.TauEntity;
import entity.TrainInfo;
import entity.LoaiVe;
import connectDB.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import util.Session;
import DAO.DAO_Ga;
import DAO.DAO_NhanVien;
import DAO.DAO_Ve.BookingResult;
import UI.TicketPaymentListener;


/**
 * Panel chọn chuyến tàu, toa, ghế và mở panel Đặt vé.
 */
public class Tau extends JPanel {
	// Panels chính
	private JPanel trainPanel;
	private JPanel cabinPanel;
	private JPanel seatPanel;

	// Trạng thái chọn
	private GheInfo selectedSeat;
	private String selectedTrain = "";
	private String selectedCabin = "";

	// Kích thước
	private final Dimension trainButtonSize = new Dimension(250, 200);
	private final Dimension cabinButtonSize = new Dimension(80, 80);
	private final Dimension seatButtonSize = new Dimension(80, 80);
	private final Dimension trainIconSize = new Dimension(120, 120);

	// Mới: Loại vé mặc định và danh sách loại vé truyền từ Menu
	private String defaultLoaiVe;
	private List<LoaiVe> listLoaiVe;

	private String ngayDiStr;
    private String loaiChuyen;
    private String ngayVeStr;


	// Listener để callback lên DanhSachVePanel
	private final TicketPaymentListener listener;

	// Dữ liệu tàu và buttons
	private final Map<String, TrainInfo> trains = new LinkedHashMap<>();
	private final List<JPanel> trainButtons = new ArrayList<>();
	private JButton lastSelectedSeatButton = null;
	
	// ở đầu class Tau
	private final List<TauEntity> outboundList;   // chuyến đi
	private final List<TauEntity> inboundList;    // chuyến về (có thể rỗng)
	
	/** Map từ mã tàu → TauEntity gốc để truy xuất ga đi/ga đến */
    private final Map<String, TauEntity> entityMap = new HashMap<>();

    private String originGa;
    private String destGa;

 // ========================
    // 1) Constructor “đầy đủ nhất”
    // ========================
    public Tau(
        List<TauEntity> outboundList,
        List<TauEntity> inboundList,
        String originGa,
        String destGa,
        String defaultLoaiVe,
        List<LoaiVe> listLoaiVe,
        String ngayDiStr,
        String loaiChuyen,
        String ngayVeStr,
        TicketPaymentListener listener
    ) {
        // gán 2 final field
        this.outboundList  = outboundList != null ? new ArrayList<>(outboundList) : Collections.emptyList();
        this.inboundList   = inboundList  != null ? new ArrayList<>(inboundList)  : Collections.emptyList();

        // gán các field còn lại
        this.originGa      = originGa;
        this.destGa        = destGa;
        this.defaultLoaiVe = defaultLoaiVe;
        this.listLoaiVe    = listLoaiVe   != null ? new ArrayList<>(listLoaiVe) : Collections.emptyList();
        this.ngayDiStr     = ngayDiStr;
        this.loaiChuyen    = loaiChuyen;
        this.ngayVeStr     = ngayVeStr;
        this.listener      = listener;

        // khởi tạo dữ liệu chuyến
        loadTrainInfos(this.outboundList);
        // build UI
        initUI();
    }

    // ========================
    // 2) Overload: chỉ chuyến đi, chiều về mặc định empty
    // ========================
    public Tau(
        List<TauEntity> outboundList,
        String originGa,
        String destGa,
        String defaultLoaiVe,
        List<LoaiVe> listLoaiVe,
        String ngayDiStr,
        String loaiChuyen,
        String ngayVeStr,
        TicketPaymentListener listener
    ) {
        this(
            outboundList,
            Collections.emptyList(),
            originGa,
            destGa,
            defaultLoaiVe,
            listLoaiVe,
            ngayDiStr,
            loaiChuyen,
            ngayVeStr,
            listener
        );
    }

    // ========================
    // 3) Overload: fallback ngày = hôm nay, một chiều
    // ========================
    public Tau(
        List<TauEntity> outboundList,
        String originGa,
        String destGa,
        String defaultLoaiVe,
        List<LoaiVe> listLoaiVe,
        TicketPaymentListener listener
    ) {
        this(
            outboundList,
            Collections.emptyList(),
            originGa,
            destGa,
            defaultLoaiVe,
            listLoaiVe,
            new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
            "Một chiều",
            "",
            listener
        );
    }

    // ========================
    // 4) Overload: chỉ truyền list chuyến, các param khác null/empty
    // ========================
    public Tau(List<TauEntity> danhSachTau) {
        this(
            danhSachTau,
            Collections.emptyList(),
            "",      // originGa
            "",      // destGa
            null,    // defaultLoaiVe
            null,    // listLoaiVe
            null,    // ngayDiStr
            null,    // loaiChuyen
            null,    // ngayVeStr
            null     // listener
        );
    }

    // ========================
    // 5) Overload: mặc định rỗng
    // ========================
    public Tau() {
        this(Collections.emptyList());
    }

	/** Khởi tạo UI */
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

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		JButton btnBack = new JButton("Quay lại");
		btnBack.setPreferredSize(new Dimension(140, 40));
		btnBack.setFont(btnBack.getFont().deriveFont(Font.BOLD, 16f));
		btnBack.setMargin(new Insets(8, 16, 8, 16));
		btnBack.addActionListener(e -> {
			selectedTrain = "";
			selectedCabin = "";
			selectedSeat = null;
			createTrainPanel();
			cabinPanel.removeAll();
			seatPanel.removeAll();
			cabinPanel.revalidate();
			seatPanel.revalidate();
			Component parent = this.getParent();
			if (parent instanceof JPanel) {
				CardLayout cl = (CardLayout) ((JPanel) parent).getLayout();
				cl.show((JPanel) parent, "TrangChu");
			}
		});
		southPanel.add(btnBack);
		add(southPanel, BorderLayout.SOUTH);
	}
	
	// ===========================
    // Cập nhật dữ liệu mới sau tìm kiếm
    // ===========================
	public void updateData(
		    List<TauEntity> danhSachTau,
		    String defaultLoaiVe,
		    List<LoaiVe> listLoaiVe,
		    String originGa,
		    String destGa,
		    String ngayDiStr,
		    String loaiChuyen,
		    String ngayVeStr
		) {
		    this.defaultLoaiVe = defaultLoaiVe;
		    this.listLoaiVe    = listLoaiVe != null ? new ArrayList<>(listLoaiVe) : Collections.emptyList();
		    this.originGa      = originGa;
		    this.destGa        = destGa;
		    this.ngayDiStr     = ngayDiStr;
		    this.loaiChuyen    = loaiChuyen;
		    this.ngayVeStr     = ngayVeStr;

		    loadEntityMap();      // nếu có
		    loadTrainInfos(danhSachTau);
		    selectedTrain = "";
		    selectedCabin = "";
		    selectedSeat  = null;
		    trainButtons.clear();
		    createTrainPanel();
		    cabinPanel.removeAll();
		    seatPanel.removeAll();
		    cabinPanel.revalidate();
		    seatPanel.revalidate();
		}
	
	/** Đổ entityMap từ outboundList để có thể truy xuất lại TauEntity theo mã */
    private void loadEntityMap() {
        entityMap.clear();
        for (TauEntity te : outboundList) {
            entityMap.put(te.getMaTau(), te);
        }
    }

    // ===========================
    // Load map trains từ danh sách TauEntity
    // ===========================
    private void loadTrainInfos(List<TauEntity> list) {
        trains.clear();
        entityMap.clear();                            // ← xóa toàn bộ bản đồ cũ
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
        for (TauEntity te : list) {
            // 1) thêm vào map hiển thị
            String di  = te.getGioKhoiHanh() != null ? fmt.format(te.getGioKhoiHanh()) : "??:??";
            String den = te.getGioDen()        != null ? fmt.format(te.getGioDen())        : "??:??";
            trains.put(te.getMaTau(),
                       new TrainInfo(te.getMaTau(), te.getTenTau(), di, den, te.getSoChoCon()));
            // 2) lưu TauEntity gốc để sau này truy xuất maGaDi/maGaDen
            entityMap.put(te.getMaTau(), te);
        }
    }


	/** Tạo các nút chuyến tàu */
	private void createTrainPanel() {
		trainPanel.removeAll();
		for (TrainInfo train : trains.values()) {
			JPanel trainButton = createTrainButton(train);
			trainPanel.add(trainButton);
		}
		trainPanel.revalidate();
		trainPanel.repaint();
	}

	/** Tạo một nút chuyến tàu */
	private JPanel createTrainButton(TrainInfo train) {
	    JPanel trainButton = new JPanel();
	    trainButton.setLayout(new BoxLayout(trainButton, BoxLayout.Y_AXIS));
	    trainButton.setPreferredSize(trainButtonSize);
	    trainButton.setOpaque(true);
	    trainButton.putClientProperty("trainId", train.getMaTau());

	    Color defaultColor  = new Color(240, 240, 240);
	    Color hoverColor    = new Color(200, 200, 200);
	    Color selectedColor = new Color(211, 211, 211);
	    trainButton.setBackground(defaultColor);

	    // icon tàu
	    ImageIcon icon = new ImageIcon("src/images/train_1.png");
	    Image scaledIcon = icon.getImage()
	                          .getScaledInstance(trainIconSize.width, trainIconSize.height, Image.SCALE_SMOOTH);
	    JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
	    iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    // tên tàu
	    JLabel idLabel = new JLabel(train.getTenTau());
	    idLabel.setFont(new Font("Arial", Font.BOLD, 14));
	    idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	 // *** HIỂN THỊ GA ĐI / GA ĐẾN từ originGa/destGa đã truyền vào Tau ***
	    JLabel fromLabel = new JLabel("Ga đi: " + originGa);
	    fromLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	    fromLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JLabel toLabel   = new JLabel("Ga đến: " + destGa);
	    toLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	    toLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


	    // giờ đi / giờ đến (như cũ)
	    JLabel deparLabel = new JLabel("TG đi: " + train.getGioKhoiHanh());
	    deparLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    JLabel arrLabel   = new JLabel("TG đến: " + train.getGioDen());
	    arrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    // ghép toàn bộ
	    trainButton.add(iconLabel);
	    trainButton.add(Box.createVerticalStrut(5));
	    trainButton.add(idLabel);
	    trainButton.add(Box.createVerticalStrut(3));
	    trainButton.add(fromLabel);
	    trainButton.add(toLabel);
	    trainButton.add(Box.createVerticalStrut(5));
	    trainButton.add(deparLabel);
	    trainButton.add(arrLabel);

	    // hover/selected effect (không thay đổi)
	    trainButton.addMouseListener(new MouseAdapter() {
	        @Override public void mouseEntered(MouseEvent e) {
	            if (!train.getMaTau().equals(selectedTrain))
	                trainButton.setBackground(hoverColor);
	        }
	        @Override public void mouseExited(MouseEvent e) {
	            if (!train.getMaTau().equals(selectedTrain))
	                trainButton.setBackground(defaultColor);
	        }
	        @Override public void mouseClicked(MouseEvent e) {
	            selectedTrain = train.getMaTau();
	            // bôi màu selected
	            for (JPanel btn : trainButtons) {
	                String id = (String) btn.getClientProperty("trainId");
	                btn.setBackground(id.equals(selectedTrain) ? selectedColor : defaultColor);
	            }
	            createCabinPanel();
	        }
	    });

	    trainButtons.add(trainButton);
	    return trainButton;
	}


	/** Tạo panel lựa chọn cabin với hai nút điều hướng */
	private void createCabinPanel() {
		cabinPanel.removeAll();
		cabinPanel.setLayout(new BorderLayout(5, 5));

		JPanel cabinsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
		cabinsContainer.setOpaque(false);
		cabinsContainer.add(createCabinButton("Đầu tàu", false));

		List<String> danhSachToa = DatabaseHelper.getCabinsByTrainId(selectedTrain);
		if (danhSachToa == null || danhSachToa.isEmpty()) {
			cabinPanel.add(new JLabel("Không có toa nào cho tàu này.", JLabel.CENTER), BorderLayout.CENTER);
		} else {
			for (String maToa : danhSachToa) {
				String text = "Toa " + maToa;
				JButton cabinButton = createCabinButton(text, true);
				cabinButton.putClientProperty("cabinId", maToa);
				cabinButton.addActionListener(e -> {
					selectedCabin = maToa;
					for (Component c : cabinsContainer.getComponents()) {
						if (c instanceof JButton btn && btn.isEnabled()) {
							String id = (String) btn.getClientProperty("cabinId");
							ImageIcon ic = id.equals(maToa) ? (ImageIcon) btn.getClientProperty("selectedIcon")
									: (ImageIcon) btn.getClientProperty("normalIcon");
							btn.setIcon(ic);
						}
					}
					loadSeatsForCabin(maToa);
				});
				cabinsContainer.add(cabinButton);
			}

			JPanel wrapper = new JPanel(new BorderLayout());
			wrapper.setOpaque(false);
			wrapper.add(createArrowButton("‹"), BorderLayout.WEST);
			wrapper.add(cabinsContainer, BorderLayout.CENTER);
			wrapper.add(createArrowButton("›"), BorderLayout.EAST);

			cabinPanel.add(wrapper, BorderLayout.CENTER);
		}

		cabinPanel.revalidate();
		cabinPanel.repaint();
	}

	/** Tạo nút cabin với icon, rollover và disabledIcon cho "Đầu tàu" */
	private JButton createCabinButton(String text, boolean isSelectable) {
		JButton button = new JButton();
		button.setPreferredSize(cabinButtonSize);
		button.setEnabled(isSelectable);

		String normalPath = text.equals("Đầu tàu") ? "/images/train-engine.png" : "/images/train-cargo.png";
		String hoverPath = text.equals("Đầu tàu") ? "/images/train-engine.png" : "/images/vietnam.png";
		String selectedPath = hoverPath;

		URL normalURL = getClass().getResource(normalPath);
		ImageIcon rawNormal = normalURL != null ? new ImageIcon(normalURL) : new ImageIcon("src/images" + normalPath);
		Image normalImg = rawNormal.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
				Image.SCALE_SMOOTH);
		ImageIcon normalScaled = new ImageIcon(normalImg);
		button.setIcon(normalScaled);
		button.putClientProperty("normalIcon", normalScaled);

		if (!isSelectable) {
			button.setDisabledIcon(normalScaled);
		}

		URL hoverURL = getClass().getResource(hoverPath);
		if (hoverURL != null) {
			ImageIcon rawHover = new ImageIcon(hoverURL);
			Image hoverImg = rawHover.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
					Image.SCALE_SMOOTH);
			button.setRolloverIcon(new ImageIcon(hoverImg));
			button.setRolloverEnabled(true);
		}

		URL selURL = getClass().getResource(selectedPath);
		if (selURL != null) {
			ImageIcon rawSel = new ImageIcon(selURL);
			Image selImg = rawSel.getImage().getScaledInstance(cabinButtonSize.width, cabinButtonSize.height,
					Image.SCALE_SMOOTH);
			button.putClientProperty("selectedIcon", new ImageIcon(selImg));
		}

		button.putClientProperty("cabinId", text);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setOpaque(false);

		return button;
	}

	/** Hiển thị seats của cabin với layout grid và bo góc */
	private void loadSeatsForCabin(String maToa) {
		seatPanel.removeAll();
		seatPanel.setLayout(new BorderLayout(10, 10));

		String cabinType = DatabaseHelper.getCabinDescription(maToa);
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Toa " + maToa + ": " + cabinType);
		tb.setTitleJustification(TitledBorder.CENTER);
		tb.setTitleFont(new Font("Arial", Font.BOLD, 14));
		seatPanel.setBorder(tb);

		List<GheInfo> danhSachGhe = getSeatsForCabin(maToa);
		if (danhSachGhe == null || danhSachGhe.isEmpty()) {
			seatPanel.add(new JLabel("Không có thông tin ghế cho toa này", JLabel.CENTER), BorderLayout.CENTER);
		} else {
			int rows = 4;
			int total = danhSachGhe.size();
			int cols = (int) Math.ceil((double) total / rows);
			int hgap = 8, vgap = 8;
			int side = Math.min(seatButtonSize.width, seatButtonSize.height);

			JPanel seatMap = new JPanel(new GridLayout(rows, cols, hgap, vgap)) {
				@Override
				protected void paintComponent(Graphics g) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(new Color(240, 240, 240));
					g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
					g2.dispose();
					super.paintComponent(g);
				}
			};
			seatMap.setOpaque(false);

			for (GheInfo ghe : danhSachGhe) {
				seatMap.add(createSeatButton(ghe, side));
			}
			for (int i = total; i < rows * cols; i++) {
				JLabel empty = new JLabel();
				empty.setPreferredSize(new Dimension(side, side));
				seatMap.add(empty);
			}

			JScrollPane sp = new JScrollPane(seatMap);
			sp.setBorder(null);
			int panelW = cols * side + (cols - 1) * hgap;
			int panelH = rows * side + (rows - 1) * vgap;
			sp.setPreferredSize(new Dimension(panelW, panelH));

			JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
			wrapper.setOpaque(false);
			wrapper.add(createArrowButton("‹"));
			wrapper.add(sp);
			wrapper.add(createArrowButton("›"));

			seatPanel.add(wrapper, BorderLayout.CENTER);
		}

		seatPanel.revalidate();
		seatPanel.repaint();
	}

	/** Nút điều hướng để chọn cabin trước/sau */
	private JButton createArrowButton(String arrow) {
		JButton b = new JButton(arrow);
		b.setFont(b.getFont().deriveFont(24f));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);

		b.addActionListener(e -> {
			JPanel wrapper = null;
			for (Component comp : cabinPanel.getComponents()) {
				if (comp instanceof JPanel p && p.getLayout() instanceof BorderLayout) {
					wrapper = p;
					break;
				}
			}
			if (wrapper == null)
				return;

			Component center = ((BorderLayout) wrapper.getLayout()).getLayoutComponent(wrapper, BorderLayout.CENTER);
			if (!(center instanceof JPanel cabinsContainer))
				return;

			List<JButton> cabinButtons = new ArrayList<>();
			for (Component c : cabinsContainer.getComponents()) {
				if (c instanceof JButton btn && btn.isEnabled()) {
					cabinButtons.add(btn);
				}
			}
			if (cabinButtons.isEmpty())
				return;

			int idx = 0;
			for (int i = 0; i < cabinButtons.size(); i++) {
				String id = (String) cabinButtons.get(i).getClientProperty("cabinId");
				if (Objects.equals(id, selectedCabin)) {
					idx = i;
					break;
				}
			}

			int delta = arrow.equals("›") ? 1 : -1;
			int newIdx = Math.max(0, Math.min(cabinButtons.size() - 1, idx + delta));
			cabinButtons.get(newIdx).doClick();
		});

		return b;
	}

	/** Tạo nút ghế bo tròn, hover, chọn đỏ */
	private JButton createSeatButton(GheInfo ghe, int side) {
		JButton b = new JButton(String.valueOf(ghe.soGhe)) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(side, side);
			}

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

		Color normal = new Color(200, 200, 200);
		Color hover = normal.brighter();
		b.setBackground(normal);
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (b != lastSelectedSeatButton)
					b.setBackground(hover);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (b != lastSelectedSeatButton)
					b.setBackground(normal);
			}
		});

		b.addActionListener(e -> {
			if (lastSelectedSeatButton != null) {
				lastSelectedSeatButton.setBackground(normal);
			}
			b.setBackground(Color.RED);
			lastSelectedSeatButton = b;

			selectedSeat = ghe;
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có muốn đặt vé với ghế số " + ghe.soGhe + " (" + ghe.loaiGhe + ")?", "Đặt vé",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				openDatVePanel();
			}
		});

		return b;
	}

	/** Mở dialog Đặt vé, truyền listener từ bên ngoài vào */
	private void openDatVePanel() {
	    // 1) Lấy thông tin chuyến và hành trình
	    String maTau   = selectedTrain;
	    TrainInfo ti   = trains.get(maTau);
	    String gioDi   = ti.getGioKhoiHanh();
	    String gioDen  = ti.getGioDen();
	    String ngayDi  = this.ngayDiStr;      // dd/MM/yyyy
	    String loaiVeName   = this.defaultLoaiVe;
	    String loaiChuyenUI = this.loaiChuyen; // “Một chiều” hoặc “Khứ hồi”
	    String ngayVeUI     = this.ngayVeStr;  // dd/MM/yyyy hoặc ""

	    // 2) Thông tin ghế & số vé
	    int cho        = selectedSeat.soGhe;
	    int soLuongVe  = 1; // hoặc cho phép thay đổi nếu cần

	    // 3) Tính tổng tiền
	    double unitPrice = listLoaiVe.stream()
	        .filter(lv -> lv.getTenLoaiVe().equals(loaiVeName))
	        .mapToDouble(LoaiVe::getGiaTien)
	        .findFirst()
	        .orElse(0);
	    double tongTien = unitPrice * soLuongVe;

	    // 4) Mã NV và mã loại vé
	    String username = Session.currentUsername;
	    String maNV     = DAO_NhanVien.getMaNVByUsername(username);
	    String maLoaiVe = listLoaiVe.stream()
	        .filter(lv -> lv.getTenLoaiVe().equals(loaiVeName))
	        .map(LoaiVe::getMaLoaiVe)
	        .findFirst()
	        .orElse(null);

	    // 5) Khởi tạo dialog Thanh toán
	    HoaDonThanhToan dlg = new HoaDonThanhToan(
	            (Frame) SwingUtilities.getWindowAncestor(this),
	            maNV,
	            null,         // maHK
	            maTau,
	            ti.getTenTau(),
	            gioDi,
	            gioDen,
	            ngayDi,       // "dd/MM/yyyy"
	            loaiVeName,
	            maLoaiVe,
	            cho,
	            soLuongVe,
	            "",           // tenKhach
	            "",           // cccd
	            "",           // sdt
	            tongTien,
	            originGa,     // ← ga đi lấy từ lớp Tau
	            destGa,       // ← ga đến lấy từ lớp Tau
	            loaiChuyenUI,
	            ngayVeUI,
	            this.listener
	        );
	        dlg.setVisible(true);
	}

	/** Lấy danh sách ghế từ DB */
	private List<GheInfo> getSeatsForCabin(String maToa) {
		return DatabaseHelper.getGheByToa(maToa);
	}

	/** Thông tin ghế */
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
}
