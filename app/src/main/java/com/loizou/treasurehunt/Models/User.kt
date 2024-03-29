package com.loizou.treasurehunt.Models

import java.io.Serializable

data class User(
    var displayName: String = "no_name",
    var email: String = "no_email",
    var score: Int = 50, // 50 points upon account creation
    var userID: String = "no_ID",
    var completedGames: MutableList<TreasureHunt> = mutableListOf()
) : Serializable
