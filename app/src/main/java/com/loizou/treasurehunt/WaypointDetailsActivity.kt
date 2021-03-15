package com.loizou.treasurehunt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint
import kotlinx.android.synthetic.main.activity_waypoint_details.*
import kotlinx.android.synthetic.main.solve_waypoint_dialog_layout.*
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

        if (mWaypoint.isSolved)
            btnSolveWaypoint.visibility = View.GONE

        findViewById<MaterialTextView>(R.id.tvWaypointDetails_description).movementMethod = ScrollingMovementMethod()

    }

    /**
     * Linked to btnSolveWaypoint
     */
    fun solveWaypoint(v: View) {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.solve_waypoint_dialog_layout, null)
        // AlertDialogBuilder
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(R.string.can_you_solve_it)
        val alertDialog = dialogBuilder.show()

        // Solve button of the dialog
        dialogView.btnDialogSolve.setOnClickListener {
            var userSolution = dialogView.tvPuzzleSolution.text!!.trim().toString()
            showMessage(dialogView, userSolution)

            userSolution = "solution" // TODO: FOR TESTING ONLY, DELETE AFTERWARDS

            // Attempt to solve the puzzle
            val attempt = mWaypoint.attemptSolve(userSolution)
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

            alertDialog.dismiss()
        }
        // Cancel button of the dialog
        dialogView.btnDialogCancel.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    fun navigateToWaypoint(v: View) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val intentURI =
            Uri.parse("http://maps.google.com/maps?daddr=${mWaypoint.coords.latitude},${mWaypoint.coords.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentURI)
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}