package com.loizou.treasurehunt.Models

import com.loizou.treasurehunt.debugLog

private var game_id = 0;

data class TreasureHunt(
    val name: String,
    val difficulty: Int, // TODO: Make enum (LOW,MID,HARD)
    val author: String?, // TODO: Make User
    val Waypoints: List<Waypoint>,
    val id: String = "TH_" + ++game_id
) {
    fun solveWaypoint(wpt: Waypoint) {
        debugLog("Waypoint ${wpt.name} solved")
        wpt.solved = true
        // Make next waypoint visible
        val wptIdex = Waypoints.indexOf(wpt)
        Waypoints[wptIdex + 1].isVisible = true;
    }
}

