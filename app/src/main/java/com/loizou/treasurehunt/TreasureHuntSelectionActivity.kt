package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.Adapters.TreasureHuntListAdapter
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

class TreasureHuntSelectionActivity : AppCompatActivity() {

    val mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_selection)

        title = "Select a treasure hunt"

        Database.fetchTreasureHunts()

        val recViewTreasureHuntsList = findViewById<RecyclerView>(R.id.recViewTreasureHunts)
        recViewTreasureHuntsList.layoutManager = LinearLayoutManager(this)
        recViewTreasureHuntsList.adapter = TreasureHuntListAdapter(Database.getTreasureHunts())

        // The adapter takes care of launching the TreasureHuntActivity once a selection is made

    }
}