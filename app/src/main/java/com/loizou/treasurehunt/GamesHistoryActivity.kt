package com.loizou.treasurehunt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loizou.treasurehunt.Adapters.FoundGamesListAdapter
import com.loizou.treasurehunt.Data.UserSingleton

class GamesHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games_history)

        title = "Completed Games"

        val recViewGameHistory = findViewById<RecyclerView>(R.id.recViewGameHistory).apply {
            layoutManager = LinearLayoutManager(this@GamesHistoryActivity)
            adapter = FoundGamesListAdapter(UserSingleton.activeUser.completedGames)
        }

    }
}