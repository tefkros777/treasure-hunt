package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.View
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(DEBUG_TAG, "Main Activity Loaded")

        val rootLayout = findViewById<ViewGroup>(R.id.rootLayout)
        // Greet user
        showMessage(rootLayout, "Welcome to treasure hunt!")

        val btnHuntMode = findViewById<View>(R.id.btnHuntMode);
        val btnBurialMode = findViewById<View>(R.id.btnBurialMode);

        btnHuntMode.setOnClickListener {
            showMessage(rootLayout, "TREASURE HUNT MODE")
        }

        btnBurialMode.setOnClickListener {
            showMessage(rootLayout, "TREASURE BURIAL MODE")
        }

    }
}