package com.loizou.treasurehunt

import android.icu.text.DateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Data.UserSingleton

class GameStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_stats)

        debugLog("Loaded Games History Activity")
        title = "My Pirate Stats"

        // Set up values

        findViewById<MaterialTextView>(R.id.tvStats_username)
            .apply { text = UserSingleton.activeUser.displayName }

        findViewById<MaterialTextView>(R.id.tvStats_email)
            .apply { text = UserSingleton.activeUser.email }

        findViewById<MaterialTextView>(R.id.tvStats_Score)
            .apply { text = UserSingleton.activeUser.score.toString() }

        findViewById<MaterialTextView>(R.id.tvStats_TotalFinds)
            .apply { text = UserSingleton.activeUser.completedGames.size.toString() }

        findViewById<MaterialTextView>(R.id.tvStats_EasyCount)
            .apply { text = UserSingleton.activeUser.completedGames.count{ it.difficulty == "Easy" }.toString() }

        findViewById<MaterialTextView>(R.id.tvStats_MediumCount)
            .apply { text = UserSingleton.activeUser.completedGames.count{ it.difficulty == "Medium" }.toString() }

        findViewById<MaterialTextView>(R.id.tvStats_HardCount)
            .apply { text = UserSingleton.activeUser.completedGames.count{ it.difficulty == "Hard" }.toString() }

    }
}