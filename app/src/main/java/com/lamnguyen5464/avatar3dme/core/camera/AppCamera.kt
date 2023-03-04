package com.lamnguyen5464.avatar3dme.core.camera

import android.annotation.SuppressLint
import android.media.Image
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
    private val executor: Executor,
    private val onCapturedImage: (Image) -> Unit
): OnImageCapturedCallback() {

    private val preview = Preview.Builder().build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    // Select back camera as a default
    private val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private val imageCapture = ImageCapture.Builder()
        .build()

    private val imageAnalyzer = ImageAnalysis.Builder()
        .build()

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCaptureSuccess(image: ImageProxy) {
        if (image.image == null) {
            println("[CAM] capture image is null")
        }
        image.image?.let { onCapturedImage(it) }
    }

    fun capture() {
        imageCapture.takePicture(executor, this)
    }

    fun start(lifecycleOwner: LifecycleOwner) {
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