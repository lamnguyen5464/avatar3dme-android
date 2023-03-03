package com.lamnguyen5464.avatar3dme

import android.media.Image

class CurrentProcessingImage private constructor(
    var image: Image? = null
) {
    companion object {
        val instance = CurrentProcessingImage()
    }
}