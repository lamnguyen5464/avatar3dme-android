package com.lamnguyen5464.avatar3dme.core.http

import org.json.JSONObject

class SimpleHttpRequest {
    private var baseUrl: String = ""
    private var subPath: String = ""
    private var method: String = "GET"
    private val headers = mutableMapOf(
        "Content-Type" to "application/json",
        "Accept" to "application/json"
    )
    private val body = JSONObject()

    fun baseUrl(baseUrl: String) = apply {
        this.baseUrl = baseUrl
    }

    fun subPath(subPath: String) = apply {
        this.subPath = subPath
    }

    fun method(method: String) = apply {
        this.method = method
    }

    fun headers(headers: Map<String, String>) = apply {
        this.headers.putAll(headers)
    }

    fun addFieldBody(key: String, value: Any?) = apply {
        this.body.put(key, value)
    }

    fun getUrl() = this.baseUrl + this.subPath
    fun getMethod() = this.method
    fun getHeaders(): Map<String, String> = this.headers
    fun getBodyString() = this.body.toString()

}