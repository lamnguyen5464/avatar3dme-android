package com.lamnguyen5464.avatar3dme

import android.app.Application
import com.lamnguyen5464.avatar3dme.core.providers.Providers

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        Providers.commonSyncProcess.execute()
    }

    companion object {
        lateinit var instance: App
    }
}