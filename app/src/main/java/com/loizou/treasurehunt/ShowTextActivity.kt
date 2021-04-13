package com.loizou.treasurehunt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView

class ShowTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_text)

        val tlt = intent.getStringExtra(INTENT_EXTRA_TITLE)!!
        val body = intent.getStringExtra(INTENT_EXTRA_TEXT)!!

        title = tlt
        findViewById<MaterialTextView>(R.id.tvShowText).apply { text = body }
    }
}