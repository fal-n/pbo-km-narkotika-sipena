package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatistikPutusan {
    private int totalPutusan;
    private double rataRataVonis;
    private double rataRataDenda;
    private int vonisTerendah;
    private int vonisTertinggi;
    private String jenisNarkotikaTerbanyak;
    private String[] distribusiPeran;

    private final ArrayList<Putusan> daftar;

    public StatistikPutusan(ArrayList<Putusan> daftar) {
        this.daftar = daftar;
        hitungSemua();
    }

    public void hitungSemua() {
        totalPutusan = daftar.size();
        if (totalPutusan == 0) {
            distribusiPeran = new String[0];
            return;
        }

        double totalVonis = 0;
        double totalDenda = 0;
        int minVonis = Integer.MAX_VALUE;
        int maxVonis = Integer.MIN_VALUE;

        HashMap<String, Integer> countJenis = new HashMap<>();
        HashMap<String, Integer> countPeran = new HashMap<>();

        for (Putusan putusan : daftar) {
            totalVonis += putusan.getVonisHukuman();
            totalDenda += putusan.getVonisDenda();

            if (putusan.getVonisHukuman() < minVonis) {
                minVonis = putusan.getVonisHukuman();
            }
            if (putusan.getVonisHukuman() > maxVonis) {
                maxVonis = putusan.getVonisHukuman();
            }

            String jenis = putusan.getJenisNarkotika();
            countJenis.put(jenis, countJenis.getOrDefault(jenis, 0) + 1);

            String peran = putusan.getPeranTerdakwa();
            countPeran.put(peran, countPeran.getOrDefault(peran, 0) + 1);
        }

        rataRataVonis  = totalVonis / totalPutusan;
        rataRataDenda  = totalDenda / totalPutusan;
        vonisTerendah  = minVonis;
        vonisTertinggi = maxVonis;

        jenisNarkotikaTerbanyak = "";
        int maxJenis = 0;
        for (Map.Entry<String, Integer> cJenis : countJenis.entrySet()) {
            if (cJenis.getValue() > maxJenis) {
                maxJenis = cJenis.getValue();
                jenisNarkotikaTerbanyak = cJenis.getKey();
            }
        }
        distribusiPeran = new String[countPeran.size()];
        int idx = 0;
        for (Map.Entry<String, Integer> cPeran : countPeran.entrySet()) {
            double pct = (double) cPeran.getValue() / totalPutusan * 100;
            distribusiPeran[idx++] = String.format("%-15s : %3d kasus (%.1f%%)", cPeran.getKey(), cPeran.getValue(), pct);
        }
    }

    public void tampilkanLaporan() {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            STATISTIK RINGKAS PUTUSAN            ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf( "║  Total Putusan            : %-21d║%n", totalPutusan);
        System.out.printf( "║  Rata-rata Vonis          : %-18s║%n", String.format("%.1f bulan", rataRataVonis));
        System.out.printf( "║  Vonis Terendah           : %-21s║%n", vonisTerendah + " bulan");
        System.out.printf( "║  Vonis Tertinggi          : %-21s║%n", vonisTertinggi + " bulan");
        System.out.printf( "║  Rata-rata Denda          : Rp %-18s║%n", String.format("%,.0f", rataRataDenda));
        System.out.printf( "║  Jenis Narkotika Terbanyak: %-21s║%n", jenisNarkotikaTerbanyak);
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Distribusi Peran Terdakwa:                      ║");
        for (String s : distribusiPeran) {
            System.out.printf("║    %-46s║%n", s);
        }
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    public int getTotalPutusan() { return totalPutusan; }
    public double getRataRataVonis() { return rataRataVonis; }
    public double getRataRataDenda() { return rataRataDenda; }
    public int getVonisTerendah() { return vonisTerendah; }
    public int getVonisTertinggi() { return vonisTertinggi; }
    public String getJenisNarkotikaTerbanyak() { return jenisNarkotikaTerbanyak; }
    public String[] getDistribusiPeran() { return distribusiPeran; }
}