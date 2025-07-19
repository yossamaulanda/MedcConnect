package App;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ControllerRK {

    @FXML
    private VBox chatMessages;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendButton;

    @FXML
    private Button endConsultationButton, scheduleButton;

    @FXML
    private Button backButton;

    private boolean consultationStarted = true;

    @FXML
    public void initialize() {
        addDoctorMessage("Selamat datang! Saya dr. Bambang Riyadi. Ada yang bisa saya bantu?");
    }

    @FXML
    private void handleSendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addPatientMessage(message);
            messageInput.clear();

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() ->
                        addDoctorMessage("Terima kasih atas informasinya. Saya akan memeriksa lebih lanjut.")
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FXML
    private void handleEndConsultation() {
        if (consultationStarted) {
            consultationStarted = false;
            addDoctorMessage("Konsultasi telah selesai. Jadwal konsultasi akan dikirimkan melalui notifikasi.");
            addSystemMessage("🔔 Konsultasi diakhiri. Terima kasih telah menggunakan MEDCONNECT.");
        }
    }

    @FXML
    private void handleKirimJadwal() {
        NotificationManager.addNotification("📅 Anda menerima jadwal kontrol baru dari dr. Bambang Riyadi.");
        showAlert("Berhasil", "Notifikasi ditambahkan ke halaman notifikasi.");
    }

    @FXML
    private void handleScheduleFollowUp() {
        handleKirimJadwal();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/halamanksl.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke halaman konsultasi.");
        }
    }

    private void addPatientMessage(String text) {
        Label label = new Label("🧑 Anda:\n" + text);
        label.setStyle("-fx-background-color: #f48c2a; -fx-text-fill: white; -fx-padding: 8 12 8 12; -fx-background-radius: 10;");
        label.setWrapText(true);
        label.setMaxWidth(350);
        chatMessages.getChildren().add(label);
    }

    private void addDoctorMessage(String text) {
        Label label = new Label("👨‍⚕️ dr. Bambang Riyadi:\n" + text);
        label.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #000; -fx-padding: 8 12 8 12; -fx-background-radius: 10;");
        label.setWrapText(true);
        label.setMaxWidth(350);
        chatMessages.getChildren().add(label);
    }

    private void addSystemMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-style: italic; -fx-text-fill: gray; -fx-padding: 5 0 5 0;");
        chatMessages.getChildren().add(label);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

