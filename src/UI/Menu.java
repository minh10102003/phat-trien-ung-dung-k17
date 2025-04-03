package UI;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class Menu extends JFrame {
    private JPanel sidebar;
    private JPanel mainContent;    // Panel chứa các màn hình (cards)
    private CardLayout cardLayout; // Quản lý chuyển màn hình
    // Map chứa các nút điều hướng để dễ thay đổi trạng thái
    private Map<String, JButton> navButtons = new HashMap<>();

    public Menu() {
        super("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        // Sử dụng GridBagLayout cho frame chính
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // ======================= SIDEBAR PANEL =======================
        sidebar = new JPanel(new GridBagLayout());
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
        String[] buttonNames = { "Trang chủ", "Vé tàu", "Tàu", "Chuyến tàu", "Hóa đơn", "Nhân viên", "Thống kê", "Đăng xuất" };
        Color[] buttonColors = { 
            new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217),
            new Color(217, 217, 217), new Color(217, 217, 217), new Color(217, 217, 217), 
            new Color(217, 217, 217), new Color(217, 217, 217) 
        };

        for (int i = 0; i < buttonNames.length; i++) {
            final String btnName = buttonNames[i];
            JButton button = new JButton(btnName);
            // Ban đầu, disable nút "Trang chủ" để đánh dấu đây là màn hình hiện tại
            if (btnName.equals("Trang chủ")) {
                button.setEnabled(false);
            }
            button.setFont(new Font("Roboto", Font.BOLD, 13));
            button.setBackground(buttonColors[i]);
            
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
                    navButtons.get("Chuyến tàu").setEnabled(false); // Vô hiệu hóa nút "Chuyến tàu" khi đang ở màn hình này
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
        gbc.weightx = 0;   // Sidebar cố định kích thước theo chiều ngang
        gbc.weighty = 1.0;
        add(sidebar, gbc);

        // ======================= MAIN CONTENT VỚI CARDLAYOUT =======================
        cardLayout = new CardLayout();
        mainContent = new JPanel(cardLayout);

        // Màn hình "Trang chủ" (gồm các panel cũ)
        JPanel homePanel = createHomePanel();
        mainContent.add(homePanel, "TrangChu");

        // Màn hình "Quản lý nhân viên" (sử dụng class QL_NhanVien)
        NhanVien qlNhanVienPanel = new NhanVien();
        mainContent.add(qlNhanVienPanel, "NhanVien");
        
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
     * Phương thức tạo màn hình "Trang chủ" (bao gồm thông tin nhân viên, ảnh và bảng ca làm việc)
     */
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        GridBagConstraints mgbc = new GridBagConstraints();
        mgbc.insets = new Insets(10, 10, 10, 10);
        mgbc.fill = GridBagConstraints.BOTH;

        // Đọc file employee.txt
        Properties employeeProps = new Properties();
        try (InputStreamReader fis = new InputStreamReader(new FileInputStream("src/data/employee.txt"), "UTF-8")) {
            employeeProps.load(fis);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Employee Info Panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new TitledBorder("Thông tin nhân viên"));
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
            infoPanel.add(new JLabel(labels[i] + ":"), igbc);
            
            igbc.gridx = 1;
            String info = employeeProps.getProperty(keys[i], "N/A");
            infoPanel.add(new JLabel(info), igbc);
        }

        // Thêm infoPanel vào homePanel (chiếm khoảng 70% chiều ngang)
        mgbc.gridx = 0;
        mgbc.gridy = 0;
        mgbc.weightx = 0.7;
        mgbc.weighty = 1.0;
        homePanel.add(infoPanel, mgbc);

        // Image Panel: hiển thị ảnh nhân viên
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(new TitledBorder("Ảnh nhân viên"));
        ImageIcon icon = new ImageIcon("src/images/profilenhanvien.jpg");
        Image scaledImage = icon.getImage().getScaledInstance(170, 260, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage), SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        mgbc.gridx = 1;
        mgbc.gridy = 0;
        mgbc.weightx = 0.3;
        mgbc.weighty = 1.0;
        homePanel.add(imagePanel, mgbc);

        // Table Panel: hiển thị ca làm việc
        JPanel tablePanel = new JPanel(new GridBagLayout());
        tablePanel.setBorder(new TitledBorder("Ca làm việc"));
        String[] columnNames = { "Ngày", "Số giờ" };
        Object[][] data = { 
            { "12/03", 8 }, 
            { "14/03", 8 }, 
            { "16/03", 6 }, 
            { "20/03", 5 }, 
            { "21/03", 6.5 },
            { "22/03", 8 }, 
            { "25/03", 8 }, 
            { "30/03", 6 }, 
            { "01/04", 5 }, 
            { "05/04", 6.5 }
        };
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.gridx = 0;
        tgbc.gridy = 0;
        tgbc.fill = GridBagConstraints.BOTH;
        tgbc.weightx = 1.0;
        tgbc.weighty = 1.0;
        tablePanel.add(scrollPane, tgbc);

        mgbc.gridx = 0;
        mgbc.gridy = 1;
        mgbc.gridwidth = 2;
        mgbc.weighty = 0.7;
        homePanel.add(tablePanel, mgbc);

        return homePanel;
    }
}