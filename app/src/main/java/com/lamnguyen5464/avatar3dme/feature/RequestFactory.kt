package com.lamnguyen5464.avatar3dme.feature

import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpRequest

object RequestFactory {

    private const val BASE_URL = "http://c967-115-79-229-226.ngrok.io/"
    fun createUploadBase64Request(base64Img: String): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("upload-image-base64")
            .method("POST")
            .addFieldBody("image", base64Img)

    }

    fun getToken(): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("token")
            .method("POST")

    }
}