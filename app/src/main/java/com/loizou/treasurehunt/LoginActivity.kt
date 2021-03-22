package com.loizou.treasurehunt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.BoringLayout.make
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar.make
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.v(LOG_TAG, "Login Activity Loaded")

        // Initialise Firebase auth
        mAuth = Firebase.auth

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)

        // Login button
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        btnLogin.setOnClickListener {
//            TODO: UNCOMMENT SECTION - DEBUGGING ONLY
//            if (!etEmail.text.isNullOrBlank() || !etPass.text.isNullOrBlank()) {
                closeKeyBoard()

                val email = etEmail.text!!.trim().toString()
                val pass = etPass.text!!.trim().toString()

                signIn(email, pass)
//            } else {
//                debugLog("Could not sign in - Email or pass were null")
//            }
        }
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun signIn(email: String, password: String) {
        // TODO: DEBUGGING ONLY
        val email = "test@test.com"
        val password = "123456"
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    debugLog("Firebase login: Successful")

                    val intent = Intent(this, GameModeSelectionActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    debugLog("Firebase login: Unsuccessful")
                    debugLog(task.exception.toString())
                }
            }
    }

}