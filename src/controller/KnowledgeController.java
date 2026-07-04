package controller;

import model.KnowledgeRepository;
import model.Putusan;
import model.StatistikPutusan;
import util.InputHandler;
import view.ConsoleView;
import java.util.ArrayList;
import java.util.Scanner;

public class KnowledgeController {

    private final KnowledgeRepository repository;
    private final ConsoleView          view;

    public KnowledgeController(KnowledgeRepository repository, ConsoleView view) {
        this.repository = repository;
        this.view       = view;
    }

    //Tambah Putusan
    public void handleTambahPutusan(Scanner sc) {
        // View mengumpulkan data mentah dari pengguna
        String[] rawData = view.inputFormPutusan(sc);

        //Controller memvalidasi & membuat objek, lalu simpan ke Model
        boolean sukses = tambahPutusan(rawData);

        // Controller memberi tahu View hasilnya
        if (sukses) {
            view.tampilkanPesan("Putusan berhasil ditambahkan! "
                    + "Total data: " + repository.getTotalData());
        } else {
            view.tampilkanPesan("Gagal menambahkan putusan. Periksa kembali data Anda.");
        }
    }

    public boolean tambahPutusan(String[] data) {
        try {
            //Validasi jumlah field
            if (data == null || data.length < 12)
                throw new IllegalArgumentException("Data tidak lengkap (butuh 12 field).");

            //Validasi nomor perkara tidak duplikat
            if (repository.cariByNomor(data[0]) != null)
                throw new IllegalArgumentException("Nomor perkara sudah ada: " + data[0]);

            //Parse dan validasi tipe data
            int    umur         = Integer.parseInt(data[4]);
            double beratBB      = Double.parseDouble(data[6]);
            int    vonisHukuman = Integer.parseInt(data[9]);
            double vonisDenda   = Double.parseDouble(data[10]);

            //Validasi batasan nilai bisnis
            if (umur <= 0 || umur > 120)
                throw new IllegalArgumentException("Umur tidak valid: " + umur);
            if (beratBB <= 0)
                throw new IllegalArgumentException("Berat barang bukti harus > 0 gram.");
            if (vonisHukuman < 0)
                throw new IllegalArgumentException("Vonis hukuman tidak boleh negatif.");
            if (vonisDenda < 0)
                throw new IllegalArgumentException("Vonis denda tidak boleh negatif.");

            //Buat objek Putusan menggunakan constructor
            Putusan p = new Putusan(
                    data[0], data[1], data[2], data[3],
                    umur,    data[5], beratBB,  data[7],
                    data[8], vonisHukuman, vonisDenda, data[11]
            );

            //Simpan ke Repository (Model)
            repository.simpan(p);
            return true;

        } catch (NumberFormatException e) {
            view.tampilkanPesan("Format angka tidak valid: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            view.tampilkanPesan("Validasi gagal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            view.tampilkanPesan("Terjadi kesalahan: " + e.getMessage());
            return false;
        }
    }

    //Tampilkan dan Cari
    public void tampilkanSemua() {
        ArrayList<Putusan> semua = repository.getDaftarSemua();
        view.tampilkanHeader("Daftar Semua Putusan (" + semua.size() + " data)");
        view.tampilkanDaftarPutusan(semua);
    }

    public ArrayList<Putusan> cariPutusan(String keyword, String mode) {
        if ("nomor".equalsIgnoreCase(mode)) {
            Putusan hasil = repository.cariByNomor(keyword);
            ArrayList<Putusan> list = new ArrayList<>();
            if (hasil != null) list.add(hasil);
            return list;
        } else {
            return repository.cariByNama(keyword);
        }
    }

    public void handleCariPutusan(Scanner sc) {
        int modeIdx = view.tampilkanMenuCari(sc);
        String mode = (modeIdx == 1) ? "nomor" : "nama";
        String prompt = (modeIdx == 1) ? "  Masukkan Nomor Perkara: " : "  Masukkan Nama Terdakwa: ";
        String keyword = view.inputKeyword(prompt, sc);

        ArrayList<Putusan> hasil = cariPutusan(keyword, mode);

        view.tampilkanHeader("Hasil Pencarian: \"" + keyword + "\"");
        if (hasil.isEmpty()) {
            view.tampilkanPesan("⚠  Data tidak ditemukan untuk keyword: " + keyword);
        } else if (hasil.size() == 1 && "nomor".equalsIgnoreCase(mode)) {
            view.tampilkanDetail(hasil.get(0));  // tampilkan detail jika cari by nomor
        } else {
            view.tampilkanDaftarPutusan(hasil);
        }
    }

    //Filter
    public ArrayList<Putusan> filterPutusan(String kriteria, String nilai) {
        switch (kriteria.toLowerCase()) {
            case "jenis":      return repository.filterByJenis(nilai);
            case "pengadilan": return repository.filterByPengadilan(nilai);
            case "kategori":   return repository.filterByKategori(nilai);
            default:
                view.tampilkanPesan("⚠  Kriteria filter tidak dikenal: " + kriteria);
                return new ArrayList<>();
        }
    }

    public void handleFilterPutusan(Scanner sc) {
        int pilihanFilter = view.tampilkanMenuFilter(sc);

        String kriteria, nilai;
        switch (pilihanFilter) {
            case 1:
                kriteria = "jenis";
                nilai = InputHandler.validasiStringDariPilihan(
                        "  Jenis Narkotika (Sabu-sabu/Ganja/Ekstasi/Heroin/Lainnya): ",
                        new String[]{"Sabu-sabu", "Ganja", "Ekstasi", "Heroin", "Lainnya"}, sc);
                break;
            case 2:
                kriteria = "pengadilan";
                nilai = view.inputKeyword("  Nama Pengadilan: ", sc);
                break;
            case 3:
                kriteria = "kategori";
                nilai = InputHandler.validasiStringDariPilihan(
                        "  Kategori Hukuman (Ringan/Sedang/Berat): ",
                        new String[]{"Ringan", "Sedang", "Berat"}, sc);
                break;
            default:
                view.tampilkanPesan("Pilihan tidak valid.");
                return;
        }

        ArrayList<Putusan> hasil = filterPutusan(kriteria, nilai);
        view.tampilkanHeader("Hasil Filter [" + kriteria + " = " + nilai + "]");
        view.tampilkanDaftarPutusan(hasil);
    }

    //Hapus Putusan
    public boolean hapusPutusan(String nomor) {
        return repository.hapus(nomor);
    }

    public void handleHapusPutusan(Scanner sc) {
        String nomor = view.inputKeyword("Masukkan Nomor Perkara yang akan dihapus: ", sc);

        //Cek dulu apakah data ada
        Putusan target = repository.cariByNomor(nomor);
        if (target == null) {
            view.tampilkanPesan("Data dengan nomor perkara '" + nomor + "' tidak ditemukan.");
            return;
        }

        //Tampilkan data yang akan dihapus sebagai konfirmasi
        view.tampilkanDetail(target);
        String konfirmasi = InputHandler.validasiStringDariPilihan(
                "  Yakin ingin menghapus? (ya/tidak): ",
                new String[]{"ya", "tidak"}, sc);

        if ("ya".equalsIgnoreCase(konfirmasi)) {
            boolean sukses = hapusPutusan(nomor);
            view.tampilkanPesan(sukses
                    ? "Putusan berhasil dihapus! Sisa data: " + repository.getTotalData()
                    : "Gagal menghapus putusan.");
        } else {
            view.tampilkanPesan("  Penghapusan dibatalkan.");
        }
    }

    //Perbarui Putusan
    public void handleUpdatePutusan(Scanner sc) {
        String nomor = view.inputKeyword("Masukkan Nomor Perkara yang akan diupdate: ", sc);

        Putusan lama = repository.cariByNomor(nomor);
        if (lama == null) {
            view.tampilkanPesan("Data tidak ditemukan: " + nomor);
            return;
        }

        view.tampilkanPesan("Data lama:");
        view.tampilkanDetail(lama);
        view.tampilkanPesan("Masukkan data baru:");

        String[] rawData = view.inputFormPutusan(sc);

        try {
            Putusan baru = new Putusan(
                    rawData[0], rawData[1], rawData[2], rawData[3],
                    Integer.parseInt(rawData[4]), rawData[5],
                    Double.parseDouble(rawData[6]), rawData[7], rawData[8],
                    Integer.parseInt(rawData[9]), Double.parseDouble(rawData[10]), rawData[11]
            );
            String konfirmasi = InputHandler.validasiStringDariPilihan(
                    "  Yakin ingin mengupdate? (ya/tidak): ",
                    new String[]{"ya", "tidak"}, sc);

            if ("ya".equalsIgnoreCase(konfirmasi)) {
                boolean sukses = repository.update(nomor, baru);
                view.tampilkanPesan(sukses
                        ? "utusan berhasil diperbarui!"
                        : "Gagal memperbarui putusan.");
            } else {
                view.tampilkanPesan("  Update dibatalkan.");
            }
        } catch (Exception e) {
            view.tampilkanPesan("Error saat update: " + e.getMessage());
        }
    }

    //Statistik
    public StatistikPutusan getStatistik() {
        return new StatistikPutusan(repository.getDaftarSemua());
    }

    public void tampilkanStatistik() {
        StatistikPutusan stat = getStatistik();
        view.tampilkanStatistik(stat);
        view.tampilkanPesan("Total objek Putusan yang dibuat: " + Putusan.getJumlahDibuat());
    }

    //GETTER util
    public ArrayList<Putusan> getDaftarSemua() {
        return repository.getDaftarSemua();
    }

    public int getTotalData() {
        return repository.getTotalData();
    }
}
