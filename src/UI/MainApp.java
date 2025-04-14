package UI;

import connectDB.ConnectDB;
import javafx.application.Platform;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
    	ConnectDB.getConnection();
        try {
            // Cần đặt TRƯỚC khi tạo bất kỳ JFrame nào
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}