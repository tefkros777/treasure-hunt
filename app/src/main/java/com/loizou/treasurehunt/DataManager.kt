package com.loizou.treasurehunt

import com.loizou.treasurehunt.Models.TreasureHunt

object DataManager {
    fun getTreasureHunts() : List<TreasureHunt>{
        return listOf(
            TreasureHunt("Treasure Hunt 1", 2, "Joe Doe"),
            TreasureHunt("Treasure Hunt 2", 1, "Dalaras")
        )

    }
}