package com.lamnguyen5464.avatar3dme.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import java.nio.ByteBuffer


fun Image.toBitMap(): Bitmap {
    val buffer = this.planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
}