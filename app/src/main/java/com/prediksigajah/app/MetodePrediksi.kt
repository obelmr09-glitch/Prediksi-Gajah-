package com.prediksigajah.app

object MetodePrediksi {

    // Tabel Mistik Lama (klasik)
    private val mistikLama = mapOf(
        0 to 5, 1 to 6, 2 to 7, 3 to 8, 4 to 9,
        5 to 0, 6 to 1, 7 to 2, 8 to 3, 9 to 4
    )

    // Tabel Mistik Baru
    private val mistikBaru = mapOf(
        0 to 1, 1 to 2, 2 to 3, 3 to 4, 4 to 5,
        5 to 6, 6 to 7, 7 to 8, 8 to 9, 9 to 0
    )

    fun mistikLama(angka: String): String {
        return angka.filter { it.isDigit() }
            .map { mistikLama[it.digitToInt()] ?: 0 }.joinToString("")
    }

    fun mistikBaru(angka: String): String {
        return angka.filter { it.isDigit() }
            .map { mistikBaru[it.digitToInt()] ?: 0 }.joinToString("")
    }

    fun indeks(angka: String): Int {
        return angka.filter { it.isDigit() }.sumOf { it.digitToInt() } % 10
    }

    // Angka Ikut: digit yang paling potensial muncul
    fun angkaIkut(angka: String): List<Int> {
        val ml = mistikLama(angka)
        val mb = mistikBaru(angka)
        val idx = indeks(angka)
        val set = mutableSetOf<Int>()
        ml.forEach { if (it.isDigit()) set.add(it.digitToInt()) }
        mb.forEach { if (it.isDigit()) set.add(it.digitToInt()) }
        set.add(idx)
        return set.sorted()
    }
}
