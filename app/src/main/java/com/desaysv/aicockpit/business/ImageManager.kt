package com.desaysv.aicockpit.business

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File

object ImageConstants {
//    var DEFAULT_PATH = Environment.getExternalStorageDirectory().path+ "/pics"
//    var DEFAULT_PATH = "/data/data/com.desaysv.aicockpit/files/defImg"
    val DEFAULT_PATH = "/sdcard/Pictures/aicockpit"
    val DEFAULT_SOUND_PICS_PATH = "/sdcard/Pictures/aicockpit"
    val DEFAULT_SOUND_AUDIO_PATH = "/sdcard/Sounds/aicockpit"
    const val ACTION_REQUEST_THEME = "com.desaysv.AI_Cockpit.MY_ACTION"
    const val ACTION_REQUEST_SOUNDS = "com.desaysv.AI_Cockpit.MY_ACTION"
    const val ACTION_RECEIVE_SOUNDS = "com.desaysv.AI_Cockpit.SOUND_RESOURCE_CHANGED"
    const val ACTION_RECEIVE_THEME = "com.desaysv.ip.MY_ACTION"
    const val KEY_AI_PATH = "ai_path"
    const val TARGET_PACKAGE = "com.desaysv.wuji"
}

object ImageManager {
    private var broadcastReceiver: BroadcastReceiver? = null


    fun loadThemeImages(context: Context, onResult: (List<String>) -> Unit) {
        val defaultPath = ImageConstants.DEFAULT_PATH
        Log.d("debugman",defaultPath)
        val images = getLocalImagesFromPath(defaultPath)
        Log.d("debugman",images.toString())
        if (images.isNotEmpty()) {
            onResult(images)
        } else {
            registerThemeReceiver(context, onResult)
            sendThemeRequestBroadcast(context)
        }
    }

    fun getLocalImagesFromPath(path: String): List<String> {
        val dir = File(path)
        Log.d("",dir.path)
        return dir.listFiles { file ->
            file.extension.lowercase() in listOf("png", "jpg", "jpeg", "webp")

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
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            broadcastReceiver,
            IntentFilter(ImageConstants.ACTION_RECEIVE_THEME),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    // 新增：被动接收广播后的注册，当被动接收到指定广播时，直接把文件夹路径传给回调
    fun registerPassiveReceiver(context: Context, onBroadcast: (String) -> Unit) {
        // 如果需要，避免重复注册，可设置单例或标记
        val passiveReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("get"+intent.action)
                if (intent.action == ImageConstants.ACTION_RECEIVE_THEME) {
                    val path = intent.getStringExtra(ImageConstants.KEY_AI_PATH)
                    if (!path.isNullOrEmpty()) {
                        onBroadcast(path)
                    }
//                    context.unregisterReceiver(this)
                }
            }
        }
        ContextCompat.registerReceiver(
            context,
            passiveReceiver,
            IntentFilter(ImageConstants.ACTION_RECEIVE_THEME),
            ContextCompat.RECEIVER_EXPORTED
        )
    }

}