package com.loizou.treasurehunt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class TreasureHuntActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTreasureHunt) as SupportMapFragment
        mapFragment.getMapAsync(this)

        title = "Swansea Marina Exploration"

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //requestLocationData()
        getLastLocation()

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

        // Add a marker in Swansea
        val meridianTower = LatLng(51.61387, -3.94321)

        val zoomLevel = 12.0f

        mMap.addMarker(MarkerOptions().position(meridianTower).title("Meridian Tower"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(meridianTower, zoomLevel))
    }

    /**
     * Request new high accuracy location
     */
    private fun requestLocationData() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10
        locationRequest.fastestInterval = 0

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // Request location permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    /**
     * Returns true if either GPS location or Network location is enabled
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation : Location = locationResult.lastLocation
            val lat = lastLocation.latitude
            val long = lastLocation.longitude

            Log.i(LOG_TAG, "Current Location: $lat, $long")

            val lastLoc = LatLng(lat, long)
            mMap.addMarker(MarkerOptions().position(lastLoc).title("Me"))
            mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLoc))

        }
    }

    /**
     * Return last location of the client. Won't update if location is changed until called again
     */
    private fun getLastLocation(){
        if (isLocationEnabled()){
            // Check for location permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                // Request location permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
                return
            }
            fusedLocationClient.lastLocation.addOnCompleteListener(this){ task ->
                val location: Location? = task.result // Wait until task has finished
                if (location == null)
                    requestLocationData() // Request new location
                else {
                    val lat = location.latitude
                    val long = location.longitude
                    Log.i(LOG_TAG, "Last Location: $lat, $long")

                    val lastLoc = LatLng(lat, long)

                    // Move map to last location
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLoc))

                    // Add marker to last location
                    mMap.addMarker(MarkerOptions().position(lastLoc).title("My last location"))

                }
            }
        }
    }

}