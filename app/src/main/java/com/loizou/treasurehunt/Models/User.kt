package com.loizou.treasurehunt.Models

import java.io.Serializable

data class User(
    var displayName: String = "no_name",
    var email: String = "no_email",
    var score: Int = -1,
    var userID: String = "no_ID"
) : Serializable
