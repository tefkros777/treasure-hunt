package com.loizou.treasurehunt.Models

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.debugLog
import java.io.Serializable
import java.util.*

data class Waypoint(
    var name: String = "waypoint",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var solution: String = "mystery",
    var tasks: String = "",
    var description: String = ""
) : Serializable {
    var id: String = UUID.randomUUID().toString()
    var isVisible = false
    var isFinal = false
    var isSolved = false
    var parentGameID = ""

    fun getCoords(): LatLng {
        return LatLng(latitude, longitude)
    }

    fun attemptSolve(solution: String): Boolean {
        return if (checkSolution(solution)) {
            debugLog("Waypoint ${this.name} solved")
            this.isSolved = true
            true
            // Next waypoint should be enabled in the calling class/activity of this method
            // via calling treasurehunt.enableNextWaypoint(wpt: Waypoint)
        } else {
            false
        }
    }

    private fun checkSolution(solution: String): Boolean {
        return solution.equals(this.solution, true)
    }

}
