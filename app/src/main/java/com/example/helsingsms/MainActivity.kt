// ... (keep your imports the same)

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // --- NEW CODE: Check if already logged in ---
        if (auth.currentUser != null) {
            // If already logged in, you can go straight to the buttons, 
            // but for simplicity, we will just pre-fill the email.
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 150, 60, 50)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }

        // ... (keep the rest of the UI code exactly the same until the loginBtn.setOnClickListener)
        
        // --- NEW CODE: Pre-fill email if already logged in ---
        if (auth.currentUser != null) {
            val currentEmail = auth.currentUser?.email
            if (currentEmail != null) {
                emailInput.setText(currentEmail)
            }
        }

        // ... (keep the rest of the login button logic and navigation logic the same)
    }
}
