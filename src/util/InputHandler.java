package util;

import java.util.Scanner;

public class InputHandler {

    public static int validasiInt(String prompt, int min, int max, Scanner sc) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                int nilai = Integer.parseInt(input);
                if (nilai < min || nilai > max) {
                    System.out.printf("Nilai harus antara %d dan %d. Coba lagi.%n", min, max);
                } else {
                    return nilai;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Masukkan angka bulat. Coba lagi.");
            }
        }
    }

    public static double validasiDouble(String prompt, double min, Scanner sc) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                double nilai = Double.parseDouble(input);
                if (nilai < min) {
                    System.out.printf("Nilai harus >= %.2f. Coba lagi.%n", min);
                } else {
                    return nilai;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, Masukkan angka desimal (gunakan titik). Coba lagi.");
            }
        }
    }

    public static String validasiString(String prompt, Scanner sc) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("Input tidak boleh kosong! Coba lagi.");
                } else {
                    return input;
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan input. Coba lagi.");
            }
        }
    }

    public static int validasiPilihan(String prompt, int min, int max, Scanner sc) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                int pilihan = Integer.parseInt(input);
                if (pilihan < min || pilihan > max) {
                    System.out.printf("Pilihan tidak valid! Masukkan angka %d-%d.%n", min, max);
                } else {
                    return pilihan;
                }
            } catch (NumberFormatException e) {
                System.out.printf("Masukkan angka %d-%d (bukan huruf).%n", min, max);
            }
        }
    }

    public static String validasiStringDariPilihan(String prompt, String[] pilihanValid, Scanner sc) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = sc.nextLine().trim();
                for (String p : pilihanValid) {
                    if (p.equalsIgnoreCase(input)) return p;
                }
                System.out.print("Pilihan tidak dikenali. Pilihan valid: ");
                for (int i = 0; i < pilihanValid.length; i++) {
                    System.out.print(pilihanValid[i]);
                    if (i < pilihanValid.length - 1) System.out.print(", ");
                }
                System.out.println(". Coba lagi.");
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan input. Coba lagi.");
            }
        }
    }
}
