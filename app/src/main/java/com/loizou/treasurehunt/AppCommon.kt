package com.loizou.treasurehunt

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val DEBUG_TAG = "TREASURE_HUNT_APP_DEBUG: "
fun showMessage(v: View, msg: Int) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
fun showMessage(v: View, msg: String) = Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()