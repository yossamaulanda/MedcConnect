package App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;

public class ProfileController {

    @FXML private Label lblNamaUser;
    @FXML private TextField txtNamaLengkap;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtTanggalLahir;
    @FXML private TextField txtAlamat;
    @FXML private TextField txtGolonganDarah;
    @FXML private TextField txtTinggiBadan;
    @FXML private TextField txtBeratBadan;
    @FXML private TextField txtAlergi;
    @FXML private TextField txtRiwayatPenyakit;

    @FXML
    public void initialize() {
        loadUserData();
        addHoverEffects();
    }

    private void addHoverEffects() {
        TextField[] fields = {txtNamaLengkap, txtEmail, txtTelepon, txtTanggalLahir, 
                            txtAlamat, txtGolonganDarah, txtTinggiBadan, txtBeratBadan, 
                            txtAlergi, txtRiwayatPenyakit};
        
        for (TextField field : fields) {
            field.setOnMouseEntered(e -> {
                field.setStyle(field.getStyle() + "; -fx-border-color: #667eea; -fx-border-width: 2px;");
            });
            
            field.setOnMouseExited(e -> {
                field.setStyle(field.getStyle().replace("; -fx-border-color: #667eea; -fx-border-width: 2px;", ""));
            });
        }
    }

    private void loadUserData() {
        txtNamaLengkap.setText("");
        txtEmail.setText("");
        txtTelepon.setText("");
        txtTanggalLahir.setText("");
        txtAlamat.setText("");
        txtGolonganDarah.setText("");
        txtTinggiBadan.setText("");
        txtBeratBadan.setText("");
        txtAlergi.setText("");
        txtRiwayatPenyakit.setText("");
        
        lblNamaUser.setText("Pasien");
    }

