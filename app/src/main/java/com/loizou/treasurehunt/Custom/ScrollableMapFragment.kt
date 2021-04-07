package com.loizou.treasurehunt.Custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment

class ScrollableMapFragment: SupportMapFragment() {
    var listener: OnTouchListener? = null
    lateinit var frameLayout: TouchableWrapper

    override fun onAttach(context: Context) {
        super.onAttach(context)

        frameLayout = TouchableWrapper(context)
        frameLayout.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View? {
        val layout = super.onCreateView(inflater, viewGroup, bundle)

        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        (layout as? ViewGroup)?.addView(frameLayout, params)

        return layout
    }

    interface OnTouchListener {
        fun onTouch()
    }

    inner class TouchableWrapper(context: Context): FrameLayout(context) {
        override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
            when (event?.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> listener?.onTouch()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}