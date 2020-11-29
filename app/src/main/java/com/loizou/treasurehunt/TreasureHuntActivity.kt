package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Transformations.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TreasureHuntActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTreasureHunt) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val swansea = LatLng(51.6, -3.95)
        val meridianTower = LatLng(51.619202, -3.932988)
        val sailBridge = LatLng(51.61388, -3.94319)



        mMap.addMarker(MarkerOptions().position(swansea).title("Marker in Swansea"))
        mMap.addMarker(MarkerOptions().position(meridianTower).title("Meridian Tower"))
        mMap.addMarker(MarkerOptions().position(sailBridge).title("Sail Bridge"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(swansea))
    }
}