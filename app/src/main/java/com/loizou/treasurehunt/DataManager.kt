package com.loizou.treasurehunt

import com.google.android.gms.maps.model.LatLng
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

object DataManager {

    private val TreasureHunts = ArrayList<TreasureHunt>()
    private val RandomWaypoints = ArrayList<Waypoint>()

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
        TreasureHunts.addAll(listOf(
            TreasureHunt("A very exciting hunt", 2, "Joe Doe", RandomWaypoints)
        ))

        RandomWaypoints.addAll(listOf(
            Waypoint(TreasureHunts.first(),"Waypoint Swansea", LatLng(51.621441, -3.943646), false, true),
            Waypoint(TreasureHunts.first(),"Waypoint Cardiff", LatLng(51.481583, -3.179090), false, false),
            Waypoint(TreasureHunts.first(),"Waypoint Bournemouth", LatLng(50.718395, -1.883377), false, false),
            Waypoint(TreasureHunts.first(),"Waypoint St. Davids", LatLng(51.882000, -5.269000), false, false),
        ))

    }

}