package com.lamnguyen5464.avatar3dme.viewmodel

import android.content.Intent
import android.util.TimeUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpFailureResponse
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpSuccessResponse
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.toBase64OfPng
import com.lamnguyen5464.avatar3dme.core.utils.toBitMap
import com.lamnguyen5464.avatar3dme.core.viewer.Model
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.feature.RequestFactory
import com.lamnguyen5464.avatar3dme.view.CustomizeActivity
import com.lamnguyen5464.avatar3dme.view.ProcessingActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.InputStream

class ProcessingViewModel(private val activity: ProcessingActivity) {

    class ProcessHandler {
        val processingState = MutableStateFlow<ProcessingState>(ProcessingState.Processing)

        fun sendImage() {
            val bitmapImg = Providers.currentProcessingImage
            Providers.commonIOScope.launch {
                if (bitmapImg == null) {
                    processingState.emit(ProcessingState.Fail(Throwable("bitmapImg is null")))
                    return@launch
                }

                val req = RequestFactory.createUploadBase64Request(
                    bitmapImg.toBase64OfPng()
                )

                when (val res = Providers.httpClient.send(request = req)) {
                    is SimpleHttpSuccessResponse -> processingState.emit(ProcessingState.Done(res.inputStream))
                    is SimpleHttpFailureResponse -> processingState.emit(ProcessingState.Fail(res.exception))
                }
            }
        }
    }

    companion object {
        val processHandlerInstance by lazy {
            ProcessHandler()
        }

    }

    interface ProcessingState {
        object Processing : ProcessingState
        class Done(val result: InputStream) : ProcessingState
        class Fail(val exception: Throwable? = null) : ProcessingState
    }

    fun init() {
        Providers.commonIOScope.launch {
            processHandlerInstance.processingState.first { state ->
                when (state) {
                    is ProcessingState.Done -> onProcessDone(state.result)
                    is ProcessingState.Fail -> onProcessFail(state.exception)
                }
                true
            }
        }

        activity.findViewById<Button>(R.id.bt_cancel).setOnClickListener {
            activity.finish()
        }
    }

    private fun onProcessFail(exception: Throwable?) {
        println("[Error] ProcessingViewModel::onProcessFail ${exception?.message}")
        activity.finish()
    }

    private fun onProcessDone(inputStream: InputStream) {
        Providers.currentModel = ObjModel(inputStream)
        val intent = Intent(activity.applicationContext, CustomizeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        activity.startActivity(intent)
        activity.finish()
    }
}