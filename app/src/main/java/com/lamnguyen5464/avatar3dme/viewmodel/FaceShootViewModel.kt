package com.lamnguyen5464.avatar3dme.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.Button
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.camera.AppCamera
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.rotate
import com.lamnguyen5464.avatar3dme.core.utils.toBitMap
import com.lamnguyen5464.avatar3dme.view.CaptureConfirmationActivity
import com.lamnguyen5464.avatar3dme.view.FaceShootActivity
import java.io.BufferedInputStream

class FaceShootViewModel(private val activity: FaceShootActivity) {
    private val camera by lazy {
        AppCamera(
            ProcessCameraProvider.getInstance(activity),
            activity.findViewById<PreviewView>(R.id.camera_view),
            activity,
            ContextCompat.getMainExecutor(activity),
        ) { image ->
            Providers.currentProcessingImage = image.toBitMap()?.rotate(-90.0f)
            activity.startActivity(
                Intent(
                    activity.applicationContext,
                    CaptureConfirmationActivity::class.java
                )
            )
        }
    }

    fun initClickListener() {
        activity.findViewById<Button>(R.id.bt_back).setOnClickListener {
            activity.finish()
        }

        activity.findViewById<Button>(R.id.bt_shoot).setOnClickListener {
            camera.capture()
        }

        activity.findViewById<Button>(R.id.bt_switch).setOnClickListener {
            camera.switchCamera()
        }

        activity.findViewById<Button>(R.id.ic_album).setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(
            Intent.createChooser(intent, "Select image of face"),
            REQUEST_PICK_IMAGE
        )
    }

    fun checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            startCamera()
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    // Permission has been denied, show message
                    Toast.makeText(
                        activity,
                        "Camera permission has been denied.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }


    private fun startCamera() {
        activity.drawSquareFrame()
        camera.reloadCamera()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val inputStream = activity.contentResolver.openInputStream(data.data!!)
            val bitmap = BitmapFactory.decodeStream(BufferedInputStream(inputStream))
            Providers.currentProcessingImage = bitmap.rotate(90f)
            activity.startActivity(
                Intent(
                    activity.applicationContext,
                    CaptureConfirmationActivity::class.java
                )
            )
            return
        }
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
        private const val REQUEST_PICK_IMAGE = 2
    }

}