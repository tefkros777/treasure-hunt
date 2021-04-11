package com.loizou.treasurehunt

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Data.UserSingleton

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        debugLog("Main Activity Loaded")

        title = "Dashboard"

        // Ask for location permission here to avoid crashing later
        checkLocationPermission(this)

        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${UserSingleton.activeUser.displayName}!")

        val tvUsername = findViewById<MaterialTextView>(R.id.tvUsername)
            .apply { text = UserSingleton.activeUser.username }
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
}