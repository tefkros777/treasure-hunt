package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        val btnHuntMode = findViewById<CardView>(R.id.btnHuntMode)
        val btnBurialMode = findViewById<CardView>(R.id.btnBurialMode)
        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${UserSingleton.activeUser.displayName}!")
    }

    fun huntMode(v: View){
        val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
        startActivity(intent)
    }

    fun burialMode(v: View){
        val intent = Intent(this, TreasureBurialWelcome::class.java)
        startActivity(intent)
    }

}