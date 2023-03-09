package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.toBitMap

class CaptureConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_confirmation)

        Providers.currentProcessingImage?.let { image ->
            findViewById<ImageView>(R.id.image_result).setImageBitmap(image.toBitMap().rotate(90F))
        }


        findViewById<Button>(R.id.bt_retry).setOnClickListener {
            startActivity(Intent(this, FaceShootActivity::class.java))
        }

        findViewById<Button>(R.id.bt_continueProcessing).setOnClickListener {
            startActivity(Intent(this, ProcessingActivity::class.java))
        }
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}