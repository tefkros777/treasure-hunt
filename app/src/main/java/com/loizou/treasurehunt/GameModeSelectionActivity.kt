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

class GameModeSelectionActivity : AppCompatActivity() {
    private val mCurrentUser: FirebaseUser = Firebase.auth.currentUser!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode_selection)
        Log.v(LOG_TAG, "Main Activity Loaded")

        title = "Dashboard"

        val rootLayout = findViewById<ViewGroup>(R.id.rootLayout)
        val btnHuntMode = findViewById<CardView>(R.id.btnHuntMode)
        val btnBurialMode = findViewById<CardView>(R.id.btnBurialMode)
        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${mCurrentUser.displayName}!")

        // Greet user
        showMessage(rootLayout, "Welcome to treasure hunt!")

        btnHuntMode.setOnClickListener {
//            showMessage(rootLayout, "TREASURE HUNT MODE")
            // val intent = Intent(this, TreasureHuntActivity::class.java)
            val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
            startActivity(intent)
        }

        btnBurialMode.setOnClickListener {
//            showMessage(rootLayout, "TREASURE BURIAL MODE")
            val intent = Intent(this, TreasureBurialWelcome::class.java)
            startActivity(intent)
        }

    }
}