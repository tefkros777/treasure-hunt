package com.loizou.treasurehunt

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mSignupForm : ViewGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialise Firebase auth
        mAuth = Firebase.auth

        mSignupForm = findViewById(R.id.signupForm)
    }

    fun loginExistingUser(view: View) {
        closeKeyBoard()
        finish()
    }

    fun createAccount(view: View) {
        val etDisplayName = mSignupForm.findViewById<TextInputEditText>(R.id.etDisplayName)
        val etEmail = mSignupForm.findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = mSignupForm.findViewById<TextInputEditText>(R.id.etPassword)
        val etPasswordConfirm = mSignupForm.findViewById<TextInputEditText>(R.id.etPasswordConfirmation)

        // Sanitise input
        val displayName = etDisplayName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val passwordConfirm = etPasswordConfirm.text.toString().trim()

        // Null check input
        if (displayName.isBlank() || email.isBlank() || password.isBlank() || passwordConfirm.isBlank()){
            showMessage(mSignupForm, "Please fill all fields in the form")
            return
        }

        // Check matching passwords
        if (password != passwordConfirm){
            showMessage(mSignupForm, "Passwords don't match")
            etPassword.text?.clear()
            etPasswordConfirm.text?.clear()
            return
        }

        // If all checks pass, proceed to firebase signup
        closeKeyBoard()
        firebaseSignup(email, password, displayName)

    }

    private fun firebaseSignup(email: String, password: String, name: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    debugLog("Firebase signup: Successful")
                    showMessage(mSignupForm, "Account created successfully")

                    // Set display name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    mAuth.currentUser?.updateProfile(profileUpdates)

                    finish()
                } else {
                    debugLog("Firebase signup: Unsuccessful")
                    debugLog(task.exception.toString())
                    showMessage(mSignupForm, "Account creation failed")
                }
            }
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}