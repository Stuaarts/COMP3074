package com.example.assignment01


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.assignment01.R



class MainActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etRate: EditText
    private lateinit var etTax: EditText
    private lateinit var tvPay: TextView
    private lateinit var tvOvertime: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Week 1 pattern. :contentReference[oaicite:6]{index=6}

        etHours = findViewById(R.id.etHours)
        etRate = findViewById(R.id.etRate)
        etTax = findViewById(R.id.etTax)

        tvPay = findViewById(R.id.tvPay)
        tvOvertime = findViewById(R.id.tvOvertime)
        tvTotal = findViewById(R.id.tvTotal)
        tvTax = findViewById(R.id.tvTax)

        findViewById<Button>(R.id.btnCalc).setOnClickListener {
            calculateAndShow()
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun calculateAndShow() {
        val hours = etHours.text.toString().toDoubleOrNull()
        val rate = etRate.text.toString().toDoubleOrNull()
        val taxRate = etTax.text.toString().toDoubleOrNull()

        if (hours == null || rate == null || taxRate == null) {
            Toast.makeText(this, "Please enter numbers in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val basePay: Double
        val overtimePay: Double
        if (hours <= 40.0) {
            basePay = hours * rate
            overtimePay = 0.0
        } else {
            basePay = 40.0 * rate
            overtimePay = (hours - 40.0) * rate * 1.5
        }
        val totalPay = basePay + overtimePay
        val tax = basePay * taxRate

        tvPay.text = getString(R.string.label_pay) + " $%.2f".format(basePay)
        tvOvertime.text = getString(R.string.label_overtime) + " $%.2f".format(overtimePay)
        tvTotal.text = getString(R.string.label_total) + " $%.2f".format(totalPay)
        tvTax.text = getString(R.string.label_tax) + " $%.2f".format(tax)
    }

    // Menu with "About"
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
