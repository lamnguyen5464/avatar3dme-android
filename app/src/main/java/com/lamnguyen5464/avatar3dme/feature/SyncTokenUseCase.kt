package com.lamnguyen5464.avatar3dme.feature

import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpSuccessResponse
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.toStringData
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class SyncTokenUseCase : CommonUseCase {
    companion object {
        val instance by lazy {
            SyncTokenUseCase()
        }
    }

    private val didSync: AtomicBoolean = AtomicBoolean(false)

    override fun execute() {
        Providers.commonIOScope.launch {
            println("Start get token...")
            if (didSync.compareAndSet(true, true)) {
                return@launch
            }
            val res = Providers.httpClient.send(request = RequestFactory.getToken())

            if (res is SimpleHttpSuccessResponse) {
                Providers.simpleToken = res.inputStream.toStringData()
                println("[TOKEN]: Providers.simpleToken: ${Providers.simpleToken}")
            }
        }
    }
}