package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;

public class Tau extends JPanel {
    public Tau() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Quản lý chuyến tàu", JLabel.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // ==== Left panel: icons + older trains list ====
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Icons panel (5 icons)
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        iconsPanel.setBackground(new Color(240, 240, 240));
        String[] iconPaths = {
            "/images/train_1.png",
            "/images/train_1.png",
            "/images/train_1.png",
            "/images/train_1.png",
            "/images/train_1.png"
        };
        String[] schedules = {
            "SGO → PTH\n30-03-2025",
            "PTH → SGO\n01-04-2025",
            "SGO → PTH\n02-04-2025",
            "PTH → SGO\n04-04-2025",
            "SGO → NT\n07-04-2025"
        };
        for (int i = 0; i < iconPaths.length; i++) {
            JButton btn = new JButton("<html>" + schedules[i].replace("\n", "<br>") + "</html>");
            
            // Load icon an toàn:
            URL imgUrl = getClass().getResource(iconPaths[i]);
            ImageIcon icon;
            if (imgUrl != null) {
                icon = new ImageIcon(imgUrl);
            } else {
                System.err.println("Không tìm thấy resource: " + iconPaths[i]);
                // Thay bằng đường dẫn tuyệt đối đến file của bạn, hoặc icon mặc định:
                icon = new ImageIcon("src/images/train_1.png");
            }
            
            btn.setIcon(icon);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setPreferredSize(new Dimension(100, 100));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            iconsPanel.add(btn);
        }
        leftPanel.add(iconsPanel, BorderLayout.NORTH);

        // Bottom panel: label + list
        JLabel olderLabel = new JLabel("Danh sách các chuyến tàu khác");
        olderLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        olderLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        String[] olderTrains = {
            "SPT1 – 03-03-2025 – Sài Gòn → Phan Thiết",
            "SE3  – 05-03-2025 – Hà Nội → Đà Nẵng",
            "SE7  – 07-03-2025 – Hà Nội → Nha Trang",
            "SPT4 – 10-03-2025 – Sài Gòn → Phan Thiết"
        };
        JList<String> olderList = new JList<>(olderTrains);
        olderList.setVisibleRowCount(5);
        JScrollPane olderScroll = new JScrollPane(olderList);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(olderLabel, BorderLayout.NORTH);
        bottomPanel.add(olderScroll, BorderLayout.CENTER);

        leftPanel.add(bottomPanel, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        // ================================================

        // Table panel
        String[] columnNames = {
            "Mã tàu", "Loại ghế",
            "Ga đi", "Giờ đi", "Ngày đi",
            "Ga đến", "Giờ đến", "Ngày đến",
            "Giá vé", "Chọn ghế"
        };
        Object[][] data = {
            {"SPT2", "Ghế ngồi mềm", "Sài Gòn", "06:30", "30-11-2024",
             "Phan Thiết", "11:05", "30-11-2024", "204,000 VND", "Chọn ghế"},
            {"SPT3", "Ghế ngồi cứng", "Sài Gòn", "07:00", "30-11-2024",
             "Nha Trang", "12:30", "30-11-2024", "350,000 VND", "Chọn ghế"},
            {"SE5", "Giường nằm",    "Hà Nội",   "19:00", "30-11-2024",
             "Đà Nẵng",   "06:30", "01-12-2024", "750,000 VND", "Chọn ghế"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == getColumnCount() - 1;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);

        table.getColumnModel()
             .getColumn(table.getColumnCount() - 1)
             .setCellRenderer(new ButtonRenderer());
        table.getColumnModel()
             .getColumn(table.getColumnCount() - 1)
             .setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách các chuyến tàu chuẩn bị khởi hành:"));
        add(scroll, BorderLayout.CENTER);
    }

    // Renderer để hiển thị JButton trong JTable
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Chọn ghế");
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor để xử lý sự kiện click vào nút
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Chọn ghế");
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBorderPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int row, int column) {
            this.row = row;
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                DefaultTableModel m = (DefaultTableModel) ((JTable)button.getParent()).getModel();
                String maTau = m.getValueAt(row, 0).toString();
                JOptionPane.showMessageDialog(
                    null,
                    "Bạn đã chọn ghế trên tàu: " + maTau,
                    "Xác nhận",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            clicked = false;
            return "Chọn ghế";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
