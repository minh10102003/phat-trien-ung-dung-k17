package UI;

import javax.swing.*;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import video.VideoLoadingScreen;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class DangNhap extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton, registerButton;
	private JLabel forgotPasswordLabel;
	private JCheckBox adminCheckBox, employeeCheckBox;
	private ButtonGroup roleGroup;

	public DangNhap() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 550);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.WHITE);

		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new BorderLayout());
		logoPanel.setBounds(0, 40, 400, 120);
		logoPanel.setBackground(Color.WHITE);

		ImageIcon gifIcon = new ImageIcon("src/images/Train_GIF.gif");
		JLabel iconLabel = new JLabel(gifIcon);
		iconLabel.setHorizontalAlignment(JLabel.CENTER);

		iconLabel.setHorizontalAlignment(JLabel.CENTER);
		logoPanel.add(iconLabel, BorderLayout.CENTER);

		JLabel appNameLabel = new JLabel("N13 EXPRESS");
		appNameLabel.setFont(new Font("Roboto", Font.BOLD, 24));
		appNameLabel.setHorizontalAlignment(JLabel.CENTER);
		logoPanel.add(appNameLabel, BorderLayout.SOUTH);

		JLabel usernameLabel = new JLabel("Tên đăng nhập:");
		usernameLabel.setBounds(60, 180, 280, 30);
		usernameLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

		usernameField = new JTextField();
		usernameField.setBounds(60, 210, 280, 40);
		usernameField.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		usernameField.setBackground(new Color(217, 217, 217));
		usernameField.setFont(new Font("Consolas", Font.PLAIN, 14));

		JLabel passwordLabel = new JLabel("Mật khẩu:");
		passwordLabel.setBounds(60, 270, 280, 30);
		passwordLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

		passwordField = new JPasswordField();
		passwordField.setBounds(60, 300, 280, 40);
		passwordField.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		passwordField.setBackground(new Color(217, 217, 217));
		passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

		roleGroup = new ButtonGroup();

		adminCheckBox = new JCheckBox("Admin");
		adminCheckBox.setBounds(60, 345, 100, 25);
		adminCheckBox.setFont(new Font("Roboto", Font.PLAIN, 16));
		adminCheckBox.setBackground(Color.WHITE);
		adminCheckBox.setSelected(true);

		employeeCheckBox = new JCheckBox("Nhân viên");
		employeeCheckBox.setBounds(170, 345, 100, 25);
		employeeCheckBox.setFont(new Font("Roboto", Font.PLAIN, 16));
		employeeCheckBox.setBackground(Color.WHITE);

		roleGroup.add(adminCheckBox);
		roleGroup.add(employeeCheckBox);

		loginButton = new JButton("Đăng nhập");
		loginButton.setBounds(60, 380, 130, 40);
		loginButton.setBackground(new Color(79, 157, 222));
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		loginButton.setBorderPainted(false);
		loginButton.setFont(new Font("Roboto", Font.BOLD, 16));

		registerButton = new JButton("Đăng ký");
		registerButton.setBounds(210, 380, 130, 40);
		registerButton.setBackground(new Color(79, 157, 222));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		registerButton.setBorderPainted(false);
		registerButton.setFont(new Font("Roboto", Font.BOLD, 16));

		forgotPasswordLabel = new JLabel("Quên mật khẩu?");
		forgotPasswordLabel.setBounds(240, 430, 150, 30);
		forgotPasswordLabel.setFont(new Font("Roboto", Font.ITALIC, 14));
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
	                // Ẩn màn hình đăng nhập
	                dispose();

	                // Hiển thị cửa sổ loading có video bằng class tiện ích
	                new VideoLoadingScreen("E:\\Train (1).mp4");


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
}
