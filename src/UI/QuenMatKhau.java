package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import DAO.DAO_TaiKhoan;
import DAO.DAO_QuenMatKhau;

public class QuenMatKhau extends JFrame {
    private JTextField usernameField;
    private JPasswordField newPasswordField;
    private JButton resetButton;

    public QuenMatKhau() {
        setTitle("Khôi phục mật khẩu");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(30, 30, 120, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 30, 150, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Mật khẩu mới:");
        passwordLabel.setBounds(30, 80, 120, 25);
        add(passwordLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(150, 80, 150, 25);
        add(newPasswordField);

        resetButton = new JButton("Đặt lại mật khẩu");
        resetButton.setBounds(90, 140, 160, 30);
        add(resetButton);

        resetButton.addActionListener(e -> datLaiMatKhau());
    }

    private void datLaiMatKhau() {
        String username = usernameField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();

        if (username.isEmpty() || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        boolean success = DAO_QuenMatKhau.capNhatMatKhau(username, newPassword);

        if (success) {
            JOptionPane.showMessageDialog(this, "Đã cập nhật mật khẩu thành công!");
            dispose(); // Đóng cửa sổ
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy tài khoản!");
        }
    }
}
