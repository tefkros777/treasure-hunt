package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng

data class Waypoint(
    val name: String,
    val coords: LatLng,
    val isFinal: Boolean,
    val isVisible: Boolean = false
)
