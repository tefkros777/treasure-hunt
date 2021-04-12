package com.loizou.treasurehunt.Adapters

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.*
import com.loizou.treasurehunt.Models.TreasureHunt
import kotlin.properties.Delegates

class TreasureHuntListAdapter(var mTreasureHuntModelList: List<TreasureHunt>, var mCurrentLocation: Location) : RecyclerView.Adapter<TreasureHuntListAdapter.ViewHolder>() {

//    var mCurrentLocation by Delegates.observable(LatLng(0.0,0.0)) { property, oldValue, newValue ->
//        debugLog("New Value $newValue")
//        debugLog("Old Value $oldValue")
//    }

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
        val firstWaypointLocation = Location("TreasureHunt_Start").apply {
            latitude = mTreasureHuntModelList[position].waypoints[0].latitude
            longitude =mTreasureHuntModelList[position].waypoints[0].longitude
        }
        val distance = mCurrentLocation.distanceTo(firstWaypointLocation)

        holder.tvDistance.text = "Distance from me: $distance"
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

            val intent = Intent(itemView.context, TreasureHuntPreviewActivity::class.java)
            intent.putExtra("game_id", mTreasureHuntModelList[adapterPosition].id)
            // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            itemView.context.startActivity(intent)

        }

        init {
            itemView.setOnClickListener(this)
        }

    }

}