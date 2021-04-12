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
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class TreasureHuntListAdapter(
    var mTreasureHuntModelList: List<TreasureHunt>,
    var mCurrentLocation: Location
) : RecyclerView.Adapter<TreasureHuntListAdapter.ViewHolder>() {

    /**
     * Called whenever new item needs to be created
     * Inflate views using the row_layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Parent calling activity/class
        // Inflate a row_layout
        val treasureHuntView = inflater.inflate(R.layout.treasure_hunt_row_layout, parent, false)
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
            longitude = mTreasureHuntModelList[position].waypoints[0].longitude
        }

        val context = holder.itemView.context

        var bearing = mCurrentLocation.bearingTo(firstWaypointLocation)
        if (bearing < 0)
            bearing += 360

        val distance = mCurrentLocation.distanceTo(firstWaypointLocation)
        val direction = getBearingDirection(bearing)

        if (distance < 1000) {
            // Matter of meters
            holder.tvDistance.text =
                context.getString(R.string.distance_from_me, distance.roundToInt(), "m", direction)
        } else {
            // Matter of km
            val km = String.format("%.2f", distance/1000)
            holder.tvDistance.text = context.getString(R.string.distance_from_me_string, km, "km", direction)
        }
    }

    override fun getItemCount(): Int {
        return mTreasureHuntModelList.size
    }

    private fun getBearingDirection(bearing: Float): String {
        var direction: String
        if (bearing <= 22.5) {
            direction = "N"
        } else if (bearing <= 67.5) {
            direction = "NE"
        } else if (bearing <= 112.5) {
            direction = "E"
        } else if (bearing <= 157.5) {
            direction = "SE"
        } else if (bearing <= 202.5) {
            direction = "S"
        } else if (bearing <= 247.5) {
            direction = "SW"
        } else if (bearing <= 292.5) {
            direction = "W"
        } else if (bearing <= 337.5) {
            direction = "NW"
        } else if (bearing <= 360) {
            direction = "N"
        } else {
            direction = "Fourth dimension!?"
        }
        return direction
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