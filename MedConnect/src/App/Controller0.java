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
    
    @FXML
    private void handlePasienClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman00.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnPasien.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Not Found");
            alert.setContentText("Cannot find halaman00.fxml file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleDokterClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman0000.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnDokter.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Not Found");
            alert.setContentText("Cannot find halaman0000.fxml file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addHoverEffect(btnPasien);
        addHoverEffect(btnDokter);
    }

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

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnPasien.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Navigation Error");
            alert.setContentText("Cannot load " + fxmlFile + " file. Please check if the file exists.");
            alert.showAndWait();
        }
    }
}