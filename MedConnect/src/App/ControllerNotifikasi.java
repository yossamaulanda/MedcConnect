package App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;

public class ControllerNotifikasi {

    @FXML
    private Button backButton, chatDokterButton;
    
    @FXML
    private VBox notificationList;

    @FXML
    private void handleBack() {
        loadPage("/App/halaman1.fxml", "MEDCONNECT");
    }

    @FXML
    private void handleChatDokter() {
        loadPage("/App/halamanksl.fxml", "Ruang Konsultasi");
    }

    @FXML
    private void onBerandaClicked() {
        loadPage("/App/halaman1.fxml", "MEDCONNECT");
    }

    @FXML
    private void onProfileClicked() {
        loadPage("/App/halamanprofile.fxml", "Profile");
    }

    private void loadPage(String fxmlPath, String title) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace(); 
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        setupHover(chatDokterButton, "#f48c2a", "#ffffff", "transparent", "#f48c2a");
        setupHover(backButton, "#f48c2a", "#ffffff", "transparent", "#f48c2a");
        loadNotifications();
    }
    
    private void loadNotifications() {
        notificationList.getChildren().clear();
        
        if (NotificationManager.getNotifications().isEmpty()) {
            Label noNotifLabel = new Label("Belum ada notifikasi");
            noNotifLabel.setStyle("-fx-text-fill: #999; -fx-font-size: 14px; -fx-font-style: italic;");
            notificationList.getChildren().add(noNotifLabel);
        } else {
            for (NotificationManager.NotificationItem notification : NotificationManager.getNotifications()) {
                VBox notifBox = createNotificationBox(notification);
                notificationList.getChildren().add(notifBox);
            }
        }
    }
    
    private VBox createNotificationBox(NotificationManager.NotificationItem notification) {
        VBox notifBox = new VBox(5);
        notifBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #e9ecef; -fx-border-width: 1px; " +
                         "-fx-border-radius: 8px; -fx-background-radius: 8px;");
        notifBox.setPadding(new Insets(15));
        
        Label messageLabel = new Label(notification.getMessage());
        messageLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333;");
        messageLabel.setWrapText(true);
        
        Label timestampLabel = new Label(notification.getTimestamp());
        timestampLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        notifBox.getChildren().addAll(messageLabel, timestampLabel);
        
        return notifBox;
    }

    private void setupHover(Button button, String bgEnter, String textEnter, String bgExit, String textExit) {
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + bgEnter + "; -fx-text-fill: " + textEnter + "; -fx-font-weight: bold;"));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: " + bgExit + "; -fx-text-fill: " + textExit + "; -fx-font-weight: bold;"));
    }
}