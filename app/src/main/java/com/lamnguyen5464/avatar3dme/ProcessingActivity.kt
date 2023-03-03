package com.lamnguyen5464.avatar3dme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ProcessingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processing)

        findViewById<Button>(R.id.bt_cancelProcess).setOnClickListener {
            startActivity(Intent(this, FaceShoot::class.java))
        }
    }
}