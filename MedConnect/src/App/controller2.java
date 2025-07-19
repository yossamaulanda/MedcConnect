package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class controller2 {

    @FXML private TextField searchField;
    @FXML private VBox dokterListContainer;
    @FXML private Button backButton;

    private ArrayList<Dokter> dokterList = new ArrayList<>();
    private ArrayList<Dokter> filteredDokterList = new ArrayList<>();
    private boolean sortByDateAsc = true;
    private boolean sortByExperienceAsc = true;

    // Inner class untuk data dokter
    public static class Dokter {
        private String nama;
        private String rumahSakit;
        private String pengalaman;
        private int pengalamanTahun;
        private String spesialisasi;
        private String targetFxml;
        private double rating;

        public Dokter(String nama, String rumahSakit, String pengalaman, String spesialisasi, String targetFxml, double rating) {
            this.nama = nama;
            this.rumahSakit = rumahSakit;
            this.pengalaman = pengalaman;
            this.spesialisasi = spesialisasi;
            this.targetFxml = targetFxml;
            this.rating = rating;
            
            // Extract tahun pengalaman untuk sorting
            try {
                this.pengalamanTahun = Integer.parseInt(pengalaman.replaceAll("\\D+", ""));
            } catch (NumberFormatException e) {
                this.pengalamanTahun = 0;
            }
        }

        // Getters
        public String getNama() { return nama; }
        public String getRumahSakit() { return rumahSakit; }
        public String getPengalaman() { return pengalaman; }
        public int getPengalamanTahun() { return pengalamanTahun; }
        public String getSpesialisasi() { return spesialisasi; }
        public String getTargetFxml() { return targetFxml; }
        public double getRating() { return rating; }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("halaman1.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Gagal kembali ke halaman utama.");
        }
    }

    @FXML
    private void handleFilter(ActionEvent event) {
        System.out.println("Filter diklik.");
        showFilterDialog();
    }
    
    private void showFilterDialog() {
        // Buat dialog untuk memilih filter spesialisasi
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Filter Dokter");
        alert.setHeaderText("Pilih Spesialisasi Dokter");
        
        // Buat button types untuk setiap spesialisasi
        ButtonType semuaButton = new ButtonType("Semua Dokter");
        ButtonType umumButton = new ButtonType("Dokter Umum");
        ButtonType mataButton = new ButtonType("Dokter Mata");
        ButtonType jantungButton = new ButtonType("Dokter Jantung");
        ButtonType batalButton = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(semuaButton, umumButton, mataButton, jantungButton, batalButton);
        
        alert.showAndWait().ifPresent(response -> {
            String filterSpesialisasi = "";
            if (response == semuaButton) {
                filterSpesialisasi = "";
            } else if (response == umumButton) {
                filterSpesialisasi = "Dokter Umum";
            } else if (response == mataButton) {
                filterSpesialisasi = "Dokter Mata";
            } else if (response == jantungButton) {
                filterSpesialisasi = "Dokter Jantung";
            }
            
            if (response != batalButton) {
                applySpecializationFilter(filterSpesialisasi);
            }
        });
    }
    
    private void applySpecializationFilter(String spesialisasi) {
        filteredDokterList.clear();
        
        if (spesialisasi == null || spesialisasi.trim().isEmpty()) {
            // Tampilkan semua dokter
            filteredDokterList.addAll(dokterList);
        } else {
            // Filter berdasarkan spesialisasi
            filteredDokterList.addAll(
                dokterList.stream()
                    .filter(dokter -> dokter.getSpesialisasi().equals(spesialisasi))
                    .collect(Collectors.toList())
            );
        }
        
        // Terapkan juga search filter jika ada
        String currentSearch = searchField.getText();
        if (currentSearch != null && !currentSearch.trim().isEmpty()) {
            performSearchOnFiltered(currentSearch);
        } else {
            updateDokterDisplay();
        }
    }

    @FXML
    private void handleSortTanggal(ActionEvent event) {
        System.out.println("Sortir berdasarkan tanggal diklik.");
        sortByDateAsc = !sortByDateAsc;
        
        // Sort berdasarkan nama (sebagai alternatif tanggal)
        filteredDokterList.sort((d1, d2) -> {
            if (sortByDateAsc) {
                return d1.getNama().compareTo(d2.getNama());
            } else {
                return d2.getNama().compareTo(d1.getNama());
            }
        });
        
        updateDokterDisplay();
    }

    @FXML
    private void handleSortPengalaman(ActionEvent event) {
        System.out.println("Sort berdasarkan pengalaman diklik.");
        sortByExperienceAsc = !sortByExperienceAsc;
        
        // Sort berdasarkan tahun pengalaman
        filteredDokterList.sort((d1, d2) -> {
            if (sortByExperienceAsc) {
                return Integer.compare(d1.getPengalamanTahun(), d2.getPengalamanTahun());
            } else {
                return Integer.compare(d2.getPengalamanTahun(), d1.getPengalamanTahun());
            }
        });
        
        updateDokterDisplay();
    }

    @FXML
    private void onMenuClicked(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        String targetFxml = "";

        if (buttonText.equals("Beranda")) {
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

    @FXML
    public void initialize() {
        // Inisialisasi data dokter
        initializeDokterData();
        
        // Setup search functionality
        setupSearchFunction();
        
        // Tampilkan semua dokter awal
        filteredDokterList.addAll(dokterList);
        updateDokterDisplay();
    }

    private void initializeDokterData() {
        // Data dokter yang sudah ada
        dokterList.add(new Dokter("dr. Andi Wijaya", "RS Harapan Medika", "8 Tahun", "Dokter Umum", "halamandr1.fxml", 5.0));
        dokterList.add(new Dokter("dr. Rina Astuti", "RS Citra Mandiri", "8 Tahun", "Dokter Umum", "halamandr2.fxml", 5.0));
        dokterList.add(new Dokter("dr. Bambang Riyadi", "RS Nusantara Medika", "17 Tahun", "Dokter Umum", "halamandr3.fxml", 5.0));
        
        // Tambahan 2 dokter baru sesuai gambar
        dokterList.add(new Dokter("dr. Sari Indah", "RS Harapan Sehat", "12 Tahun", "Dokter Umum", "halamandr4.fxml", 4.8));
        dokterList.add(new Dokter("dr. Budi Santoso", "RS Mata", "15 Tahun", "Dokter Mata", "halamandr5.fxml", 4.9));
        
        // Tambahan 4 dokter baru dengan spesialisasi beragam
        dokterList.add(new Dokter("dr. Jordi Maulanda", "RS Jantung ", "10 Tahun", "Dokter Jantung", "halamandr6.fxml", 4.7));
        dokterList.add(new Dokter("dr. Abdilah Mutasim", "RS Medika Prima", "6 Tahun", "Dokter Umum", "halamandr7.fxml", 4.6));
        dokterList.add(new Dokter("dr. Azmi Pasagama", "RS Mata", "14 Tahun", "Dokter Mata", "halamandr8.fxml", 4.8));
        dokterList.add(new Dokter("dr. Ibra Ivanka", "RS Kardio Center", "11 Tahun", "Dokter Jantung", "halamandr9.fxml", 4.9));
    }

    private void setupSearchFunction() {
        // Add listener untuk real-time search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            performSearch(newValue);
        });
    }

    private void performSearch(String keyword) {
        filteredDokterList.clear();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            // Jika tidak ada keyword, tampilkan semua dokter
            filteredDokterList.addAll(dokterList);
        } else {
            // Filter berdasarkan keyword
            String lowerKeyword = keyword.toLowerCase().trim();
            filteredDokterList.addAll(
                dokterList.stream()
                    .filter(dokter -> 
                        dokter.getNama().toLowerCase().contains(lowerKeyword) ||
                        dokter.getRumahSakit().toLowerCase().contains(lowerKeyword) ||
                        dokter.getSpesialisasi().toLowerCase().contains(lowerKeyword)
                    )
                    .collect(Collectors.toList())
            );
        }
        
        updateDokterDisplay();
    }
    
    private void performSearchOnFiltered(String keyword) {
        // Simpan hasil filter saat ini
        List<Dokter> currentBaseList = new ArrayList<>(filteredDokterList);
        filteredDokterList.clear();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            // Jika tidak ada keyword, gunakan base list yang sudah difilter
            filteredDokterList.addAll(currentBaseList);
        } else {
            // Filter berdasarkan keyword dari base list
            String lowerKeyword = keyword.toLowerCase().trim();
            filteredDokterList.addAll(
                currentBaseList.stream()
                    .filter(dokter -> 
                        dokter.getNama().toLowerCase().contains(lowerKeyword) ||
                        dokter.getRumahSakit().toLowerCase().contains(lowerKeyword) ||
                        dokter.getSpesialisasi().toLowerCase().contains(lowerKeyword)
                    )
                    .collect(Collectors.toList())
            );
        }
        
        updateDokterDisplay();
    }

    private void updateDokterDisplay() {
        dokterListContainer.getChildren().clear();
        
        for (Dokter dokter : filteredDokterList) {
            addDokterToDisplay(dokter);
        }
    }

    private void addDokterToDisplay(Dokter dokter) {
        Label namaLabel = new Label(dokter.getNama());
        namaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        
        // Tambahkan emoji sesuai spesialisasi untuk visual yang lebih menarik
        String emojiSpesialisasi = "";
        
        switch (dokter.getSpesialisasi()) {
            case "Dokter Umum":
                emojiSpesialisasi = "👨‍⚕️";
                break;
            case "Dokter Mata":
                emojiSpesialisasi = "👁️";
                break;
            case "Dokter Jantung":
                emojiSpesialisasi = "❤️";
                break;
        }
        
        Label detailLabel = new Label(emojiSpesialisasi + " " + dokter.getSpesialisasi() + "   📅 " + dokter.getPengalaman());
        detailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        Label rsLabel = new Label("🏥 " + dokter.getRumahSakit());
        rsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
        
        Label rating = new Label("⭐ " + dokter.getRating());
        rating.setStyle("-fx-font-size: 14px; -fx-text-fill: #f48c2a;");

        VBox card = new VBox(5, namaLabel, detailLabel, rsLabel, rating);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        card.setOnMouseClicked(event -> {
            try {
                // Cek apakah file FXML exists untuk dokter baru
                if (!dokter.getTargetFxml().equals("halamandr1.fxml") && 
                    !dokter.getTargetFxml().equals("halamandr2.fxml") && 
                    !dokter.getTargetFxml().equals("halamandr3.fxml")) {
                    showError("Halaman detail untuk " + dokter.getNama() + " belum tersedia.\nSilakan buat file " + dokter.getTargetFxml());
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(dokter.getTargetFxml()));
                Parent root = loader.load();
                Stage stage = (Stage) card.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Detail Dokter - " + dokter.getNama());
            } catch (Exception e) {
                e.printStackTrace();
                showError("Gagal membuka halaman dokter: " + e.getMessage());
            }
        });

        card.setOnMouseEntered(event -> {
            card.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);");
        });

        card.setOnMouseExited(event -> {
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        });

        dokterListContainer.getChildren().add(card);
    }

    // Method lama untuk backward compatibility
    private void addDokter(String nama, String rumahSakit, String pengalaman, boolean isClickable) {
        // Method ini tetap ada untuk backward compatibility tapi tidak digunakan
        // Semua dokter sekarang ditangani melalui ArrayList
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kesalahan");
        alert.setHeaderText("Terjadi kesalahan");
        alert.setContentText(message);
        alert.showAndWait();
    }
}