package com.loizou.treasurehunt

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

object DataManager {

    private val TreasureHunts = ArrayList<TreasureHunt>()

    fun getTreasureHunts() : ArrayList<TreasureHunt> {
        return TreasureHunts
    }

    fun getTreasureHuntById(id: String) : TreasureHunt?{
        return TreasureHunts.find { it.id == id }
    }

    fun getWaypointById(id: String, treasureHunt: TreasureHunt) : Waypoint?{
        return treasureHunt.Waypoints.find { it.id == id }
    }

    fun getWaypointById(id: String) : Waypoint?{
        for (hunt in TreasureHunts){
            for (wpt in hunt.Waypoints){
                if (wpt.id == id){
                    return wpt
                }
            }
        }
        debugLog("Could not find waypoint with waypoint it $id")
        return null
    }

    init {

        //TODO: This is a bug waiting to happen!!

//        var randomWaypoints = listOf(
//            Waypoint("Waypoint Swansea", LatLng(51.621441, -3.943646), "solution"),
//            Waypoint("Waypoint Cardiff", LatLng(51.481583, -3.179090), "solution"),
//            Waypoint("Waypoint Bournemouth", LatLng(50.718395, -1.883377), "solution"),
//            Waypoint("Waypoint St. Davids", LatLng(51.882000, -5.269000), "solution"),
//
//        )
//        TreasureHunts.addAll(listOf(
//            TreasureHunt("A very exciting hunt", 2, "Joe Doe", randomWaypoints)
//        ))
//
//        for (th : TreasureHunt in TreasureHunts)
//            th.processWaypoints()
    }

}