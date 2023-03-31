package com.lamnguyen5464.avatar3dme.core.http

import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class SimpleHttpClient {
    fun send(request: SimpleHttpRequest): SimpleHttpResponse {
        return try {
            val url = URL(request.getUrl())
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = request.getMethod()
            urlConnection.doOutput = true

            request.getHeaders().forEach { urlConnection.setRequestProperty(it.key, it.value) }

            if (request.getMethod() != "GET") {
                val outputStream: OutputStream = BufferedOutputStream(urlConnection.outputStream)
                outputStream.write(request.getBodyString().encodeToByteArray())
                outputStream.flush()
                outputStream.close()
            }
            val statusCode = urlConnection.responseCode

            if (statusCode == 200) {
                SimpleHttpSuccessResponse(
                    urlConnection.responseMessage,
                    urlConnection.inputStream
                )
            } else {
                SimpleHttpFailureResponse(statusCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            SimpleHttpFailureResponse()
        }

    }
}