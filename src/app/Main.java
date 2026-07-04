package app;

import controller.KnowledgeController;
import model.KnowledgeRepository;
import model.Putusan;
import util.DataSample;
import util.InputHandler;
import util.PdfParser;
import view.ConsoleView;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Inisialisasi komponen MVC
        KnowledgeRepository repository = new KnowledgeRepository();// MODEL
        ConsoleView view = new ConsoleView();// VIEW
        KnowledgeController controller = new KnowledgeController(repository, view);// CTRL

        Scanner sc = new Scanner(System.in);
        //Menginisiasi scanner
        System.out.println("Menginisialisasi sistem...");

        System.out.println("  Pilih sumber data awal:");
        System.out.println("  1. Data sampel hardcoded (50 putusan)");
        System.out.println("  2. Parsing otomatis dari folder PDF");
        int sumberData = InputHandler.validasiPilihan("  Pilihan [1-2]: ", 1, 2, sc);

        if (sumberData == 1) {
            DataSample.loadData(repository);
        } else {
            String pathFolder = InputHandler.validasiString(
                    "  Masukkan path folder PDF (mis. dataset/pdf-putusan): ", sc);
            ArrayList<Putusan> hasilParsing = PdfParser.parseDirektori(pathFolder);
            for (Putusan p : hasilParsing) {
                repository.simpan(p);
            }
            if (repository.getTotalData() == 0) {
                view.tampilkanPesan("   Tidak ada data dari PDF, memuat data sampel sebagai cadangan.");
                DataSample.loadData(repository);
            }
        }

        //Muat data sampel 50 putusan
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   KMS PUTUSAN PENGADILAN NARKOTIKA — JAVA MVC    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        //Loop menu utama
        boolean berjalan = true;
        while (berjalan) {
            int pilihan = view.tampilkanMenu(sc);

            switch (pilihan) {
                case 1:
                    //Tampilkan semua putusan
                    controller.tampilkanSemua();
                    break;

                case 2:
                    //Tambah putusan baru
                    controller.handleTambahPutusan(sc);
                    break;

                case 3:
                    //Cari putusan
                    controller.handleCariPutusan(sc);
                    break;

                case 4:
                    //Filter putusan
                    controller.handleFilterPutusan(sc);
                    break;

                case 5:
                    //Hapus putusan
                    controller.handleHapusPutusan(sc);
                    break;

                case 6:
                    //Update putusan
                    controller.handleUpdatePutusan(sc);
                    break;

                case 7:
                    //Tampilkan statistik
                    controller.tampilkanStatistik();
                    break;

                case 8:
                    // Keluar
                    berjalan = false;
                    System.out.println("\nTerima kasih telah menggunakan KMS Putusan Narkotika.");
                    System.out.println("Total data tersimpan : " + controller.getTotalData());
                    System.out.println("Sampai jumpa\n");
                    break;

                default:
                    view.tampilkanPesan("Pilihan tidak valid.");
            }
        }

        sc.close();
    }
}