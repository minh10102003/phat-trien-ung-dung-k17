package UI;

import DAO.DAO_TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DangKy extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JCheckBox adminCheckBox, employeeCheckBox;
    private ButtonGroup roleGroup;

    public DangKy() {
        setTitle("Đăng Ký Tài Khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(230, 230, 230));

        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(60, 50, 280, 30);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        usernameField = new JTextField();
        usernameField.setBounds(60, 80, 280, 40);

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBounds(60, 130, 280, 30);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordField = new JPasswordField();
        passwordField.setBounds(60, 160, 280, 40);

        roleGroup = new ButtonGroup();
        adminCheckBox = new JCheckBox("Admin");
        adminCheckBox.setBounds(60, 210, 100, 25);
        adminCheckBox.setBackground(new Color(230, 230, 230));
        adminCheckBox.setSelected(true);

        employeeCheckBox = new JCheckBox("Nhân viên");
        employeeCheckBox.setBounds(170, 210, 100, 25);
        employeeCheckBox.setBackground(new Color(230, 230, 230));

        roleGroup.add(adminCheckBox);
        roleGroup.add(employeeCheckBox);

        registerButton = new JButton("Đăng ký");
        registerButton.setBounds(60, 250, 280, 40);
        registerButton.setBackground(new Color(79, 157, 222));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(adminCheckBox);
        panel.add(employeeCheckBox);
        panel.add(registerButton);

        setContentPane(panel);
        addEventListeners();
    }

    private void addEventListeners() {
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = adminCheckBox.isSelected() ? "admin" : "nhanvien";

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(DangKy.this,
                        "Vui lòng nhập đầy đủ thông tin đăng ký",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                DAO_TaiKhoan dao = new DAO_TaiKhoan();
                if (dao.dangKyTaiKhoan(username, password, role)) {
                    JOptionPane.showMessageDialog(DangKy.this,
                            "Đăng ký thành công!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Đóng cửa sổ đăng ký
                } else {
                    JOptionPane.showMessageDialog(DangKy.this,
                            "Tên tài khoản đã tồn tại. Vui lòng chọn tên khác.",
                            "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
