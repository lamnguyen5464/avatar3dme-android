package com.lamnguyen5464.avatar3dme.core.utils

import android.R.attr.angle
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

fun Image.toBitMap(): Bitmap? {
    return try {
        val bytes = this.toByteArray()
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
    } catch (e: Exception) {
        println("[ERROR] tobitMap error:${e}")
        null
    }
}

fun Bitmap.toBase64OfPng(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 25, outputStream)
    val byteArray: ByteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.flipX(): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1f, 1f, width / 2f, height / 2f);
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)

}