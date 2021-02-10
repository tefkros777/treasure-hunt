package com.loizou.treasurehunt

import com.loizou.treasurehunt.Models.TreasureHunt

object DataManager {

    private val TreasureHunts = ArrayList<TreasureHunt>()

    fun getTreasureHunts() : ArrayList<TreasureHunt> {
        return TreasureHunts
    }

    fun getTreasureHuntById(id: String) : TreasureHunt?{
        return TreasureHunts.find { it.id == id }
    }

    init {
        TreasureHunts.addAll(listOf(
            TreasureHunt("Treasure Hunt 1", 2, "Joe Doe"),
            TreasureHunt("Treasure Hunt 2", 1, "Dalaras"))
        )
    }

}