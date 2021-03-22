package com.loizou.treasurehunt

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint

object Database {
    private var mTreasureHunts = ArrayList<TreasureHunt>()
    val db = Firebase.firestore

    fun getTreasureHuntById(id: String) : TreasureHunt?{
        return mTreasureHunts.find { it.id == id }
    }

    fun getTreasureHunts() : ArrayList<TreasureHunt>{
        return mTreasureHunts
    }

    fun fetchTreasureHunts(){
        db.collection("treasure_hunts")
            .get()
            .addOnSuccessListener { result ->
                debugLog("Fetched ${result.documents.size} games from the database")
                for (document in result) {
                    Log.d(LOG_TAG, "${document.id} => ${document.data}")
                    val foo = document.toObject(TreasureHunt::class.java)
                    foo.toString()
                }
            }
            .addOnFailureListener { exception ->
                debugLog("Error while fetching games from the database")
                debugLog(exception.toString())
            }
    }

    init {
        // Fetch treasure hunts
        fetchTreasureHunts()

    }

    fun seedTestData(){
        var randomWaypoints = listOf(
            Waypoint("Waypoint Swansea", 51.621441, -3.943646, "solution"),
            Waypoint("Waypoint Cardiff", 51.481583, -3.179090, "solution"),
            Waypoint("Waypoint Bournemouth", 50.718395, -1.883377, "solution"),
            Waypoint("Waypoint St. Davids", 51.882000, -5.269000, "solution"))

        val th1 = TreasureHunt("A very exciting hunt", 2, "Joe Doe", randomWaypoints)

        mTreasureHunts.addAll(listOf(th1))

        // Perform waypoint post-processing
        for (th : TreasureHunt in mTreasureHunts)
            th.processWaypoints()

        // Add a new document with a generated ID
        db.collection("treasure_hunts")
            .add(th1)
            .addOnSuccessListener { documentReference ->
                Log.d(LOG_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(LOG_TAG, "Error adding document", e)
            }
    }

}