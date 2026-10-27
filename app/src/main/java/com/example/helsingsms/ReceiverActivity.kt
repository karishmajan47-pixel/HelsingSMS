package com.example.helsingsms

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ReceiverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 200, 50, 50)
        }

        val numberInput = EditText(this).apply { hint = "Enter Linked Mobile Number" }
        val secretInput = EditText(this).apply { hint = "Enter Secret Letters" }
        val activeBtn = Button(this).apply { 
            text = "ACTIVE"
            setBackgroundColor(Color.parseColor("#ADD8E6"))
            setTextColor(Color.BLACK)
        }

        layout.addView(numberInput)
        layout.addView(secretInput)
        layout.addView(activeBtn)
        setContentView(layout)

        activeBtn.setOnClickListener {
            if (secretInput.text.toString().trim() == "HELSING") {
                activeBtn.setBackgroundColor(Color.GREEN)
                Toast.makeText(this, "Receiver Activated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid Secret Letters", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
