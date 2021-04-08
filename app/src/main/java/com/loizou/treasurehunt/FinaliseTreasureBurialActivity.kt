package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.loizou.treasurehunt.Models.Difficulty
import com.loizou.treasurehunt.Models.Waypoint

class FinaliseTreasureBurialActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var mWaypointList: ArrayList<Waypoint>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalise_treasure_burial)

        title = "Finalise Treasure Burial"

        mWaypointList = intent.getSerializableExtra("waypoint_list") as ArrayList<Waypoint>

        // Setup difficulty selection spinner
        val spinner = findViewById<Spinner>(R.id.spinnerDifficulty)
        // Create ArrayAdapter for difficulty spinner
        ArrayAdapter.createFromResource(this, R.array.difficulty_array, R.layout.support_simple_spinner_dropdown_item)
            .also { adapter ->
                // Layout to use when list of choices appears
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = this
            }
    }

    // Linked to Publish Treasure Hunt Button
    fun saveTreasureHunt(v: View){
        debugLog("Creating treasure hunt...")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Selection
        var diff = when (parent?.getItemAtPosition(position).toString()) {
            "EASY" -> Difficulty.EASY
            "MEDIUM" -> Difficulty.MEDIUM
            "HARD" -> Difficulty.HARD
            else -> {
                debugLog("Error while selecting Treasure Hunt difficulty")
                return
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        debugLog("No difficulty selection was made")
    }
}