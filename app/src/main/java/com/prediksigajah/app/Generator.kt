package com.prediksigajah.app

/**
 * Generator pasangan angka 4D, 3D, 2D, CB dari Angka Ikut
 */
object Generator {

    /**
     * Generate 4 pasang 4D dari angka ikut
     */
    fun generate4D(angkaIkut: List<Int>, bobot: FloatArray): List<String> {
        if (angkaIkut.size < 4) return emptyList()
        val result = mutableSetOf<String>()
        val topDigit = bobot.mapIndexed { i, b -> i to b }
            .sortedByDescending { it.second }
            .take(7)
            .map { it.first }
        
        var attempts = 0
        while (result.size < 4 && attempts < 100) {
            val shuffled = (topDigit + angkaIkut).distinct().shuffled().take(4)
            if (shuffled.size == 4) {
                result.add(shuffled.joinToString(""))
            }
            attempts++
        }
        return result.toList()
    }

    /**
     * Generate 5 pasang 3D
     */
    fun generate3D(angkaIkut: List<Int>, bobot: FloatArray): List<String> {
        val result = mutableSetOf<String>()
        val topDigit = bobot.mapIndexed { i, b -> i to b }
            .sortedByDescending { it.second }
            .take(6)
            .map { it.first }

        var attempts = 0
        while (result.size < 5 && attempts < 100) {
            val shuffled = (topDigit + angkaIkut).distinct().shuffled().take(3)
            if (shuffled.size == 3) {
                result.add(shuffled.joinToString(""))
            }
            attempts++
        }
        return result.toList()
    }

    /**
     * Generate 10 pasang 2D
     */
    fun generate2D(angkaIkut: List<Int>, bobot: FloatArray): List<String> {
        val result = mutableSetOf<String>()
        val topDigit = bobot.mapIndexed { i, b -> i to b }
            .sortedByDescending { it.second }
            .take(6)
            .map { it.first }

        val pool = (topDigit + angkaIkut).distinct()
        
        // Buat pasangan 2D kombinasi
        for (a in pool) {
            for (b in pool) {
                if (result.size >= 10) break
                result.add("$a$b")
            }
            if (result.size >= 10) break
        }
        return result.take(10)
    }

    /**
     * Colok Bebas = Top 4 angka ikut
     */
    fun colokBebas(angkaIkut: List<Int>, bobot: FloatArray): List<Int> {
        val combined = angkaIkut.map { it to bobot[it] }
            .sortedByDescending { it.second }
            .take(4)
            .map { it.first }
            .sorted()
        return combined
    }
}
