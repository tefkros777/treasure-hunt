package com.loizou.treasurehunt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.Data.Database
import com.loizou.treasurehunt.Data.UserSingleton
import com.loizou.treasurehunt.Models.User
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val SIGNUP_ACTIVITY_REQ: Int = 2
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLoginForm: ViewGroup
    private lateinit var mLoginButton: MaterialButton
    private lateinit var mActiveUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        debugLog("Login Activity Loaded")

        // Initialise Firebase auth
        mAuth = Firebase.auth

        mLoginForm = findViewById(R.id.loginForm)
        mLoginButton = findViewById(R.id.btnLogin)

    }

    fun login(view: View) {
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPass = findViewById<TextInputEditText>(R.id.etPassword)
        if (!etEmail.text.isNullOrBlank() || !etPass.text.isNullOrBlank()) {
            mLoginButton.isEnabled = false
            progressBar.visibility = View.VISIBLE
            closeKeyBoard()
            val email = etEmail.text!!.trim().toString()
            val pass = etPass.text!!.trim().toString()
            firebaseSignIn(email, pass)
        } else {
            debugLog("Could not sign in - Email or pass were null")
        }
    }

    fun createNewUser(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivityForResult(intent, SIGNUP_ACTIVITY_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            showMessage(mLoginForm, "Account created successfully, please sign in")
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun firebaseSignIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    debugLog("FIREBASE AUTH: Successful")
                    // Update the ActiveUser
                    val docRef = Database.db.collection(Database.USERS_COLLECTION_PATH)
                        .document(mAuth.currentUser!!.email)
                        .get()
                        .addOnSuccessListener { documentReference ->
                            if (documentReference != null) {
                                debugLog("DATABASE: Successfully retrieved document for email $email")

                                // Deserialize user document to object
                                val deserializedUser = documentReference.toObject(User::class.java)!!

                                // Update UserSingleton
                                UserSingleton.activeUser = deserializedUser

                                // If all is successful, move into next activity (Dashboard)
                                val intent = Intent(this, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                debugLog("DATABASE: Could not find User document for email $email")
                            }
                        }
                        .addOnFailureListener { exception ->
                            debugLog("DATABASE: Request failed $exception")
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    debugLog("FIREBASE AUTH: Login Unsuccessful")
                    debugLog(task.exception.toString())
                    showMessage(mLoginForm, task.exception?.message.toString())
                    mLoginButton.isEnabled = true
                    progressBar.visibility = View.GONE
                }
            }
    }

}