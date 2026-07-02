package model;

public class Putusan extends BaseEntity {
    private String nomorPerkara;
    private String pengadilan;
    private String tanggalPutusan;
    private String namaTerdakwa;
    private int umurTerdakwa;
    private String jenisNarkotika;
    private double beratBarangBukti;
    private String pasalDilanggar;
    private String peranTerdakwa;
    private int vonisHukuman;
    private double vonisDenda;
    private String namaHakim;

    private static int jumlahDibuat = 0;

    public Putusan() {
        super();
        jumlahDibuat++;
    }

    public Putusan(String nomorPerkara, String pengadilan, String tanggalPutusan, String namaTerdakwa, int umurTerdakwa, String jenisNarkotika, double beratBarangBukti, String pasalDilanggar, String peranTerdakwa, int vonisHukuman, double vonisDenda, String namaHakim) {
        super(nomorPerkara);
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.namaTerdakwa = namaTerdakwa;
        this.umurTerdakwa = umurTerdakwa;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBukti = beratBarangBukti;
        this.pasalDilanggar = pasalDilanggar;
        this.peranTerdakwa = peranTerdakwa;
        this.vonisHukuman = vonisHukuman;
        this.vonisDenda = vonisDenda;
        this.namaHakim = namaHakim;
        jumlahDibuat++;
    }

    public String getNomorPerkara() { return nomorPerkara; }
    public void setNomorPerkara(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Nomor perkara tidak boleh kosong!");
        this.nomorPerkara = v;
    }

    public String getPengadilan() { return pengadilan; }
    public void setPengadilan(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Nama pengadilan tidak boleh kosong!");
        this.pengadilan = v;
    }

    public String getTanggalPutusan() { return tanggalPutusan; }
    public void setTanggalPutusan(String v) { this.tanggalPutusan = v; }

    public String getNamaTerdakwa() { return namaTerdakwa; }
    public void setNamaTerdakwa(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Nama terdakwa tidak boleh kosong!");
        this.namaTerdakwa = v;
    }

    public int getUmurTerdakwa() { return umurTerdakwa; }
    public void setUmurTerdakwa(int v) {
        if (v <= 0 || v > 120) throw new IllegalArgumentException("Umur harus antara 1-120 tahun!");
        this.umurTerdakwa = v;
    }

    public String getJenisNarkotika() { return jenisNarkotika; }
    public void setJenisNarkotika(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Jenis narkotika tidak boleh kosong!");
        this.jenisNarkotika = v;
    }

    public double getBeratBarangBukti() { return beratBarangBukti; }
    public void setBeratBarangBukti(double v) {
        if (v <= 0) throw new IllegalArgumentException("Berat barang bukti harus > 0 gram!");
        this.beratBarangBukti = v;
    }

    public String getPasalDilanggar() { return pasalDilanggar; }
    public void setPasalDilanggar(String v) { this.pasalDilanggar = v; }

    public String getPeranTerdakwa() { return peranTerdakwa; }
    public void setPeranTerdakwa(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Peran terdakwa tidak boleh kosong!");
        this.peranTerdakwa = v;
    }

    public int getVonisHukuman() { return vonisHukuman; }
    public void setVonisHukuman(int v) {
        if (v < 0) throw new IllegalArgumentException("Vonis hukuman tidak boleh negatif!");
        this.vonisHukuman = v;
    }

    public double getVonisDenda() { return vonisDenda; }
    public void setVonisDenda(double v) {
        if (v < 0) throw new IllegalArgumentException("Vonis denda tidak boleh negatif!");
        this.vonisDenda = v;
    }

    public String getNamaHakim() { return namaHakim; }
    public void setNamaHakim(String v) {
        if (v == null || v.trim().isEmpty()) throw new IllegalArgumentException("Nama hakim tidak boleh kosong!");
        this.namaHakim = v;
    }

    public static int getJumlahDibuat() { return jumlahDibuat; }

    public String getKategoriHukuman() {
        if (vonisHukuman <= 12) {
            return "Ringan";
        } else if (vonisHukuman <= 60) {
            return "Sedang";
        } else {
            return "Berat";
        }
    }

    public void tampilkan() {
        System.out.printf("%-32s | %-22s | %-10s | %4d bln | %-6s%n", nomorPerkara, namaTerdakwa, jenisNarkotika, vonisHukuman, getKategoriHukuman());
    }

    public void tampilkan(boolean detail) {
        if (detail) {
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║     DETAIL PUTUSAN PENGADILAN NARKOTIKA      ║");
            System.out.println("╠══════════════════════════════════════════════╣");
            System.out.printf( "║  Nomor Perkara    : %-26s║%n", nomorPerkara);
            System.out.printf( "║  Pengadilan       : %-26s║%n", pengadilan);
            System.out.printf( "║  Tanggal Putusan  : %-26s║%n", tanggalPutusan);
            System.out.printf( "║  Nama Terdakwa    : %-26s║%n", namaTerdakwa);
            System.out.printf( "║  Umur             : %-26s║%n", umurTerdakwa + " tahun");
            System.out.printf( "║  Jenis Narkotika  : %-26s║%n", jenisNarkotika);
            System.out.printf( "║  Berat Barang BB  : %-26s║%n", beratBarangBukti + " gram");
            System.out.printf( "║  Pasal Dilanggar  : %-26s║%n", pasalDilanggar);
            System.out.printf( "║  Peran Terdakwa   : %-26s║%n", peranTerdakwa);
            System.out.printf( "║  Vonis Hukuman    : %-26s║%n", vonisHukuman + " bulan");
            System.out.printf( "║  Vonis Denda      : Rp %-23s║%n", String.format("%,.0f", vonisDenda));
            System.out.printf( "║  Nama Hakim       : %-26s║%n", namaHakim);
            System.out.printf( "║  Kategori Hukuman : %-26s║%n", getKategoriHukuman());
            System.out.println("╚══════════════════════════════════════════════╝");
        } else {
            tampilkan();
        }
    }

    @Override
    public String getInfo() {
        return String.format("[%s] %s | %s | %d bln | %s", nomorPerkara, namaTerdakwa, jenisNarkotika, vonisHukuman, getKategoriHukuman());
    }

    @Override
    public String toString() {
        return String.format("Putusan{nomor='%s', terdakwa='%s', narkotika='%s', vonis=%d bln, kategori='%s'}", nomorPerkara, namaTerdakwa, jenisNarkotika, vonisHukuman, getKategoriHukuman());
    }
}