package com.lamnguyen5464.avatar3dme.core.http

import java.io.InputStream


abstract class SimpleHttpResponse(
    val status: Int
)

class SimpleHttpSuccessResponse(
    private val responseMessage: String,
    val inputStream: InputStream
) : SimpleHttpResponse(200) {
    init {
        println("HTTP::Response: $responseMessage")
    }

    override fun toString(): String {
        return responseMessage
    }
}

class SimpleHttpFailureResponse(status: Int = -1) : SimpleHttpResponse(status)
