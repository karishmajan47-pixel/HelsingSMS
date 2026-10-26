package com.example.helsingsms

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }
        scrollView.addView(layout)
        setContentView(scrollView)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            layout.addView(TextView(this).apply { text = "Please login on the main page first." })
            return
        }

        val db = FirebaseFirestore.getInstance()
        
        // Listen for messages belonging to this email
        db.collection("messages")
            .whereEqualTo("email", user.email)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null) {
                    layout.removeAllViews()
                    if (snapshot.isEmpty) {
                        layout.addView(TextView(this).apply { text = "No messages yet." })
                    }
                    for (doc in snapshot.documents) {
                        val sender = doc.getString("sender") ?: "Unknown"
                        val msg = doc.getString("message") ?: ""
                        
                        val textView = TextView(this).apply {
                            text = "From: $sender\n$msg\n\n"
                            textSize = 16f
                        }
                        layout.addView(textView)
                    }
                }
            }
    }
}
