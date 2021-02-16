package com.loizou.treasurehunt

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

object DataManager {

    private val TreasureHunts = ArrayList<TreasureHunt>()
    private val RandomWaypoints = listOf(
        Waypoint("Waypoint Swansea", LatLng(51.621441, -3.943646), false, true),
        Waypoint("Waypoint Cardiff", LatLng(51.481583, -3.179090), false, true),
        Waypoint("Waypoint Bournemouth", LatLng(50.718395, -1.883377), false, true),
        Waypoint("Waypoint St. Davids", LatLng(51.882000, -5.269000), false, true),
    )

    fun getTreasureHunts() : ArrayList<TreasureHunt> {
        return TreasureHunts
    }

    fun getTreasureHuntById(id: String) : TreasureHunt?{
        return TreasureHunts.find { it.id == id }
    }

    fun getWaypointById(id: String, treasureHunt: TreasureHunt) : Waypoint?{
        return treasureHunt.Waypoints.find { it.id == id }
    }

    init {
        TreasureHunts.addAll(listOf(
            TreasureHunt("Treasure Hunt 1", 2, "Joe Doe", RandomWaypoints),
            TreasureHunt("Treasure Hunt 2", 1, "Dalaras", RandomWaypoints))
        )
    }

}