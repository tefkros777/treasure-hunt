package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.debugLog

private var waypoint_id = 0

data class Waypoint(
    val parent_game: TreasureHunt,
    val name: String,
    val coords: LatLng,
    var solution: String,
    val id: String = "WP_" + ++waypoint_id,
    var description: String = "",
    var tasks: String = ""

) {
    var isVisible = false
    var isFinal = false
    var isSolved = false

    fun attemptSolve(solution: String): Boolean {
        return if (checkSolution(solution)) {
            debugLog("Waypoint ${this.name} solved")
            this.isSolved = true
            parent_game.enableNextWaypoint(this)
            true
        } else {
            false
        }

    }

    private fun checkSolution(solution: String): Boolean {
        return solution.equals(this.solution, true)
    }

}