    @FXML
    private void onSimpanClicked() {
        if (isInputValid()) {
            try {
                saveUserData();
                String namaLengkap = txtNamaLengkap.getText().trim();
                if (!namaLengkap.isEmpty()) {
                    lblNamaUser.setText(namaLengkap);
                } else {
                    lblNamaUser.setText("Pasien");
                }
                
                showSuccessAlert("Berhasil", "Data profile berhasil disimpan! ✅");
                
            } catch (Exception e) {
                showErrorAlert("Gagal menyimpan data profile: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onResetClicked() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("🔄 Konfirmasi Reset");
        alert.setHeaderText("Reset Data Profile");
        alert.setContentText("Apakah Anda yakin ingin mereset semua data ke nilai default?");
        alert.getDialogPane().setStyle("-fx-background-color: #f7fafc; -fx-font-family: 'Segoe UI';");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            loadUserData();
            showSuccessAlert("Reset Berhasil", "Data profile telah direset ke nilai default. 🔄");
        }
    }

    @FXML
    private void onEditPhotoClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("📸 Pilih Foto Profile");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) txtNamaLengkap.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            showSuccessAlert("Foto Profile", "Foto profile akan diupdate dengan: " + selectedFile.getName() + " 📸");
        }
    }

    @FXML
    private void onBackClicked() {
        onBerandaClicked();
    }

    @FXML
    private void onBerandaClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("halaman1.fxml"));
            Stage stage = (Stage) txtNamaLengkap.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MedConnect - Beranda");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Gagal membuka halaman beranda: " + e.getMessage());
        }
    }

    @FXML
    private void onLogoutClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("🚪 Konfirmasi Logout");
        alert.setHeaderText("Keluar dari MedConnect");
        alert.setContentText("Apakah Anda yakin ingin keluar dari aplikasi?");
        alert.getDialogPane().setStyle("-fx-background-color: #f7fafc; -fx-font-family: 'Segoe UI';");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("halaman0.fxml"));
                Stage stage = (Stage) txtNamaLengkap.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("MedConnect - Login");
                
            } catch (Exception e) {
                e.printStackTrace();
                showErrorAlert("Gagal melakukan logout: " + e.getMessage());
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (txtNamaLengkap.getText() == null || txtNamaLengkap.getText().trim().isEmpty()) {
            errorMessage += "📝 Nama lengkap tidak boleh kosong!\n";
        }

        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            errorMessage += "📧 Email tidak boleh kosong!\n";
        } else if (!isValidEmail(txtEmail.getText())) {
            errorMessage += "📧 Format email tidak valid!\n";
        }

        if (txtTelepon.getText() == null || txtTelepon.getText().trim().isEmpty()) {
            errorMessage += "📱 Nomor telepon tidak boleh kosong!\n";
        } else if (!isValidPhoneNumber(txtTelepon.getText())) {
            errorMessage += "📱 Format nomor telepon tidak valid!\n";
        }

        if (txtTanggalLahir.getText() == null || txtTanggalLahir.getText().trim().isEmpty()) {
            errorMessage += "🎂 Tanggal lahir tidak boleh kosong!\n";
        } else if (!isValidDate(txtTanggalLahir.getText())) {
            errorMessage += "🎂 Format tanggal lahir tidak valid! Gunakan format DD/MM/YYYY\n";
        }

        if (txtTinggiBadan.getText() != null && !txtTinggiBadan.getText().trim().isEmpty()) {
            try {
                double tinggi = Double.parseDouble(txtTinggiBadan.getText());
                if (tinggi <= 0 || tinggi > 250) {
                    errorMessage += "📏 Tinggi badan harus antara 1-250 cm!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "📏 Tinggi badan harus berupa angka!\n";
            }
        }

        if (txtBeratBadan.getText() != null && !txtBeratBadan.getText().trim().isEmpty()) {
            try {
                double berat = Double.parseDouble(txtBeratBadan.getText());
                if (berat <= 0 || berat > 500) {
                    errorMessage += "⚖️ Berat badan harus antara 1-500 kg!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "⚖️ Berat badan harus berupa angka!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            showValidationErrorAlert(errorMessage);
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10,13}");
    }

    private boolean isValidDate(String date) {
        return date.matches("\\d{2}/\\d{2}/\\d{4}");
    }

    private void saveUserData() {
        System.out.println("Menyimpan data profile:");
        System.out.println("Nama: " + txtNamaLengkap.getText());
        System.out.println("Email: " + txtEmail.getText());
        System.out.println("Telepon: " + txtTelepon.getText());
        System.out.println("Tanggal Lahir: " + txtTanggalLahir.getText());
        System.out.println("Alamat: " + txtAlamat.getText());
        System.out.println("Golongan Darah: " + txtGolonganDarah.getText());
        System.out.println("Tinggi Badan: " + txtTinggiBadan.getText());
        System.out.println("Berat Badan: " + txtBeratBadan.getText());
        System.out.println("Alergi: " + txtAlergi.getText());
        System.out.println("Riwayat Penyakit: " + txtRiwayatPenyakit.getText());
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle(
            "-fx-background-color: linear-gradient(135deg, #f0fff4, #e6fffa); " +
            "-fx-font-family: 'Segoe UI'; " +
            "-fx-border-color: #48bb78; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px;"
        );
        
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("❌ Kesalahan");
        alert.setHeaderText("Terjadi kesalahan");
        alert.setContentText(message);
        alert.getDialogPane().setStyle(
            "-fx-background-color: linear-gradient(135deg, #fff5f5, #fed7d7); " +
            "-fx-font-family: 'Segoe UI'; " +
            "-fx-border-color: #f56565; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px;"
        );
        
        alert.showAndWait();
    }

    private void showValidationErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("⚠️ Input Tidak Valid");
        alert.setHeaderText("Silakan perbaiki input berikut:");
        alert.setContentText(errorMessage);
        alert.getDialogPane().setStyle(
            "-fx-background-color: linear-gradient(135deg, #fffaf0, #fef5e7); " +
            "-fx-font-family: 'Segoe UI'; " +
            "-fx-border-color: #ed8936; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 10px; " +
            "-fx-background-radius: 10px;"
        );
        
        alert.showAndWait();
    }

}