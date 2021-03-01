package com.loizou.treasurehunt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.button.MaterialButton
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

class WaypointDetails : AppCompatActivity() {

    private lateinit var mTreasureHunt: TreasureHunt
    private lateinit var mWaypoint: Waypoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoint_details)

        mWaypoint = DataManager.getWaypointById(intent.getStringExtra("waypoint_id")!!)!!
        mTreasureHunt = mWaypoint.parent_game

        title = mWaypoint.name

    }

    /**
     * Linked to btnSolveWaypoint
     */
    fun solveWaypoint(v: View) {
        val attempt = mWaypoint.attemptSolve("solution") //TODO: Read solution from user
        if (attempt) {
            // Solve attempt successful
            val data = Intent()
            data.putExtra("waypoint_index", mTreasureHunt.Waypoints.indexOf(mWaypoint))
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            // Solve attempt unsuccessful
            val btnSolve = findViewById<MaterialButton>(R.id.btnSolveWaypoint)
            showMessage(btnSolve, R.string.try_again)
        }
    }

}