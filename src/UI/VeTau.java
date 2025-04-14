package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.toedter.calendar.JDateChooser;

public class VeTau {
    public static class DatVePanel extends JPanel {
        // --- fields ---
        private JPanel topPanel, centerPanel, returnDatePanel;
        private JButton searchButton;
        private JRadioButton roundTripRadio, oneWayRadio;
        private ButtonGroup tripTypeGroup;
        private JDateChooser departureDateChooser, returnDateChooser;
        private JLabel departureDateLabel, departureDayLabel, returnDateLabel;
        private SimpleDateFormat dayFormat;

        private JLabel departureStationLabel, arrivalStationLabel;
        private JTextField nameField, phoneField, idField;

        public DatVePanel() {
            // 1. Thiết lập layout chính của DatVePanel
            setLayout(new BorderLayout(10, 10));
            setBackground(new Color(240, 240, 240));
            setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            dayFormat = new SimpleDateFormat("EEEE", new Locale("vi", "VN"));

            // 2. Tạo các sub‑panel riêng lẻ
            createTopPanel();
            createFormPanel();
            createSearchButton();
            JPanel infoPanel = createInfoPanel();

            // 3. Gom các phần form (centerPanel) và thông tin khách hàng (infoPanel) theo chiều dọc
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setOpaque(false);
            formPanel.add(centerPanel);
            formPanel.add(Box.createVerticalStrut(15));
            formPanel.add(infoPanel);

            // 4. Tạo bookingPanel chứa toàn bộ phần “Đặt vé”
            JPanel bookingPanel = new JPanel(new BorderLayout(10, 10));
            bookingPanel.setOpaque(false);
            // Phần đầu: chọn loại vé (topPanel)
            bookingPanel.add(topPanel, BorderLayout.NORTH);
            // Phần giữa: formPanel chứa các ô đặt vé
            bookingPanel.add(formPanel, BorderLayout.CENTER);
            // Phần cuối: nút “Đặt vé”
            bookingPanel.add(searchButton, BorderLayout.SOUTH);

            // 5. Tạo imagePanel (chỗ để bạn tự thêm hình ảnh)
            JPanel imagePanel = new JPanel();
            imagePanel.setBackground(Color.WHITE);
            // Bạn có thể thiết lập layout hoặc add hình ảnh theo ý muốn, ví dụ:
            // imagePanel.setLayout(new BorderLayout());
            // imagePanel.add(new JLabel(new ImageIcon("duong_dan_anh.png")), BorderLayout.CENTER);

            // 6. Gom bookingPanel và imagePanel vào contentWrapper
            JPanel contentWrapper = new JPanel(new BorderLayout());
            contentWrapper.add(bookingPanel, BorderLayout.WEST);
            contentWrapper.add(imagePanel, BorderLayout.CENTER);

            // 7. Thêm contentWrapper vào DatVePanel (không cần thêm topPanel hay searchButton riêng biệt nữa)
            add(contentWrapper, BorderLayout.CENTER);
        }

        private void createTopPanel() {
            topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            topPanel.setOpaque(false);

            roundTripRadio = new JRadioButton("Khứ hồi");
            oneWayRadio    = new JRadioButton("Một chiều");
            roundTripRadio.setFont(new Font("Arial", Font.PLAIN, 14));
            oneWayRadio.setFont(new Font("Arial", Font.PLAIN, 14));
            roundTripRadio.setOpaque(false);
            oneWayRadio.setOpaque(false);

            tripTypeGroup = new ButtonGroup();
            tripTypeGroup.add(roundTripRadio);
            tripTypeGroup.add(oneWayRadio);

            // Khi chọn, show/hide panel Ngày về
            roundTripRadio.addActionListener(e -> {
                returnDateChooser.setEnabled(true);
                returnDatePanel.setVisible(true);
                centerPanel.revalidate(); centerPanel.repaint();
            });
            oneWayRadio.addActionListener(e -> {
                returnDateChooser.setEnabled(false);
                returnDatePanel.setVisible(false);
                centerPanel.revalidate(); centerPanel.repaint();
            });

            topPanel.add(roundTripRadio);
            topPanel.add(oneWayRadio);
        }

