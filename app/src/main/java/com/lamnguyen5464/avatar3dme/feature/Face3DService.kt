package com.lamnguyen5464.avatar3dme.feature

import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpRequest

object Face3DService {

    private const val BASE_URL = "http://192.168.0.100:3000/"
    fun createUploadBase64Request(base64Img: String): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("upload-image-base64")
            .method("POST")
            .addFieldBody("image", base64Img)

    }
}