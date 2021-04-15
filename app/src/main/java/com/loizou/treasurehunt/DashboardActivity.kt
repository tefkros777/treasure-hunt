package com.loizou.treasurehunt

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.loizou.treasurehunt.Data.UserSingleton

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var mDrawer: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle

    private var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        debugLog("Main Activity Loaded")

        // Setting up drawer layout and appbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        mDrawer = findViewById(R.id.drawerLayout)
        mToggle = ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawer.addDrawerListener(mToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        findViewById<NavigationView>(R.id.nav_view)
            .apply { setNavigationItemSelectedListener(this@DashboardActivity) }

        // Ask for location permission here to avoid crashing later
        checkLocationPermission(this)

        val tvAhoy = findViewById<MaterialTextView>(R.id.tvAhoy)
        tvAhoy.append(" ${UserSingleton.activeUser.displayName}!")

        findViewById<MaterialTextView>(R.id.tvName)
            .apply { text = UserSingleton.activeUser.displayName }
        findViewById<MaterialTextView>(R.id.tvEmail)
            .apply { text = UserSingleton.activeUser.email }

        //findViewById<MaterialTextView>(R.id.nav_header_textView).apply { text = "manasou" }
    }

    override fun onResume() {
        super.onResume()
        val tvScore = findViewById<MaterialTextView>(R.id.tvScore)
            .apply { text = UserSingleton.activeUser.score.toString() }
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
        } else {
            val builder = AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.exit_promt)
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // TODO: Maybe perform some saving?
                    finish()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.cancel()
                }
            builder.create().show()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (mToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_about -> {
                debugLog("About button pressed")
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.item_help -> {
                val intent = Intent(this, ShowHelpActivity::class.java)
                intent.putExtra(INTENT_EXTRA_TITLE, "Help")
                intent.putExtra(INTENT_EXTRA_TEXT, getString(R.string.help))
                intent.putExtra(INTENT_EXTRA_IMG, R.drawable.pirate_hat_2)
                startActivity(intent)
            }
            R.id.item_logout -> {
                logout()
            }
        }
        mDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun huntMode(v: View) {
        val intent = Intent(this, TreasureHuntSelectionActivity::class.java)
        startActivity(intent)
    }

    fun burialMode(v: View) {
        val intent = Intent(this, TreasureBurialWelcome::class.java)
        startActivity(intent)
    }

    fun logout() {
        // Show confirmation dialog
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.app_name)
            .setMessage(R.string.signout_promt)
            .setCancelable(true)
            .setPositiveButton("Yes") { dialog, id ->
                // TODO: Maybe perform some saving?
                debugLog("Logging out")
                mAuth.signOut()

                // Go back to login screen
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }
}