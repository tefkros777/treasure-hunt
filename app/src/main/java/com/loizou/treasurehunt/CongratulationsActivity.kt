package com.loizou.treasurehunt

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

class CongratulationsActivity : AppCompatActivity() {

    private lateinit var mTitle : String
    private lateinit var mBody : String
    private lateinit var mBtnText : String
    private var mImgSrc : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)

        mTitle = intent.getStringExtra(CONGRATS_TITLE)!!
        mBody = intent.getStringExtra(CONGRATS_BODY)!!
        mBtnText = intent.getStringExtra(CONGRATS_BTN_TXT)!!
        mImgSrc = intent.getIntExtra(CONGRATS_IMG_SRC, 0)

        val tvTitle = findViewById<MaterialTextView>(R.id.tvCongrats_title)
        val tvBody = findViewById<MaterialTextView>(R.id.tvCongrats_body)
        val btn = findViewById<MaterialButton>(R.id.btnCongrats_button)
        val img = findViewById<ShapeableImageView>(R.id.ivCongrats_img)

        tvTitle.text = mTitle
        tvBody.text = mBody
        btn.text = mBtnText
        img.setBackgroundResource(mImgSrc)
        img.maxWidth = 250
    }

    fun btnAction(v: View){
        setResult(Activity.RESULT_OK)
        finish()
    }
}