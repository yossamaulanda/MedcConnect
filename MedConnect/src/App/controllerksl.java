package App;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

// Doctor class to represent doctor data
class Doctor {
    private String name;
    private String message;
    private String specialty;
    private String fxmlFile;

    public Doctor(String name, String message, String specialty, String fxmlFile) {
        this.name = name;
        this.message = message;
        this.specialty = specialty;
        this.fxmlFile = fxmlFile;
    }

    // Getters
    public String getName() { return name; }
    public String getMessage() { return message; }
    public String getSpecialty() { return specialty; }
    public String getFxmlFile() { return fxmlFile; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setMessage(String message) { this.message = message; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setFxmlFile(String fxmlFile) { this.fxmlFile = fxmlFile; }

    @Override
    public String toString() {
        return name + " - " + specialty;
    }
}

public class controllerksl {
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button backButton;
    
    @FXML
    private ComboBox<String> filterComboBox;
    
    @FXML
    private VBox doctorListContainer;
    
    // ArrayList to store doctor data
    private ArrayList<Doctor> doctorList;
    private ArrayList<Doctor> filteredDoctorList;
    private String currentSearchText = "";
    private String currentFilter = "Semua Dokter";
    
    @FXML
    private void initialize() {
        // Initialize doctor data
        initializeDoctorData();
        
        // Initialize filter ComboBox
        initializeFilterComboBox();
        
        // Initialize search field with real-time detection
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                currentSearchText = newValue != null ? newValue.trim() : "";
                System.out.println("Search keyword detected: " + currentSearchText);
                performSearch();
            });
        }
        
        // Initialize filter ComboBox with change detection
        if (filterComboBox != null) {
            filterComboBox.setOnAction(e -> {
                String selectedFilter = filterComboBox.getValue();
                if (selectedFilter != null) {
                    currentFilter = selectedFilter;
                    System.out.println("Filter changed to: " + currentFilter);
                    performSearch();
                }
            });
        }
        
