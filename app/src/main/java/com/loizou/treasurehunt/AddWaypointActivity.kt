package com.loizou.treasurehunt

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Custom.ScrollableMapFragment
import com.loizou.treasurehunt.Models.Waypoint
import kotlinx.android.synthetic.main.activity_add_waypoint.*
import kotlinx.android.synthetic.main.enter_coords_dialog.view.*
import kotlinx.android.synthetic.main.solve_waypoint_dialog_layout.view.btnDialogCancel

class AddWaypointActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var mLoadingDialog: AlertDialog

    private lateinit var mRootLayout: ViewGroup
    private lateinit var mTvAddWaypoint_coords: MaterialTextView

    private lateinit var mWaypointList: ArrayList<Waypoint>
    private var mWaypointCoords: LatLng? = null
    private var WAYPOINT_NUM = 0

    private val ZOOM_LEVEL: Float = 18f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_waypoint)

        title = getString(R.string.waypoint_num, ++WAYPOINT_NUM)

        // GPS Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mRootLayout = findViewById(R.id.vgAddWaypointRootLayout)
        mTvAddWaypoint_coords = findViewById(R.id.tvAddWaypoint_coords)

        mWaypointList = arrayListOf()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapWaypointLocationPreview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Configure loading dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.getting_current_location)
        mLoadingDialog = dialogBuilder.create()
    }

    // Button Add Location Manually
    fun addLocationManually(v: View) {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.enter_coords_dialog, null)
        // AlertDialogBuilder
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.enter_coords)
        val alertDialog = dialogBuilder.show()

        // Solve button of the dialog
        dialogView.btnDialogDone.setOnClickListener {

            // Input validation
            var errorFlag = false // True if one or more errors occur
            if (dialogView.tvCoordsLat.text.isNullOrBlank()) {
                dialogView.tvCoordsLat.error = getString(R.string.cannot_be_blank)
                errorFlag = true
            }
            if (dialogView.tvCoordsLong.text.isNullOrBlank()) {
                dialogView.tvCoordsLong.error = getString(R.string.cannot_be_blank)
                errorFlag = true
            }
            if (errorFlag) return@setOnClickListener

            mWaypointCoords = LatLng(
                dialogView.tvCoordsLat.text!!.trim().toString().toDouble(),
                dialogView.tvCoordsLong.text!!.trim().toString().toDouble()
            )

            showCoordsOnMap()
            alertDialog.dismiss()
        }

        // Cancel button of the dialog
        dialogView.btnDialogCancel.setOnClickListener { alertDialog.dismiss() }
    }

    // Button Current Location
    fun setCurrentLocation(v: View) {
        debugLog("Getting current location")
        getLastLocation()
    }

    // Called when getLastLocation finishes successfully
    fun onLocationAcquired(loc: LatLng) {
        debugLog("Location acquired successfully - $loc")
        mWaypointCoords = LatLng(loc.latitude, loc.longitude)
        showCoordsOnMap()
    }

    // Button Save Waypoint
    fun saveWaypoint(v: View) {
        val tvName = findViewById<TextInputEditText>(R.id.tvWptName)
        val tvDesc = findViewById<TextInputEditText>(R.id.tvWptDescription)
        val tvTask = findViewById<TextInputEditText>(R.id.tvWptTask)
        val tvSolution = findViewById<TextInputEditText>(R.id.tvWptSolution)

        // Input validation
        var errorFlag = false // True if one or more error occur
        if (tvName.text.isNullOrBlank()) {
            tvName.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (tvDesc.text.isNullOrBlank()) {
            tvDesc.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (tvTask.text.isNullOrBlank()) {
            tvTask.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (tvSolution.text.isNullOrBlank()) {
            tvSolution.error = getString(R.string.cannot_be_blank)
            errorFlag = true
        }
        if (mWaypointCoords == null) {
            showMessage(mRootLayout, "Coordinates not set!")
            errorFlag = true
        }
        if (errorFlag) return

        // Construct Waypoint based on user input
        val newWaypoint = Waypoint(
            name = tvName.text.toString(),
            description = tvDesc.text.toString(),
            tasks = tvTask.text.toString(),
            solution = tvSolution.text.toString(),
            latitude = mWaypointCoords!!.latitude,
            longitude = mWaypointCoords!!.longitude
        )

        // Add waypoint to the list
        mWaypointList.add(newWaypoint)
        debugLog("Added waypoint to the list")

        // Show dialog asking if this is the final waypoint
        showLastWaypointPromt()
    }

    fun showLastWaypointPromt() {
        val dialog = AlertDialog.Builder(this, R.style.AlertDialogStyle)
            .setTitle("Waypoint Saved!")
            .setMessage("Do you wish to add more waypoints?")
            .setPositiveButton("Add more") { dialog, id ->
                clearForm()
            }
            .setNegativeButton("That was the final one") { dialog, id ->
                // proceed to add treasure hunt details/metadata
                // pass mWaypointList to next activity
                val intent = Intent(this, FinaliseTreasureBurialActivity::class.java)
                intent.putExtra("waypoint_list", mWaypointList)
                startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }

    private fun clearForm() {
        // Update title
        title = getString(R.string.waypoint_num, ++WAYPOINT_NUM)

        // Clear all fields
        findViewById<TextInputEditText>(R.id.tvWptName).text?.clear()
        findViewById<TextInputEditText>(R.id.tvWptDescription).text?.clear()
        findViewById<TextInputEditText>(R.id.tvWptTask).text?.clear()
        findViewById<TextInputEditText>(R.id.tvWptSolution).text?.clear()
        findViewById<MaterialTextView>(R.id.tvAddWaypoint_coords).text = getString(R.string.not_set)

        // Clear location markers
        mMap.clear()

        // Clear coordinates
        mWaypointCoords = null

        // Scroll to top
        scrollView.scrollTo(0,0)
    }

    // Callback
    override fun onMapReady(map: GoogleMap) {
        mMap = map
        (supportFragmentManager.findFragmentById(R.id.mapWaypointLocationPreview) as? ScrollableMapFragment)?.let {
            it.listener = object : ScrollableMapFragment.OnTouchListener {
                override fun onTouch() {
                    scrollView?.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        checkLocationPermission(this)
        mMap.isMyLocationEnabled = true
        debugLog("Add Waypoint Activity: Map Ready")
    }

    // Show coordinates as a marker on the map
    private fun showCoordsOnMap() {
        // Animate map to the coords location
        val wptMarker = MarkerOptions()
            .position(mWaypointCoords!!)
            .title("${mWaypointCoords!!.latitude}, ${mWaypointCoords!!.longitude}")

        // Clear markers first
        mMap.clear()
        mMap.addMarker(wptMarker)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(wptMarker.position, ZOOM_LEVEL))

        val coordsFormatted = "${mWaypointCoords!!.latitude}, ${mWaypointCoords!!.longitude}"

        // showMessage(mRootLayout, "Coordinates set to $coordsFormatted")
        mTvAddWaypoint_coords.text = coordsFormatted
    }

    // Location-related methods

    /**
     * Return last location of the client.
     * Won't update if location is changed until called again
     */
    private fun getLastLocation() {
        checkLocationPermission(this)
        if (!isLocationEnabled()) return

        // Show loading window
        mLoadingDialog.show()

        mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result // Wait until task has finished
            if (location == null)
                requestLocationUpdates() // Request new location
            else {
                val lastLoc = LatLng(location.latitude, location.longitude)
                debugLog("Last Location: ${lastLoc.latitude}, ${lastLoc.longitude}")
                mLoadingDialog.dismiss()
                onLocationAcquired(lastLoc)
            }
        }
    }

    /**
     * Request location updates from the client
     */
    private fun requestLocationUpdates() {
        checkLocationPermission(this)

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    /**
     * Called when new location becomes available
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lat = locationResult.lastLocation.latitude
            val long = locationResult.lastLocation.longitude
            debugLog("New Location: $lat, $long")
            if (mLoadingDialog.isShowing) mLoadingDialog.dismiss()
        }
    }

    /**
     * Returns true if GPS location is enabled
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
