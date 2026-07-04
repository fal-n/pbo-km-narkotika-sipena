# KMS Putusan Pengadilan Narkotika
---

## 1. Deskripsi Proyek

Proyek ini dibangun untuk mengelola data putusan pengadilan pidana narkotika menggunakan bahasa Java JDK 11+ dengan pola arsitektur MVC (Model-View-Controller). Data bersumber dari file pdf

Fitur yang tersedia: tambah, tampilkan, cari, filter, hapus, update putusan, serta kalkulasi statistik ringkas (rata-rata vonis, distribusi peran, jenis narkotika terbanyak).

---

## 2. Cara Kompilasi

```bash
# Dari folder root project, buat folder output terlebih dahulu
mkdir -p out

# Kompilasi semua file Java
javac -d out src/model/*.java src/util/InputHandler.java src/util/DataSample.java src/view/*.java src/controller/*.java src/app/Main.java
```

> Untuk fitur parsing PDF (bonus), tambahkan `-cp lib/pdfbox-app-3.0.5.jar` dan sertakan `src/util/PdfParser.java` ke perintah di atas.

---

## 3. Cara Menjalankan

```bash
java -cp out app.Main
```

Saat pertama dijalankan, pilih sumber data:
- **1** → gunakan 50 data sampel yang sudah tersedia
- **2** → parsing otomatis dari folder PDF (butuh PDFBox)
- **3** → input data secara manual

---

## 4. Video Demo

**[Tonton Video Demo di YouTube](https://www.youtube.com/watch?v=)**

---

## 5. Daftar Anggota Kelompok

| No | Nama Lengkap | NIM | Peran |
|----|-------------|-----|-------|
| 1 | Ramadani Dino Yulianto Saputra | 202510370110049 | Backend Developer (Controller) |
| 2 | Ahmad Naufal Nasution | 202510370110051 | Knowledge Engineer (Model) |
| 3 | Janatin Alya | 202510370110006 | GUI Designer (View) |
