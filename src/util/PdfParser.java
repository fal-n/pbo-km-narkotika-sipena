package util;

import model.Putusan;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfParser {
    private static final Pattern P_NOMOR = Pattern.compile(
            "(?:Nomor\\.?\\s*:?\\s*)?(\\d+/.+?/\\d{4}/[A-Z]{2,5}\\.?\\s*[\\w.-]+)");

    private static final Pattern P_PENGADILAN = Pattern.compile(
            "Pengadilan\\s*(?:\\r?\\n\\s*)*(?:Negeri|Agama|Tata\\s+Usaha\\s+Negara|Militer)\\s*(?:\\r?\\n\\s*)*([^\\n,]+?)(?=\\s*(?:,|yang\\s+(?:mengadili|memeriksa)|Nomor|$))");

    private static final Pattern P_NAMA = Pattern.compile(
            "Nama\\s+[Ll]engkap\\s*:\\s*([^\\n\\r]+)");

    private static final Pattern P_UMUR = Pattern.compile(
            "Umur[^:]*:\\s*(\\d{1,3})\\s*tahun");

    private static final Pattern P_TANGGAL_PUTUSAN = Pattern.compile(
            "[Dd]iputuskan[\\s\\S]*?(?:pada\\s+)?(?:hari\\s+\\w+\\s*,?\\s*)?tanggal\\s+(\\d{1,2}\\s+\\w+\\s+\\d{4})");

    private static final Pattern P_PASAL = Pattern.compile(
            "Pasal\\s+(\\d+\\s*(?:ayat\\s*\\(\\d+\\))?[^.\\n]{0,40}?)\\s*(?:Undang[- ]Undang|UU)[^.\\n]*?35\\s*[Tt]ahun\\s*2009");

    private static final Pattern P_BERAT = Pattern.compile(
            "(?:berat|seberat|netto)[^0-9]{0,20}([\\d.,]+)\\s*gram", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_VONIS_TAHUN_BULAN = Pattern.compile(
            "penjara\\s+selama\\s+(\\d+)\\s*\\([^)]*\\)\\s*tahun(?:\\s+(?:dan\\s+)?(\\d+)\\s*\\([^)]*\\)\\s*bulan)?",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern P_VONIS_BULAN_SAJA = Pattern.compile(
            "penjara\\s+selama\\s+(\\d+)\\s*\\([^)]*\\)\\s*bulan", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_DENDA = Pattern.compile(
            "denda\\s+sebesar\\s+Rp\\.?\\s*([\\d.,]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_HAKIM_KETUA = Pattern.compile(
            "([A-Z][A-Za-z.,'\\s]{3,40}?,?\\s*S\\.?H\\.?(?:\\s*,?\\s*M\\.?\\s*H\\.?)?)\\s*(?:selaku\\s+)?[Hh]akim\\s+[Kk]etua");

    private static final String[] JENIS_NARKOTIKA = {
            "Sabu-sabu", "Shabu", "Ganja", "Ekstasi", "Heroin", "Kokain"
    };

    private static final String[][] KATA_KUNCI_PERAN = {
            {"bandar",          "Bandar"},
            {"pengedar",        "Kurir"},
            {"kurir",           "Kurir"},
            {"perantara",       "Perantara"},
            {"penghubung",      "Perantara"},
            {"menyimpan",       "Penyimpan"},
            {"penyimpan",       "Penyimpan"},
            {"konsumsi sendiri","Pengguna"},
            {"pengguna",        "Pengguna"}
    };

    public static ArrayList<Putusan> parseDirektori(String pathDirektori) {
        return parseDirektori(pathDirektori, false);
    }

    public static ArrayList<Putusan> parseDirektori(String pathDirektori, boolean rekursif) {
        ArrayList<Putusan> hasil = new ArrayList<>();
        File folder = new File(pathDirektori);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Direktori tidak ditemukan: " + pathDirektori);
            return hasil;
        }

        ArrayList<File> daftarFile = new ArrayList<>();
        kumpulkanFilePdf(folder, daftarFile, rekursif);
        System.out.println("Ditemukan " + daftarFile.size() + " file PDF. Memulai parsing...");

        int sukses = 0, gagal = 0;
        ArrayList<String> daftarGagal = new ArrayList<>();

        for (File file : daftarFile) {
            try {
                Putusan p = parseSatuFile(file);
                hasil.add(p);
                sukses++;
            } catch (IOException e) {
                gagal++;
                daftarGagal.add(file.getName() + " — gagal dibaca: " + e.getMessage());
            } catch (Exception e) {
                gagal++;
                daftarGagal.add(file.getName() + " — error: " + e.getMessage());
            }
        }

        System.out.println("Berhasil : " + sukses + " putusan");
        System.out.println("Gagal    : " + gagal + " file");
        if (!daftarGagal.isEmpty()) {
            System.out.println("Detail file gagal diparsing:");
            for (String g : daftarGagal) {
                System.out.println("   - " + g);
            }
        }
        return hasil;
    }

    public static Putusan parseSatuFile(File file) throws IOException {
        String teks;
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            teks = stripper.getText(document);
        }
        String nomorPerkara = ekstrak(P_NOMOR, teks, file.getName().replaceAll("(?i)\\.pdf$", "")) + " " + file.getName().replaceAll("(?i)\\.pdf$", "");
        String namaPengadilan = ekstrak(P_PENGADILAN, teks, "Tidak Diketahui");
        String namaTerdakwa = ekstrak(P_NAMA, teks, "Tidak Diketahui");
        String tanggalPutusan = ekstrak(P_TANGGAL_PUTUSAN, teks, "-");
        int umurTerdakwa = ekstrakInt(P_UMUR, teks, 1);
        String jenisNarkotika = cariJenisNarkotika(teks);
        double beratBB = ekstrakAngkaMin(P_BERAT, teks, 0.01);
        String pasalDilanggar = ekstrakPasal(teks);
        String peranTerdakwa = cariPeranTerdakwa(teks);
        int vonisHukuman = ekstrakVonisBulan(teks);
        double vonisDenda = ekstrakAngka(P_DENDA, teks, 0.0);
        String namaHakim = ekstrak(P_HAKIM_KETUA, teks, "Tidak Diketahui");

        return new Putusan(
                nomorPerkara, namaPengadilan, tanggalPutusan, namaTerdakwa,
                umurTerdakwa, jenisNarkotika, beratBB, pasalDilanggar,
                peranTerdakwa, vonisHukuman, vonisDenda, namaHakim
        );
    }

    private static void kumpulkanFilePdf(File folder, ArrayList<File> output, boolean rekursif) {
        File[] isi = folder.listFiles();
        if (isi == null) return;
        for (File f : isi) {
            if (f.isDirectory() && rekursif) {
                kumpulkanFilePdf(f, output, true);
            } else if (f.isFile() && f.getName().toLowerCase().endsWith(".pdf")) {
                output.add(f);
            }
        }
    }

    private static String ekstrak(Pattern pola, String teks, String defaultValue) {
        Matcher m = pola.matcher(teks);
        if (m.find()) {
            return m.group(1).trim().replaceAll("\\s+", " ");
        }
        return defaultValue;
    }

    private static int ekstrakInt(Pattern pola, String teks, int defaultValue) {
        Matcher m = pola.matcher(teks);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1).trim());
            } catch (NumberFormatException ignored) { /* pakai default */ }
        }
        return defaultValue;
    }

    private static double ekstrakAngka(Pattern pola, String teks, double defaultValue) {
        Matcher m = pola.matcher(teks);
        if (m.find()) {
            double nilai = parseAngkaIndonesia(m.group(1));
            return nilai > 0 ? nilai : defaultValue;
        }
        return defaultValue;
    }

    private static double ekstrakAngkaMin(Pattern pola, String teks, double nilaiMinimum) {
        double hasil = ekstrakAngka(pola, teks, 0.0);
        return hasil > 0 ? hasil : nilaiMinimum;
    }

    private static String ekstrakPasal(String teks) {
        Matcher m = P_PASAL.matcher(teks);
        if (m.find()) {
            return ("Pasal " + m.group(1).trim() + " UU No.35/2009").replaceAll("\\s+", " ");
        }
        return "Belum teridentifikasi";
    }

    private static int ekstrakVonisBulan(String teks) {
        Matcher m1 = P_VONIS_TAHUN_BULAN.matcher(teks);
        if (m1.find()) {
            int tahun = Integer.parseInt(m1.group(1));
            int bulan = (m1.group(2) != null) ? Integer.parseInt(m1.group(2)) : 0;
            return (tahun * 12) + bulan;
        }
        Matcher m2 = P_VONIS_BULAN_SAJA.matcher(teks);
        if (m2.find()) {
            return Integer.parseInt(m2.group(1));
        }
        return 0;
    }

    private static double parseAngkaIndonesia(String raw) {
        try {
            String bersih = raw.trim().replace(".", "").replace(",", ".");
            return Double.parseDouble(bersih);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static String cariJenisNarkotika(String teks) {
        String teksLower = teks.toLowerCase();
        for (String jenis : JENIS_NARKOTIKA) {
            if (teksLower.contains(jenis.toLowerCase())) {
                return jenis.equalsIgnoreCase("Shabu") ? "Sabu-sabu" : jenis;
            }
        }
        return "Tidak Diketahui";
    }

    private static String cariPeranTerdakwa(String teks) {
        String teksLower = teks.toLowerCase();
        for (String[] pasangan : KATA_KUNCI_PERAN) {
            if (teksLower.contains(pasangan[0])) {
                return pasangan[1];
            }
        }
        return "Tidak Diketahui";
    }
}