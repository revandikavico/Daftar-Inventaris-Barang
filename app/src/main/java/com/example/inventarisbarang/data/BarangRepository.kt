package com.example.inventarisbarang.data

import android.content.Context
import android.content.SharedPreferences
import com.example.inventarisbarang.model.Barang
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * BarangRepository mengelola operasi CRUD data barang menggunakan SharedPreferences.
 *
 * Analogi: SharedPreferences itu seperti buku catatan kecil yang selalu
 * dibawa di saku — ringan, cepat diakses, dan datanya tetap ada
 * meskipun aplikasi ditutup. Cocok untuk data kecil-menengah.
 *
 * Cara kerja penyimpanan:
 * ┌──────────────┐    Gson     ┌──────────────┐    putString    ┌───────────────────┐
 * │ List<Barang> │ ─────────► │ JSON String  │ ──────────────► │ SharedPreferences │
 * └──────────────┘            └──────────────┘                 └───────────────────┘
 *
 * Cara kerja pembacaan:
 * ┌───────────────────┐   getString   ┌──────────────┐    Gson     ┌──────────────┐
 * │ SharedPreferences │ ────────────► │ JSON String  │ ─────────► │ List<Barang> │
 * └───────────────────┘               └──────────────┘            └──────────────┘
 *
 * Catatan penting:
 * - SharedPreferences menyimpan data dalam format XML di internal storage
 * - Untuk data besar atau relasional, gunakan Room Database
 * - Semua operasi tulis menggunakan apply() (asinkron, tidak memblokir UI)
 *
 * @param context Context Android diperlukan untuk mengakses SharedPreferences
 */
class BarangRepository(context: Context) {

    // === INISIALISASI ===

    /**
     * SharedPreferences dengan nama file "inventaris_prefs".
     * MODE_PRIVATE = hanya aplikasi ini yang bisa mengakses file ini.
     *
     * File fisik tersimpan di: /data/data/com.example.inventarisbarang/shared_prefs/inventaris_prefs.xml
     */
    private val prefs: SharedPreferences =
        context.getSharedPreferences("inventaris_prefs", Context.MODE_PRIVATE)

    /**
     * Gson digunakan untuk serialisasi (objek → JSON) dan
     * deserialisasi (JSON → objek).
     *
     * Contoh serialisasi:
     * Barang(nama="Tas", harga=245000) → {"nama":"Tas","harga":245000.0,...}
     */
    private val gson = Gson()

    /** Key yang digunakan untuk menyimpan daftar barang */
    companion object {
        private const val KEY_DAFTAR = "daftar_barang"
    }

    // === OPERASI READ ===

    /**
     * Mengambil semua data barang dari SharedPreferences.
     *
     * Proses:
     * 1. Baca string JSON dari SharedPreferences menggunakan key "daftar_barang"
     * 2. Jika null (belum ada data), kembalikan list kosong
     * 3. Jika ada data, konversi JSON string ke List<Barang> menggunakan Gson
     *
     * TypeToken diperlukan karena Gson perlu tahu tipe generic yang tepat
     * (List<Barang>) saat melakukan deserialisasi dari JSON.
     * Tanpa TypeToken, Gson tidak tahu harus membuat objek Barang.
     *
     * @return List<Barang> daftar semua barang, atau empty list jika belum ada data
     */
    fun getSemuaBarang(): List<Barang> {
        val json = prefs.getString(KEY_DAFTAR, null)

        // Jika belum ada data tersimpan, kembalikan list kosong
        if (json == null) return emptyList()

        // TypeToken memberitahu Gson: "Saya ingin List yang isinya objek Barang"
        val type = object : TypeToken<List<Barang>>() {}.type
        return gson.fromJson(json, type)
    }

    /**
     * Mengambil satu barang berdasarkan ID.
     *
     * find {} akan mencari item pertama yang memenuhi kondisi (id cocok).
     * Jika tidak ditemukan, mengembalikan null.
     *
     * @param id ID unik barang yang dicari
     * @return Barang jika ditemukan, null jika tidak ada
     */
    fun getBarangById(id: Long): Barang? {
        return getSemuaBarang().find { it.id == id }
    }

    // === OPERASI WRITE ===

    /**
     * Menyimpan seluruh daftar barang ke SharedPreferences.
     *
     * Proses:
     * 1. Konversi List<Barang> ke JSON string menggunakan Gson
     * 2. Simpan JSON string ke SharedPreferences dengan key "daftar_barang"
     *
     * apply() vs commit():
     * - apply()  = asinkron (di background thread), tidak memblokir UI → DIREKOMENDASIKAN
     * - commit() = sinkron (di main thread), mengembalikan true/false
     *
     * @param daftar List<Barang> yang akan disimpan
     */
    private fun simpanSemuaBarang(daftar: List<Barang>) {
        val json = gson.toJson(daftar)
        prefs.edit().putString(KEY_DAFTAR, json).apply()
    }

    /**
     * Menambahkan satu barang baru ke daftar.
     *
     * Proses:
     * 1. Ambil daftar yang sudah ada (bisa kosong)
     * 2. Buat mutableList agar bisa ditambah
     * 3. Tambahkan barang baru menggunakan add()
     * 4. Simpan kembali seluruh daftar
     *
     * @param barang Objek Barang yang akan ditambahkan
     */
    fun tambahBarang(barang: Barang) {
        val daftar = getSemuaBarang().toMutableList()
        daftar.add(barang)
        simpanSemuaBarang(daftar)
    }

    /**
     * Mengupdate data barang yang sudah ada berdasarkan ID.
     *
     * map {} memeriksa setiap item dalam list:
     * - Jika ID cocok → ganti dengan data barang yang baru
     * - Jika ID tidak cocok → biarkan apa adanya (it)
     *
     * Contoh: update harga barang ID=123 dari 100000 ke 150000
     * [Barang(id=123, harga=100000), Barang(id=456, harga=200000)]
     * → map → [Barang(id=123, harga=150000), Barang(id=456, harga=200000)]
     *
     * @param barang Objek Barang dengan data terbaru (ID harus sama)
     */
    fun updateBarang(barang: Barang) {
        val daftar = getSemuaBarang().map {
            if (it.id == barang.id) barang else it
        }
        simpanSemuaBarang(daftar)
    }

    /**
     * Menghapus satu barang berdasarkan ID.
     *
     * filter {} membuat list baru yang HANYA berisi item
     * yang ID-nya TIDAK sama dengan id yang ingin dihapus.
     * Item dengan ID yang cocok akan "terfilter keluar" dari list.
     *
     * Contoh: hapus barang ID=123
     * [Barang(id=123), Barang(id=456), Barang(id=789)]
     * → filter { it.id != 123 } → [Barang(id=456), Barang(id=789)]
     *
     * @param id ID barang yang akan dihapus
     */
    fun hapusBarang(id: Long) {
        val daftar = getSemuaBarang().filter { it.id != id }
        simpanSemuaBarang(daftar)
    }
}
