package com.loizou.treasurehunt.Models

import java.util.*

data class TreasureHunt(
    var name: String = "default name",
    var difficulty: String = "NOT_SET",
    var author: String? = "nobody", // TODO: Make User
    var waypoints: List<Waypoint> = emptyList(),
    var id: String = UUID.randomUUID().toString(),
    var description: String = "",
    var isSolved: Boolean = false
) {

    fun processWaypoints(){
        waypoints.first().isVisible = true
        waypoints.last().isFinal = true
        for (wpt: Waypoint in waypoints)
            wpt.parentGameID = this.id.toString()
    }

    fun enableNextWaypoint(wpt: Waypoint){
        // Make next waypoint visible
        val wptIndex = waypoints.indexOf(wpt)
        if (wpt.isFinal || wptIndex == waypoints.size-1){
            // There is no next waypoint
            this.isSolved = true
            return
        }
        if (wptIndex + 1 <= waypoints.size)
            waypoints[wptIndex + 1].isVisible = true;
    }
}

