/*
   Copyright (c) 2023. Hernán Rodríguez Garnica.
*/

package com.example.hernanrodriguezmasterd

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var etTotalAmount: EditText
    private lateinit var etNumberOfPeople: EditText
    private lateinit var rgTipOptions: RadioGroup
    private lateinit var tilTotalAmount: TextInputLayout
    private lateinit var tilNumberOfPeople: TextInputLayout
    private lateinit var mainLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar vistas
        etTotalAmount = findViewById(R.id.etTotalAmount)
        etNumberOfPeople = findViewById(R.id.etNumberOfPeople)
        rgTipOptions = findViewById(R.id.rgTipOptions)
        tilTotalAmount = findViewById(R.id.tilTotalAmount)
        tilNumberOfPeople = findViewById(R.id.tilNumberOfPeople)
        mainLayout = findViewById(R.id.mainLayout)

        // Configurar el botón de calcular
        val btnCalculate: Button = findViewById(R.id.btnCalculate)
        btnCalculate.setOnClickListener {
            if (validateFields()) {
                calculateAndNavigate()
            } else {
                showValidationError()
            }
        }

        // Configurar el botón de limpiar
        val btnClear: Button = findViewById(R.id.btnClear)
        btnClear.setOnClickListener {
            clearFields()
        }

        // Configurar el clic en el diseño principal para ocultar el teclado
        mainLayout.setOnClickListener {
            hideKeyboard()
        }
    }

    // Método para ocultar el teclado
    private fun hideKeyboard() {
        etTotalAmount.clearFocus()
        etNumberOfPeople.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainLayout.windowToken, 0)
    }

    // Método para validar los campos
    private fun validateFields(): Boolean {
        val totalAmount = etTotalAmount.text.toString()
        val numberOfPeople = etNumberOfPeople.text.toString()
        var isValid = true

        if (totalAmount.isEmpty()) {
            tilTotalAmount.error = "Campo obligatorio"
            isValid = false
        } else {
            tilTotalAmount.error = null
        }

        if (numberOfPeople.isEmpty()) {
            tilNumberOfPeople.error = "Campo obligatorio"
            isValid = false
        } else {
            tilNumberOfPeople.error = null
        }

        return isValid
    }

    // Método para realizar el cálculo y navegar a la siguiente pantalla
    private fun calculateAndNavigate() {
        val totalAmount = etTotalAmount.text.toString()
        val numberOfPeople = etNumberOfPeople.text.toString()
        val tip = when (rgTipOptions.checkedRadioButtonId) {
            R.id.rbNoTip -> "0%"
            R.id.rb5Percent -> "5%"
            R.id.rb10Percent -> "10%"
            else -> "0%"
        }

        val intent = Intent(this, ResultActivity::class.java)

        intent.putExtra("TOTAL_AMOUNT", totalAmount)
        intent.putExtra("NUMBER_OF_PEOPLE", numberOfPeople)
        intent.putExtra("TIP", tip)

        startActivity(intent)
    }

    // Método para mostrar un mensaje de error en caso de campos no válidos
    private fun showValidationError() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Por favor, completa los campos obligatorios.",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    // Método para limpiar los campos
    private fun clearFields() {
        etTotalAmount.text.clear()
        etNumberOfPeople.text.clear()
        rgTipOptions.check(R.id.rbNoTip)
        tilTotalAmount.error = null
        tilNumberOfPeople.error = null
        etTotalAmount.clearFocus()
        etNumberOfPeople.clearFocus()
    }
}
