package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class ShowHelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_help)
        debugLog("Help Activity Loaded")

        val tlt = intent.getStringExtra(INTENT_EXTRA_TITLE)!!
        val body = intent.getStringExtra(INTENT_EXTRA_TEXT)!!
        val img = intent.getIntExtra(INTENT_EXTRA_IMG, 0)

        title = tlt

        findViewById<MaterialTextView>(R.id.tvShowText_txt).apply { text = body }
        findViewById<ShapeableImageView>(R.id.ivShowText_img).apply { setImageResource(img) }

    }
}