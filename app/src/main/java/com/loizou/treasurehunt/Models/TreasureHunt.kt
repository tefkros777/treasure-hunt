package com.loizou.treasurehunt.Models

private var game_id = 0;

data class TreasureHunt(
    var name: String = "default name",
    var difficulty: Int = 0, // TODO: Make enum (LOW,MID,HARD)
    var author: String? = "nobody", // TODO: Make User
    var Waypoints: List<Waypoint> = emptyList(),
    var id: String = "TH_" + ++game_id,
    var description: String = "",
    var isSolved: Boolean = false
) {

    fun processWaypoints(){
        Waypoints.first().isVisible = true
        Waypoints.last().isFinal = true
        for (wpt: Waypoint in Waypoints)
            wpt.parentGameID = this.id
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

