package com.loizou.treasurehunt

import android.Manifest
import android.content.pm.PackageManager
import android.nfc.Tag
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

// Debugging
const val LOG_TAG = "TREASURE_HUNT_APP_DEBUG: "

// Request codes
const val LOCATION_PERMISSION_REQ_CODE = 42
const val WPT_DETAILS_REQ_CODE: Int = 1

// Intents
const val ACTIVE_USER_FLAG: String = "active_user"

fun showMessage(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
fun showMessage(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
fun showMessageLong(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun showMessageLong(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun debugLog(msg: String) = Log.d(LOG_TAG, msg)

/**
 * Checks if location permission is granted and requests it if it's not
 */
fun checkLocationPermission(callingActivity: AppCompatActivity) {
    // Check for location permission
    if (ActivityCompat.checkSelfPermission(
            callingActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Request location permission
        ActivityCompat.requestPermissions(
            callingActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQ_CODE
        )
        return
    }
}