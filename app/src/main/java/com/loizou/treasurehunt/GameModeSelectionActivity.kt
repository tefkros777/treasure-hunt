package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.Models.User

class GameModeSelectionActivity : AppCompatActivity() {
    private lateinit var mActiveUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode_selection)
        debugLog("Main Activity Loaded")

        mActiveUser = intent.getSerializableExtra(ACTIVE_USER_FLAG) as User
        title = "Dashboard"

        // Ask for location permission here to avoid crashing later
        checkLocationPermission(this)

        val btnHuntMode = findViewById<CardView>(R.id.btnHuntMode)
        val btnBurialMode = findViewById<CardView>(R.id.btnBurialMode)
        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${mActiveUser.displayName}!")

        btnHuntMode.setOnClickListener {
            val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
            startActivity(intent)
        }

        btnBurialMode.setOnClickListener {
            val intent = Intent(this, TreasureBurialWelcome::class.java)
            startActivity(intent)
        }

    }
}