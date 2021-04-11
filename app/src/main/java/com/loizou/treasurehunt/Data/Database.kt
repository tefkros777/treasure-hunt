package com.loizou.treasurehunt.Data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.loizou.treasurehunt.LOG_TAG
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.User
import com.loizou.treasurehunt.Models.Waypoint
import com.loizou.treasurehunt.databaseLog
import com.loizou.treasurehunt.debugLog
import java.util.*
import kotlin.collections.ArrayList

object Database : Observable() {
    private var mTreasureHunts = ArrayList<TreasureHunt>()
    val TREASURE_HUNT_COLLECTION_PATH = "treasure_hunts"

    val USER_SCORE_DOCUMENT = "score"
    val USERS_COLLECTION_PATH = "users"

    val db = Firebase.firestore

    fun getTreasureHuntById(id: String): TreasureHunt? {
        return mTreasureHunts.find { it.id.toString() == id }
    }

    fun getTreasureHunts(): ArrayList<TreasureHunt> {
        return mTreasureHunts
    }

    fun getWaypointById(id: String): Waypoint? {
        for (hunt in mTreasureHunts) {
            for (wpt in hunt.waypoints) {
                if (wpt.id.toString() == id) {
                    return wpt
                }
            }
        }
        databaseLog("Could not find waypoint with waypoint it $id")
        return null
    }

    // Get Treasure Hunts from the DB
    fun fetchTreasureHunts() {
        val thCollection = db.collection("treasure_hunts")
        thCollection.get().addOnCompleteListener {task ->

            if (task.isSuccessful) {
                databaseLog("Fetched ${task.result?.documents?.size} games")

                for (document in task.result!!) {
                    // Deserialize every document into a TreasureHunt object
                    val deserializedDoc = document.toObject(TreasureHunt::class.java)
                    // Add treasure hunts to mTreasureHunts only if they don't already exist
                    if (!mTreasureHunts.contains(deserializedDoc))
                        mTreasureHunts.add(deserializedDoc)
                }

                setChanged()
                notifyObservers(mTreasureHunts)

            } else {
                databaseLog("Error while fetching games")
                databaseLog(task.exception.toString())
            }
        }
    }

    init {
        // Fetch treasure hunts
        // fetchTreasureHunts()
        // seedTestData()
    }

    fun seedTestData() {
        var randomWaypoints = listOf(
            Waypoint("Waypoint Swansea", 51.621441, -3.943646, "solution"),
            Waypoint("Waypoint Cardiff", 51.481583, -3.179090, "solution"),
            Waypoint("Waypoint Bournemouth", 50.718395, -1.883377, "solution"),
            Waypoint("Waypoint St. Davids", 51.882000, -5.269000, "solution")
        )

        val th1 = TreasureHunt("The first second attempt", "Easy", "Marium Mosbi", randomWaypoints)

        mTreasureHunts.addAll(listOf(th1))

        // Perform waypoint post-processing
        for (th: TreasureHunt in mTreasureHunts)
            th.processWaypoints()

        addTreasureHunt(th1)

    }

    fun addTreasureHunt(treasureHunt: TreasureHunt) {
        // Add TreasureHunt to firestore
        db.collection(TREASURE_HUNT_COLLECTION_PATH)
            .add(treasureHunt)
            .addOnSuccessListener { documentReference ->
                databaseLog("DocumentSnapshot added with ID: ${documentReference.id}")
                databaseLog("Treasure Hunt uploaded successfully")
            }
            .addOnFailureListener { e ->
                databaseLog("Error adding document - $e")
            }
    }

    /**
     * Add user as an encapsulated object into the database
     */
    fun addUserToDatabase(user: User){
        db.collection(USERS_COLLECTION_PATH)
            .document(user.email)
            .set(user)
    }

    fun updateUserScore(user: User) {
        db.collection(USERS_COLLECTION_PATH)
            .document(user.email)
            .update(USER_SCORE_DOCUMENT, user.score)
            .addOnSuccessListener { databaseLog("Successfully updated user score") }
            .addOnFailureListener { e -> databaseLog("Failed to update user score - $e") }
    }
}