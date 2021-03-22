package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.debugLog

private var waypoint_id = 0

data class Waypoint(
    val name: String,
    val coords: LatLng,
    var solution: String,
    var tasks: String = "",
    var description: String = ""
) {
    val id: String = "WP_" + ++waypoint_id
    var isVisible = false
    var isFinal = false
    var isSolved = false
    lateinit var parentGame: TreasureHunt

    fun attemptSolve(solution: String): Boolean {
        return if (checkSolution(solution)) {
            debugLog("Waypoint ${this.name} solved")
            this.isSolved = true
            parentGame.enableNextWaypoint(this)
            true
        } else {
            false
        }

    }

    private fun checkSolution(solution: String): Boolean {
        return solution.equals(this.solution, true)
    }

}
