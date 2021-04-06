package com.loizou.treasurehunt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.enter_coords_dialog.view.*
import kotlinx.android.synthetic.main.solve_waypoint_dialog_layout.view.*
import kotlinx.android.synthetic.main.solve_waypoint_dialog_layout.view.btnDialogCancel

private const val ZOOM_LEVEL: Float = 15f

class AddWaypointActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mRootLayout : ViewGroup
    private lateinit var mWaypointCoords : LatLng

    private var WAYPOINT_NUM = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_waypoint)

        mRootLayout = findViewById(R.id.vgAddWaypointRootLayout)
        title = getString(R.string.waypoint_num, ++WAYPOINT_NUM)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapWaypointLocationPreview) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    fun addLocationManually(v: View) {
        showMessage(v, "Add location manually")

        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.enter_coords_dialog, null)
        // AlertDialogBuilder
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.enter_coords)
        val alertDialog = dialogBuilder.show()

        // Solve button of the dialog
        dialogView.btnDialogDone.setOnClickListener {

            mWaypointCoords = LatLng(
                dialogView.tvCoordsLat.text!!.trim().toString().toDouble(),
                dialogView.tvCoordsLong.text!!.trim().toString().toDouble()
            )

            // Animate map to the coords location
            val wptMarker = MarkerOptions()
                .position(mWaypointCoords)
                .title(getString(R.string.waypoint_coords))
            mMap.addMarker(wptMarker)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(wptMarker.position, ZOOM_LEVEL))

            showMessage(mRootLayout, "Coordinates set to${mWaypointCoords.latitude}, ${mWaypointCoords.longitude}")

            alertDialog.dismiss()
        }


        // Cancel button of the dialog
        dialogView.btnDialogCancel.setOnClickListener { alertDialog.dismiss() }
    }

    fun setCurrentLocation(v: View) {
        showMessage(v, "Set to current location")
    }

    fun saveWaypoint(v: View) {
        // TODO: Save waypoint and ask whether to add another one or finish via popup
        // check if all properties are set before proceeding
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.uiSettings.setAllGesturesEnabled(false) // Disable zooming
        checkLocationPermission(this)
        mMap.isMyLocationEnabled = true
        debugLog("Add Waypoint Activity: Map Ready")
        // do stuff ...
    }

}
