package com.loizou.treasurehunt.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.R

class TreasureHuntListAdapter(val mTreasureHuntModelList: List<TreasureHunt>) : RecyclerView.Adapter<TreasureHuntListAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvHuntName)
        val tvDifficulty = itemView.findViewById<MaterialTextView>(R.id.tvHuntDifficulty)
        val tvDistance = itemView.findViewById<MaterialTextView>(R.id.tvHuntDistanceFromMe)
    }

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
        holder.tvDifficulty.text = "Difficulty: " + mTreasureHuntModelList[position].difficulty.toString()
        holder.tvDistance.text = "Distance from me: RANDOM DISTANCE"
    }

    override fun getItemCount(): Int {
        return mTreasureHuntModelList.size
    }
}