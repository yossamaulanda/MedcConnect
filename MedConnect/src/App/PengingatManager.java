package App;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.List;

public class PengingatManager {
    private static final PengingatManager instance = new PengingatManager();
    private final ObservableList<Pengingat> pengingatList;
    private final String FILE_NAME = "pengingat.xml";
    
    private PengingatManager() {
        pengingatList = FXCollections.observableArrayList();
        loadFromXML(); 
    }
    
    public static PengingatManager getInstance() {
        return instance;
    }
    
    public ObservableList<Pengingat> getPengingatList() {
        return pengingatList;
    }
    
    public void saveToXML() {
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.allowTypes(new Class[]{Pengingat.class});
            xstream.alias("pengingat", Pengingat.class);
            
            List<Pengingat> list = new java.util.ArrayList<>(pengingatList);
            String xml = xstream.toXML(list);
            
            FileOutputStream out = new FileOutputStream(FILE_NAME);
            out.write(xml.getBytes("UTF-8"));
            out.close();
            
            System.out.println("Data pengingat berhasil disimpan ke " + FILE_NAME);
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
            xstream.allowTypes(new Class[]{Pengingat.class});
            xstream.alias("pengingat", Pengingat.class);
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<Pengingat> list = (List<Pengingat>) xstream.fromXML(reader);
            pengingatList.setAll(list);
            
            System.out.println("Data pengingat berhasil dimuat dari " + FILE_NAME);
        } catch (Exception e) {
            System.out.println("Gagal load dari XML: " + e.getMessage());
        }
    }
    
    public void addPengingat(Pengingat pengingat) {
        pengingatList.add(pengingat);
        saveToXML();
    }
    
    public void removePengingat(Pengingat pengingat) {
        pengingatList.remove(pengingat);
        saveToXML();
    }
    
    public void updatePengingat(int index, Pengingat pengingat) {
        if (index >= 0 && index < pengingatList.size()) {
            pengingatList.set(index, pengingat);
            saveToXML();
        }
    }
}