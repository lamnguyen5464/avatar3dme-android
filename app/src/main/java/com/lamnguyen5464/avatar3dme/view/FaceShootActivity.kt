package com.lamnguyen5464.avatar3dme.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.camera.AppCamera
import com.lamnguyen5464.avatar3dme.core.providers.Providers

class FaceShootActivity : AppCompatActivity() {

    private val camera by lazy {
        AppCamera(
            ProcessCameraProvider.getInstance(this),
            findViewById<PreviewView>(R.id.camera_view),
            ContextCompat.getMainExecutor(this),
        ) { image ->
            Providers.currentProcessingImage = image
            startActivity(Intent(this.applicationContext, CaptureConfirmationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_shoot)

        checkAndRequestCameraPermission()

        findViewById<ImageView>(R.id.bt_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.bt_shoot).setOnClickListener {
            camera.capture()
        }
    }

    private fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    // Permission has been denied, show message
                    Toast.makeText(this, "Camera permission has been denied.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    private fun startCamera() {
        drawSquareFrame()
        camera.start(this)
    }

    private fun drawSquareFrame() {
        val metrics: DisplayMetrics? = resources?.displayMetrics
        val SCREEN_WIDTH = metrics?.widthPixels ?: 0
        val SCREEN_HEIGHT = metrics?.heightPixels ?: 0

        val PADDDING = SCREEN_WIDTH * 1 / 5
        val EDGE_LENGTH = SCREEN_WIDTH - 2 * PADDDING


        val backgroundLayout: RelativeLayout = findViewById(R.id.camera_layout)

//        container_text
        val layoutText = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutText.setMargins(0, EDGE_LENGTH / 2, 0, 0)
        layoutText.addRule(RelativeLayout.CENTER_HORIZONTAL)
        findViewById<RelativeLayout>(R.id.container_text).layoutParams = layoutText
        findViewById<TextView>(R.id.text_desc).text = intent?.extras?.get("desc")?.toString() ?: ""

        //right
        val viewPaddingRight = View(this)
        val layoutPaddingRight = RelativeLayout.LayoutParams(PADDDING, SCREEN_HEIGHT)
        layoutPaddingRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        viewPaddingRight.layoutParams = layoutPaddingRight
        viewPaddingRight.setBackgroundColor(ContextCompat.getColor(this, R.color.BLACK_SHADOW))
        backgroundLayout.addView(viewPaddingRight)

        //left
        val viewPaddingLeft = View(this)
        val layoutPaddingLeft = RelativeLayout.LayoutParams(PADDDING, SCREEN_HEIGHT)
        layoutPaddingLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        viewPaddingLeft.layoutParams = layoutPaddingLeft
        viewPaddingLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.BLACK_SHADOW))
        backgroundLayout.addView(viewPaddingLeft)

        //top
        val viewPaddingTop = View(this)
        val layoutPaddingTop = RelativeLayout.LayoutParams(EDGE_LENGTH, EDGE_LENGTH)
        layoutPaddingTop.addRule(RelativeLayout.CENTER_HORIZONTAL)
        viewPaddingTop.layoutParams = layoutPaddingTop
        viewPaddingTop.setBackgroundColor(ContextCompat.getColor(this, R.color.BLACK_SHADOW))
        backgroundLayout.addView(viewPaddingTop)

        //bottom
        val viewPaddingBottom = View(this)
        val layoutPaddingBottom = RelativeLayout.LayoutParams(EDGE_LENGTH, SCREEN_HEIGHT)
        layoutPaddingBottom.setMargins(0, EDGE_LENGTH * 2, 0, 0)
        layoutPaddingBottom.addRule(RelativeLayout.CENTER_HORIZONTAL)
        viewPaddingBottom.layoutParams = layoutPaddingBottom
        viewPaddingBottom.setBackgroundColor(ContextCompat.getColor(this, R.color.BLACK_SHADOW))
        backgroundLayout.addView(viewPaddingBottom)

        //view scan cursor
        val viewCursor = View(this)
        val layoutCursor = RelativeLayout.LayoutParams(EDGE_LENGTH, 5)
        layoutCursor.setMargins(0, EDGE_LENGTH, 0, 0)
        layoutCursor.addRule(RelativeLayout.CENTER_HORIZONTAL)
        viewCursor.layoutParams = layoutCursor
        viewCursor.setBackgroundColor(ContextCompat.getColor(this, R.color.BLUE_FUND))
        backgroundLayout.addView(viewCursor)

        val translateCursorAnimation: Animation = TranslateAnimation(
            0F,
            0F,
            0F,
            EDGE_LENGTH.toFloat()
        )
        translateCursorAnimation.repeatMode = Animation.REVERSE
        translateCursorAnimation.repeatCount = Animation.INFINITE
        translateCursorAnimation.duration = 2000

        viewCursor.startAnimation(translateCursorAnimation)

    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}