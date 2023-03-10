package com.lamnguyen5464.avatar3dme.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.util.Base64
import java.io.ByteArrayOutputStream


fun Image.toByteArray(): ByteArray {
    val buffer = this.planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return bytes
}

fun Image.toBitMap(): Bitmap {
    val bytes = this.toByteArray()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
}

fun Bitmap.toBase64OfPng(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray: ByteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}