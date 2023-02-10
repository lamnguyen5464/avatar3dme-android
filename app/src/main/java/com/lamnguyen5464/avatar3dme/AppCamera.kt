package com.lamnguyen5464.avatar3dme

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executor

class AppCamera(
    private val listenerCamera: ListenableFuture<ProcessCameraProvider>,
    private val previewView: PreviewView,
    private val executor: Executor,
    private val onFoundQRCallBack: (String) -> Unit
) {

    fun start(lifecycleOwner: LifecycleOwner) {
        listenerCamera.addListener({
            try {
                val cameraProvider: ProcessCameraProvider = listenerCamera.get()
                val preview = Preview.Builder().build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val imageCapture = ImageCapture.Builder()
                    .build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
//                        it.setAnalyzer(executor, this::getCameraAnalyzer)
                    }

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