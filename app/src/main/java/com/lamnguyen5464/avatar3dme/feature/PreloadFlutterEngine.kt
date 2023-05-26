package com.lamnguyen5464.avatar3dme.feature

import com.google.vr.cardboard.ThreadUtils.runOnUiThread
import com.lamnguyen5464.avatar3dme.App
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.FileUtils
import com.lamnguyen5464.avatar3dme.core.utils.ScreenShotUtils
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class PreloadFlutterEngine : CommonUseCase, MethodChannel.MethodCallHandler,
    EventChannel.StreamHandler {
    private var eventSink: EventChannel.EventSink? = null

    companion object {
        val instance by lazy {
            PreloadFlutterEngine()
        }
        const val ENGINE_ID = "flutter_cache_tag"
        private const val COMMON_CHANNEL_TAG = "channel.common"
        private const val IMAGE_SAVED_SIGNAL_TAG = "new_image_saved"
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

            val eventChannel =
                EventChannel(flutterEngine.dartExecutor.binaryMessenger, IMAGE_SAVED_SIGNAL_TAG)

            eventChannel.setStreamHandler(this)

            ScreenShotUtils.newImageSavedSignal
                .onEach {
                    runOnUiThread {
                        this.eventSink?.success("")
                    }
                }
                .launchIn(Providers.commonIOScope)

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

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        this.eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        TODO("Not yet implemented")
    }
}