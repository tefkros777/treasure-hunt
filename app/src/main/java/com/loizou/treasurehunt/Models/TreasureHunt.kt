package com.loizou.treasurehunt.Models

import com.loizou.treasurehunt.debugLog

private var game_id = 0;

data class TreasureHunt(
    val name: String,
    val difficulty: Int, // TODO: Make enum (LOW,MID,HARD)
    val author: String?, // TODO: Make User
    val Waypoints: List<Waypoint>,
    val id: String = "TH_" + ++game_id,
    var description: String = "",
    var isSolved: Boolean = false
) {

    fun processWaypoints(){
        Waypoints.first().isVisible = true
        Waypoints.last().isFinal = true
    }

    fun enableNextWaypoint(wpt: Waypoint){
        // Make next waypoint visible
        val wptIndex = Waypoints.indexOf(wpt)
        if (wpt.isFinal || wptIndex == Waypoints.size-1){
            // There is no next waypoint
            this.isSolved = true
            return
        }
        if (wptIndex + 1 <= Waypoints.size)
            Waypoints[wptIndex + 1].isVisible = true;
    }
}

