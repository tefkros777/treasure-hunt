package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.textview.MaterialTextView

class AddWaypointActivity : AppCompatActivity(), OnMapReadyCallback {

    private var WAYPOINT_NUM = 0
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_waypoint)

        title = getString(R.string.waypoint_num, ++WAYPOINT_NUM)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // do stuff ...
    }
}