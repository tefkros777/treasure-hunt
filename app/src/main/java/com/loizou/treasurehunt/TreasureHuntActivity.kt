package com.loizou.treasurehunt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mRecViewWaypointList: RecyclerView
    private lateinit var mTreasureHunt: TreasureHunt

    private lateinit var mVisibleWaypointList: MutableList<Waypoint>
    private lateinit var mMarkerList: MutableList<MarkerOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt)

        val game_id = intent.getStringExtra("game_id")!!
        mTreasureHunt = Database.getTreasureHuntById(game_id)!!
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
            WaypointListAdapter(mTreasureHunt.waypoints.filter { it.isVisible } as MutableList<Waypoint>,
                this) { item ->
                handleWaypointClick(item)
            }

        mMarkerList = mutableListOf()

        // Cache the Waypoint list of the adapter (data source)
        mVisibleWaypointList = (mRecViewWaypointList.adapter as WaypointListAdapter).mWaypointList

        // Get location access
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
                if (nextWptIndex >= mTreasureHunt.waypoints.size) {
                    showMessage(mRecViewWaypointList, R.string.game_finished)
                    return
                }
                mVisibleWaypointList.add(mTreasureHunt.waypoints[nextWptIndex])
                mRecViewWaypointList.adapter!!.notifyItemChanged(nextWptIndex)
                addWaypointMarkers(mVisibleWaypointList)
            }
        }
    }

    /**
     * Adds a marker on the map for every visible waypoint
     */
    private fun addWaypointMarkers(waypoints: List<Waypoint>) {
        for (waypoint in waypoints) {
            // Solved waypoints have already been added. Don't add them again
            if (waypoint.isVisible && !mMarkerList.contains(waypoint)) {
                val marker = MarkerOptions()
                    .position(LatLng(waypoint.latitude, waypoint.longitude))
                    .title(waypoint.name)
                mMap.addMarker(marker)
                mMarkerList.add(marker)
                debugLog("Added marker for ${waypoint.name}")
            }
        }
    }

    fun handleWaypointClick(waypoint: Waypoint) {
        Log.d(LOG_TAG, "SELECTED WAYPOINT: ${waypoint.name}")
        val zoomLevel = 18.0f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(waypoint.getCoords(), zoomLevel))
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
        checkLocationPermission(this)
        mMap.isMyLocationEnabled = true
        // getLastLocation() // TODO: Maybe we don't need this?
        addWaypointMarkers(mVisibleWaypointList)
    }

    /**
     * Return last location of the client and zoom into it on the map.
     * Won't update if location is changed until called again
     */
    private fun getLastLocation() {
        if (isLocationEnabled()) {
            checkLocationPermission(this)
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
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
     * Request new high accuracy location
     */
    private fun getLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10
        locationRequest.fastestInterval = 0

        checkLocationPermission(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
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