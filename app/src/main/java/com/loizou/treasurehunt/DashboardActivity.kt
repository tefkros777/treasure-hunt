package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.loizou.treasurehunt.Data.UserSingleton

class DashboardActivity : AppCompatActivity() {
    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        debugLog("Main Activity Loaded")

        title = "Dashboard"

        // Ask for location permission here to avoid crashing later
        checkLocationPermission(this)

        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${UserSingleton.activeUser.displayName}!")

        val tvName = findViewById<MaterialTextView>(R.id.tvName)
            .apply { text = UserSingleton.activeUser.displayName }
        val tvEmail = findViewById<MaterialTextView>(R.id.tvEmail)
            .apply { text = UserSingleton.activeUser.email }
        val tvScore = findViewById<MaterialTextView>(R.id.tvScore)
            .apply { text = UserSingleton.activeUser.score.toString() }
    }

    fun huntMode(v: View) {
        val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
        startActivity(intent)
    }

    fun burialMode(v: View) {
        val intent = Intent(this, TreasureBurialWelcome::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.exit_promt)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // TODO: Maybe perform some saving?
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }

    fun logout(view: View) {
        // Show confirmation dialog
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.signout_promt)
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                // TODO: Maybe perform some saving?
                debugLog("Logging out")
                mAuth.signOut()

                // Go back to login screen
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }
}