package com.loizou.treasurehunt

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar

// Debugging
const val LOG_TAG = "TREASURE_HUNT_APP_DEBUG: "
const val DB_LOG_TAG = "TREASURE_HUNT_APP_DATABASE: "

// Request codes
const val LOCATION_PERMISSION_REQ_CODE = 42
const val WPT_DETAILS_REQ_CODE: Int = 1
const val FINISH_GAME_REQ_CODE: Int = 2
const val PUBLISH_TH_REQ_CODE: Int = 3
const val NOT_ENOUGH_POINTS_REQ_CODE: Int = 4

// Intents
const val CONGRATS_TITLE: String = "congrats_title"
const val CONGRATS_BODY: String = "congrats_body"
const val CONGRATS_IMG_SRC: String = "congrats_img_src"
const val CONGRATS_BTN_TXT: String = "congrats_btn_text"

// Fixed values
const val POINTS_BURY_TREASURE: Int = 20
const val POINTS_EASY_PLAY: Int = 5
const val POINTS_EASY_FIND: Int = 6
const val POINTS_MEDIUM_PLAY: Int = 10
const val POINTS_MEDIUM_FIND: Int = 12
const val POINTS_HARD_PLAY: Int = 15
const val POINTS_HARD_FIND: Int = 20


fun showMessage(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
fun showMessage(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show()
fun showMessageLong(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun showMessageLong(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun debugLog(msg: String) = Log.d(LOG_TAG, msg)
fun databaseLog(msg: String) = Log.d(DB_LOG_TAG, msg)

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

// Extension functions
fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()
fun Float.round(decimals: Int = 2): Float = "%.${decimals}f".format(this).toFloat()
