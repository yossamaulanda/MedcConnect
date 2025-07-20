package App;

public class Pengingat {
    private String nama;
    private String namaDokter;
    private String saranDokter;
    
    public Pengingat(String nama, String namaDokter, String saranDokter) {
        this.nama = nama;
        this.namaDokter = namaDokter;
        this.saranDokter = saranDokter;
    }
    
    public Pengingat() {
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getNamaDokter() {
        return namaDokter;
    }
    
    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }
    
    public String getSaranDokter() {
        return saranDokter;
    }
    
    public void setSaranDokter(String saranDokter) {
        this.saranDokter = saranDokter;
    }
    
    @Override
    public String toString() {
        return "Pengingat{" +
                "nama='" + nama + '\'' +
                ", namaDokter='" + namaDokter + '\'' +
                ", saranDokter='" + saranDokter + '\'' +
                '}';
    }
}