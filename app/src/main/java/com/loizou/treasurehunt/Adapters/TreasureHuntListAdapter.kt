package com.loizou.treasurehunt.Adapters

import android.content.Intent
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.*
import com.loizou.treasurehunt.Models.TreasureHunt

class TreasureHuntListAdapter(val mTreasureHuntModelList: List<TreasureHunt>) : RecyclerView.Adapter<TreasureHuntListAdapter.ViewHolder>() {

    /**
     * Called whenever new item needs to be created
     * Inflate views using the row_layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Parent calling activity/class
        // Inflate a row_layout
        val treasureHuntView = inflater.inflate(R.layout.treasure_hunt_row_layout, parent,false)
        return ViewHolder(treasureHuntView)
    }

    /**
     * Populate data, feed information from the data source to the view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = mTreasureHuntModelList[position].name
        holder.tvDifficulty.text = mTreasureHuntModelList[position].difficulty
        holder.tvDistance.text = "Distance from me: RANDOM DISTANCE"
    }

    override fun getItemCount(): Int {
        return mTreasureHuntModelList.size
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvHuntName)
        val tvDifficulty = itemView.findViewById<MaterialTextView>(R.id.tvHuntDifficulty)
        val tvDistance = itemView.findViewById<MaterialTextView>(R.id.tvHuntDistanceFromMe)

        override fun onClick(v: View?) {
            // Use adapterPosition to get index of selected item
            Log.d(LOG_TAG, mTreasureHuntModelList[adapterPosition].name + " clicked")

            val intent = Intent(itemView.context, TreasureHuntDetailsActivity::class.java)
            intent.putExtra("game_id", mTreasureHuntModelList[adapterPosition].id)
            // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            itemView.context.startActivity(intent)

        }

        init {
            itemView.setOnClickListener(this)
        }

    }

}