package App;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

public class RiwayatPasienManager {
    private static final RiwayatPasienManager instance = new RiwayatPasienManager();
    private final ObservableList<RiwayatPasien> riwayatPasienList;
    private final String FILE_NAME = "riwayat_pasien.xml";
    
    private RiwayatPasienManager() {
        riwayatPasienList = FXCollections.observableArrayList();
        loadFromXML(); 
    }
    
    public static RiwayatPasienManager getInstance() {
        return instance;
    }
    
    public ObservableList<RiwayatPasien> getRiwayatPasienList() {
        return riwayatPasienList;
    }
    
    public void saveToXML() {
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.allowTypes(new Class[]{RiwayatPasien.class});
            xstream.alias("riwayatPasien", RiwayatPasien.class);
            
            List<RiwayatPasien> list = new java.util.ArrayList<>(riwayatPasienList);
            String xml = xstream.toXML(list);
            
            FileOutputStream out = new FileOutputStream(FILE_NAME);
            out.write(xml.getBytes("UTF-8"));
            out.close();
            
            System.out.println("Data riwayat pasien berhasil disimpan ke " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan ke XML: " + e.getMessage());
        }
    }
    
    public void loadFromXML() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("File " + FILE_NAME + " tidak ditemukan, membuat data default.");
                return;
            }
            
            XStream xstream = new XStream(new DomDriver());
            xstream.allowTypes(new Class[]{RiwayatPasien.class});
            xstream.alias("riwayatPasien", RiwayatPasien.class);
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<RiwayatPasien> list = (List<RiwayatPasien>) xstream.fromXML(reader);
            riwayatPasienList.setAll(list);
            
            System.out.println("Data riwayat pasien berhasil dimuat dari " + FILE_NAME);
        } catch (Exception e) {
            System.out.println("Gagal load dari XML: " + e.getMessage());
        }
    }
    
    public void addRiwayatPasien(RiwayatPasien riwayatPasien) {
        riwayatPasienList.add(riwayatPasien);
        saveToXML();
    }
    
    public void removeRiwayatPasien(RiwayatPasien riwayatPasien) {
        riwayatPasienList.remove(riwayatPasien);
        saveToXML();
    }
    
    public void updateRiwayatPasien(int index, RiwayatPasien riwayatPasien) {
        if (index >= 0 && index < riwayatPasienList.size()) {
            riwayatPasienList.set(index, riwayatPasien);
            saveToXML();
        }
    }
}