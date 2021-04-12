package com.loizou.treasurehunt.Models

import com.loizou.treasurehunt.*
import java.io.Serializable
import java.util.*

data class TreasureHunt(
    var name: String = "default name",
    var difficulty: String = "NOT_SET",
    var author: String? = "nobody",
    var waypoints: List<Waypoint> = emptyList(),
    var id: String = UUID.randomUUID().toString(),
    var description: String = "",
    var isSolved: Boolean = false
) : Serializable {

    var cost : Int = when(difficulty){
        "Easy" -> POINTS_EASY_PLAY
        "Medium" -> POINTS_MEDIUM_PLAY
        "Hard" -> POINTS_HARD_PLAY
        else -> 0
    }

    var points : Int = when(difficulty){
        "Easy" -> POINTS_EASY_FIND
        "Medium" -> POINTS_MEDIUM_FIND
        "Hard" -> POINTS_HARD_FIND
        else -> 0
    }

    fun processWaypoints(){
        waypoints.first().isVisible = true
        waypoints.last().isFinal = true
        for (wpt: Waypoint in waypoints)
            wpt.parentGameID = this.id.toString()
    }

    fun enableNextWaypoint(wpt: Waypoint){
        // Make next waypoint visible
        val wptIndex = waypoints.indexOf(wpt)
        if (wptIndex + 1 <= waypoints.size)
            waypoints[wptIndex + 1].isVisible = true;
    }
}

