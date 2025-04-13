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
    val DEFAULT_SOUND_PICS_PATH = "/sdcard/Pictures/aicockpit"
    val DEFAULT_SOUND_AUDIO_PATH = "/sdcard/Sounds/aicockpit"
//    val DEFAULT_SOUND_AUDIO_PATH: String
//        get() = context.getExternalFilesDir("Sounds/aicockpit")!!.absolutePath

    const val ACTION_REQUEST_THEME = "com.desaysv.AI_Cockpit.MY_ACTION"
    const val ACTION_REQUEST_SOUNDS = "com.desaysv.AI_Cockpit.MY_ACTION"
    const val ACTION_UPDATE_ELE_LAUNCHER = "com.desaysv.AI_Cockpit.UPDATE_LAUNCHER"
    const val ACTION_RECEIVE_SOUNDS = "com.desaysv.AI_Cockpit.SOUND_RESOURCE_CHANGED"
    const val ACTION_RECEIVE_THEME = "com.desaysv.ip.MY_ACTION"
    const val KEY_AI_PATH = "ai_path"
    const val TARGET_PACKAGE = "com.desaysv.wuji"
}

