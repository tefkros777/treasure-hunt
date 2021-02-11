package com.loizou.treasurehunt.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.LOG_TAG
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint
import com.loizou.treasurehunt.R
import com.loizou.treasurehunt.TreasureHuntActivity

class WaypointListAdapter(val mWaypointList: List<Waypoint>) : RecyclerView.Adapter<WaypointListAdapter.ViewHolder>() {

    /**
     * Called whenever new item needs to be created
     * Inflate views using the row_layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Parent calling activity/class
        // Inflate a row_layout
        val waypointView = inflater.inflate(R.layout.waypoint_row_layout, parent,false)
        return ViewHolder(waypointView)
    }

    /**
     * Populate data, feed information from the data source to the view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = mWaypointList[position].name
        holder.tvLat.text = "Latitude: " + mWaypointList[position].coords.latitude.toString()
        holder.tvLng.text = "Longtitude: " + mWaypointList[position].coords.longitude.toString()
    }

    override fun getItemCount(): Int {
        return mWaypointList.size
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvWaypointName)
        val tvLat = itemView.findViewById<MaterialTextView>(R.id.tvWaypointLat)
        val tvLng = itemView.findViewById<MaterialTextView>(R.id.tvWaypointLng)

        override fun onClick(v: View?) {
            // Use adapterPosition to get index of selected item
            Log.d(LOG_TAG, mWaypointList[adapterPosition].name + " clicked")
        }

        init {
            itemView.setOnClickListener(this)
        }

    }

}