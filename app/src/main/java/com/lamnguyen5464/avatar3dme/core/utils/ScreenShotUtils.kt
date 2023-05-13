package com.lamnguyen5464.avatar3dme.core.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View


object ScreenShotUtils {
    fun take(activity: Activity): Bitmap? {
        return try {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            // Create a bitmap of the screen
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val rootView = activity.window.decorView
            rootView.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}