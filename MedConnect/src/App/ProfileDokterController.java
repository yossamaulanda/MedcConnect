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

public class ProfileDokterController {

    @FXML private Label lblNamaDokter;
    @FXML private TextField txtNamaLengkap;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtTanggalLahir;
    @FXML private TextField txtAlamat;
    @FXML private TextField txtSpesialisasi;
    @FXML private TextField txtNomorSTR;
    @FXML private TextField txtUniversitas;
    @FXML private TextField txtTahunLulus;
    @FXML private TextField txtPengalamanKerja;
    @FXML private TextField txtRumahSakit;

    @FXML
    public void initialize() {
        loadUserData();
        addHoverEffects();
    }

    private void addHoverEffects() {
        TextField[] fields = {txtNamaLengkap, txtEmail, txtTelepon, txtTanggalLahir, 
                            txtAlamat, txtSpesialisasi, txtNomorSTR, txtUniversitas, 
                            txtTahunLulus, txtPengalamanKerja, txtRumahSakit};
        
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
        txtSpesialisasi.setText("");
        txtNomorSTR.setText("");
        txtUniversitas.setText("");
        txtTahunLulus.setText("");
        txtPengalamanKerja.setText("");
        txtRumahSakit.setText("");
        
        lblNamaDokter.setText("Dokter");
    }

    @FXML
    private void onSimpanClicked() {
        if (isInputValid()) {
            try {
                saveUserData();
                String namaLengkap = txtNamaLengkap.getText().trim();
                if (!namaLengkap.isEmpty()) {
                    lblNamaDokter.setText("Dr. " + namaLengkap);
                } else {
                    lblNamaDokter.setText("Dokter");
                }
                showSuccessAlert("Berhasil", "Data profil dokter berhasil disimpan! ✅");
                
            } catch (Exception e) {
                showErrorAlert("Gagal menyimpan data profil dokter: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onResetClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("🔄 Konfirmasi Reset");
        alert.setHeaderText("Reset Data Profil Dokter");
        alert.setContentText("Apakah Anda yakin ingin mereset semua data ke nilai default?");
        alert.getDialogPane().setStyle("-fx-background-color: #f7fafc; -fx-font-family: 'Segoe UI';");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            loadUserData();
            showSuccessAlert("Reset Berhasil", "Data profil dokter telah direset ke nilai default. 🔄");
        }
    }

    @FXML
    private void onEditPhotoClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("📸 Pilih Foto Profil Dokter");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        Stage stage = (Stage) txtNamaLengkap.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        if (selectedFile != null) {
            showSuccessAlert("Foto Profil Dokter", "Foto profil dokter akan diupdate dengan: " + selectedFile.getName() + " 📸");
        }
    }

    @FXML
    private void onBackClicked() {
        onBerandaClicked();
    }

    @FXML
    private void onBerandaClicked() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("halamandokter.fxml"));
            Stage stage = (Stage) txtNamaLengkap.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MedConnect - Beranda Dokter");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Gagal membuka halaman beranda dokter: " + e.getMessage());
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
            errorMessage += "👨⚕️ Nama lengkap dokter tidak boleh kosong!\n";
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

        if (txtSpesialisasi.getText() == null || txtSpesialisasi.getText().trim().isEmpty()) {
            errorMessage += "🏥 Spesialisasi tidak boleh kosong!\n";
        }

        if (txtNomorSTR.getText() == null || txtNomorSTR.getText().trim().isEmpty()) {
            errorMessage += "📜 Nomor STR tidak boleh kosong!\n";
        }

        if (txtUniversitas.getText() == null || txtUniversitas.getText().trim().isEmpty()) {
            errorMessage += "🏛️ Universitas tidak boleh kosong!\n";
        }

        if (txtTahunLulus.getText() != null && !txtTahunLulus.getText().trim().isEmpty()) {
            try {
                int tahun = Integer.parseInt(txtTahunLulus.getText());
                if (tahun < 1950 || tahun > 2025) {
                    errorMessage += "🎓 Tahun lulus harus antara 1950-2025!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "🎓 Tahun lulus harus berupa angka!\n";
            }
        }

        if (txtPengalamanKerja.getText() != null && !txtPengalamanKerja.getText().trim().isEmpty()) {
            try {
                int pengalaman = Integer.parseInt(txtPengalamanKerja.getText());
                if (pengalaman < 0 || pengalaman > 50) {
                    errorMessage += "💼 Pengalaman kerja harus antara 0-50 tahun!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "💼 Pengalaman kerja harus berupa angka!\n";
            }
        }

        if (txtRumahSakit.getText() == null || txtRumahSakit.getText().trim().isEmpty()) {
            errorMessage += "🏢 Rumah sakit tidak boleh kosong!\n";
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
        System.out.println("Menyimpan data profil dokter:");
        System.out.println("Nama: " + txtNamaLengkap.getText());
        System.out.println("Email: " + txtEmail.getText());
        System.out.println("Telepon: " + txtTelepon.getText());
        System.out.println("Tanggal Lahir: " + txtTanggalLahir.getText());
        System.out.println("Alamat: " + txtAlamat.getText());
        System.out.println("Spesialisasi: " + txtSpesialisasi.getText());
        System.out.println("Nomor STR: " + txtNomorSTR.getText());
        System.out.println("Universitas: " + txtUniversitas.getText());
        System.out.println("Tahun Lulus: " + txtTahunLulus.getText());
        System.out.println("Pengalaman Kerja: " + txtPengalamanKerja.getText());
        System.out.println("Rumah Sakit: " + txtRumahSakit.getText());
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
