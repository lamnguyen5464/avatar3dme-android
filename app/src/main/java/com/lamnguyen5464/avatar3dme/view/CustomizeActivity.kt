package com.lamnguyen5464.avatar3dme.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.FileUtils
import com.lamnguyen5464.avatar3dme.core.utils.ScreenShotUtils
import com.lamnguyen5464.avatar3dme.core.utils.ScreenShotUtils.newImageSavedSignal
import com.lamnguyen5464.avatar3dme.core.viewer.ModelSurfaceView
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.model.CreditCardsModel
import com.lamnguyen5464.avatar3dme.viewmodel.CustomizeViewModel
import kotlinx.android.synthetic.main.activity_customize.*
import kotlinx.coroutines.launch

class CustomizeActivity : AppCompatActivity() {

    private val viewModel = CustomizeViewModel(this)
    private var modelView: ModelSurfaceView? = null

    private val onUpdateModel = Observer<ObjModel> { value ->
        runOnUiThread {
            val containerView =
                findViewById<RelativeLayout>(R.id.customize_container)
            containerView.removeView(modelView)
            modelView = ModelSurfaceView(applicationContext, value)
            containerView.addView(modelView, 0)
        }
    }


    fun showLoading() = runOnUiThread {
        findViewById<RelativeLayout>(R.id.layout_loading)?.let {
            it.visibility = View.VISIBLE
            it.isClickable = false
        }
    }

    fun hideLoading() = runOnUiThread {
        findViewById<RelativeLayout>(R.id.layout_loading)?.let {
            it.visibility = View.GONE
            it.isClickable = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize)

        viewModel.init()

        viewModel
            .modelState
            .observe(this, onUpdateModel)

        viewModel
            .modelStream
            .observe(this) {
                bindCard(it)
            }

        viewModel.initClickListener()

        findViewById<Button>(R.id.bt_undo_custom).setOnClickListener {
        }

        findViewById<Button>(R.id.bt_save_custom).setOnClickListener {
            modelView?.renderer?.cacheBitmap?.let { img ->
//                ScreenShotUtils.createBitmapFromGLSurface(this, view.renderer.onDrawFrame())?.let { img ->
                FileUtils.saveBitmap(
                    bitmap = img,
                    context = this,
                )
                Providers.commonIOScope.launch {
                    newImageSavedSignal.emit("")
                }

                val bottomSheetShare = SharePlaygroundFragment()
                bottomSheetShare.show(supportFragmentManager, SharePlaygroundFragment.TAG)

//                }
            }
        }

        val currentCard = findViewById<MaterialCardView>(R.id.cardCenter)
        motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                currentCard.setCardForegroundColor(getColorStateList(R.color.TRANSPARENT))
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                currentCard.setCardForegroundColor(getColorStateList(R.color.BLACK_OVERLAY))
                motionLayout.post {
                    when (currentId) {
                        R.id.secondCard -> {
                            motionLayout.progress = 0f
                            viewModel.swipeRight()
                        }

                        R.id.firstCard -> {
                            motionLayout.progress = 0f
                            viewModel.swipeLeft()
                        }
                    }
                }
            }
        })
    }

    private fun bindCard(it: CreditCardsModel) {
        cardLeft1.getChildAt(0).setBackgroundResource(it.cardOneLeft.thumbnailURI)
        cardCenter.getChildAt(0).setBackgroundResource(it.cardCenter.thumbnailURI)
        cardRight1.getChildAt(0).setBackgroundResource(it.cardOneRight.thumbnailURI)
    }
}