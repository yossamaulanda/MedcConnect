package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller0 implements Initializable {
    
    @FXML
    private Button btnPasien;
    
    @FXML
    private Button btnDokter;
    
    /**
     * Handles the action when "Pilih Pasien" button is clicked
     */
    @FXML
    private void handlePasienClick(ActionEvent event) {
        try {
            // Load halaman00.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman00.fxml"));
            Parent root = loader.load();
            
            // Get current stage
            Stage stage = (Stage) btnPasien.getScene().getWindow();
            
            // Create new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert if file not found
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Not Found");
            alert.setContentText("Cannot find halaman00.fxml file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
    
    /**
     * Handles the action when "Pilih Dokter" button is clicked
     */
    @FXML
    private void handleDokterClick(ActionEvent event) {
        try {
            // Load halaman0000.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman0000.fxml"));
            Parent root = loader.load();
            
            // Get current stage
            Stage stage = (Stage) btnDokter.getScene().getWindow();
            
            // Create new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            // Show error alert if file not found
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Not Found");
            alert.setContentText("Cannot find halaman0000.fxml file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
    
    /**
     * Initialize method called after FXML loading
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add hover effects to buttons
        addHoverEffect(btnPasien);
        addHoverEffect(btnDokter);
    }
    
    /**
     * Adds hover effects to buttons
     */
    private void addHoverEffect(Button button) {
        String originalStyle = button.getStyle();
        
        button.setOnMouseEntered(e -> {
            if (button == btnPasien) {
                button.setStyle(originalStyle + "-fx-background-color: #d1691a;");
            } else if (button == btnDokter) {
                button.setStyle(originalStyle + "-fx-background-color: #d1691a;");
            }
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(originalStyle);
        });
    }
    
    /**
     * Utility method for scene navigation (to be implemented)
     */
    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            // Get current stage
            Stage stage = (Stage) btnPasien.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            
            // Show error alert
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Cannot load " + fxmlFile + " file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
}