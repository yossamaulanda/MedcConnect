package App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ControllerHaldok {
    
    @FXML
    private Label lblWelcome;
    
    @FXML
    private Label lblPasienHariIni;
    
    @FXML
    private Label lblKonsultasiAktif;
    
    @FXML
    private Label lblRatingDokter;
    
    @FXML
    private Button btnNotifikasi;
    
    @FXML
    private Button btnKonsultasiOnline;
    
    @FXML
    private Button btnJadwalPraktik; 
    
    @FXML
    private Button btnRiwayatPasien; 
    
    @FXML
    private Button btnProfilDokter;
    
    private String userRole = "DOKTER";
    
    @FXML
    private void initialize() {
        updateWelcomeMessage();
        loadStatistikDokter();
    }

    public void setUserRole(String role) {
        this.userRole = role;
        System.out.println("User Role set di ControllerHaldok: " + role);
        updateWelcomeMessage();
    }

    private void updateWelcomeMessage() {
        if (lblWelcome != null) {
            lblWelcome.setText("SELAMAT DATANG");
        }
    }

    private void loadStatistikDokter() {
        if (lblPasienHariIni != null) {
            lblPasienHariIni.setText("12");
        }
        if (lblKonsultasiAktif != null) {
            lblKonsultasiAktif.setText("5");
        }
        if (lblRatingDokter != null) {
            lblRatingDokter.setText("4.8");
        }
    }

    @FXML
    private void handleNotifikasi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("notifikasi.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Notifikasi");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman notifikasi: " + e.getMessage());
        }
    }

    @FXML
    private void handleKonsultasiOnline(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanchatdok.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnKonsultasiOnline.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Konsultasi Online - Panel Dokter");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman konsultasi: " + e.getMessage());
        }
    }

    @FXML
    private void handleJadwalPraktik(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanjadwaltemu.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnJadwalPraktik.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Jadwal Temu");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman jadwal temu: " + e.getMessage());
        }
    }

    @FXML
    private void handleRiwayatPasien(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanriwayat.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnRiwayatPasien.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chat Dokter");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman riwayat: " + e.getMessage());
        }
    }

    @FXML
    private void handleProfilDokter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanprofiledok.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnProfilDokter.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Profil Dokter");
        } catch (IOException e) {
            showAlert("Error", "Tidak dapat membuka halaman profil dokter: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getUserRole() {
        return userRole;
    }
}