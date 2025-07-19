package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class controller1 {

    @FXML private ImageView imageDokter;
    @FXML private ImageView imageRuang;
    @FXML private Button btnKonsultasiOnline;

    @FXML
    public void initialize() {
        try {
            Image dokterImage = new Image(getClass().getResource("/assets/dokter.png").toExternalForm());
            Image ruangImage = new Image(getClass().getResource("/assets/ruang.png").toExternalForm());
            imageDokter.setImage(dokterImage);
            imageRuang.setImage(ruangImage);
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar: " + e.getMessage());
        }
    }

    @FXML
    private void onMenuClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();

        if (buttonText.equals("Konsultasi Online")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("halaman2.fxml"));
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman Konsultasi Online.");
            }

        } else if (buttonText.equals("Profile")) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanprofile.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Profile - MEDCONNECT");
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman Profile: " + e.getMessage());
            }

        } else if (buttonText.equals("Ruang Konsultasi")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("halamanksl.fxml"));
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman Ruang Konsultasi.");
            }

        } else if (buttonText.equals("Tabel Pengingat")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("halamantabel.fxml"));
                Stage stage = (Stage) clickedButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman Beranda.");
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Menu Dipilih");
            alert.setHeaderText(null);
            alert.setContentText("Fitur \"" + buttonText + "\" belum tersedia.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onNotificationClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("halamannotifikasi.fxml"));
            Stage stage = (Stage) btnKonsultasiOnline.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Konsultasi - MEDCONNECT");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal membuka halaman notifikasi.");
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