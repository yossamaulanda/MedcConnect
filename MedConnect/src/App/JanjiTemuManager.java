package App;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class untuk mengelola data janji temu
 * Memungkinkan sharing data antar controller
 */
public class JanjiTemuManager {
    private static JanjiTemuManager instance;
    private List<ControllerJanjitemu.JanjiTemu> daftarJanjiTemu;
    
    private JanjiTemuManager() {
        daftarJanjiTemu = new ArrayList<>();
        initDefaultData();
    }
    
    public static JanjiTemuManager getInstance() {
        if (instance == null) {
            instance = new JanjiTemuManager();
        }
        return instance;
    }
    
    private void initDefaultData() {
        // Data default janji temu
        daftarJanjiTemu.add(new ControllerJanjitemu.JanjiTemu("001", "Azmi", "0812-1234-5678", 
            "25 Juli 2025", "09:00 - 09:30", "Demam dan sakit tenggorokan", "MENUNGGU"));
            
        daftarJanjiTemu.add(new ControllerJanjitemu.JanjiTemu("002", "Ibra", "0813-5678-9012",
            "25 Juli 2025", "13:00 - 13:30", "Kontrol rutin diabetes", "MENUNGGU"));
            
        daftarJanjiTemu.add(new ControllerJanjitemu.JanjiTemu("003", "Abdil", "0814-7890-1234",
            "24 Juli 2025", "15:30 - 16:00", "Penyakit demam", "MENUNGGU"));
    }
    
    public void addJanjiTemu(String nama, String telepon, String tanggal, 
                            String waktu, String keluhan) {
        String id = generateId();
        ControllerJanjitemu.JanjiTemu janjiTemu = new ControllerJanjitemu.JanjiTemu(
            id, nama, telepon, tanggal, waktu, keluhan, "MENUNGGU"
        );
        daftarJanjiTemu.add(janjiTemu);
        System.out.println("Janji temu baru ditambahkan: ID " + id + " - " + nama);
    }
    
    public void addJanjiTemuFromChat(String chatContent) {
        // Parse chat content untuk mendapatkan data janji temu
        try {
            String[] lines = chatContent.split("\n");
            String nama = "";
            String telepon = "";
            String tanggal = "";
            String waktu = "";
            String keluhan = "";
            
            for (String line : lines) {
                if (line.contains("Judul:")) {
                    keluhan = line.substring(line.indexOf("Judul:") + 6).trim();
                } else if (line.contains("Tanggal:")) {
                    tanggal = line.substring(line.indexOf("Tanggal:") + 8).trim();
                } else if (line.contains("Waktu:")) {
                    waktu = line.substring(line.indexOf("Waktu:") + 6).trim();
                } else if (line.contains("Dibuat oleh:")) {
                    nama = line.substring(line.indexOf("Dibuat oleh:") + 12).trim();
                }
            }
            
            // Set default values jika tidak ada
            if (telepon.isEmpty()) telepon = "Tidak tersedia";
            if (nama.isEmpty()) nama = "Pasien";
            if (keluhan.isEmpty()) keluhan = "Konsultasi umum";
            
            // Generate waktu slot jika hanya waktu yang disebutkan
            if (!waktu.isEmpty() && !waktu.contains("-")) {
                waktu = waktu + " - " + addMinutes(waktu, 30);
            }
            
            addJanjiTemu(nama, telepon, tanggal, waktu, keluhan);
            
        } catch (Exception e) {
            System.err.println("Error parsing chat content for janji temu: " + e.getMessage());
            // Fallback - tambah dengan data minimal
            addJanjiTemu("Pasien dari Chat", "Tidak tersedia", 
                        "Belum ditentukan", "Belum ditentukan", "Konsultasi dari chat room");
        }
    }
    
    private String addMinutes(String time, int minutes) {
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int mins = Integer.parseInt(parts[1]) + minutes;
            
            if (mins >= 60) {
                hours += mins / 60;
                mins = mins % 60;
            }
            
            return String.format("%02d:%02d", hours, mins);
        } catch (Exception e) {
            return time; // Return original if parsing fails
        }
    }
    
    private String generateId() {
        return String.format("%03d", daftarJanjiTemu.size() + 1);
    }
    
    public List<ControllerJanjitemu.JanjiTemu> getDaftarJanjiTemu() {
        return new ArrayList<>(daftarJanjiTemu);
    }
    
    public void removeJanjiTemu(ControllerJanjitemu.JanjiTemu janji) {
        daftarJanjiTemu.remove(janji);
    }
    
    public void updateStatus(String id, String status) {
        for (ControllerJanjitemu.JanjiTemu janji : daftarJanjiTemu) {
            if (janji.getId().equals(id)) {
                janji.setStatus(status);
                break;
            }
        }
    }
    
    public int getTotalJanjiTemu() {
        return daftarJanjiTemu.size();
    }
    
    public int getCountByStatus(String status) {
        return (int) daftarJanjiTemu.stream()
                .filter(janji -> janji.getStatus().equals(status))
                .count();
    }
}