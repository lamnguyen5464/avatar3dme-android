package com.lamnguyen5464.avatar3dme.core.providers

import android.media.Image
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpClient
import com.lamnguyen5464.avatar3dme.core.viewer.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object Providers {
    var currentProcessingImage: Image? = null
    var currentModel: Model? = null
    var simpleToken: String? = null

    val httpClient by lazy {
        SimpleHttpClient()
    }
    val commonIOScope by lazy {
        CoroutineScope(Dispatchers.IO + Job())
    }
}