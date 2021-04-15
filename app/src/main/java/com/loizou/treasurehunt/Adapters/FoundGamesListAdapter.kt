package com.loizou.treasurehunt.Adapters

import android.icu.text.DateFormat.getDateInstance
import android.icu.text.DateFormat.getDateTimeInstance
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.R
import com.loizou.treasurehunt.debugLog
import java.text.SimpleDateFormat

class FoundGamesListAdapter(
    var mFoundGames: List<TreasureHunt>
) : RecyclerView.Adapter<FoundGamesListAdapter.ViewHolder>() {

    /**
     * Called whenever new item needs to be created
     * Inflate views using the row_layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoundGamesListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Get inflater from calling activity/class
        // Inflate they layout
        val treasureHuntView = inflater.inflate(R.layout.past_games_row_layout, parent, false)
        return ViewHolder(treasureHuntView)
    }

    /**
     * Populate data, feed information from the data source to the view
     */
    override fun onBindViewHolder(holder: FoundGamesListAdapter.ViewHolder, position: Int) {
        val context = holder.itemView.context

        holder.tvName.text = mFoundGames[position].name
        holder.tvDifficulty.text = context.getString(R.string.difficulty_with_placeholder, mFoundGames[position].difficulty)
        holder.tvPoints.text = context.getString(R.string.points_gained_with_placeholder, mFoundGames[position].points)

        var foundDate = mFoundGames[position].solvedOn?.toDate()

        var formatted = "null"
        if (foundDate != null) formatted =  getDateInstance().format(foundDate).toString()

        holder.tvFoundoOn.text = context.getString(R.string.found_on_with_placeholder, formatted)
        when(mFoundGames[position].difficulty){
            "Easy" -> { holder.ivTrophy.setImageResource(R.drawable.trophy1_easy) }
            "Medium" -> { holder.ivTrophy.setImageResource(R.drawable.trophy1_med) }
            "Hard" -> { holder.ivTrophy.setImageResource(R.drawable.trophy1_hard) }
            else -> { holder.ivTrophy.setImageResource(R.drawable.pirate_parrot) }
        }
    }

    override fun getItemCount(): Int {
        return mFoundGames.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvFoundGame_Name)
        val tvDifficulty = itemView.findViewById<MaterialTextView>(R.id.tvFoundGame_Difficulty)
        val tvPoints = itemView.findViewById<MaterialTextView>(R.id.tvFoundGame_Points)
        val tvFoundoOn = itemView.findViewById<MaterialTextView>(R.id.tvFoundGame_FoundOn)
        val ivTrophy = itemView.findViewById<ShapeableImageView>(R.id.ivFoundGames_Trophie)

        override fun onClick(v: View?) {
            // Use adapterPosition to get index of selected item
            debugLog("Past game clicked")

        }

        init {
            itemView.setOnClickListener(this)
        }

    }
}