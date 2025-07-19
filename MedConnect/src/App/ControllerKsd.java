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
import javafx.application.Platform;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControllerKsd {
    
    @FXML
    private VBox chatMessages;
    
    @FXML
    private TextField messageInput;
    
    @FXML
    private Button sendButton, diagnoseButton, prescribeButton, scheduleButton;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label patientNameLabel, patientFullName, patientAge, patientComplaint, consultationStatus;
    
    private boolean consultationActive = true;
    private String patientName = "Azmi";
    private String doctorName = "dr. Bambang Riyadi";
    
    @FXML
    public void initialize() {
        // Set informasi pasien
        patientNameLabel.setText("dengan Pasien: " + patientName);
        patientFullName.setText(patientName);
        patientAge.setText("28 tahun");
        patientComplaint.setText("Demam dan sakit tenggorokan");
        
        // Chat area kosong - tidak ada pesan otomatis
    }
    
    @FXML
    private void handleSendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty() && consultationActive) {
            addDoctorMessage(message);
            messageInput.clear();
            
            // Simulasi respon otomatis pasien setelah delay
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    Platform.runLater(() -> {
                        String[] responses = {
                            "Baik dok, saya mengerti. Terima kasih atas penjelasannya.",
                            "Apakah ada efek samping yang perlu saya perhatikan dok?",
                            "Berapa lama waktu yang dibutuhkan untuk sembuh dok?",
                            "Baik dok, saya akan mengikuti saran dokter.",
                            "Apakah saya perlu kontrol lagi dok?"
                        };
                        String response = responses[(int) (Math.random() * responses.length)];
                        addPatientMessage(response);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
    
    @FXML
    private void handleGiveDiagnosis() {
        if (consultationActive) {
            addDoctorMessage("📋 DIAGNOSIS:\n" +
                "Berdasarkan gejala yang Anda sampaikan, diagnosis sementara adalah:\n" +
                "• Faringitis akut (radang tenggorokan)\n" +
                "• Kemungkinan disertai infeksi virus atau bakteri\n" +
                "• Kondisi masih dalam tahap awal dan dapat diobati");
            
            addSystemMessage("✅ Diagnosis telah diberikan dan tercatat dalam sistem");
        }
    }
    
    @FXML
    private void handlePrescribeMedicine() {
        if (consultationActive) {
            addDoctorMessage("💊 RESEP OBAT:\n" +
                "Saya akan memberikan resep obat berikut:\n" +
                "1. Paracetamol 500mg - 3x sehari setelah makan\n" +
                "2. Amoxicillin 500mg - 3x sehari setelah makan\n" +
                "3. Lozenges untuk tenggorokan - sesuai kebutuhan\n" +
                "4. Banyak minum air hangat dan istirahat cukup");
            
            addSystemMessage("💊 Resep obat telah dikirim ke apotek dan tersedia untuk diambil");
            NotificationManager.addNotification("💊 Resep obat baru dari " + doctorName + " telah tersedia di apotek.");
        }
    }
    
    @FXML
    private void handleScheduleFollowUp() {
        if (consultationActive) {
            addDoctorMessage("📅 JADWAL KONTROL:\n" +
                "Saya jadwalkan kontrol ulang dalam 3 hari ke depan.\n" +
                "Jika gejala tidak membaik atau malah memburuk, segera hubungi saya atau datang ke rumah sakit.");
            
            addSystemMessage("📅 Jadwal kontrol telah dibuat dan notifikasi akan dikirim");
            NotificationManager.addNotification("📅 Jadwal kontrol baru: 3 hari dari sekarang dengan " + doctorName);
        }
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/halamanchatdok.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke halaman konsultasi.");
        }
    }
    
    private void addDoctorMessage(String text) {
        Label label = new Label("👨‍⚕️ " + doctorName + ":\n" + text);
        label.setStyle("-fx-background-color: #f48c2a; -fx-text-fill: white; -fx-padding: 12 16 12 16; -fx-background-radius: 15; -fx-font-size: 14px;");
        label.setWrapText(true);
        label.setMaxWidth(500);
        chatMessages.getChildren().add(label);
    }
    
    private void addPatientMessage(String text) {
        Label label = new Label("🧑 " + patientName + ":\n" + text);
        label.setStyle("-fx-background-color: #f48c2a; -fx-text-fill: white; -fx-padding: 12 16 12 16; -fx-background-radius: 15; -fx-font-size: 14px; -fx-border-color: #d35400; -fx-border-radius: 15;");
        label.setWrapText(true);
        label.setMaxWidth(500);
        chatMessages.getChildren().add(label);
    }
    
    private void addSystemMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-style: italic; -fx-text-fill: #7f8c8d; -fx-padding: 8 0 8 0; -fx-font-size: 12px;");
        label.setWrapText(true);
        chatMessages.getChildren().add(label);
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy"));
    }
}