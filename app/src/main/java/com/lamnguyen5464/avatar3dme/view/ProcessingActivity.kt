package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.lamnguyen5464.avatar3dme.R

class ProcessingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_processing)

        findViewById<Button>(R.id.bt_cancel).setOnClickListener {
            startActivity(Intent(this, FaceShootActivity::class.java))
        }
    }
}