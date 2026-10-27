package com.example.helsingsms

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
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
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val scrollView = android.widget.ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 100, 60, 50)
            gravity = Gravity.CENTER_HORIZONTAL
        }
        scrollView.addView(layout)
        setContentView(scrollView)

        // 1. Welcome Title
        title = TextView(this).apply {
            text = "Welcome to HelsingSMS"
            textSize = 28f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.WHITE)
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

        // PINK TEXT BOX
        val pinkTextBox = TextView(this).apply {
            val styledText = 
                "<b><font color='#D81B60'>Thank you</font> <font color='#8E24AA'>for being</font> " +
                "<font color='#3949AB'>a partner</font> <font color='#00897B'>who shares</font> " +
                "<font color='#F4511E'>our vision</font> <font color='#6D4C41'>for the</font> " +
                "<font color='#546E7A'>future.</font> <font color='#5E35B1'>We</font> " +
                "<font color='#43A047'>appreciate</font> <font color='#E53935'>your</font> " +
                "<font color='#FB8C00'>continued</font> <font color='#1E88E5'>support.</font></b>" +
                "<br><br>" +
                "<b><font color='#D81B60'>感谢您</font> <font color='#8E24AA'>成为</font> " +
                "<font color='#3949AB'>与我们</font> <font color='#00897B'>拥有</font> " +
                "<font color='#F4511E'>共同愿景</font> <font color='#6D4C41'>的</font> " +
                "<font color='#546E7A'>合作伙伴。</font> <font color='#5E35B1'>我们</font> " +
                "<font color='#43A047'>感谢</font> <font color='#E53935'>您</font> " +
                "<font color='#FB8C00'>一直以来的</font> <font color='#1E88E5'>支持。</font></b>" +
                "<br><br>" +
                "<b><font color='#D81B60'>Спасибо,</font> <font color='#8E24AA'>что вы</font> " +
                "<font color='#3949AB'>партнер,</font> <font color='#00897B'>который разделяет</font> " +
                "<font color='#F4511E'>наше видение</font> <font color='#6D4C41'>на</font> " +
                "<font color='#546E7A'>будущее.</font> <font color='#5E35B1'>Мы</font> " +
                "<font color='#43A047'>ценим</font> <font color='#E53935'>вашу</font> " +
                "<font color='#FB8C00'>постоянную</font> <font color='#1E88E5'>поддержку.</font></b>"
            
            text = Html.fromHtml(styledText, Html.FROM_HTML_MODE_LEGACY)
            textSize = 16f
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#FCE4EC"))
            setPadding(40, 40, 40, 40)
            
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 60, 0, 60)
            layoutParams = params
        }

        layout.addView(title)
        layout.addView(emailInput)
        layout.addView(passInput)
        layout.addView(loginBtn)
        layout.addView(spacer)
        layout.addView(btnForward)
        layout.addView(btnReceiver)
        layout.addView(btnPlace)
        layout.addView(pinkTextBox) 
        
        if (auth.currentUser != null) {
            title.text = "Welcome to HelsingSMS\n(${auth.currentUser?.email})"
            emailInput.setText(auth.currentUser?.email)
        }

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val pass = passInput.text.toString().trim()
            
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // Show a loading text at the top
                title.text = "Logging in..." 

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // SUCCESS! Message appears at the top.
                            title.text = "Logged In Successfully!\nWelcome to HelsingSMS\n($email)"
                            title.setTextColor(Color.GREEN)
                        } else {
                            auth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener { createTask ->
                                    if (createTask.isSuccessful) {
                                        title.text = "Account Created & Logged In!\n($email)"
                                        title.setTextColor(Color.GREEN)
                                    } else {
                                        // ERROR! Message appears at the top.
                                        val errorMsg = createTask.exception?.message ?: "Unknown Error"
                                        title.text = "Login Error:\n$errorMsg"
                                        title.setTextColor(Color.RED)
                                    }
                                }
                        }
                    }
            } else {
                // ERROR! Message appears at the top.
                title.text = "Error:\nPlease enter email and password"
                title.setTextColor(Color.RED)
            }
        }

        btnForward.setOnClickListener { 
            if (auth.currentUser == null) {
                title.text = "Error:\nPlease login first"
                title.setTextColor(Color.RED)
            } else startActivity(Intent(this, ForwardActivity::class.java)) 
        }
        
        btnReceiver.setOnClickListener { 
            if (auth.currentUser == null) {
                title.text = "Error:\nPlease login first"
                title.setTextColor(Color.RED)
            } else startActivity(Intent(this, ReceiverActivity::class.java)) 
        }
        
        btnPlace.setOnClickListener { 
            if (auth.currentUser == null) {
                title.text = "Error:\nPlease login first"
                title.setTextColor(Color.RED)
            } else startActivity(Intent(this, PlaceActivity::class.java)) 
        }
    }
}
