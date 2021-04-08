package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.textfield.TextInputEditText
import com.loizou.treasurehunt.Models.Difficulty
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

class FinaliseTreasureBurialActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var mWaypointList: ArrayList<Waypoint>
    private lateinit var mDifficulty: Difficulty
    private  lateinit var mRootLayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalise_treasure_burial)

        title = "Finalise Treasure Burial"

        mWaypointList = intent.getSerializableExtra("waypoint_list") as ArrayList<Waypoint>
        mRootLayout = findViewById(R.id.vgFinaliseTB_Root)

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
        if (!this::mDifficulty.isInitialized){
            debugLog("Difficulty level has not been set")
            showMessage(mRootLayout,"Please select a difficulty level")
            return
        }
        debugLog("Creating treasure hunt...")

        val tvName = findViewById<TextInputEditText>(R.id.tvFinaliseTH_name)
        val tvDesc = findViewById<TextInputEditText>(R.id.tvFinaliseTH_desc)
        val diff = mDifficulty

        // Input validation
        var errorFlag = false // True if one or more error occur
        if (tvName.text.isNullOrBlank()) {
            tvName.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (tvDesc.text.isNullOrBlank()) {
            tvDesc.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (errorFlag) return

        // Validation Passed
        val th = TreasureHunt(
            //todo add author
            name = tvName.text!!.trim().toString(),
            description = tvDesc.text!!.trim().toString(),
            difficulty = diff,
            waypoints = mWaypointList
        )

        debugLog("Performing waypoint post-processing...")
        th.processWaypoints()

        // todo: add to database

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Selection
        mDifficulty = when (parent?.getItemAtPosition(position).toString()) {
            "Easy" -> Difficulty.EASY
            "Medium" -> Difficulty.MEDIUM
            "Hard" -> Difficulty.HARD
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