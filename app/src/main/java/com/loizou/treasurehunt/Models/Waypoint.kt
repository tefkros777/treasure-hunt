package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng

private var waypoint_id = 0

data class Waypoint(
    val parent_game: TreasureHunt,
    val name: String,
    val coords: LatLng,
    val isFinal: Boolean,
    var isVisible: Boolean = false,
    var solved: Boolean = false,
    val id: String = "WP_" + ++waypoint_id,
    var description : String = "",
    var tasks : String = ""

){
    fun solve(){
        parent_game.solveWaypoint(this)
    }
}
