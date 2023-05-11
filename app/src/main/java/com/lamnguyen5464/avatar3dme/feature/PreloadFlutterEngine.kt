package com.lamnguyen5464.avatar3dme.feature

import com.lamnguyen5464.avatar3dme.App
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpSuccessResponse
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.toStringData
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class PreloadFlutterEngine : CommonUseCase {
    companion object {
        val instance by lazy {
            PreloadFlutterEngine()
        }
        const val ENGINE_ID = "flutter_cache_tag"
    }

    override fun execute() {
        Providers.commonMainScope.launch {
            val flutterEngine = FlutterEngine(App.instance.applicationContext)

            // Start executing Dart code in the FlutterEngine.
            flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            // Cache the pre-warmed FlutterEngine to be used later by FlutterFragment.
            FlutterEngineCache
                .getInstance()
                .put(ENGINE_ID, flutterEngine)
        }
    }
}