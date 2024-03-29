package com.loizou.treasurehunt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import com.google.firebase.Timestamp
import com.loizou.treasurehunt.Adapters.WaypointListAdapter
import com.loizou.treasurehunt.Data.Database
import com.loizou.treasurehunt.Data.UserSingleton
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
        debugLog("Activity result called - Request code: $requestCode")
        when (requestCode){
            WPT_DETAILS_REQ_CODE -> {
                // Waypoint details activity has finished
                if (resultCode == Activity.RESULT_OK) {
                    if (data!!.hasExtra("waypoint_index")) {
                        val solvedWptIndex = data.extras!!.getInt("waypoint_index")
                        // If there is no next waypoint (this is the last one)
                        if (solvedWptIndex + 1 >= mTreasureHunt.waypoints.size) {
                            // Perform saving before showing congratulation activity

                            // Set as solved
                            mTreasureHunt.isSolved = true
                            mTreasureHunt.solvedOn = Timestamp.now()

                            // Add to user's solved games list
                            UserSingleton.activeUser.completedGames.add(mTreasureHunt)
                            Database.updateUserCompletedGames(UserSingleton.activeUser)

                            // Award Pirate Points to the player
                            UserSingleton.activeUser.score += mTreasureHunt.points
                            Database.updateUserScore(UserSingleton.activeUser)

                            // Show congratulations activity
                            val intent = Intent(this, CongratulationsActivity::class.java)
                            intent.putExtra(CONGRATS_TITLE, getString(R.string.congrats))
                            intent.putExtra(CONGRATS_BODY, getString(R.string.finish_game_body))
                            intent.putExtra(CONGRATS_BTN_TXT, getString(R.string.done))
                            intent.putExtra(CONGRATS_IMG_SRC, R.drawable.trophy1)
                            startActivityForResult(intent, FINISH_GAME_REQ_CODE)
                            return
                        } else {
                            val solvedWpt = mTreasureHunt.waypoints[solvedWptIndex]
                            mTreasureHunt.enableNextWaypoint(solvedWpt)
                            val nextWpt = mTreasureHunt.waypoints[solvedWptIndex + 1]
                            mVisibleWaypointList.add(nextWpt)
                            mRecViewWaypointList.adapter!!.notifyItemChanged(solvedWptIndex + 1)
                            addWaypointMarkers(mVisibleWaypointList)
                        }
                    }
                }
            }
            FINISH_GAME_REQ_CODE -> {
                // Congratulations activity has finished - Game has finished

                // Go back to treasurehunt selection
                val homeIntent = Intent(this, DashboardActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(homeIntent)
                finish()
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
        addWaypointMarkers(mVisibleWaypointList)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.warning)
            .setMessage(R.string.quit_hunt_promt)
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                // TODO: Maybe perform some saving?
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
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