package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Controllerdr2 {

    @FXML
    private Button backButton;
    
    @FXML
    private Button chatButton;

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("halaman2.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal kembali ke halaman dokter.");
        }
    }

    @FXML
    private void handleChat(ActionEvent event) {
        try {
            // Navigasi ke halaman chat (halamanrk2.fxml)
            Parent root = FXMLLoader.load(getClass().getResource("halamanrk2.fxml"));
            Stage stage = (Stage) chatButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal membuka halaman chat.");
        }
    }

    @FXML
    private void onMenuClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        String targetFxml = "";

        if (buttonText.equals("🏠 Beranda")) {
            targetFxml = "halaman1.fxml";
        } else if (buttonText.equals("Profile")) {
            targetFxml = "halamanprofile.fxml";
        }

        if (!targetFxml.isEmpty()) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(targetFxml));
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman " + buttonText);
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText("Terjadi kesalahan");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
