package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loizou.treasurehunt.Models.Waypoint

private lateinit var mWaypoint : Waypoint

class WaypointDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoint_details)

        // title = mWaypoint.name

    }
}