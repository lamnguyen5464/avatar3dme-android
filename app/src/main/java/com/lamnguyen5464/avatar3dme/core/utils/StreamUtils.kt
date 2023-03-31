package com.lamnguyen5464.avatar3dme.core.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


fun InputStream.toStringData(): String? {
    return try {
        val bufferedReader = BufferedReader(InputStreamReader(this))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { line ->
            stringBuilder.append(line)
        }
        stringBuilder.toString()
    } catch (e: Exception) {
        null
    }
}