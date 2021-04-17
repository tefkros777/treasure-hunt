package com.loizou.treasurehunt

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.loizou.treasurehunt.Adapters.TreasureHuntListAdapter
import com.loizou.treasurehunt.Data.Database
import com.loizou.treasurehunt.Models.TreasureHunt
import java.util.*

class TreasureHuntSelectionActivity : AppCompatActivity(), Observer {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mCurrentLocation: Location
    private lateinit var mAdapter: TreasureHuntListAdapter
    private lateinit var mLoadingDialog: AlertDialog

    private val LOCATION_UPDATE_INTERVAL: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_selection)

        title = "Select a treasure hunt"

        // Location is initially set to point (0,0)
        mCurrentLocation = Location("location_not_yet_known").apply { latitude = 0.0; longitude = 0.0 }

        val mRecViewTreasureHuntsList = findViewById<RecyclerView>(R.id.recViewTreasureHunts).apply {
            layoutManager = LinearLayoutManager(this@TreasureHuntSelectionActivity)
            adapter = TreasureHuntListAdapter(listOf(), mCurrentLocation) // List is initially empty
        }
        mAdapter = mRecViewTreasureHuntsList.adapter as TreasureHuntListAdapter

        // GPS Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationUpdates()

        // Configure loading dialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .setTitle(R.string.getting_current_location)
        mLoadingDialog = dialogBuilder.create()
        mLoadingDialog.show()

        Database.addObserver(this)
        Database.fetchTreasureHunts()

        // The adapter takes care of launching the TreasureHuntDetailsActivity once a selection is made
    }

    // SUBSCRIBE TO DATABASE EVENT AND AWAIT FETCHING COMPLETION
    override fun update(o: Observable?, arg: Any?) {
        when (o) {
            // Treasure hunt list is ready/updated
            is Database -> {
                val thList = arg as List<TreasureHunt>
                mAdapter.mTreasureHuntModelList = thList
                mAdapter.notifyDataSetChanged()
            }
            else -> println(o?.javaClass.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        Database.deleteObserver(this)

    }

    /**
     * Request location updates from the client
     */
    private fun requestLocationUpdates() {
        checkLocationPermission(this)

        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_UPDATE_INTERVAL

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
            if (mLoadingDialog.isShowing)
                mLoadingDialog.dismiss()

            mCurrentLocation = locationResult.lastLocation
            debugLog("New Location: ${mCurrentLocation.latitude}, ${mCurrentLocation.longitude}")

            // Update the location parameter of the adapter
            mAdapter.mCurrentLocation = mCurrentLocation
            mAdapter.notifyDataSetChanged()
        }
    }

}