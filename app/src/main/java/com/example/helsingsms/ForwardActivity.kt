package com.example.helsingsms

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ForwardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 200, 50, 50)
        }

        val numberInput = EditText(this).apply { hint = "Enter Mobile Number with Country Code" }
        val activeBtn = Button(this).apply { 
            text = "ACTIVE"
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.WHITE)
        }

        layout.addView(numberInput)
        layout.addView(activeBtn)
        setContentView(layout)

        activeBtn.setOnClickListener {
            val permissions = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
            ActivityCompat.requestPermissions(this, permissions, 101)
            
            activeBtn.setBackgroundColor(Color.GREEN)
            Toast.makeText(this, "SMS Forwarding Activated", Toast.LENGTH_SHORT).show()
        }
    }
}
