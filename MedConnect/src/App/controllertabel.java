package App;

import javafx.beans.property.SimpleStringProperty;
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

public class controllertabel {
    
    @FXML
    private TableView<Pengingat> tablePengingat;
    
    @FXML
    private TableColumn<Pengingat, String> columnNamaDokter;
    
    @FXML
    private TableColumn<Pengingat, String> columnSaranDokter;
    
    @FXML
    private TextField fieldNamaDokter;
    
    @FXML
    private TextField fieldSaranDokter;
    
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
    
    private ObservableList<Pengingat> pengingatList;
    
    @FXML
    public void initialize() {
        columnNamaDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        columnSaranDokter.setCellValueFactory(new PropertyValueFactory<>("saranDokter"));
        columnNamaDokter.setCellFactory(TextFieldTableCell.forTableColumn());
        columnSaranDokter.setCellFactory(TextFieldTableCell.forTableColumn());
        

        tablePengingat.setEditable(true);
        pengingatList = PengingatManager.getInstance().getPengingatList();
        tablePengingat.setItems(pengingatList);
        updateTotalLabel();
        pengingatList.addListener((javafx.collections.ListChangeListener<Pengingat>) change -> {
            updateTotalLabel();
        });
        
        columnNamaDokter.setOnEditCommit(event -> {
            Pengingat pengingat = event.getRowValue();
            pengingat.setNamaDokter(event.getNewValue());
            PengingatManager.getInstance().saveToXML();
            showAlert("Berhasil", "Data nama dokter berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
        
        columnSaranDokter.setOnEditCommit(event -> {
            Pengingat pengingat = event.getRowValue();
            pengingat.setSaranDokter(event.getNewValue());
            PengingatManager.getInstance().saveToXML();
            showAlert("Berhasil", "Data saran dokter berhasil diperbarui!", Alert.AlertType.INFORMATION);
        });
    }
    
    @FXML
    private void onTambahClicked() {
        String namaDokter = fieldNamaDokter.getText().trim();
        String saranDokter = fieldSaranDokter.getText().trim();
        
        if (namaDokter.isEmpty() || saranDokter.isEmpty()) {
            showAlert("Error", "Nama dokter dan saran dokter harus diisi!", Alert.AlertType.ERROR);
            return;
        }
        
        Pengingat pengingat = new Pengingat(namaDokter, namaDokter, saranDokter);
        PengingatManager.getInstance().addPengingat(pengingat);
        fieldNamaDokter.clear();
        fieldSaranDokter.clear();
        showAlert("Berhasil", "Data pengingat berhasil ditambahkan!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void onEditClicked() {
        Pengingat selected = tablePengingat.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang akan diedit terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        String namaDokter = fieldNamaDokter.getText().trim();
        String saranDokter = fieldSaranDokter.getText().trim();
        
        if (namaDokter.isEmpty() || saranDokter.isEmpty()) {
            showAlert("Error", "Nama dokter dan saran dokter harus diisi!", Alert.AlertType.ERROR);
            return;
        }

        selected.setNama(namaDokter);
        selected.setNamaDokter(namaDokter);
        selected.setSaranDokter(saranDokter);
        
        PengingatManager.getInstance().saveToXML();
        tablePengingat.refresh();
        fieldNamaDokter.clear();
        fieldSaranDokter.clear();
        
        showAlert("Berhasil", "Data pengingat berhasil diperbarui!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void onHapusClicked() {
        Pengingat selected = tablePengingat.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang akan dihapus terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin menghapus data ini?");
        confirmAlert.setContentText("Nama Dokter: " + selected.getNamaDokter() + 
                                  "\nSaran Dokter: " + selected.getSaranDokter());
        
        if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            PengingatManager.getInstance().removePengingat(selected);
            showAlert("Berhasil", "Data pengingat berhasil dihapus!", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void onKembaliClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("halaman1.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnKembali.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MedConnect - Halaman Utama");
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal kembali ke halaman utama: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void onTableClicked() {
        Pengingat selected = tablePengingat.getSelectionModel().getSelectedItem();
        if (selected != null) {
            fieldNamaDokter.setText(selected.getNamaDokter());
            fieldSaranDokter.setText(selected.getSaranDokter());
        }
    }
    
    private void updateTotalLabel() {
        int total = pengingatList.size();
        labelTotal.setText("Total Pengingat: " + total);
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}