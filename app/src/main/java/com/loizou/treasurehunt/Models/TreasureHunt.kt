package com.loizou.treasurehunt.Models

private var game_id = 0;

data class TreasureHunt (
        val name: String,
        val difficulty: Int, // TODO: Make enum (LOW,MID,HARD)
        val author: String?, // TODO: Make User
        val Waypoints: List<Waypoint>,
        val id: String = "TH_" + ++game_id
)