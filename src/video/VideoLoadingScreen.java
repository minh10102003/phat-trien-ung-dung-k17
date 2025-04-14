package video;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.io.File;

import UI.Menu;

public class VideoLoadingScreen {

    private JDialog dialog;

    public VideoLoadingScreen(String videoPath) {
        // Tạo khung Swing không viền
        dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setSize(450, 450); // Cỡ khung video phù hợp
        dialog.setLocationRelativeTo(null);

        // Tạo JFXPanel để nhúng JavaFX vào Swing
        JFXPanel jfxPanel = new JFXPanel();
        dialog.add(jfxPanel);
        dialog.setVisible(true); // Hiển thị khung chứa video

        // Chạy JavaFX trên luồng JavaFX
        Platform.runLater(() -> {
            try {
                // Load video từ đường dẫn tuyệt đối
                File file = new File(videoPath);
                if (!file.exists()) {
                    System.err.println("File video không tồn tại: " + videoPath);
                    return;
                }

                Media media = new Media(file.toURI().toString());
                MediaPlayer player = new MediaPlayer(media);
                MediaView mediaView = new MediaView(player);

                // Cài đặt kích thước và tỉ lệ video
                mediaView.setFitWidth(450);
                mediaView.setFitHeight(450);
                mediaView.setPreserveRatio(true);

                // Đăng ký bắt lỗi nếu có
                player.setOnError(() -> {
                    System.err.println("Lỗi player: " + player.getError());
                });
                media.setOnError(() -> {
                    System.err.println("Lỗi media: " + media.getError());
                });

                // Hiển thị video trong scene
                StackPane root = new StackPane(mediaView);
                Scene scene = new Scene(root, 450, 450);
                jfxPanel.setScene(scene);

                // Tự động phát
                player.setAutoPlay(true);

                // Khi phát xong video
                player.setOnEndOfMedia(() -> {
                    SwingUtilities.invokeLater(() -> {
                        dialog.dispose(); // Tắt khung video
                        new Menu().setVisible(true); // Mở màn hình Menu
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
