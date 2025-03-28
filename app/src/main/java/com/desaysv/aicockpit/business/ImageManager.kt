package com.desaysv.aicockpit.business

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import java.io.File

object ImageConstants {
    const val DEFAULT_PATH = "/storage/emulated/0/pics"
    const val ACTION_REQUEST_THEME = "com.desaysv.AI_Cockpit.MY_ACTION"
    const val ACTION_RECEIVE_THEME = "com.desaysv.ip.MY_ACTION"
    const val KEY_AI_PATH = "ai_path"
    const val TARGET_PACKAGE = "com.desaysv.wuji"
}

object ImageManager {
    private var isRequesting = false
    private var broadcastReceiver: BroadcastReceiver? = null

    fun loadThemeImages(context: Context, onResult: (List<String>) -> Unit) {
        val defaultPath = ImageConstants.DEFAULT_PATH
        val images = getLocalImagesFromPath(defaultPath)

        if (images.isNotEmpty()) {
            onResult(images)
        } else if (!isRequesting) {
            isRequesting = true
            sendThemeRequestBroadcast(context)
            registerThemeReceiver(context, onResult)
        }
    }

    fun getLocalImagesFromPath(path: String): List<String> {
        val dir = File(path)
        return dir.listFiles { file ->
//            file.extension.lowercase() in listOf("jpg", "png", "webp") 可以拓展
            file.extension.lowercase() =="png"
        }?.map { it.absolutePath } ?: emptyList()
    }

    private fun sendThemeRequestBroadcast(context: Context) {
        val intent = Intent(ImageConstants.ACTION_REQUEST_THEME)
        intent.setPackage(ImageConstants.TARGET_PACKAGE)
        context.sendBroadcast(intent)
    }

    private fun registerThemeReceiver(context: Context, onResult: (List<String>) -> Unit) {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ImageConstants.ACTION_RECEIVE_THEME) {
                    val path = intent.getStringExtra(ImageConstants.KEY_AI_PATH)
                    val result = path?.let { getLocalImagesFromPath(it) } ?: emptyList()
                    onResult(result)
                    context.unregisterReceiver(this)
                    broadcastReceiver = null
                    isRequesting = false
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            broadcastReceiver,
            IntentFilter(ImageConstants.ACTION_RECEIVE_THEME),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }
}
