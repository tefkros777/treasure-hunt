package com.loizou.treasurehunt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint


class TreasureHuntDetailsActivity() : AppCompatActivity(), OnMapReadyCallback {

    private val ZOOM_LEVEL: Float = 15f
    private lateinit var mMap: GoogleMap
    private lateinit var mTreasureHunt: TreasureHunt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_details)
        title = getString(R.string.th_preview)

        mTreasureHunt = DataManager.getTreasureHuntById(intent.getStringExtra("game_id")!!)!!
        debugLog("Loaded preview for ${mTreasureHunt.name}")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTreasureHuntPreview) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun startTreasureHunt(view: View) {
        val intent = Intent(this, TreasureHuntActivity::class.java)
        intent.putExtra("game_id", mTreasureHunt.id)
        this.startActivity(intent)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                mTreasureHunt.Waypoints[0].coords,
                ZOOM_LEVEL
            )
        )
        drawTreasureHunt()
    }

    // Draw a line from all the waypoints of the treasure hunt
    private fun drawTreasureHunt() {
        val line = PolylineOptions()
        for (waypoint: Waypoint in mTreasureHunt.Waypoints)
            line.add(waypoint.coords)
        line.width(5f).color(Color.RED)
        mMap.addPolyline(line)

    }
}