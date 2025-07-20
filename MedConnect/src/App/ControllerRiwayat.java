package App;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ControllerRiwayat {
    
    @FXML
    private TableView<RiwayatPasien> tableRiwayat;
    
    @FXML
    private TableColumn<RiwayatPasien, String> columnNamaPasien;
    
    @FXML
    private TableColumn<RiwayatPasien, String> columnKeluhan;
    
    @FXML
    private TableColumn<RiwayatPasien, String> columnDiagnosis;
    
    @FXML
    private TableColumn<RiwayatPasien, String> columnTanggal;
    
    @FXML
    private TableColumn<RiwayatPasien, String> columnResepObat;
    
    @FXML
    private TextField fieldNamaPasien;
    
    @FXML
    private TextField fieldKeluhan;
    
    @FXML
    private TextField fieldDiagnosis;
    
    @FXML
    private TextField fieldTanggal;  
    
    @FXML
    private TextField fieldResepObat;
    
    @FXML
    private Button btnTambah;
    
    @FXML
    private Button btnEdit;
    
    @FXML
    private Button btnHapus;
    
    @FXML
    private Button btnKembali;
    
    @FXML
    private Label labelTotal;
    
    private ObservableList<RiwayatPasien> riwayatPasienList;
    private ArrayList<RiwayatPasien> riwayatArrayList;  
    
    @FXML
    public void initialize() {
        riwayatArrayList = new ArrayList<>();
        columnNamaPasien.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        columnKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        columnDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        columnTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        columnResepObat.setCellValueFactory(new PropertyValueFactory<>("resepObat"));
        
        columnNamaPasien.setCellFactory(TextFieldTableCell.forTableColumn());
        columnKeluhan.setCellFactory(TextFieldTableCell.forTableColumn());
        columnDiagnosis.setCellFactory(TextFieldTableCell.forTableColumn());
        columnTanggal.setCellFactory(TextFieldTableCell.forTableColumn());
        columnResepObat.setCellFactory(TextFieldTableCell.forTableColumn());
    
        tableRiwayat.setEditable(true);
        riwayatPasienList = RiwayatPasienManager.getInstance().getRiwayatPasienList();
        tableRiwayat.setItems(riwayatPasienList);
        syncArrayListWithObservableList();
        fieldTanggal.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        updateTotalLabel();
        
        riwayatPasienList.addListener((javafx.collections.ListChangeListener<RiwayatPasien>) change -> {
            updateTotalLabel();
            syncArrayListWithObservableList();
        });
        
        columnNamaPasien.setOnEditCommit(event -> {
            RiwayatPasien riwayat = event.getRowValue();
            riwayat.setNamaPasien(event.getNewValue());
            RiwayatPasienManager.getInstance().saveToXML();
            syncArrayListWithObservableList();
            showAlert("Berhasil", "Data nama pasien berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
        
        columnKeluhan.setOnEditCommit(event -> {
            RiwayatPasien riwayat = event.getRowValue();
            riwayat.setKeluhan(event.getNewValue());
            RiwayatPasienManager.getInstance().saveToXML();
            syncArrayListWithObservableList();
            showAlert("Berhasil", "Data keluhan berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
        
        columnDiagnosis.setOnEditCommit(event -> {
            RiwayatPasien riwayat = event.getRowValue();
            riwayat.setDiagnosis(event.getNewValue());
            RiwayatPasienManager.getInstance().saveToXML();
            syncArrayListWithObservableList();
            showAlert("Berhasil", "Data diagnosis berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
        
        columnTanggal.setOnEditCommit(event -> {
            RiwayatPasien riwayat = event.getRowValue();
            String newDate = event.getNewValue();
            if (isValidDateFormat(newDate)) {
                riwayat.setTanggal(newDate);
                RiwayatPasienManager.getInstance().saveToXML();
                syncArrayListWithObservableList();
                showAlert("Berhasil", "Data tanggal berhasil diperbarui!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Format tanggal tidak valid! Gunakan format DD-MM-YYYY", Alert.AlertType.ERROR);
                tableRiwayat.refresh();
            }
        });
        
        columnResepObat.setOnEditCommit(event -> {
            RiwayatPasien riwayat = event.getRowValue();
            riwayat.setResepObat(event.getNewValue());
            RiwayatPasienManager.getInstance().saveToXML();
            syncArrayListWithObservableList();
            showAlert("Berhasil", "Data resep obat berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
    }
    
    @FXML
    private void onTambahClicked() {
        String namaPasien = fieldNamaPasien.getText().trim();
        String keluhan = fieldKeluhan.getText().trim();
        String diagnosis = fieldDiagnosis.getText().trim();
        String tanggal = fieldTanggal.getText().trim();
        String resepObat = fieldResepObat.getText().trim();
        
        if (namaPasien.isEmpty() || keluhan.isEmpty() || diagnosis.isEmpty() || resepObat.isEmpty()) {
            showAlert("Error", "Semua field harus diisi!", Alert.AlertType.ERROR);
            return;
        }
        
        if (tanggal.isEmpty() || !isValidDateFormat(tanggal)) {
            showAlert("Error", "Tanggal harus diisi dengan format DD/MM/YYYY!", Alert.AlertType.ERROR);
            return;
        }
        
        RiwayatPasien riwayat = new RiwayatPasien(namaPasien, keluhan, diagnosis, tanggal, resepObat);
        RiwayatPasienManager.getInstance().addRiwayatPasien(riwayat);
        riwayatArrayList.add(riwayat);
        clearFields();
        
        showAlert("Berhasil", "Data riwayat pasien berhasil ditambahkan!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void onEditClicked() {
        RiwayatPasien selected = tableRiwayat.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang akan diedit terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        String namaPasien = fieldNamaPasien.getText().trim();
        String keluhan = fieldKeluhan.getText().trim();
        String diagnosis = fieldDiagnosis.getText().trim();
        String tanggal = fieldTanggal.getText().trim();
        String resepObat = fieldResepObat.getText().trim();
        
        if (namaPasien.isEmpty() || keluhan.isEmpty() || diagnosis.isEmpty() || resepObat.isEmpty()) {
            showAlert("Error", "Semua field harus diisi!", Alert.AlertType.ERROR);
            return;
        }
        
        if (tanggal.isEmpty() || !isValidDateFormat(tanggal)) {
            showAlert("Error", "Tanggal harus diisi dengan format DD-MM-YYYY!", Alert.AlertType.ERROR);
            return;
        }
        
        selected.setNamaPasien(namaPasien);
        selected.setKeluhan(keluhan);
        selected.setDiagnosis(diagnosis);
        selected.setTanggal(tanggal);
        selected.setResepObat(resepObat);
        
        RiwayatPasienManager.getInstance().saveToXML();
        tableRiwayat.refresh();
        syncArrayListWithObservableList();
        clearFields();
        
        showAlert("Berhasil", "Data riwayat pasien berhasil diperbarui!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void onHapusClicked() {
        RiwayatPasien selected = tableRiwayat.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang akan dihapus terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin menghapus data ini?");
        confirmAlert.setContentText("Nama Pasien: " + selected.getNamaPasien() + 
                                  "\nKeluhan: " + selected.getKeluhan() + 
                                  "\nDiagnosis: " + selected.getDiagnosis());
        
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            RiwayatPasienManager.getInstance().removeRiwayatPasien(selected);
            riwayatArrayList.remove(selected);
            showAlert("Berhasil", "Data riwayat pasien berhasil dihapus!", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void onKembaliClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halamandokter.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnKembali.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MEDCONNECT - Halaman Dokter");
            stage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke halaman utama: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onTableClicked() {
        RiwayatPasien selected = tableRiwayat.getSelectionModel().getSelectedItem();
        if (selected != null) {
            fieldNamaPasien.setText(selected.getNamaPasien());
            fieldKeluhan.setText(selected.getKeluhan());
            fieldDiagnosis.setText(selected.getDiagnosis());
            fieldResepObat.setText(selected.getResepObat());
            fieldTanggal.setText(selected.getTanggal());
        }
    }
    
    private void clearFields() {
        fieldNamaPasien.clear();
        fieldKeluhan.clear();
        fieldDiagnosis.clear();
        fieldResepObat.clear();
        fieldTanggal.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void updateTotalLabel() {
        int total = riwayatPasienList.size();
        labelTotal.setText("Total Riwayat Pasien: " + total);
    }
    
    private void syncArrayListWithObservableList() {
        riwayatArrayList.clear();
        riwayatArrayList.addAll(riwayatPasienList);
    }
    
    private boolean isValidDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public ArrayList<RiwayatPasien> getRiwayatArrayList() {
        return riwayatArrayList;
    }
    
    public List<RiwayatPasien> searchByName(String name) {
        List<RiwayatPasien> result = new ArrayList<>();
        for (RiwayatPasien riwayat : riwayatArrayList) {
            if (riwayat.getNamaPasien().toLowerCase().contains(name.toLowerCase())) {
                result.add(riwayat);
            }
        }
        return result;
    }
    
    public List<RiwayatPasien> searchByDiagnosis(String diagnosis) {
        List<RiwayatPasien> result = new ArrayList<>();
        for (RiwayatPasien riwayat : riwayatArrayList) {
            if (riwayat.getDiagnosis().toLowerCase().contains(diagnosis.toLowerCase())) {
                result.add(riwayat);
            }
        }
        return result;
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}