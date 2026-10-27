package com.example.helsingsms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class ForwardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 200, 50, 50)
        }

        val numberInput = EditText(this).apply { hint = "Enter Mobile Number with Country Code" }
        val activeBtn = Button(this).apply { text = "ACTIVE" }
        
        layout.addView(numberInput)
        layout.addView(activeBtn)
        setContentView(layout)

        // Load saved state
        val prefs = getSharedPreferences("HelsingPrefs", Context.MODE_PRIVATE)
        val savedNumber = prefs.getString("forward_number", "")
        val isActive = prefs.getBoolean("is_forward_active", false)

        numberInput.setText(savedNumber)

        if (isActive) {
            activeBtn.setBackgroundColor(Color.GREEN)
            activeBtn.setTextColor(Color.WHITE)
        } else {
            activeBtn.setBackgroundColor(Color.BLUE)
            activeBtn.setTextColor(Color.WHITE)
        }

        activeBtn.setOnClickListener {
            val number = numberInput.text.toString().trim()
            if (number.isEmpty()) {
                Toast.makeText(this, "Please enter a number first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Request SMS permissions
            val permissions = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
            ActivityCompat.requestPermissions(this, permissions, 101)
            
            // Save state
            prefs.edit()
                .putString("forward_number", number)
                .putBoolean("is_forward_active", true)
                .apply()

            activeBtn.setBackgroundColor(Color.GREEN)
            activeBtn.setTextColor(Color.WHITE)
            Toast.makeText(this, "SMS Forwarding Activated & Saved", Toast.LENGTH_SHORT).show()
        }
    }
}
