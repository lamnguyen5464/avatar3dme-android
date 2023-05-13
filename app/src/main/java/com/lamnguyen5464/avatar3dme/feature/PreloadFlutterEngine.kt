package com.lamnguyen5464.avatar3dme.feature

import com.google.vr.cardboard.ThreadUtils.runOnUiThread
import com.lamnguyen5464.avatar3dme.App
import com.lamnguyen5464.avatar3dme.core.utils.FileUtils
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PreloadFlutterEngine : CommonUseCase, MethodChannel.MethodCallHandler {
    companion object {
        val instance by lazy {
            PreloadFlutterEngine()
        }
        const val ENGINE_ID = "flutter_cache_tag"
        private const val COMMON_CHANNEL_TAG = "channel.common"
    }

    override fun execute() {
        runOnUiThread {
            val flutterEngine = FlutterEngine(App.instance.applicationContext)

            // Start executing Dart code in the FlutterEngine.
            flutterEngine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )

            val channel = MethodChannel(
                flutterEngine.dartExecutor.binaryMessenger,
                COMMON_CHANNEL_TAG
            )

            channel.setMethodCallHandler(this)

            // Cache the pre-warmed FlutterEngine to be used later by FlutterFragment.
            FlutterEngineCache
                .getInstance()
                .put(ENGINE_ID, flutterEngine)
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getImagePath" -> {
                val fileName = call.arguments as? String
                if (fileName.isNullOrBlank()) {
                    result.error("401", "File name is empty", null)
                }
                val path = "${FileUtils.getInternalPath(App.instance.applicationContext)}/$fileName"
                result.success(path)
            }

            else -> result.notImplemented()
        }
    }
}