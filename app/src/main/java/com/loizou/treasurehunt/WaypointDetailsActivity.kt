package com.loizou.treasurehunt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint
import kotlinx.android.synthetic.main.solve_waypoint_dialog_layout.view.*


class WaypointDetails : AppCompatActivity() {

    private lateinit var mTreasureHunt: TreasureHunt
    private lateinit var mWaypoint: Waypoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waypoint_details)

        mWaypoint = DataManager.getWaypointById(intent.getStringExtra("waypoint_id")!!)!!
        mTreasureHunt = mWaypoint.parent_game

        title = mWaypoint.name

    }

    /**
     * Linked to btnSolveWaypoint
     */
    fun solveWaypoint(v: View) {
        // Read solution from user
      //  showDialog()

        val attempt = mWaypoint.attemptSolve("solution")
        if (attempt) {
            // Solve attempt successful
            val data = Intent()
            data.putExtra("waypoint_index", mTreasureHunt.Waypoints.indexOf(mWaypoint))
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            // Solve attempt unsuccessful
            val btnSolve = findViewById<MaterialButton>(R.id.btnSolveWaypoint)
            showMessage(btnSolve, R.string.try_again)
        }
    }


    fun showDialog(v: View) {
        //Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.solve_waypoint_dialog_layout, null)
        //AlertDialogBuilder
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.can_you_solve_it)

        val  alertDialog = dialogBuilder.show()
        //login button click of custom layout
        dialogView.btnAttemptSolve.setOnClickListener {
            //dismiss dialog
            alertDialog.dismiss()

        }
        // Cancel button
        dialogView.btnAttemptCancel.setOnClickListener {
            alertDialog.dismiss()
        }

    }


}