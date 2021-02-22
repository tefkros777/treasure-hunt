package com.loizou.treasurehunt

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.loizou.treasurehunt.Models.Waypoint

class WaypointDetails : AppCompatActivity() {

    private lateinit var mWaypoint: Waypoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoint_details)

        mWaypoint = DataManager.getWaypointById(intent.getStringExtra("waypoint_id")!!)!!
        title = mWaypoint.name

        val btnSolve = findViewById<MaterialButton>(R.id.btnSolveWaypoint)
        btnSolve.setOnClickListener {
            if(solve()){
                // Solve attempt successful
                showMessage(btnSolve, R.string.puzzle_solved)
                // TODO: End activity with result
                finish()
            } else {
                // Solve attempt unsuccessful
                showMessage(btnSolve, R.string.try_again)
            }
        }
    }

    override fun finish() {
        setResult(Activity.RESULT_OK)
        super.finish()
    }

    fun solve(): Boolean {
        //TODO: Check for puzzle solution
        mWaypoint.solve()
        // Notify adapter
        return true
    }

}