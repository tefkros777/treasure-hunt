package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

class WaypointDetails : AppCompatActivity() {

    private lateinit var mWaypoint : Waypoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoint_details)

        val waypoint_id = intent.getStringExtra("waypoint_id")!!
        mWaypoint = DataManager.getWaypointById(waypoint_id)!!
        title = mWaypoint.name

    }
}