package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.viewmodel.ProcessingViewModel.Companion.processHandlerInstance

class CaptureConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_confirmation)

        Providers.currentProcessingImage?.let { image ->
            findViewById<ImageView>(R.id.image_result).setImageBitmap(image)
        }

        processHandlerInstance.sendImage()

        findViewById<Button>(R.id.bt_retry).setOnClickListener {
            Providers.currentProcessingImage = null
            finish()
        }

        findViewById<Button>(R.id.bt_continueProcessing).setOnClickListener {
            val intent = Intent(this, ProcessingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent)
            finish()
        }
    }

}