        private void createFormPanel() {
            // GridLayout 3x2 để chứa 5 ô + 1 ô trống
            centerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            centerPanel.setOpaque(false);

            // Ga đi / Ga đến
            centerPanel.add(createLocationPanel("Ga đi",  "Chọn ga đi"));
            centerPanel.add(createLocationPanel("Ga đến", "Chọn ga đến"));
            // Số vé
            centerPanel.add(createPassengerPanel());
            // Ngày đi
            centerPanel.add(createDepartureDatePanel());
            // Ngày về (có thể ẩn)
            centerPanel.add(returnDatePanel = createReturnDatePanel());
            // Ô trống
            centerPanel.add(Box.createGlue());
        }

        private JPanel createInfoPanel() {
            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setOpaque(false);

            JLabel lblName  = new JLabel("Tên khách hàng:");
            JLabel lblPhone = new JLabel("Số điện thoại:");
            JLabel lblID    = new JLabel("CCCD:");
            for (JLabel lbl : new JLabel[]{lblName, lblPhone, lblID}) {
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
            }

            nameField  = new JTextField();
            phoneField = new JTextField();
            idField    = new JTextField();

            panel.add(lblName);  panel.add(nameField);
            panel.add(lblPhone); panel.add(phoneField);
            panel.add(lblID);    panel.add(idField);

            return panel;
        }

