package com.lamnguyen5464.avatar3dme.core.utils

import android.app.Activity
import android.graphics.Bitmap
import android.opengl.GLException
import android.util.DisplayMetrics
import android.view.View
import kotlinx.coroutines.flow.MutableSharedFlow
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10


object ScreenShotUtils {
    fun take(view: View): Bitmap? {
        return try {
//            val displayMetrics = DisplayMetrics()
//            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//            val width = displayMetrics.widthPixels
//            val height = displayMetrics.heightPixels

            // Create a bitmap of the screen
//            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(bitmap)
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    val newImageSavedSignal = MutableSharedFlow<Any>()
}