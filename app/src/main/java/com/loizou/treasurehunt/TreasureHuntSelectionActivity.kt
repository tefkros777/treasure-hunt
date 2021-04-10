package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.Adapters.TreasureHuntListAdapter
import com.loizou.treasurehunt.Data.Database
import com.loizou.treasurehunt.Models.TreasureHunt
import java.util.*

class TreasureHuntSelectionActivity : AppCompatActivity(), Observer {

    val mDb = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_selection)

        title = "Select a treasure hunt"

        Database.addObserver(this)
        Database.fetchTreasureHunts()

        // The adapter takes care of launching the TreasureHuntDetailsActivity once a selection is made

    }

    // SUBSCRIBE TO DATABASE EVENT AND AWAIT FETCHING COMPLETION
    override fun update(o: Observable?, arg: Any?) {
        when (o){
            // When the observer is the Database
            is Database -> {
                val thList = arg as List<TreasureHunt>
                val recViewTreasureHuntsList = findViewById<RecyclerView>(R.id.recViewTreasureHunts)
                recViewTreasureHuntsList.layoutManager = LinearLayoutManager(this)
                recViewTreasureHuntsList.adapter = TreasureHuntListAdapter(thList)
            }
            else -> println(o?.javaClass.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        Database.deleteObserver(this)

    }
}