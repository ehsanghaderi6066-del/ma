package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var operand1: Double? = null
    private var pendingOp: Char? = null
    private var clearOnNextDigit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.tvDisplay)
    }

    fun onDigit(view: View) {
        val b = view as Button
        if (clearOnNextDigit || display.text.toString() == "0") {
            display.text = ""
            clearOnNextDigit = false
        }
        display.append(b.text)
    }

    fun onDot(@Suppress("UNUSED_PARAMETER") view: View) {
        if (clearOnNextDigit) {
            display.text = "0"
            clearOnNextDigit = false
        }
        if (!display.text.contains('.')) {
            display.append(".")
        }
    }

    fun onOperator(view: View) {
        val op = (view as Button).text.first()
        val current = display.text.toString().toDoubleOrNull() ?: 0.0

        if (operand1 == null) {
            operand1 = current
        } else if (pendingOp != null && !clearOnNextDigit) {
            operand1 = calculate(operand1!!, current, pendingOp!!)
            display.text = format(operand1!!)
        }
        pendingOp = op
        clearOnNextDigit = true
    }

    fun onEquals(@Suppress("UNUSED_PARAMETER") view: View) {
        val current = display.text.toString().toDoubleOrNull() ?: return
        if (operand1 != null && pendingOp != null) {
            val result = calculate(operand1!!, current, pendingOp!!)
            display.text = format(result)
            operand1 = result
            pendingOp = null
            clearOnNextDigit = true
        }
    }

    fun onClear(@Suppress("UNUSED_PARAMETER") view: View) {
        operand1 = null
        pendingOp = null
        display.text = "0"
        clearOnNextDigit = true
    }

    private fun calculate(a: Double, b: Double, op: Char): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b == 0.0) Double.NaN else a / b
            else -> b
        }
    }

    private fun format(x: Double): String {
        return if (x.isNaN()) "Error" else if (x % 1.0 == 0.0) x.toLong().toString() else x.toString()
    }
}
