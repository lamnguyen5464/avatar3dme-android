package com.lamnguyen5464.avatar3dme.core.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.FileOutputStream

object FileUtils {
    fun getInternalPath(context: Context): String {
        return context.filesDir.toString()
    }

    private const val DEFAULT_IMG_NAME = "img.png"
    fun saveBitmap(
        bitmap: Bitmap,
        context: Context,
        fileName: String = DEFAULT_IMG_NAME,
        defaultQuality: Int = 50,
        defaultFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
    ): Boolean {
        return try {
            val directory = getInternalPath(context)
            val uri = "$directory/$fileName"
            val outputStream = FileOutputStream(uri)
            bitmap.compress(defaultFormat, defaultQuality, outputStream)

            outputStream.flush()
            outputStream.close()
            true
        } catch (e: Exception) {
            false
        }
    }

}