package com.lamnguyen5464.avatar3dme.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.lamnguyen5464.avatar3dme.R

class OnboardingActivity : AppCompatActivity() {
    private lateinit var mSlideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var backBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var skipBtn: Button

    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        val editor = prefs.edit()
//        editor.putBoolean("isFirstRun", false)
//        editor.apply()

        //Once onboarding is done, do not repeat if return to previous screens
//        val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
//        if (sharedPref.getBoolean("is_onboarding_completed", false)) {
//            // Onboarding is already completed, navigate to the main activity
//            startActivity(Intent(this, FaceShootActivity::class.java))
//            finish()
//        } else {
            backBtn = findViewById(R.id.backBtn)
            nextBtn = findViewById(R.id.nextBtn)
            skipBtn = findViewById(R.id.skipBtn)

            backBtn.setOnClickListener {
                if (getItem(0) > 0) {
                    mSlideViewPager.setCurrentItem(getItem(-1), true)
                }
            }

            nextBtn.setOnClickListener {
                if (getItem(0) < 3) {
                    mSlideViewPager.setCurrentItem(getItem(1), true)
                } else {
                    startActivity(Intent(this, FaceShootActivity::class.java))
                    finish()
                }
            }

            skipBtn.setOnClickListener {
                startActivity(Intent(this, FaceShootActivity::class.java))
                finish()
            }

            mSlideViewPager = findViewById(R.id.slideViewPager)
            mDotLayout = findViewById(R.id.indicatorLayout)

            viewPagerAdapter = ViewPagerAdapter(this)

            mSlideViewPager.adapter = viewPagerAdapter

            setUpIndicator(0);
            mSlideViewPager.addOnPageChangeListener(viewListener)

//            sharedPref.edit().putBoolean("is_onboarding_completed", true).apply()
//        }
    }

    fun setUpIndicator(position: Int) {
        dots = Array(4) { TextView(this) }
        mDotLayout.removeAllViews()

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text =
                HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            dots[i].textSize = 35f
            dots[i].setTextColor(
                resources.getColor(R.color.md_theme_light_secondaryContainer, theme)
            )
            mDotLayout.addView(dots[i])
        }

        dots[position].setTextColor(resources.getColor(R.color.md_theme_light_primary, theme))
    }

    val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Do something on page scrolled
        }

        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

            if (position > 0) {
                backBtn.visibility = View.VISIBLE
            } else {
                backBtn.visibility = View.INVISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Do something on page scroll state changed
        }
    }

    private fun getItem(i: Int): Int {
        return mSlideViewPager.currentItem + i
    }
}
