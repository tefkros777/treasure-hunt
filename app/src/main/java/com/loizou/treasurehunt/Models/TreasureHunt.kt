package com.loizou.treasurehunt.Models

data class TreasureHunt (
        val name: String,
        val difficulty: Int, // TODO: Make enum (LOW,MID,HARD)
        val author: String? // TODO: Make User
        )