        private void createSearchButton() {
            searchButton = new JButton("Đặt vé");
            searchButton.setFont(new Font("Arial", Font.BOLD, 16));
            searchButton.setForeground(Color.WHITE);
            searchButton.setBackground(new Color(255, 140, 0));
            searchButton.setFocusPainted(false);
            searchButton.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            searchButton.setOpaque(true);
            searchButton.setContentAreaFilled(true);
            searchButton.setBorderPainted(false);

            searchButton.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    searchButton.setBackground(new Color(255, 160, 40));
                }
                @Override public void mouseExited(MouseEvent e) {
                    searchButton.setBackground(new Color(255, 140, 0));
                }
            });

            searchButton.addActionListener(e -> {
                // 1. Validate Ga đi / Ga đến
                String dep = departureStationLabel.getText();
                String arr = arrivalStationLabel.getText();
                if (dep.equals("Chọn ga đi") || arr.equals("Chọn ga đến")) {
                    JOptionPane.showMessageDialog(
                        DatVePanel.this,
                        "Vui lòng chọn cả Ga đi và Ga đến.",
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                // 2. Nếu Khứ hồi thì phải chọn Ngày về
                if (roundTripRadio.isSelected()
                    && returnDateLabel.getText().equals("Chọn nếu mua vé về")) {
                    JOptionPane.showMessageDialog(
                        DatVePanel.this,
                        "Vui lòng chọn Ngày về.",
                        "Lỗi nhập liệu",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // 3. Hiển thị thông tin
                StringBuilder msg = new StringBuilder("Thông tin tìm kiếm:\n");
                msg.append("- Ga đi: ").append(dep).append("\n");
                msg.append("- Ga đến: ").append(arr).append("\n");
                msg.append("- Loại vé: ")
                   .append(roundTripRadio.isSelected() ? "Khứ hồi" : "Một chiều").append("\n");
                msg.append("- Ngày đi: ").append(departureDateLabel.getText()).append("\n");
                if (roundTripRadio.isSelected()) {
                    msg.append("- Ngày về: ").append(returnDateLabel.getText()).append("\n");
                }
                msg.append("- Tên khách hàng: ").append(nameField.getText()).append("\n");
                msg.append("- SĐT: ").append(phoneField.getText()).append("\n");
                msg.append("- CCCD: ").append(idField.getText()).append("\n");

                JOptionPane.showMessageDialog(
                    DatVePanel.this,
                    msg.toString(),
                    "Tìm chuyến tàu",
                    JOptionPane.INFORMATION_MESSAGE
                );
            });
        }

        // ===== Các helper tạo sub‑panel =====

        private JPanel createLocationPanel(String title, String placeholder) {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1,true),
                BorderFactory.createEmptyBorder(10,10,10,10)
            ));
            panel.setBackground(Color.WHITE);

            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
            lblTitle.setForeground(Color.GRAY);

            JLabel placeholderLabel = new JLabel(placeholder);
            placeholderLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            // Gán để validate
            if (title.equals("Ga đi"))  departureStationLabel = placeholderLabel;
            if (title.equals("Ga đến")) arrivalStationLabel   = placeholderLabel;

            JPanel iconP = new JPanel(new BorderLayout());
            iconP.setOpaque(false);
            iconP.add(new JLabel(createTrainIcon()), BorderLayout.WEST);

            JPanel textP = new JPanel(new GridLayout(2,1));
            textP.setOpaque(false);
            textP.add(lblTitle);
            textP.add(placeholderLabel);

            panel.add(iconP, BorderLayout.WEST);
            panel.add(textP, BorderLayout.CENTER);

            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    // TODO: hiện danh sách ga, sau đó:
                    // placeholderLabel.setText(tênGaChọn);
                }
                @Override public void mouseEntered(MouseEvent e) { panel.setBackground(new Color(245,245,245)); }
                @Override public void mouseExited(MouseEvent e)  { panel.setBackground(Color.WHITE); }
            });

            return panel;
        }

        private JPanel createPassengerPanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1,true),
                BorderFactory.createEmptyBorder(10,10,10,10)
            ));
            panel.setBackground(Color.WHITE);

            JLabel lblTitle = new JLabel("Số lượng vé");
            lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
            lblTitle.setForeground(Color.GRAY);

            JLabel lblQty = new JLabel("1 vé");
            lblQty.setFont(new Font("Arial", Font.PLAIN, 14));

            JPanel iconP = new JPanel(new BorderLayout());
            iconP.setOpaque(false);
            iconP.add(new JLabel(createPersonIcon()), BorderLayout.WEST);

            JPanel textP = new JPanel(new GridLayout(2,1));
            textP.setOpaque(false);
            textP.add(lblTitle);
            textP.add(lblQty);

            panel.add(iconP, BorderLayout.WEST);
            panel.add(textP, BorderLayout.CENTER);

            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    String[] opts = {"1 vé","2 vé","3 vé","4 vé","5 vé"};
                    String sel = (String) JOptionPane.showInputDialog(
                        panel, "Chọn số lượng vé:", "Số lượng vé",
                        JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]
                    );
                    if (sel != null) lblQty.setText(sel);
                }
                @Override public void mouseEntered(MouseEvent e) { panel.setBackground(new Color(245,245,245)); }
                @Override public void mouseExited(MouseEvent e)  { panel.setBackground(Color.WHITE); }
            });

            return panel;
        }

        private JPanel createDepartureDatePanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1,true),
                BorderFactory.createEmptyBorder(10,10,10,10)
            ));
            panel.setBackground(Color.WHITE);

            departureDateChooser = new JDateChooser(new Date());
            departureDateChooser.setDateFormatString("dd/MM/yyyy");
            departureDateChooser.setOpaque(false);

            Calendar cal = Calendar.getInstance();
            cal.setTime(departureDateChooser.getDate());
            int d = cal.get(Calendar.DAY_OF_MONTH), m = cal.get(Calendar.MONTH)+1;

            departureDateLabel = new JLabel(d + " tháng " + m);
            departureDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            departureDayLabel  = new JLabel(dayFormat.format(departureDateChooser.getDate()));
            departureDayLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            departureDayLabel.setForeground(Color.GRAY);

            JPanel iconP = new JPanel(new BorderLayout());
            iconP.setOpaque(false);
            iconP.add(new JLabel(createCalendarIcon()), BorderLayout.WEST);

            JPanel textP = new JPanel(new GridLayout(2,1));
            textP.setOpaque(false);
            textP.add(departureDateLabel);
            textP.add(departureDayLabel);

            panel.add(iconP, BorderLayout.WEST);
            panel.add(textP, BorderLayout.CENTER);

            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Chọn ngày đi", true);
                    JDateChooser chooser = new JDateChooser(departureDateChooser.getDate());
                    chooser.setPreferredSize(new Dimension(200, 30));
                    JButton ok = new JButton("Chọn");
                    ok.addActionListener(ae -> {
                        departureDateChooser.setDate(chooser.getDate());
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(chooser.getDate());
                        departureDateLabel.setText(c2.get(Calendar.DAY_OF_MONTH) + " tháng " + (c2.get(Calendar.MONTH) + 1));
                        departureDayLabel.setText(dayFormat.format(chooser.getDate()));
                        dlg.dispose();
                    });
                    JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    btnP.add(ok);
                    JPanel cnt = new JPanel(new BorderLayout(10,10));
                    cnt.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    cnt.add(chooser, BorderLayout.CENTER);
                    cnt.add(btnP, BorderLayout.SOUTH);
                    dlg.add(cnt);
                    dlg.pack();
                    dlg.setLocationRelativeTo(panel);
                    dlg.setVisible(true);
                }
                @Override public void mouseEntered(MouseEvent e) { panel.setBackground(new Color(245,245,245)); }
                @Override public void mouseExited(MouseEvent e)  { panel.setBackground(Color.WHITE); }
            });

            return panel;
        }

        private JPanel createReturnDatePanel() {
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220),1,true),
                BorderFactory.createEmptyBorder(10,10,10,10)
            ));
            panel.setBackground(Color.WHITE);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            returnDateChooser = new JDateChooser(cal.getTime());
            returnDateChooser.setDateFormatString("dd/MM/yyyy");
            returnDateChooser.setOpaque(false);

            returnDateLabel = new JLabel("Chọn nếu mua vé về");
            returnDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JPanel iconP = new JPanel(new BorderLayout());
            iconP.setOpaque(false);
            iconP.add(new JLabel(createCalendarIcon()), BorderLayout.WEST);

            JPanel textP = new JPanel(new GridLayout(2,1));
            textP.setOpaque(false);
            textP.add(new JLabel("Ngày về (Khứ hồi)"));
            textP.add(returnDateLabel);

            panel.add(iconP, BorderLayout.WEST);
            panel.add(textP, BorderLayout.CENTER);

            panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    if (!returnDateChooser.isEnabled()) return;
                    JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(panel), "Chọn ngày về", true);
                    JDateChooser chooser = new JDateChooser(returnDateChooser.getDate());
                    chooser.setPreferredSize(new Dimension(200, 30));
                    JButton ok = new JButton("Chọn");
                    ok.addActionListener(ae -> {
                        returnDateChooser.setDate(chooser.getDate());
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(chooser.getDate());
                        returnDateLabel.setText(c2.get(Calendar.DAY_OF_MONTH) + " tháng " + (c2.get(Calendar.MONTH) + 1));
                        dlg.dispose();
                    });
                    JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    btnP.add(ok);
                    JPanel cnt = new JPanel(new BorderLayout(10,10));
                    cnt.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    cnt.add(chooser, BorderLayout.CENTER);
                    cnt.add(btnP, BorderLayout.SOUTH);
                    dlg.add(cnt);
                    dlg.pack();
                    dlg.setLocationRelativeTo(panel);
                    dlg.setVisible(true);
                }
                @Override public void mouseEntered(MouseEvent e) {
                    if (returnDateChooser.isEnabled()) panel.setBackground(new Color(245,245,245));
                }
                @Override public void mouseExited(MouseEvent e)  { panel.setBackground(Color.WHITE); }
            });

            return panel;
        }

        // Ví dụ icon mặc định
        private Icon createTrainIcon()   { return UIManager.getIcon("FileView.fileIcon"); }
        private Icon createPersonIcon()  { return UIManager.getIcon("FileView.directoryIcon"); }
        private Icon createCalendarIcon(){ return UIManager.getIcon("FileChooser.homeFolderIcon"); }
    }
}
