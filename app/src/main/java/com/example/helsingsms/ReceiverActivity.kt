package com.example.helsingsms

import android.content.Context
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

class ReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Root FrameLayout to hold Background + Mask + UI
        val rootLayout = android.widget.FrameLayout(this)
        
        // 1. Set Background Image
        val bgImage = resources.getIdentifier("receiver_bg", "drawable", packageName)
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
            setBackgroundColor(Color.parseColor("#AAFFFFFF"))
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
            text = "SMS RECEIVER"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.parseColor("#5500BFFF")) // Light Blue Border effect
            setPadding(30, 15, 30, 15)
            gravity = Gravity.CENTER
        }

        val numberInput = EditText(this).apply { 
            hint = "Enter Linked Mobile Number" 
            setTextColor(Color.BLACK)
            setHintTextColor(Color.DKGRAY)
        }
        val secretInput = EditText(this).apply { 
            hint = "Enter Secret Letters" 
            setTextColor(Color.BLACK)
            setHintTextColor(Color.DKGRAY)
        }

        val activeBtn = Button(this).apply { 
            text = "ACTIVE"
            setTypeface(null, Typeface.BOLD)
            textSize = 18f
        }

        // Multi-shade INACTIVE Button
        val inactiveBtn = Button(this).apply {
            text = "INACTIVE"
            setTypeface(null, Typeface.BOLD)
            textSize = 20f
            setTextColor(Color.WHITE)
            // Gradient: Cyan to Blue to Purple
            background = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(Color.parseColor("#00E5FF"), Color.parseColor("#2979FF"), Color.parseColor("#D500F9"))
            )
            setPadding(0, 20, 0, 20)
        }

        layout.addView(header)
        val spacer1 = TextView(this).apply { setPadding(0, 0, 0, 50) }
        layout.addView(spacer1)
        layout.addView(numberInput)
        layout.addView(secretInput)
        layout.addView(activeBtn)
        val spacer2 = TextView(this).apply { setPadding(0, 0, 0, 30) }
        layout.addView(spacer2)
        layout.addView(inactiveBtn)

        rootLayout.addView(layout)
        setContentView(rootLayout)

        // --- STATE MEMORY LOGIC ---
        val prefs = getSharedPreferences("HelsingPrefs", Context.MODE_PRIVATE)
        val savedNumber = prefs.getString("receiver_number", "")
        val isReceiverActive = prefs.getBoolean("is_receiver_active", false)
        
        numberInput.setText(savedNumber)

        fun updateButtonState(isActive: Boolean) {
            if (isActive) {
                activeBtn.setBackgroundColor(Color.GREEN)
                activeBtn.setTextColor(Color.WHITE)
            } else {
                activeBtn.setBackgroundColor(Color.parseColor("#ADD8E6")) // Light Blue when inactive
                activeBtn.setTextColor(Color.BLACK)
            }
        }

        // Load saved state on screen open
        updateButtonState(isReceiverActive)

        activeBtn.setOnClickListener {
            val number = numberInput.text.toString().trim()
            if (secretInput.text.toString().trim() == "HELSING" && number.isNotEmpty()) {
                
                // Save state, turn ON receiver, turn OFF forward
                prefs.edit()
                    .putString("receiver_number", number)
                    .putBoolean("is_receiver_active", true)
                    .putBoolean("is_forward_active", false) 
                    .apply()

                updateButtonState(true)
                Toast.makeText(this, "Receiver Activated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid Secret Letters or Empty Number", Toast.LENGTH_SHORT).show()
            }
        }

        inactiveBtn.setOnClickListener {
            prefs.edit().putBoolean("is_receiver_active", false).apply()
            updateButtonState(false)
            Toast.makeText(this, "Receiver Deactivated", Toast.LENGTH_SHORT).show()
        }
    }
}
