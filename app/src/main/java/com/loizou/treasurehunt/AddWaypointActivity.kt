package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView

class AddWaypointActivity : AppCompatActivity() {

    private var WAYPOINT_NUM = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_waypoint)

        title = getString(R.string.waypoint_num, ++WAYPOINT_NUM)


    }
}