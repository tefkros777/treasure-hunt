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

        val recViewTreasureHuntsList = findViewById<RecyclerView>(R.id.recViewTreasureHunts)
        recViewTreasureHuntsList.layoutManager = LinearLayoutManager(this)
        recViewTreasureHuntsList.adapter = TreasureHuntListAdapter(DataManager.getTreasureHunts())

        // The adapter takes care of launching the TreasureHuntActivity once a selection is made

    }
}