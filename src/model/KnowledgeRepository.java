package model;
import java.util.ArrayList;

public class KnowledgeRepository {
    private ArrayList<Putusan> daftarPutusan;

    public KnowledgeRepository() {
        this.daftarPutusan = new ArrayList<>();
    }

    public void simpan(Putusan putusan) {
        daftarPutusan.add(putusan);
    }

    public Putusan cariByNomor(String nomor) {
        for (Putusan putusan : daftarPutusan) {
            if (putusan.getNomorPerkara().equalsIgnoreCase(nomor.trim())) {
                return putusan;
            }
        }
        return null;
    }

    public ArrayList<Putusan> cariByNama(String nama) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan putusan : daftarPutusan) {
            if (putusan.getNamaTerdakwa().toLowerCase().contains(nama.toLowerCase())) {
                hasil.add(putusan);
            }
        }
        return hasil;
    }

    public ArrayList<Putusan> filterByJenis(String jenis) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan putusan : daftarPutusan) {
            if (putusan.getJenisNarkotika().equalsIgnoreCase(jenis.trim())) {
                hasil.add(putusan);
            }
        }
        return hasil;
    }

    public ArrayList<Putusan> filterByPengadilan(String pengadilan) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan putusan : daftarPutusan) {
            if (putusan.getPengadilan().toLowerCase().contains(pengadilan.toLowerCase())) {
                hasil.add(putusan);
            }
        }
        return hasil;
    }

    public ArrayList<Putusan> filterByKategori(String kategori) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        for (Putusan p : daftarPutusan) {
            if (p.getKategoriHukuman().equalsIgnoreCase(kategori.trim())) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public boolean hapus(String nomor) {
        Putusan target = cariByNomor(nomor);
        if (target != null) {
            daftarPutusan.remove(target);
            return true;
        }
        return false;
    }

    public ArrayList<Putusan> getDaftarSemua() {
        return daftarPutusan;
    }

    public int getTotalData() {
        return daftarPutusan.size();
    }
}
