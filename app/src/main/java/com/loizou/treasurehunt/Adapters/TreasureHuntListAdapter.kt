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
import com.google.android.material.imageview.ShapeableImageView
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
        val context = holder.itemView.context

        holder.tvName.text = mTreasureHuntModelList[position].name
        holder.tvDifficulty.text = context.getString(R.string.difficulty_with_placeholder, mTreasureHuntModelList[position].difficulty)
        holder.tvCost.text = context.getString(R.string.cost_with_placeholder, mTreasureHuntModelList[position].cost)
        holder.tvPoints.text = context.getString(R.string.available_points_with_placeholder, mTreasureHuntModelList[position].points)
        val firstWaypointLocation = Location("TreasureHunt_Start").apply {
            latitude = mTreasureHuntModelList[position].waypoints[0].latitude
            longitude = mTreasureHuntModelList[position].waypoints[0].longitude
        }

        var bearing = mCurrentLocation.bearingTo(firstWaypointLocation)
        if (bearing < 0)
            bearing += 360

        val distance = mCurrentLocation.distanceTo(firstWaypointLocation)
        val direction = getBearingDirection(bearing)

        // Select direction icon
        when(direction){
            "N" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_n)}
            "NE" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_ne)}
            "E" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_e)}
            "SE" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_se)}
            "S" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_s)}
            "SW" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_sw)}
            "W" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_w)}
            "NW" -> {holder.ivNavArrow.setImageResource(R.drawable.navigation_nw)}
            else -> {holder.ivNavArrow.setImageResource(R.drawable.pirate_hat_2)}
        }


        if (distance < 1000) {
            // Matter of meters
            holder.tvDistance.text = context.getString(R.string.distance_from_me, distance.roundToInt(), "m", direction)
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
        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvTHRow_Name)
        val tvDifficulty = itemView.findViewById<MaterialTextView>(R.id.tvTHRow_Difficulty)
        val tvDistance = itemView.findViewById<MaterialTextView>(R.id.tvTHRow_Distance)
        val tvCost = itemView.findViewById<MaterialTextView>(R.id.tvTHRow_Cost)
        val tvPoints = itemView.findViewById<MaterialTextView>(R.id.tvTHRow_Points)
        val ivNavArrow = itemView.findViewById<ShapeableImageView>(R.id.ivTHRow_Direction)

        override fun onClick(v: View?) {
            // Use adapterPosition to get index of selected item
            debugLog(mTreasureHuntModelList[adapterPosition].name + " clicked")

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