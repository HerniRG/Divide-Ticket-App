/*
   Copyright (c) 2023. Hernán Rodríguez Garnica.
*/

package com.example.hernanrodriguezmasterd

class TipCalculator(private val totalAmount: Double, private val tipPercentage: Double) {

    fun calculateTipAmount(): Double = totalAmount * tipPercentage

    fun calculateTotalWithTip(): Double = totalAmount + calculateTipAmount()

    fun calculateAmountPerPerson(numberOfPeople: Int): Double = calculateTotalWithTip() / numberOfPeople
}
