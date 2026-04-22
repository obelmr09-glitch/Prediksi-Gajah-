package com.prediksigajah.app

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var aiEngine: AIEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aiEngine = AIEngine(this)

        val edtInput = findViewById<EditText>(R.id.edtInput)
        val btnProses = findViewById<Button>(R.id.btnProses)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val tvHasil = findViewById<TextView>(R.id.tvHasil)
        val tvInfoAI = findViewById<TextView>(R.id.tvInfoAI)

        edtInput.filters = arrayOf(InputFilter.LengthFilter(4))

        updateInfoAI(tvInfoAI)

        btnProses.setOnClickListener {
            val input = edtInput.text.toString().trim()
            
            if (input.length != 4 || !input.all { it.isDigit() }) {
                Toast.makeText(this, "Masukkan 4 digit angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edtInput.windowToken, 0)

            aiEngine.train(input)

            val ml = MetodePrediksi.mistikLama(input)
            val mb = MetodePrediksi.mistikBaru(input)
            val idx = MetodePrediksi.indeks(input)
            val angkaIkut = aiEngine.prediksiAngkaIkut(input)
            val bobot = aiEngine.getBobot()

            val cb = Generator.colokBebas(angkaIkut, bobot)
            val d4 = Generator.generate4D(angkaIkut, bobot)
            val d3 = Generator.generate3D(angkaIkut, bobot)
            val d2 = Generator.generate2D(angkaIkut, bobot)

            val hasil = StringBuilder()
            hasil.append("INPUT: $input\n")
            hasil.append("====================\n\n")
            hasil.append("METODE MISTIK\n")
            hasil.append("Mistik Lama : $ml\n")
            hasil.append("Mistik Baru : $mb\n")
            hasil.append("Indeks      : $idx\n\n")
            hasil.append("ANGKA IKUT (AI)\n")
            hasil.append("${angkaIkut.joinToString(" - ")}\n\n")
            hasil.append("COLOK BEBAS (CB)\n")
            hasil.append("${cb.joinToString(" - ")}\n\n")
            hasil.append("PREDIKSI 4D (4 pasang)\n")
            d4.forEachIndexed { i, v -> hasil.append("${i+1}. $v\n") }
            hasil.append("\n")
            hasil.append("PREDIKSI 3D (5 pasang)\n")
            d3.forEachIndexed { i, v -> hasil.append("${i+1}. $v\n") }
            hasil.append("\n")
            hasil.append("PREDIKSI 2D (10 pasang)\n")
            d2.chunked(2).forEach { pair ->
                hasil.append(pair.joinToString("   ") + "\n")
            }

            tvHasil.text = hasil.toString()
            updateInfoAI(tvInfoAI)
        }

        btnReset.setOnClickListener {
            edtInput.setText("")
            tvHasil.text = "Masukkan 4 digit angka lalu tekan PROSES"
        }
    }

    private fun updateInfoAI(tv: TextView) {
        val total = aiEngine.getTotalTraining()
        val last = aiEngine.getLastTrain()
        val lastStr = if (last == 0L) "Belum pernah"
                      else SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(Date(last))
        tv.text = "AI telah belajar: $total kali  |  Update: $lastStr"
    }
}
