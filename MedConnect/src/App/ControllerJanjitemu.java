package App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerJanjitemu {
    
    @FXML private Button btnKembali;
    @FXML private Button btnRefresh;
    @FXML private Button btnCari;
    @FXML private ComboBox<String> comboFilter;
    @FXML private TextField txtSearch;
    @FXML private Label lblTotalJanji;
    @FXML private Label lblMenunggu;
    @FXML private Label lblDikonfirmasi;
    @FXML private Label lblSelesai;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox vboxDaftarJanji;
    
    private List<JanjiTemu> daftarJanjiTemu;
    private List<JanjiTemu> filteredJanjiTemu;
    private JanjiTemuManager janjiTemuManager; 
    
    public void initialize() {
        janjiTemuManager = JanjiTemuManager.getInstance();
        ObservableList<String> filterItems = FXCollections.observableArrayList(
            "Semua", "Menunggu", "Dikonfirmasi", "Selesai"
        );
        comboFilter.setItems(filterItems);
        comboFilter.setValue("Semua");
        loadDataFromManager();
        filteredJanjiTemu = new ArrayList<>(daftarJanjiTemu);
        updateStats();
        displayJanjiTemu();
        comboFilter.setOnAction(e -> applyFilter());
        
        System.out.println("ControllerJanjitemu initialized with " + daftarJanjiTemu.size() + " appointments");
    }
    
    private void loadDataFromManager() {
        daftarJanjiTemu = janjiTemuManager.getDaftarJanjiTemu();
        System.out.println("Loaded " + daftarJanjiTemu.size() + " appointments from manager");
    }
    
    private void updateStats() {
        int total = janjiTemuManager.getTotalJanjiTemu();
        int menunggu = janjiTemuManager.getCountByStatus("MENUNGGU");
        int dikonfirmasi = janjiTemuManager.getCountByStatus("DIKONFIRMASI");
        int selesai = janjiTemuManager.getCountByStatus("SELESAI");
        
        lblTotalJanji.setText(String.valueOf(total));
        lblMenunggu.setText(String.valueOf(menunggu));
        lblDikonfirmasi.setText(String.valueOf(dikonfirmasi));
        lblSelesai.setText(String.valueOf(selesai));
        
        System.out.println("Stats updated - Total: " + total + ", Menunggu: " + menunggu + 
                          ", Dikonfirmasi: " + dikonfirmasi + ", Selesai: " + selesai);
    }
    
    private void applyFilter() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        String filterStatus = comboFilter.getValue();
        
        filteredJanjiTemu = daftarJanjiTemu.stream()
            .filter(janji -> {
                boolean statusMatch = filterStatus.equals("Semua") || 
                                    janji.getStatus().toLowerCase().contains(filterStatus.toLowerCase());
                boolean searchMatch = searchText.isEmpty() || 
                                    janji.getNama().toLowerCase().contains(searchText);
                
                return statusMatch && searchMatch;
            })
            .collect(Collectors.toList());
        
        displayJanjiTemu();
    }
    
    private void displayJanjiTemu() {
        vboxDaftarJanji.getChildren().clear();
        
        for (JanjiTemu janji : filteredJanjiTemu) {
            HBox janjiBox = createJanjiTemuBox(janji);
            vboxDaftarJanji.getChildren().add(janjiBox);
        }
        
        if (filteredJanjiTemu.isEmpty()) {
            Label noDataLabel = new Label("Tidak ada data janji temu yang ditemukan");
            noDataLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-padding: 20px;");
            vboxDaftarJanji.getChildren().add(noDataLabel);
        }
    }
    
    private HBox createJanjiTemuBox(JanjiTemu janji) {
        HBox janjiBox = new HBox();
        janjiBox.setAlignment(Pos.CENTER_LEFT);
        janjiBox.setSpacing(15);
        janjiBox.setPrefWidth(1130);
        janjiBox.setStyle("-fx-background-color: white; -fx-background-radius: 8px; -fx-padding: 20px;");
        
        VBox patientInfo = new VBox();
        patientInfo.setSpacing(5);
        patientInfo.setPrefWidth(150);
        
        Label namaLabel = new Label(janji.getNama());
        namaLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label teleponLabel = new Label(janji.getTelepon());
        teleponLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        patientInfo.getChildren().addAll(namaLabel, teleponLabel);
        
        VBox timeInfo = new VBox();
        timeInfo.setSpacing(5);
        timeInfo.setPrefWidth(130);
        
        Label tanggalLabel = new Label(janji.getTanggal());
        tanggalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label waktuLabel = new Label(janji.getWaktu());
        waktuLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        
        timeInfo.getChildren().addAll(tanggalLabel, waktuLabel);
        
        VBox keluhanInfo = new VBox();
        keluhanInfo.setSpacing(5);
        keluhanInfo.setPrefWidth(250);
        
        Label keluhanLabel = new Label(janji.getKeluhan());
        keluhanLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");
        keluhanLabel.setWrapText(true); 
        
        keluhanInfo.getChildren().add(keluhanLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
        VBox statusInfo = new VBox();
        statusInfo.setAlignment(Pos.CENTER);
        statusInfo.setSpacing(5);
        statusInfo.setPrefWidth(110);
        
        Label statusLabel = new Label(janji.getStatus());
        String statusStyle = getStatusStyle(janji.getStatus());
        statusLabel.setStyle(statusStyle);
        
        statusInfo.getChildren().add(statusLabel);
        
        VBox buttonBox = new VBox();
        buttonBox.setSpacing(8);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button terimaBtn = new Button("Terima");
        terimaBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-pref-width: 80px; -fx-pref-height: 32px;");
        terimaBtn.setOnAction(e -> handleTerima(janji));
        
        Button tolakBtn = new Button("Tolak");
        tolakBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 5px; -fx-pref-width: 80px; -fx-pref-height: 32px;");
        tolakBtn.setOnAction(e -> handleTolak(janji));
        
        if (!janji.getStatus().equals("MENUNGGU")) {
            terimaBtn.setDisable(true);
            tolakBtn.setDisable(true);
            terimaBtn.setStyle(terimaBtn.getStyle() + "; -fx-opacity: 0.6;");
            tolakBtn.setStyle(tolakBtn.getStyle() + "; -fx-opacity: 0.6;");
        }
        
        buttonBox.getChildren().addAll(terimaBtn, tolakBtn);
        
        janjiBox.getChildren().addAll(patientInfo, timeInfo, keluhanInfo, spacer, statusInfo, buttonBox);
        
        return janjiBox;
    }
    
    private String getStatusStyle(String status) {
        switch (status) {
            case "MENUNGGU":
                return "-fx-font-size: 11px; -fx-text-fill: #ff9800; -fx-background-color: #fff3cd; -fx-background-radius: 15px; -fx-padding: 8px 12px; -fx-font-weight: bold;";
            case "DIKONFIRMASI":
                return "-fx-font-size: 11px; -fx-text-fill: #4caf50; -fx-background-color: #d4edda; -fx-background-radius: 15px; -fx-padding: 8px 12px; -fx-font-weight: bold;";
            case "SELESAI":
                return "-fx-font-size: 11px; -fx-text-fill: #2196f3; -fx-background-color: #cce7ff; -fx-background-radius: 15px; -fx-padding: 8px 12px; -fx-font-weight: bold;";
            default:
                return "-fx-font-size: 11px; -fx-text-fill: #666; -fx-background-color: #f8f9fa; -fx-background-radius: 15px; -fx-padding: 8px 12px;";
        }
    }
    
    private String getStatusColor(String status) {
        switch (status) {
            case "MENUNGGU":
                return "#f48c2a";
            case "DIKONFIRMASI":
                return "#4caf50";
            case "SELESAI":
                return "#2196f3";
            default:
                return "#666";
        }
    }
    
    @FXML
    private void handleKembali(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamandokter.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnKembali.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Gagal memuat halaman dokter: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRefresh(ActionEvent event) {
        loadDataFromManager();
        filteredJanjiTemu = new ArrayList<>(daftarJanjiTemu);
        updateStats();
        displayJanjiTemu();
        comboFilter.setValue("Semua");
        txtSearch.clear();
        
        showAlert("Info", "Data berhasil direfresh!\n" + 
                         "Total janji temu: " + daftarJanjiTemu.size());
    }
    
    @FXML
    private void handleCari(ActionEvent event) {
        applyFilter();
        
        String searchText = txtSearch.getText();
        String filter = comboFilter.getValue();
        
        if (searchText.isEmpty()) {
            showAlert("Info", "Menampilkan data dengan filter: " + filter + 
                     "\nTotal: " + filteredJanjiTemu.size() + " janji temu");
        } else {
            showAlert("Info", "Mencari: '" + searchText + "' dengan filter: " + filter + 
                     "\nDitemukan " + filteredJanjiTemu.size() + " hasil");
        }
    }
    
    private void handleTerima(JanjiTemu janji) {
        janjiTemuManager.updateStatus(janji.getId(), "DIKONFIRMASI");
        janji.setStatus("DIKONFIRMASI");
        
        updateStats();
        displayJanjiTemu();
        
        showAlert("Notifikasi", "Janji temu dengan " + janji.getNama() + " telah diterima.\n" +
                  "Notifikasi persetujuan telah dikirimkan ke pasien melalui SMS dan email.\n\n" +
                  "Detail Janji Temu:\n" +
                  "- Tanggal: " + janji.getTanggal() + "\n" +
                  "- Waktu: " + janji.getWaktu() + "\n" +
                  "- Status: DIKONFIRMASI");
    }
    
    private void handleTolak(JanjiTemu janji) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Penolakan");
        confirmAlert.setHeaderText("Tolak Janji Temu");
        confirmAlert.setContentText("Apakah Anda yakin ingin menolak janji temu dengan " + 
                                   janji.getNama() + "?\n\n" +
                                   "Tanggal: " + janji.getTanggal() + "\n" +
                                   "Waktu: " + janji.getWaktu());
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                janjiTemuManager.removeJanjiTemu(janji);
                loadDataFromManager();
                applyFilter();
                updateStats();
                
                showAlert("Notifikasi", "Janji temu dengan " + janji.getNama() + " telah ditolak.\n" +
                          "Notifikasi penolakan telah dikirimkan ke pasien melalui SMS dan email.\n\n" +
                          "Pasien dapat memilih jadwal alternatif yang tersedia.");
            }
        });
    }
    
    @FXML
    private void handleDetail(ActionEvent event) {
        showAlert("Info", "Detail janji temu akan ditampilkan");
    }
    
    @FXML
    private void handleEdit(ActionEvent event) {
        showAlert("Info", "Fitur edit janji temu akan segera tersedia");
    }
    
    @FXML
    private void handleLihat(ActionEvent event) {
        showAlert("Info", "Riwayat janji temu akan ditampilkan");
    }
    
    @FXML
    private void handleResep(ActionEvent event) {
        showAlert("Info", "Resep untuk pasien akan ditampilkan");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static class JanjiTemu {
        private String id;
        private String nama;
        private String telepon;
        private String tanggal;
        private String waktu;
        private String keluhan;
        private String status;
        
        public JanjiTemu(String id, String nama, String telepon, String tanggal, 
                        String waktu, String keluhan, String status) {
            this.id = id;
            this.nama = nama;
            this.telepon = telepon;
            this.tanggal = tanggal;
            this.waktu = waktu;
            this.keluhan = keluhan;
            this.status = status;
        }
        
        public String getId() { return id; }
        public String getNama() { return nama; }
        public String getTelepon() { return telepon; }
        public String getTanggal() { return tanggal; }
        public String getWaktu() { return waktu; }
        public String getKeluhan() { return keluhan; }
        public String getStatus() { return status; }
        
        public void setId(String id) { this.id = id; }
        public void setNama(String nama) { this.nama = nama; }
        public void setTelepon(String telepon) { this.telepon = telepon; }
        public void setTanggal(String tanggal) { this.tanggal = tanggal; }
        public void setWaktu(String waktu) { this.waktu = waktu; }
        public void setKeluhan(String keluhan) { this.keluhan = keluhan; }
        public void setStatus(String status) { this.status = status; }
        
        @Override
        public String toString() {
            return "JanjiTemu{" +
                   "id='" + id + '\'' +
                   ", nama='" + nama + '\'' +
                   ", telepon='" + telepon + '\'' +
                   ", tanggal='" + tanggal + '\'' +
                   ", waktu='" + waktu + '\'' +
                   ", keluhan='" + keluhan + '\'' +
                   ", status='" + status + '\'' +
                   '}';
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            JanjiTemu janjiTemu = (JanjiTemu) obj;
            return id.equals(janjiTemu.id);
        }
        
        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}