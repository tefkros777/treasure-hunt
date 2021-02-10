package com.loizou.treasurehunt

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val LOG_TAG = "TREASURE_HUNT_APP_DEBUG: "
const val LOCATION_PERMISSION_REQ_CODE = 42
fun showMessage(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun showMessage(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()