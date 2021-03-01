package com.loizou.treasurehunt

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint


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
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.solve_waypoint_dialog_layout)
        val tvTitle = dialog.findViewById(R.id.tvTitle) as MaterialTextView
        val tvInput = dialog.findViewById(R.id.tvInput) as TextInputEditText
        val btnAttemptSolve = dialog.findViewById(R.id.btnAttemptSolve) as MaterialButton
        val btnCancelSolve = dialog.findViewById(R.id.btnCancelSolve) as MaterialButton
        btnAttemptSolve.setOnClickListener {
            dialog.dismiss()
        }
        btnCancelSolve.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}