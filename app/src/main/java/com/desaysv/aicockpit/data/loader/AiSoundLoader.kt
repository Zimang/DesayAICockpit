package com.desaysv.aicockpit.data.loader
 

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.ListSound
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.utils.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
 
object AiSoundLoader :ResourceLoader<SoundItemData>{

    override suspend fun loadOnce(): List<SoundItemData>
            =loadFromJSON()


    override fun observe(context: Context): Flow<List<SoundItemData>> = callbackFlow {
        val filter = IntentFilter("com.desaysv.AI_Cockpit.UPDATE_AI_SOUNDS")

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d("Broadcast received, reloading config")
                try {
                    // 加载数据并发送到 flow
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = loadOnce()
                        trySend(data).isSuccess
                    }
                } catch (e: Exception) {
                    e.printStackTrace()                    }
            }
        }

        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(receiver)
            Log.d("AiSoundLoader", "BroadcastReceiver unregistered")
        }
    }
    //android.permission.WRITE_SECURE_SETTINGS
    suspend fun loadFromJSON()= withContext(Dispatchers.IO){
        Log.d("请求解析AISOUND JSON")
        //这个路径有问题
        val configFile = File(
//            Environment.getExternalStorageDirectory(),CONFIG_PATH
            CONFIG_SOUNDS_PATH
        )
        checkConfigDirState()
        if (!configFile.exists()) {
            Log.d("Config file not found at: ${configFile.absolutePath}")
            return@withContext emptyList<SoundItemData>()
        }
        return@withContext try {
            FileReader(configFile).use { reader ->
                val type = object : TypeToken<MutableList<ListSound>>() {}.type
                val themes: MutableList<ListSound> = Gson().fromJson(reader, type)
                Log.d(themes.toString())
                themes.map {
                    SoundItemData(
                        soundName = it.title,
                        imageName = it.title,
                        imgPath = it.wallpaperPath,
                        audioPath = it.audioPath,
                    )
                }.toList()
            }
        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }
}