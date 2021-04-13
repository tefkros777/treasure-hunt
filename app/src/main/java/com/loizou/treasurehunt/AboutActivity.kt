package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        debugLog("About Activity Loaded")

        val tlt = "About This App"
        val body = getString(R.string.about_body)
        val credits = getString(R.string.credits)
        val version = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        val img = R.drawable.octopus

        title = tlt

        findViewById<MaterialTextView>(R.id.tvAbout_body).apply { text = body }
        findViewById<MaterialTextView>(R.id.tvAbout_credits).apply { text = credits }
        findViewById<MaterialTextView>(R.id.tvAbout_appVersion).apply { text = version }
        findViewById<ShapeableImageView>(R.id.ivAbout_img).apply { setImageResource(img) }
    }
}