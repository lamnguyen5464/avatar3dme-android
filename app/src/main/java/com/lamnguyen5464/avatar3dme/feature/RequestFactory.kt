package com.lamnguyen5464.avatar3dme.feature

import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpRequest
import com.lamnguyen5464.avatar3dme.core.providers.Providers

object RequestFactory {

    private const val BASE_URL = "https://e442-115-74-34-247.ngrok-free.app/"
    fun createUploadBase64Request(base64Img: String): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("upload-image-base64")
            .method("POST")
            .addFieldBody("image", base64Img)
            .addFieldBody("token", Providers.simpleToken ?: "")

    }

    fun createDecorRequest(item: String, attachment: String = ""): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("decor")
            .method("POST")
            .addFieldBody("item", item)
            .addFieldBody("token", "${Providers.simpleToken ?: ""}$attachment")

    }

    fun getToken(): SimpleHttpRequest {
        return SimpleHttpRequest()
            .baseUrl(BASE_URL)
            .subPath("token")
            .method("POST")

    }
}