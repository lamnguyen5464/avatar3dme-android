package com.lamnguyen5464.avatar3dme.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.viewer.ModelSurfaceView
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.toCaptureViewBt).setOnClickListener {
            startActivity(Intent(this, FaceShootActivity::class.java))
        }

        var modelView: ModelSurfaceView? = null

        findViewById<Button>(R.id.toModelViewBt).setOnClickListener {
            val scope = CoroutineScope(Dispatchers.IO + Job())

            scope.launch {
                val model = applicationContext.resources.openRawResource(R.raw.default_face).let {
                    val model = ObjModel(it)
                    Providers.currentModel = model
                    it.close()
                    model
                }

                runOnUiThread {
                    val containerView = findViewById<RelativeLayout>(R.id.container)
                    containerView.removeView(modelView)
                    modelView = ModelSurfaceView(applicationContext, model)
                    containerView.addView(modelView, 0)
                }


//                startActivity(Intent(applicationContext, ModelViewerActivity::class.java))
            }
        }

    }
}