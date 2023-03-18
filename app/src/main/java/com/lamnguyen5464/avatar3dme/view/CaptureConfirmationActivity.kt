package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.rotate
import com.lamnguyen5464.avatar3dme.core.utils.toBase64OfPng
import com.lamnguyen5464.avatar3dme.core.utils.toBitMap
import com.lamnguyen5464.avatar3dme.feature.Face3DService
import kotlinx.coroutines.launch

class CaptureConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_confirmation)


        Providers.currentProcessingImage?.let { image ->
            val bitmapImg = image.toBitMap()
            if (bitmapImg == null) {
                Toast.makeText(this, "Capture image failed!", Toast.LENGTH_LONG).show()
                return
            }
            findViewById<ImageView>(R.id.image_result).setImageBitmap(bitmapImg)

            Providers.commonIOScope.launch {
                println("Start request...")
                val req = Face3DService.createUploadBase64Request(
                    bitmapImg.toBase64OfPng()
                )
                val res = Providers.httpClient.send(request = req)


                println("Res: $res")
            }
        }


        findViewById<Button>(R.id.bt_retry).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.bt_continueProcessing).setOnClickListener {
            startActivity(Intent(this, ProcessingActivity::class.java))
        }
    }

}