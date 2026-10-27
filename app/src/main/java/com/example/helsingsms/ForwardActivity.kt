package com.example.helsingsms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ForwardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Root FrameLayout to hold Background + Mask + UI
        val rootLayout = android.widget.FrameLayout(this)
        
        // 1. Set Background Image
        val bgImage = resources.getIdentifier("forward_bg", "drawable", packageName)
        if (bgImage != 0) {
            rootLayout.setBackgroundResource(bgImage)
        } else {
            rootLayout.setBackgroundColor(Color.BLACK)
        }

        // 2. Add Bright Thin Mask over the background
        val maskView = android.view.View(this).apply {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                android.widget.FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#AAFFFFFF")) // 66% transparent white
        }
        rootLayout.addView(maskView)

        // 3. UI Layout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        // Header
        val header = TextView(this).apply {
            text = "SMS FORWARD"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.parseColor("#55FF0000")) // Light Red Border effect
            setPadding(30, 15, 30, 15)
            gravity = Gravity.CENTER
        }

        val numberInput = EditText(this).apply { 
            hint = "Enter Mobile Number with Country Code"
            setTextColor(Color.BLACK)
            setHintTextColor(Color.DKGRAY)
        }

        val activeBtn = Button(this).apply { 
            text = "ACTIVE"
            setTypeface(null, Typeface.BOLD)
            textSize = 18f
        }

        // Multi-shade DEACTIVATE Button
        val deactivateBtn = Button(this).apply {
            text = "DEACTIVATE"
            setTypeface(null, Typeface.BOLD)
            textSize = 20f
            setTextColor(Color.WHITE)
            // Gradient: Red to Orange to Yellow
            background = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#FF1744"), Color.parseColor("#FF9100"), Color.parseColor("#FFEA00"))
            )
            setPadding(0, 20, 0, 20)
        }

        layout.addView(header)
        val spacer1 = TextView(this).apply { setPadding(0, 0, 0, 50) }
        layout.addView(spacer1)
        layout.addView(numberInput)
        layout.addView(activeBtn)
        val spacer2 = TextView(this).apply { setPadding(0, 0, 0, 30) }
        layout.addView(spacer2)
        layout.addView(deactivateBtn)

        rootLayout.addView(layout)
        setContentView(rootLayout)

        // --- STATE MEMORY LOGIC ---
        val prefs = getSharedPreferences("HelsingPrefs", Context.MODE_PRIVATE)
        val savedNumber = prefs.getString("forward_number", "")
        val isForwardActive = prefs.getBoolean("is_forward_active", false)
        
        numberInput.setText(savedNumber)

        // Function to update UI based on state
        fun updateButtonState(isActive: Boolean) {
            if (isActive) {
                activeBtn.setBackgroundColor(Color.GREEN)
                activeBtn.setTextColor(Color.WHITE)
            } else {
                activeBtn.setBackgroundColor(Color.GRAY)
                activeBtn.setTextColor(Color.WHITE)
            }
        }

        // Load saved state on screen open
        updateButtonState(isForwardActive)

        activeBtn.setOnClickListener {
            val number = numberInput.text.toString().trim()
            if (number.isEmpty()) {
                Toast.makeText(this, "Please enter a number first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Request SMS permissions if not granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 101)
            }
            
            // Save state, turn ON forward, turn OFF receiver
            prefs.edit()
                .putString("forward_number", number)
                .putBoolean("is_forward_active", true)
                .putBoolean("is_receiver_active", false) 
                .apply()

            updateButtonState(true)
            Toast.makeText(this, "SMS Forward Activated", Toast.LENGTH_SHORT).show()
        }

        deactivateBtn.setOnClickListener {
            prefs.edit().putBoolean("is_forward_active", false).apply()
            updateButtonState(false)
            Toast.makeText(this, "SMS Forward Deactivated", Toast.LENGTH_SHORT).show()
        }
    }
}
