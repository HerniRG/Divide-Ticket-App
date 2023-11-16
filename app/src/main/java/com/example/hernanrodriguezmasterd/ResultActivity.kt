/*
   Copyright (c) 2023. Hernán Rodríguez Garnica.
*/

package com.example.hernanrodriguezmasterd

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // Inicializar vistas
        tvResult = findViewById(R.id.tvResult)
        btnBack = findViewById(R.id.btnBack)

        // Obtener datos del Intent
        val totalAmount = intent.getStringExtra("TOTAL_AMOUNT")?.toDoubleOrNull()
        val numberOfPeople = intent.getStringExtra("NUMBER_OF_PEOPLE")?.toIntOrNull()
        val tipString = intent.getStringExtra("TIP")

        // Verificar la validez de los datos
        if (totalAmount == null || numberOfPeople == null || tipString == null) {
            // Datos no válidos, mostrar mensaje de error
            showError()
        } else {
            // Realizar cálculos y mostrar resultados
            calculateAndShowResults(totalAmount, numberOfPeople, tipString)
        }

        // Configurar botón "Volver"
        btnBack.setOnClickListener {
            // Volver a la pantalla anterior
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun showError(errorMessage: String = getString(R.string.error_Data)) {
        tvResult.text = errorMessage
    }

    private fun calculateAndShowResults(totalAmount: Double, numberOfPeople: Int, tipString: String) {
        // Convertir la propina a un número decimal de manera segura
        val tip = parseTip(tipString)

        if (tip == null) {
            // La conversión de la propina falló, mostrar mensaje de error
            showError("Error al convertir la propina.")
        } else {
            // Realizar cálculos
            val tipCalculator = TipCalculator(totalAmount, tip)
            val tipAmount = tipCalculator.calculateTipAmount()
            val totalWithTip = tipCalculator.calculateTotalWithTip()
            val amountPerPerson = tipCalculator.calculateAmountPerPerson(numberOfPeople)

            // Formatear valores con dos decimales
            val formattedTotalAmount = formatCurrency(totalAmount)
            val formattedTipAmount = formatCurrency(tipAmount)
            val formattedTotalWithTip = formatCurrency(totalWithTip)
            val formattedAmountPerPerson = formatCurrency(amountPerPerson)

            // Mostrar resultados en tvResult
            showResults(
                formattedTotalAmount,
                numberOfPeople,
                tipString,
                formattedTipAmount,
                formattedTotalWithTip,
                formattedAmountPerPerson
            )
        }
    }

    private fun parseTip(tipString: String): Double? {
        return try {
            tipString.replace("%", "").toDouble() / 100.0
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun formatCurrency(value: Double): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val formattedValue = numberFormat.format(value).replace(numberFormat.currency!!.symbol, "").trim()

        // Eliminar decimales si son .00
        return if (formattedValue.endsWith(".00")) {
            formattedValue.substring(0, formattedValue.length - 3)
        } else {
            formattedValue
        }
    }

    private fun showResults(
        totalAmount: String,
        numberOfPeople: Int,
        tipString: String,
        formattedTipAmount: String,
        formattedTotalWithTip: String,
        formattedAmountPerPerson: String
    ) {
        val resultText = "Importe total $totalAmount\n" +
                "Nº de comensales $numberOfPeople\n" +
                "Propina $tipString\n\n" +
                "Resultados:\n" +
                "Importe de la propina $formattedTipAmount\n" +
                "Total con propina $formattedTotalWithTip\n\n\n" +
                "Importe por persona $formattedAmountPerPerson"

        tvResult.text = resultText
    }
}
