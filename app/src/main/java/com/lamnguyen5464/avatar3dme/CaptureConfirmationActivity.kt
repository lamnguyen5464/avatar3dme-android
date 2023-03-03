package com.lamnguyen5464.avatar3dme

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.lamnguyen5464.avatar3dme.utils.toBitMap

class CaptureConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_confirmation)

        CurrentProcessingImage.instance.image?.let { image ->
            findViewById<ImageView>(R.id.image_result).setImageBitmap(image.toBitMap())
        }


        findViewById<Button>(R.id.bt_retry).setOnClickListener {
            startActivity(Intent(this, FaceShoot::class.java))
        }

        findViewById<Button>(R.id.bt_continueProcessing).setOnClickListener {
            startActivity(Intent(this, ProcessingActivity::class.java))
        }
    }
}