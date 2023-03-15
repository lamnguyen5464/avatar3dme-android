package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.lamnguyen5464.avatar3dme.R

class ProcessingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processing)

        findViewById<Button>(R.id.bt_cancel).setOnClickListener {
            startActivity(Intent(this, FaceShootActivity::class.java))
        }

        // Get the current dark mode state
        val darkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val anim_view = findViewById<LottieAnimationView>(R.id.animation_view)

        var filter = SimpleColorFilter(getColor(R.color.BLACK))

        if (darkMode == Configuration.UI_MODE_NIGHT_YES) {
            // Set the button background tint to a dark color
            filter = SimpleColorFilter(getColor(R.color.md_theme_dark_primary))
        } else {
            // Set the button background tint to a light color
            filter = SimpleColorFilter(getColor(R.color.md_theme_light_primary))
        }

        val keyPath = KeyPath("**")
        val callback: LottieValueCallback<ColorFilter> = LottieValueCallback(filter)

        anim_view.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
    }
}