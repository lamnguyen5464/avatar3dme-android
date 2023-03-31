package com.lamnguyen5464.avatar3dme.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.Observer
import com.google.android.material.card.MaterialCardView
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.viewer.ModelSurfaceView
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.model.CreditCardsModel
import com.lamnguyen5464.avatar3dme.viewmodel.CustomizeViewModel
import kotlinx.android.synthetic.main.activity_customize.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CustomizeActivity : AppCompatActivity() {

    private val viewModel = CustomizeViewModel(this)

    private val onUpdateModel = object : Observer<ObjModel> {
        private var modelView: ModelSurfaceView? = null
        override fun onChanged(model: ObjModel?) {
            runOnUiThread {
                val containerView =
                    findViewById<RelativeLayout>(R.id.customize_container)
                containerView.removeView(modelView)
                modelView = ModelSurfaceView(applicationContext, model)
                containerView.addView(modelView, 0)
            }
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