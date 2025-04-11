package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DangNhap extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel forgotPasswordLabel;
    private JCheckBox adminCheckBox, employeeCheckBox;
    private ButtonGroup roleGroup;

    public DangNhap() {
        setTitle("N13 Express");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(230, 230, 230));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.setBounds(0, 40, 400, 120);
        logoPanel.setBackground(new Color(230, 230, 230));

        ImageIcon trainIcon = new ImageIcon("src/images/train_login.png");
        Image img = trainIcon.getImage();
        Image resizedImg = img.getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        trainIcon = new ImageIcon(resizedImg);

        JLabel iconLabel = new JLabel(trainIcon);
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        logoPanel.add(iconLabel, BorderLayout.CENTER);

        JLabel appNameLabel = new JLabel("N13 Express");
        appNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        appNameLabel.setHorizontalAlignment(JLabel.CENTER);
        logoPanel.add(appNameLabel, BorderLayout.SOUTH);

        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(60, 180, 280, 30);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        usernameField = new JTextField();
        usernameField.setBounds(60, 210, 280, 40);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBounds(60, 270, 280, 30);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 300, 280, 40);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        roleGroup = new ButtonGroup();

        adminCheckBox = new JCheckBox("Admin");
        adminCheckBox.setBounds(60, 345, 100, 25);
        adminCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        adminCheckBox.setBackground(new Color(230, 230, 230));
        adminCheckBox.setSelected(true);

        employeeCheckBox = new JCheckBox("Nhân viên");
        employeeCheckBox.setBounds(170, 345, 100, 25);
        employeeCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
        employeeCheckBox.setBackground(new Color(230, 230, 230));

        roleGroup.add(adminCheckBox);
        roleGroup.add(employeeCheckBox);

        loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(60, 380, 130, 40);
        loginButton.setBackground(new Color(79, 157, 222));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        registerButton = new JButton("Đăng ký");
        registerButton.setBounds(210, 380, 130, 40);
        registerButton.setBackground(new Color(79, 157, 222));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        forgotPasswordLabel = new JLabel("Quên mật khẩu?");
        forgotPasswordLabel.setBounds(240, 430, 150, 30);
        forgotPasswordLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        forgotPasswordLabel.setForeground(Color.DARK_GRAY);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        mainPanel.add(logoPanel);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(adminCheckBox);
        mainPanel.add(employeeCheckBox);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);
        mainPanel.add(forgotPasswordLabel);

        setContentPane(mainPanel);
        addEventListeners();
    }

    private void addEventListeners() {
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = adminCheckBox.isSelected() ? "admin" : "nhanvien";

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(DangNhap.this,
                        "Vui lòng nhập đầy đủ thông tin đăng nhập",
                        "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            } else {
                if (kiemTraDangNhap(username, password, role)) {
                    JOptionPane.showMessageDialog(DangNhap.this,
                            "Đăng nhập thành công!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // Gọi Menu bằng SwingUtilities
                    SwingUtilities.invokeLater(() -> {
                        new Menu().setVisible(true);
                    });

                    dispose();
                } else {
                    JOptionPane.showMessageDialog(DangNhap.this,
                            "Tên đăng nhập, mật khẩu hoặc vai trò không đúng!",
                            "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(e -> {
            System.out.println("Registration button clicked");
        });

        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Forgot password clicked");
            }
        });
    }

    private boolean kiemTraDangNhap(String username, String password, String role) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyVeTau;encrypt=true;trustServerCertificate=true";
        String dbUser = "sa";
        String dbPass = "sapassword";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass)) {
            String sql = "SELECT * FROM TaiKhoan WHERE tenTK = ? AND matKhau = ? AND loaiTK = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến CSDL", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            // Thiết lập font mặc định nếu cần
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new DangNhap().setVisible(true));
    }
}
