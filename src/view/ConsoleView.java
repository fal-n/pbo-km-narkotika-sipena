package view;

import model.Putusan;
import model.StatistikPutusan;
import util.InputHandler;

import java.util.ArrayList;
import java.util.Scanner;



public class ConsoleView {


    public int tampilkanMenu(Scanner sc) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   KMS PUTUSAN PENGADILAN NARKOTIKA — MENU UTAMA  ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  1. Tampilkan Semua Putusan                      ║");
        System.out.println("║  2. Tambah Putusan Baru                          ║");
        System.out.println("║  3. Cari Putusan                                 ║");
        System.out.println("║  4. Filter Putusan                               ║");
        System.out.println("║  5. Hapus Putusan                                ║");
        System.out.println("║  6. Update Putusan                               ║");
        System.out.println("║  7. Lihat Statistik                              ║");
        System.out.println("║  8. Keluar                                       ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        return InputHandler.validasiPilihan("  Pilih menu [1-8]: ", 1, 8, sc);
    }



    public void tampilkanDaftarPutusan(ArrayList<Putusan> list) {
        if (list == null || list.isEmpty()) {
            tampilkanPesan("⚠  Tidak ada data putusan yang ditemukan.");
            return;
        }
        System.out.println("\n┌──────────────────────────────────────────────────────────────────────────────────────────┐");
        System.out.printf( "│  %-3s │ %-30s │ %-20s │ %-10s │ %6s │ %-6s │%n",
                "No", "Nomor Perkara", "Nama Terdakwa", "Narkotika", "Vonis", "Kat.");
        System.out.println("├──────────────────────────────────────────────────────────────────────────────────────────┤");
        for (int i = 0; i < list.size(); i++) {
            Putusan p = list.get(i);
            System.out.printf("│  %-3d │ %-30s │ %-20s │ %-10s │ %4d bl │ %-6s │%n",
                    (i + 1),
                    truncate(p.getNomorPerkara(), 30),
                    truncate(p.getNamaTerdakwa(), 20),
                    truncate(p.getJenisNarkotika(), 10),
                    p.getVonisHukuman(),
                    p.getKategoriHukuman());
        }
        System.out.println("└──────────────────────────────────────────────────────────────────────────────────────────┘");
        System.out.printf("  Total: %d putusan ditampilkan.%n", list.size());
    }


    public void tampilkanDetail(Putusan p) {
        if (p == null) {
            tampilkanPesan("⚠  Data putusan tidak ditemukan.");
            return;
        }
        p.tampilkan(true);  // panggil method overloading tampilkan(boolean)
    }


    public void tampilkanStatistik(StatistikPutusan stat) {
        if (stat == null) {
            tampilkanPesan("⚠  Data statistik tidak tersedia.");
            return;
        }
        stat.tampilkanLaporan();
    }


    public void tampilkanPesan(String pesan) {
        System.out.println("\n  >> " + pesan);
    }


    public void tampilkanHeader(String judul) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("  " + judul.toUpperCase());
        System.out.println("══════════════════════════════════════════");
    }


    public String[] inputFormPutusan(Scanner sc) {
        tampilkanHeader("FORM INPUT PUTUSAN BARU");
        String[] data = new String[12];

        data[0]  = InputHandler.validasiString("  Nomor Perkara    : ", sc);
        data[1]  = InputHandler.validasiString("  Pengadilan       : ", sc);
        data[2]  = InputHandler.validasiString("  Tanggal Putusan  : ", sc);
        data[3]  = InputHandler.validasiString("  Nama Terdakwa    : ", sc);
        data[4]  = String.valueOf(InputHandler.validasiInt("  Umur Terdakwa    : ", 1, 120, sc));

        System.out.println("  Jenis Narkotika  (1=Sabu-sabu, 2=Ganja, 3=Ekstasi, 4=Heroin, 5=Lainnya)");
        int jenisIdx = InputHandler.validasiPilihan("  Pilihan           : ", 1, 5, sc);
        String[] pilihanJenis = {"Sabu-sabu", "Ganja", "Ekstasi", "Heroin", "Lainnya"};
        data[5]  = pilihanJenis[jenisIdx - 1];

        data[6]  = String.valueOf(InputHandler.validasiDouble("  Berat BB (gram)  : ", 0.01, sc));
        data[7]  = InputHandler.validasiString("  Pasal Dilanggar  : ", sc);

        System.out.println("  Peran Terdakwa   (1=Bandar, 2=Kurir, 3=Pengguna, 4=Penyimpan, 5=Perantara)");
        int peranIdx = InputHandler.validasiPilihan("  Pilihan           : ", 1, 5, sc);
        String[] pilihanPeran = {"Bandar", "Kurir", "Pengguna", "Penyimpan", "Perantara"};
        data[8]  = pilihanPeran[peranIdx - 1];

        data[9]  = String.valueOf(InputHandler.validasiInt("  Vonis Hukuman (bln): ", 0, 9999, sc));
        data[10] = String.valueOf(InputHandler.validasiDouble("  Vonis Denda (Rp): ", 0, sc));
        data[11] = InputHandler.validasiString("  Nama Hakim       : ", sc);

        return data;
    }


    public String inputKeyword(String prompt, Scanner sc) {
        return InputHandler.validasiString(prompt, sc);
    }


    public int tampilkanMenuCari(Scanner sc) {
        System.out.println("\n  Cari berdasarkan:");
        System.out.println("  1. Nomor Perkara");
        System.out.println("  2. Nama Terdakwa");
        return InputHandler.validasiPilihan("  Pilihan [1-2]: ", 1, 2, sc);
    }


    public int tampilkanMenuFilter(Scanner sc) {
        System.out.println("\n  Filter berdasarkan:");
        System.out.println("  1. Jenis Narkotika");
        System.out.println("  2. Pengadilan");
        System.out.println("  3. Kategori Hukuman (Ringan/Sedang/Berat)");
        return InputHandler.validasiPilihan("  Pilihan [1-3]: ", 1, 3, sc);
    }


    private String truncate(String str, int max) {
        if (str == null) return "";
        return str.length() <= max ? str : str.substring(0, max - 2) + "..";
    }
}
