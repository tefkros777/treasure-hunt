package com.loizou.treasurehunt

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.textview.MaterialTextView
import com.loizou.treasurehunt.Data.Database
import com.loizou.treasurehunt.Data.UserSingleton
import com.loizou.treasurehunt.Models.TreasureHunt
import com.loizou.treasurehunt.Models.Waypoint


class TreasureHuntPreviewActivity() : AppCompatActivity(), OnMapReadyCallback {

    private val ZOOM_LEVEL: Float = 15f
    private lateinit var mMap: GoogleMap
    private lateinit var mTreasureHunt: TreasureHunt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasure_hunt_preview)
        title = getString(R.string.th_preview)

        mTreasureHunt = Database.getTreasureHuntById(intent.getStringExtra("game_id")!!)!!
        debugLog("Loaded preview for ${mTreasureHunt.name}")

        // Make description scrollable
        findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_description).movementMethod =
            ScrollingMovementMethod()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapTreasureHuntPreview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        loadGameData()
    }

    fun loadGameData() {
        val tvName = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_name)
            .apply { text = mTreasureHunt.name }
        val tvAuthor = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_author)
            .apply { text = mTreasureHunt.author }
        val tvDesc = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_description)
            .apply { text = mTreasureHunt.description }
        val tvDiff = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_difficulty)
            .apply { text = mTreasureHunt.difficulty }
        val tvPoints = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_points)
            .apply { text = mTreasureHunt.points.toString() }
        val tvCost = findViewById<MaterialTextView>(R.id.tvTreasureHuntDetails_cost)
            .apply { text = mTreasureHunt.cost.toString() }
    }

    fun startTreasureHunt(view: View) {
        // If player doesn't have enough points to attend this game
        if (UserSingleton.activeUser.score < mTreasureHunt.cost){
            debugLog("Not enough points for this treasure hunt")

            val intent = Intent(this, CongratulationsActivity::class.java)
            intent.putExtra(CONGRATS_TITLE, getString(R.string.oh_no))
            intent.putExtra(CONGRATS_BODY, getString(R.string.not_enough_points))
            intent.putExtra(CONGRATS_BTN_TXT, getString(R.string.go_back))
            intent.putExtra(CONGRATS_IMG_SRC, R.drawable.skull)
            startActivityForResult(intent, NOT_ENOUGH_POINTS_REQ_CODE)
            return
        }

        // Deduct Pirate points
        UserSingleton.activeUser.score -= mTreasureHunt.cost
        Database.updateUserScore(UserSingleton.activeUser)

        val intent = Intent(this, TreasureHuntActivity::class.java)
        intent.putExtra("game_id", mTreasureHunt.id)
        this.startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        debugLog("Activity result called - Request code: $requestCode")
        when (requestCode){
            NOT_ENOUGH_POINTS_REQ_CODE ->{
                // Go back to dashboard
                val homeIntent = Intent(this, DashboardActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(homeIntent)
                finish()
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val group = LatLngBounds.Builder()
        for (wpt: Waypoint in mTreasureHunt.waypoints)
            group.include(wpt.getCoords())
        val area = group.build()
        drawTreasureHunt()
        mMap.uiSettings.setAllGesturesEnabled(false) // Disable all gestures
//        mMap.uiSettings.isZoomGesturesEnabled = false // Disable zooming
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(area!!, 25))
    }

    // Draw a line from all the waypoints of the treasure hunt
    private fun drawTreasureHunt() {
        val line = PolylineOptions()
        for (waypoint: Waypoint in mTreasureHunt.waypoints)
            line.add(waypoint.getCoords())
        line.width(10f).color(R.color.colorPrimary)
        mMap.addPolyline(line)
    }
}