        // Initial display of all doctors
        performSearch();
    }
    
    private void initializeDoctorData() {
        doctorList = new ArrayList<>();
        doctorList.add(new Doctor(
            "dr. Andi Wijaya", 
            "Selamat datang! Saya dr. Andi Wijaya. Ada yang bisa saya bantu?",
            "Dokter Umum",
            "Chatconsultation.fxml"
        ));
        
        doctorList.add(new Doctor(
            "dr. Rina Astuti", 
            "Selamat datang! Saya dr. Rina Astuti. Ada yang bisa saya bantu?",
            "Dokter Umum",
            "halamanrk2.fxml"
        ));
        
        doctorList.add(new Doctor(
            "dr. Bambang Riyadi", 
            "Selamat datang! Saya dr. Bambang Riyadi. Ada yang bisa saya bantu?",
            "Dokter Jantung",
            "halamanrk.fxml"
        ));
        
        // Add more doctors as needed
        doctorList.add(new Doctor(
            "dr. Sari Indah", 
            "Halo! Saya dr. Sari Indah, siap membantu konsultasi Anda.",
            "Dokter Umum",
            "halamanrk4.fxml"
        ));
        
        doctorList.add(new Doctor(
            "dr. Budi Santoso", 
            "Selamat datang di konsultasi kesehatan. Saya dr. Budi Santoso.",
            "Dokter Mata",
            "halamanrk5.fxml"
        ));
        
        // Initialize filtered list
        filteredDoctorList = new ArrayList<>(doctorList);
    }
    
    private void initializeFilterComboBox() {
        if (filterComboBox != null) {
            refreshFilterOptions();
        }
    }
    
    private void performSearch() {
        filteredDoctorList.clear();
        
        // Apply both search and filter simultaneously
        for (Doctor doctor : doctorList) {
            boolean matchesSearch = true;
            boolean matchesFilter = true;
            
            if (!currentSearchText.isEmpty()) {
                String searchLower = currentSearchText.toLowerCase();
                matchesSearch = doctor.getName().toLowerCase().contains(searchLower) ||
                               doctor.getSpecialty().toLowerCase().contains(searchLower) ||
                               doctor.getMessage().toLowerCase().contains(searchLower);
            }
            
            if (!currentFilter.equals("Semua Dokter")) {
                matchesFilter = doctor.getSpecialty().equals(currentFilter);
            }
            
            
            if (matchesSearch && matchesFilter) {
                filteredDoctorList.add(doctor);
            }
        }
        
        // Update UI display
        updateDoctorDisplay();
        
        // Print search results
        System.out.println("Search Results:");
        System.out.println("Keyword: '" + currentSearchText + "'");
        System.out.println("Filter: " + currentFilter);
        System.out.println("Found " + filteredDoctorList.size() + " doctor(s):");
        for (Doctor doctor : filteredDoctorList) {
            System.out.println("- " + doctor.getName() + " (" + doctor.getSpecialty() + ")");
        }
        System.out.println("---");
    }
    
    private void updateDoctorDisplay() {
        if (doctorListContainer != null) {
            // Clear existing UI elements
            doctorListContainer.getChildren().clear();
            
            // Add filtered doctors to UI
            for (Doctor doctor : filteredDoctorList) {
                VBox doctorCard = createDoctorCard(doctor);
                doctorListContainer.getChildren().add(doctorCard);
            }
            
            // Show "No doctors found" message if empty
            if (filteredDoctorList.isEmpty()) {
                Label noResultLabel = new Label("Tidak ada dokter yang ditemukan");
                noResultLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 14px; -fx-padding: 20;");
                doctorListContainer.getChildren().add(noResultLabel);
            }
        }
    }
    
    private VBox createDoctorCard(Doctor doctor) {
        VBox doctorCard = new VBox();
        doctorCard.setSpacing(8);
        doctorCard.setStyle("-fx-background-color: linear-gradient(to right, #ffffff, #fefefe); " +
                           "-fx-padding: 18; -fx-background-radius: 15; -fx-cursor: hand; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 3); " +
                           "-fx-border-color: rgba(244,140,42,0.1); -fx-border-width: 1; " +
                           "-fx-border-radius: 15;");
        
        // Add click event based on doctor
        doctorCard.setOnMouseClicked(e -> openDoctorChat(doctor));
        
        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setSpacing(12);
        
        // Orange accent bar
        Region accentBar = new Region();
        accentBar.setStyle("-fx-background-color: #f48c2a; -fx-pref-width: 4; " +
                          "-fx-pref-height: 40; -fx-background-radius: 2;");
        
        VBox textBox = new VBox();
        textBox.setSpacing(4);
        
        HBox nameBox = new HBox();
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setSpacing(8);
        
        Label nameLabel = new Label(doctor.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333; -fx-font-size: 16px;");
        
        Label specialtyLabel = new Label("• " + doctor.getSpecialty());
        specialtyLabel.setStyle("-fx-text-fill: #f48c2a; -fx-font-size: 12px; -fx-font-weight: bold;");
        
        nameBox.getChildren().addAll(nameLabel, specialtyLabel);
        
        Label messageLabel = new Label(doctor.getMessage());
        messageLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 13px; -fx-wrap-text: true;");
        messageLabel.setWrapText(true);
        
        textBox.getChildren().addAll(nameBox, messageLabel);
        contentBox.getChildren().addAll(accentBar, textBox);
        doctorCard.getChildren().add(contentBox);
        
        return doctorCard;
    }
    
    private void openDoctorChat(Doctor doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(doctor.getFxmlFile()));
            Parent chatPage = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(chatPage));
            System.out.println("Opening chat with: " + doctor.getName());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error opening chat for: " + doctor.getName());
        }
    }
    
    // Method to get doctor by name with keyword detection
    public Doctor getDoctorByName(String name) {
        return doctorList.stream()
                .filter(doctor -> doctor.getName().toLowerCase().contains(name.toLowerCase()))
                .findFirst()
                .orElse(null);
    }
    
    // Method to search doctors by keyword
    public ArrayList<Doctor> searchDoctors(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(doctorList);
        }
        
        String searchLower = keyword.toLowerCase();
        return doctorList.stream()
                .filter(doctor -> doctor.getName().toLowerCase().contains(searchLower) ||
                                doctor.getSpecialty().toLowerCase().contains(searchLower) ||
                                doctor.getMessage().toLowerCase().contains(searchLower))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // Method to filter doctors by specialty
    public ArrayList<Doctor> filterBySpecialty(String specialty) {
        if (specialty == null || specialty.equals("Semua Dokter")) {
            return new ArrayList<>(doctorList);
        }
        
        return doctorList.stream()
                .filter(doctor -> doctor.getSpecialty().equals(specialty))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // Method to get all available specialties
    public ArrayList<String> getAvailableSpecialties() {
        return doctorList.stream()
                .map(Doctor::getSpecialty)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    // Method to refresh filter options based on current data
    public void refreshFilterOptions() {
        if (filterComboBox != null) {
            ArrayList<String> specialties = getAvailableSpecialties();
            specialties.add(0, "Semua Dokter");
            
            ObservableList<String> filterOptions = FXCollections.observableArrayList(specialties);
            filterComboBox.setItems(filterOptions);
            filterComboBox.setValue("Semua Dokter");
        }
    }
    
    // Method to add new doctor with automatic UI update
    public void addDoctor(Doctor doctor) {
        doctorList.add(doctor);
        refreshFilterOptions();
        performSearch(); // Refresh display
        System.out.println("Doctor added: " + doctor.getName());
    }
    
    // Method to remove doctor with automatic UI update
    public void removeDoctor(String name) {
        boolean removed = doctorList.removeIf(doctor -> doctor.getName().equals(name));
        if (removed) {
            refreshFilterOptions();
            performSearch(); // Refresh display
            System.out.println("Doctor removed: " + name);
        }
    }
    
    // Getter for doctor list
    public ArrayList<Doctor> getDoctorList() {
        return new ArrayList<>(doctorList);
    }
    
    // Getter for filtered doctor list
    public ArrayList<Doctor> getFilteredDoctorList() {
        return new ArrayList<>(filteredDoctorList);
    }
    
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman1.fxml"));
            Parent halaman1 = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(halaman1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleOpenChatBambang() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanrk.fxml"));
            Parent chatPage = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(chatPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleOpenChatRina() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanrk2.fxml"));
            Parent chatPage = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(chatPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleOpenChatAndi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamanrk3.fxml"));
            Parent chatPage = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(chatPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}