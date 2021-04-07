package com.loizou.treasurehunt.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.*
import com.loizou.treasurehunt.Models.Waypoint

class WaypointListAdapter(val mWaypointList: MutableList<Waypoint>, val mCallingActivity: Activity, val listener: (Waypoint) -> Unit) :
    RecyclerView.Adapter<WaypointListAdapter.ViewHolder>() {

    /**
     * Called whenever new item needs to be created
     * Inflate views using the row_layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Parent calling activity/class
        // Inflate a row_layout
        val waypointView = inflater.inflate(R.layout.waypoint_row_layout, parent, false)
        return ViewHolder(waypointView)
    }

    /**
     * Populate data, feed information from the data source to the view
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mWaypointList[position]
        holder.bind(item)
        holder.waypointRow.setOnClickListener { listener(item) }
        holder.btnShowDetails.setOnClickListener {
            val intent = Intent(mCallingActivity, WaypointDetails::class.java)
            intent.putExtra("waypoint_id", mWaypointList[position].id)
            intent.putExtra("game_id", mWaypointList[position].parentGameID)
            /**
             * When secondary activity finishes, the onActivityResult method of the mCallingActivity
             * class will be called
             */
            mCallingActivity.startActivityForResult(intent, WPT_DETAILS_REQ_CODE)
        }
    }

    override fun getItemCount(): Int {
        return mWaypointList.size
    }

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<MaterialTextView>(R.id.tvWaypointRowLayout_Name)
        val tvLat = itemView.findViewById<MaterialTextView>(R.id.tvWaypointLat)
        val tvLng = itemView.findViewById<MaterialTextView>(R.id.tvWaypointLng)
        val waypointRow = itemView.findViewById<ViewGroup>(R.id.waypointDetailsLayout)
        val btnShowDetails = itemView.findViewById<MaterialButton>(R.id.btnShowWaypointDetails)

        fun bind(item: Waypoint) {
            tvName.text = item.name
            tvLat.text = "Latitude: " + item.latitude.toString()
            tvLng.text = "Longtitude: " + item.longitude.toString()
        }

    }

}