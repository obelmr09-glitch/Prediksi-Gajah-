package com.prediksigajah.app

import android.content.Context

/**
 * AI Engine: Belajar dari input user sebelumnya.
 * Semakin sering dipakai, bobot makin akurat (adaptive learning).
 */
class AIEngine(context: Context) {

    private val prefs = context.getSharedPreferences("ai_model", Context.MODE_PRIVATE)

    /**
     * Latih AI dengan angka input sebelumnya
     */
    fun train(inputAngka: String) {
        val digits = inputAngka.filter { it.isDigit() }
        val editor = prefs.edit()
        digits.forEach { ch ->
            val d = ch.digitToInt()
            val key = "digit_$d"
            val bobotLama = prefs.getFloat(key, 1.0f)
            editor.putFloat(key, bobotLama + 0.5f)
        }
        // Riwayat input
        val history = prefs.getString("history", "") ?: ""
        val newHistory = if (history.length > 200) history.takeLast(180) + "," + inputAngka
                         else "$history,$inputAngka"
        editor.putString("history", newHistory)
        editor.putLong("last_train", System.currentTimeMillis())
        editor.apply()
    }

    /**
     * Dapatkan bobot AI untuk setiap digit (0-9)
     */
    fun getBobot(): FloatArray {
        return FloatArray(10) { prefs.getFloat("digit_$it", 1.0f) }
    }

    /**
     * Prediksi Angka Ikut berdasarkan AI + Metode
     */
    fun prediksiAngkaIkut(inputAngka: String): List<Int> {
        val bobot = getBobot()
        val metodeResult = MetodePrediksi.angkaIkut(inputAngka)
        
        // Boost digit dari metode mistik
        metodeResult.forEach { bobot[it] += 2.5f }
        
        // Boost digit dari input
        inputAngka.filter { it.isDigit() }.forEach { 
            bobot[it.digitToInt()] += 1.5f 
        }

        return bobot.mapIndexed { idx, b -> idx to b }
            .sortedByDescending { it.second }
            .take(5)
            .map { it.first }
            .sorted()
    }

    fun getLastTrain(): Long = prefs.getLong("last_train", 0L)
    
    fun getTotalTraining(): Int {
        val history = prefs.getString("history", "") ?: ""
        return history.split(",").filter { it.isNotBlank() }.size
    }
}
