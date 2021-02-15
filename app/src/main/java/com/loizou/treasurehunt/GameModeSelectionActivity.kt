package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class GameModeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode_selection)
        Log.v(LOG_TAG, "Main Activity Loaded")

        val rootLayout = findViewById<ViewGroup>(R.id.rootLayout)
        // Greet user
        showMessage(rootLayout, "Welcome to treasure hunt!")

        val btnHuntMode = findViewById<CardView>(R.id.btnHuntMode);
        val btnBurialMode = findViewById<CardView>(R.id.btnBurialMode);

        btnHuntMode.setOnClickListener {
            showMessage(rootLayout, "TREASURE HUNT MODE")
            // val intent = Intent(this, TreasureHuntActivity::class.java)
            val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
            startActivity(intent)
        }

        btnBurialMode.setOnClickListener {
            showMessage(rootLayout, "TREASURE BURIAL MODE")
        }

    }
}