package com.example.helsingsms

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 100, 60, 50) // Moved everything up a bit
            gravity = Gravity.CENTER_HORIZONTAL
        }

        // 1. Welcome Title (Made White and Bold)
        val title = TextView(this).apply {
            text = "Welcome to HelsingSMS"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE) // Changed to WHITE
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 80)
        }

        val emailInput = EditText(this).apply { 
            hint = "Email Address"
            inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
        }
        
        val passInput = EditText(this).apply { 
            hint = "Password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
        }
        
        val loginBtn = Button(this).apply { 
            text = "LOGIN / REGISTER EMAIL" 
            setBackgroundColor(Color.parseColor("#2196F3"))
            setTextColor(Color.WHITE)
        }
        
        val spacer = TextView(this).apply { setPadding(0, 0, 0, 50) }

        val btnForward = Button(this).apply { text = "SMS FORWARD" }
        val btnReceiver = Button(this).apply { text = "SMS RECEIVER" }
        val btnPlace = Button(this).apply { text = "SMS PLACE" }

        layout.addView(title)
        layout.addView(emailInput)
        layout.addView(passInput)
        layout.addView(loginBtn)
        layout.addView(spacer)
        layout.addView(btnForward)
        layout.addView(btnReceiver)
        layout.addView(btnPlace)
        
        setContentView(layout)

        // Check if user is already logged in
        if (auth.currentUser != null) {
            title.text = "Welcome to HelsingSMS\n(${auth.currentUser?.email})"
            emailInput.setText(auth.currentUser?.email)
        }

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passInput.text.toString().trim()
            
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            title.text = "Welcome to HelsingSMS\n($email)"
                            Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show()
                        } else {
                            auth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener { createTask ->
                                    if (createTask.isSuccessful) {
                                        title.text = "Welcome to HelsingSMS\n($email)"
                                        Toast.makeText(this, "Account Created & Logged in!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Error: ${createTask.exception?.message}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        btnForward.setOnClickListener { 
            if (auth.currentUser == null) Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, ForwardActivity::class.java)) 
        }
        
        btnReceiver.setOnClickListener { 
            if (auth.currentUser == null) Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, ReceiverActivity::class.java)) 
        }
        
        btnPlace.setOnClickListener { 
            if (auth.currentUser == null) Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, PlaceActivity::class.java)) 
        }
    }
}
