package App;

public class RiwayatPasien {
    private String namaPasien;
    private String keluhan;
    private String diagnosis;
    private String tanggal;
    private String resepObat;
    
    public RiwayatPasien(String namaPasien, String keluhan, String diagnosis, String tanggal, String resepObat) {
        this.namaPasien = namaPasien;
        this.keluhan = keluhan;
        this.diagnosis = diagnosis;
        this.tanggal = tanggal;
        this.resepObat = resepObat;
    }
    
    // Constructor tanpa parameter untuk XStream
    public RiwayatPasien() {
    }
    
    public String getNamaPasien() {
        return namaPasien;
    }
    
    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }
    
    public String getKeluhan() {
        return keluhan;
    }
    
    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }
    
    public String getDiagnosis() {
        return diagnosis;
    }
    
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    
    public String getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    
    public String getResepObat() {
        return resepObat;
    }
    
    public void setResepObat(String resepObat) {
        this.resepObat = resepObat;
    }
    
    @Override
    public String toString() {
        return "RiwayatPasien{" +
                "namaPasien='" + namaPasien + '\'' +
                ", keluhan='" + keluhan + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", resepObat='" + resepObat + '\'' +
                '}';
    }
}