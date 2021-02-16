package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng

private var waypoint_id = 0

data class Waypoint(
    val name: String,
    val coords: LatLng,
    val isFinal: Boolean,
    val isVisible: Boolean = false,
    val sloved: Boolean = false,
    val id: String = "WP_" + ++waypoint_id,
    var description : String = ""

)
