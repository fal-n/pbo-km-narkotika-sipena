package app;

import controller.KnowledgeController;
import model.KnowledgeRepository;
import util.DataSample;
import view.ConsoleView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Inisialisasi komponen MVC
        KnowledgeRepository repository = new KnowledgeRepository();// MODEL
        ConsoleView view = new ConsoleView();// VIEW
        KnowledgeController controller = new KnowledgeController(repository, view);// CTRL

        //Muat data sampel 50 putusan
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   KMS PUTUSAN PENGADILAN NARKOTIKA — JAVA MVC    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
        DataSample.loadData(repository);

        //Scanner tunggal untuk seluruh aplikas
        Scanner sc = new Scanner(System.in);

        //Menginisiasi scanner
        System.out.println("Menginisialisasi sistem...");

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
