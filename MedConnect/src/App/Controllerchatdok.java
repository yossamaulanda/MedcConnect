package App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class Controllerchatdok {
    
    @FXML
    private Button btnBack;
    
    @FXML
    private ComboBox<String> filterComboBox;
    
    @FXML
    private void initialize() {
        setupFilterComboBox();
        loadChatList();
    }
    
    private void setupFilterComboBox() {
        if (filterComboBox != null) {
            filterComboBox.getItems().addAll(
                "Semua Pasien",
                "Pasien Aktif",
                "Pasien Baru",
                "Konsultasi Selesai"
            );
            filterComboBox.setValue("Semua Pasien");
        }
    }
    
    private void loadChatList() {
        System.out.println("Chat list loaded");
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamandokter.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Dashboard Dokter");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat kembali ke dashboard: " + e.getMessage());
        }
    }
    

    @FXML
    private void handleOpenChatAzmi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanksd.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chat dengan Azmi");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka chat dengan Azmi: " + e.getMessage());
        }
    }
    

    @FXML
    private void handleOpenChatIbra() {
        openChatWindow("Ibra");
    }
    
    @FXML
    private void handleOpenChatAbdil() {
        openChatWindow("Abdil");
    }
    
    private void openChatWindow(String patientName) {
        try {
            showInfo("Chat", "Membuka chat dengan " + patientName);
        } catch (Exception e) {
            showAlert("Error", "Tidak dapat membuka chat dengan " + patientName + ": " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void addChatItem(String name, String lastMessage, String time, boolean hasNotification) {
        System.out.println("Adding chat item: " + name);
    }
}