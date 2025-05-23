package UI;

import entity.TauEntity;
import entity.TrainInfo;

import javax.swing.*;

import connectDB.DatabaseHelper;

import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
            trains.put(tau.getMaTau(), new TrainInfo(
                tau.getMaTau(), tau.getTenTau(), gioDi, gioDen, tau.getSoChoCon()
            ));
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

    private JPanel createTrainButton(TrainInfo train) {
        JPanel trainButton = new JPanel();
        trainButton.setLayout(new BoxLayout(trainButton, BoxLayout.Y_AXIS));
        trainButton.setPreferredSize(trainButtonSize);
        trainButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        trainButton.setBackground(train.getMaTau().equals(selectedTrain)
                ? new Color(135, 206, 250)
                : new Color(220, 220, 220));
        trainButton.setOpaque(true);

        ImageIcon icon = new ImageIcon("src/images/train_1.png");
        Image scaledIcon = icon.getImage().getScaledInstance(
                trainIconSize.width, trainIconSize.height, Image.SCALE_SMOOTH);
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

        trainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedTrain = train.getMaTau();
                createCabinPanel();
            }
        });

        return trainButton;
    }

    private void createCabinPanel() {
        cabinPanel.removeAll();
        JPanel cabinsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        cabinsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        cabinsContainer.add(createCabinButton("Đầu tàu", false));

        // Lấy danh sách mã toa dạng String
        List<String> danhSachToa = DatabaseHelper.getCabinsByTrainId(selectedTrain);
        if (danhSachToa == null || danhSachToa.isEmpty()) {
            cabinPanel.add(new JLabel("Không có toa nào cho tàu này."));
            cabinPanel.revalidate();
            cabinPanel.repaint();
            return;
        }

        for (String maToa : danhSachToa) {
            JButton cabinButton = createCabinButton("Toa " + maToa, true);
            cabinButton.addActionListener(e -> {
                selectedCabin = maToa;
                loadSeatsForCabin(maToa);
            });
            cabinsContainer.add(cabinButton);
        }

        cabinPanel.add(cabinsContainer);
        cabinPanel.revalidate();
        cabinPanel.repaint();
    }

    private JButton createCabinButton(String text, boolean isSelectable) {
        JButton button = new JButton(text);
        button.setPreferredSize(cabinButtonSize);
        button.setBackground(new Color(220, 220, 220));
        button.setEnabled(isSelectable);
        return button;
    }

    private void loadSeatsForCabin(String maToa) {
        seatPanel.removeAll();
        JPanel seatMapPanel = new JPanel(new GridBagLayout());
        List<GheInfo> danhSachGhe = getSeatsForCabin(maToa);

        if (danhSachGhe != null && !danhSachGhe.isEmpty()) {
            int cols = 4;
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            for (int i = 0; i < danhSachGhe.size(); i++) {
                GheInfo ghe = danhSachGhe.get(i);
                JButton seatButton = new JButton("Số " + ghe.soGhe + "\n" + ghe.loaiGhe);
                seatButton.setPreferredSize(seatButtonSize);
                seatButton.setBackground(new Color(60, 179, 113));
                seatButton.setForeground(Color.WHITE);
                seatButton.setOpaque(true);
                seatButton.setContentAreaFilled(true);
                seatButton.setBorderPainted(false);
                seatButton.addActionListener(e -> {
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
                gbc.gridx = i % cols;
                gbc.gridy = i / cols;
                seatMapPanel.add(seatButton, gbc);
            }
        } else {
            seatMapPanel.add(new JLabel("Không có thông tin ghế cho toa này"));
        }

        JScrollPane scrollPane = new JScrollPane(seatMapPanel);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        seatPanel.add(scrollPane);
        seatPanel.revalidate();
        seatPanel.repaint();
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

        VeTau.DatVePanel datVePanel = new VeTau.DatVePanel(
            maTau, gioDi, gioDen, ngayDi, cho, soLuongVe, null
        );

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
