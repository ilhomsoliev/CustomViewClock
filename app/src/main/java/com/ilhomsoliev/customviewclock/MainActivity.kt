package com.ilhomsoliev.customviewclock

import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val analogClockView = AnalogClockView(context = applicationContext, attributeSet = null)
        val layoutParams = ViewGroup.LayoutParams(
            300,
            300
        )
        analogClockView.layoutParams = layoutParams
        val rootView = findViewById<ViewGroup>(R.id.rootView)
        rootView.addView(analogClockView)
    }

}

