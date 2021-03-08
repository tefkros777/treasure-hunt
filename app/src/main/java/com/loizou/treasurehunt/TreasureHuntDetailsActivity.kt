package com.loizou.treasurehunt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.loizou.treasurehunt.Models.TreasureHunt

class TreasureHuntDetailsActivity() : AppCompatActivity() {

    private lateinit var mTreasureHunt: TreasureHunt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_details)
        title = getString(R.string.th_preview)

        mTreasureHunt = DataManager.getTreasureHuntById(intent.getStringExtra("game_id")!!)!!
        debugLog("Loaded preview for ${mTreasureHunt.name}")

    }

    fun startTreasureHunt(view: View) {
        val intent = Intent(this, TreasureHuntActivity::class.java)
        intent.putExtra("game_id", mTreasureHunt.id)
        this.startActivity(intent)
    }
}