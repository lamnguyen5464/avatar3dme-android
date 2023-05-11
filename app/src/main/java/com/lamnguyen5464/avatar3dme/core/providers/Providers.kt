package com.lamnguyen5464.avatar3dme.core.providers

import android.graphics.Bitmap
import android.media.Image
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpClient
import com.lamnguyen5464.avatar3dme.core.viewer.Model
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.feature.CommonUseCase
import com.lamnguyen5464.avatar3dme.feature.PreloadFlutterEngine
import com.lamnguyen5464.avatar3dme.feature.SyncTokenUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object Providers {
    var currentProcessingImage: Bitmap? = null
    var currentModel: ObjModel? = null
    var simpleToken: String? = null

    val httpClient by lazy {
        SimpleHttpClient()
    }
    val commonIOScope by lazy {
        CoroutineScope(Dispatchers.IO + Job())
    }

    val commonMainScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    val commonSyncProcess by lazy {
        CommonUseCase.of(
            SyncTokenUseCase.instance,
            PreloadFlutterEngine.instance
        )
    }
}