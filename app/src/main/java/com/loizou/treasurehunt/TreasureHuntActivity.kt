package com.loizou.treasurehunt

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.loizou.treasurehunt.Adapters.WaypointListAdapter
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint


class TreasureHuntActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mTreasureHunt: TreasureHunt
    private lateinit var mRecViewWaypointList: RecyclerView

    private lateinit var visibleWaypointList: MutableList<Waypoint>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt)

        val game_id = intent.getStringExtra("game_id")!!
        mTreasureHunt = DataManager.getTreasureHuntById(game_id)!!
        title = mTreasureHunt.name
        Log.d(LOG_TAG, "LOADED GAME WITH ID: ${mTreasureHunt.id}")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTreasureHunt) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Setup waypoint recycler view
        mRecViewWaypointList = findViewById(R.id.recViewWaypointList)
        mRecViewWaypointList.layoutManager = LinearLayoutManager(this)

        // Include only visible waypoints to the list
        mRecViewWaypointList.adapter =
            WaypointListAdapter(mTreasureHunt.Waypoints.filter { it.isVisible } as MutableList<Waypoint>,
                this) { item ->
                handleWaypointClick(item)
            }

        // Cache the Waypoint list of the adapter (data source)
        visibleWaypointList = (mRecViewWaypointList.adapter as WaypointListAdapter).mWaypointList

        // Get location access
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    /**
     * Called when WaypointDetailsActivity finishes
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == WPT_DETAILS_REQ_CODE) {
            if (data!!.hasExtra("waypoint_index")) {
                val nextWptIndex = data.extras!!.getInt("waypoint_index") + 1
                // If there is no next waypoint
                if (nextWptIndex >= mTreasureHunt.Waypoints.size) {
                    showMessage(mRecViewWaypointList, R.string.game_finished)
                    return
                }
                visibleWaypointList.add(mTreasureHunt.Waypoints[nextWptIndex])
                mRecViewWaypointList.adapter!!.notifyItemChanged(nextWptIndex)
                addWaypointMarkers(mTreasureHunt.Waypoints)
            }
        }
    }

    /**
     * Adds a marker on the map for every visible waypoint
     */
    private fun addWaypointMarkers(waypoints: List<Waypoint>) {
        for (waypoint in waypoints) {
            // Solved waypoints have already been added. Don't add them again
            if (waypoint.isVisible && !waypoint.solved) {
                val marker = LatLng(waypoint.coords.latitude, waypoint.coords.longitude)
                mMap.addMarker(MarkerOptions().position(marker).title(waypoint.name))
                debugLog("Added marker for ${waypoint.name}")
            }
        }
    }

    fun handleWaypointClick(waypoint: Waypoint) {
        Log.d(LOG_TAG, "SELECTED WAYPOINT: ${waypoint.name}")
        val zoomLevel = 15.0f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(waypoint.coords, zoomLevel))
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
        checkLocationPermission()
        mMap.isMyLocationEnabled = true
        getLastLocation()
        addWaypointMarkers(mTreasureHunt.Waypoints)
    }

    /**
     * Checks if location permission is granted and requests it if it's not
     */
    private fun checkLocationPermission() {
        // Check for location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
            return
        }
    }

    /**
     * Request new high accuracy location
     */
    private fun getLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10
        locationRequest.fastestInterval = 0

        checkLocationPermission()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    /**
     * Return last location of the client and zoom into it on the map.
     * Won't update if location is changed until called again
     */
    private fun getLastLocation() {
        if (isLocationEnabled()) {
            checkLocationPermission()
            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                val location: Location? = task.result // Wait until task has finished
                if (location == null)
                    getLocationData() // Request new location
                else {
                    val lat = location.latitude
                    val long = location.longitude
                    Log.i(LOG_TAG, "Last Location: $lat, $long")

                    val lastLoc = LatLng(lat, long)
                    val zoomLevel = 15.0f

                    // Move map to last location
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoc, zoomLevel))
                }
            }
        }
    }

    /**
     * Returns true if either GPS location or Network location is enabled
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))
    }

    /**
     * Called whenever new location becomes available
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation
            val lat = lastLocation.latitude
            val long = lastLocation.longitude

            Log.i(LOG_TAG, "Current Location: $lat, $long")

            val lastLoc = LatLng(lat, long)
            mMap.addMarker(MarkerOptions().position(lastLoc).title("Me"))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLoc))
        }
    }

}