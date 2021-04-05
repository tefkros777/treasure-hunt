package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TreasureBurialWelcome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_burial_welcome)
        debugLog("Treasure burial mode selected")

        title = "Treasure Burial Mode"
    }

    fun beginTreasureBurial(v: View){
        val intent = Intent(this, AddWaypointActivity::class.java)
        startActivity(intent)
    }

}