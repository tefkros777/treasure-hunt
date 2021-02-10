package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loizou.treasurehunt.Adapters.TreasureHuntListAdapter

class TreasureHuntSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_selection)

        title = "Select a treasure hunt"

        val recViewTreasureHuntsList = findViewById<RecyclerView>(R.id.TreasureHuntsRecyclerView)
        recViewTreasureHuntsList.adapter = TreasureHuntListAdapter(DataManager.getTreasureHunts())
        recViewTreasureHuntsList.layoutManager = LinearLayoutManager(this)
    }
}