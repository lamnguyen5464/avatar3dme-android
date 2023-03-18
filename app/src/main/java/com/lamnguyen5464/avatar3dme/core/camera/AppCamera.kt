package com.lamnguyen5464.avatar3dme.core.camera

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor

class AppCamera(
    private val listenerCamera: ListenableFuture<ProcessCameraProvider>,
    private val previewView: PreviewView,
    private val lifecycleOwner: LifecycleOwner,
    private val executor: Executor,
    private val onCapturedImage: (Image) -> Unit
) : OnImageCapturedCallback() {

    private val preview = Preview.Builder().build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    // Select back camera as a default
    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private val imageCapture = ImageCapture.Builder()
        .build()

    private val imageAnalyzer = ImageAnalysis.Builder()
        .build()

    fun switchCamera() {
        try {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                CameraSelector.DEFAULT_FRONT_CAMERA
            else
                CameraSelector.DEFAULT_BACK_CAMERA
            this.reloadCamera()
        } catch (e: Exception) {
            Log.e("[CAM]", "Cannot switch camera", e)
        }
    }


    @SuppressLint("UnsafeOptInUsageError")
    override fun onCaptureSuccess(image: ImageProxy) {
        if (image.image == null) {
            println("[CAM] capture image is null")
        }
//        val rotation = image.imageInfo.rotationDegrees
        image.image?.let { onCapturedImage(it) }
    }

    fun capture() {
        imageCapture.takePicture(executor, this)
    }

    fun reloadCamera() {
        listenerCamera.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = listenerCamera.get()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, executor)
    }


}