package com.example.helsingsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            if (bundle != null) {
                try {
                    val pdus = bundle.get("pdus") as Array<*>
                    val format = bundle.getString("format")
                    val sb = StringBuilder()
                    var sender = ""

                    for (pdu in pdus) {
                        val smsMessage = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            SmsMessage.createFromPdu(pdu as ByteArray, format)
                        } else {
                            @Suppress("DEPRECATION")
                            SmsMessage.createFromPdu(pdu as ByteArray)
                        }
                        sender = smsMessage.originatingAddress ?: "Unknown"
                        sb.append(smsMessage.messageBody)
                    }

                    sendToFirebase(context, sender, sb.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendToFirebase(context: Context, sender: String, message: String) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            val smsData = hashMapOf(
                "sender" to sender,
                "message" to message,
                "email" to user.email,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("messages").add(smsData)
                .addOnSuccessListener {
                    Toast.makeText(context, "SMS Forwarded to Cloud!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
