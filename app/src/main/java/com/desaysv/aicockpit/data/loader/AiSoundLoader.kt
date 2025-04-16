package com.desaysv.aicockpit.data.loader
 

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.business.ImageConstants.CONFIG_SOUNDS_PATH
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
            =loadSoundItemsFromConfig()


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
    val allSounds = listOf(
        SoundItemData(
            soundName = "布谷鸟",
            imageName = "布谷鸟",
            imgId = -1,
            imgPath = "29@2x.png",
            audioPath = "布谷鸟.mp3"
        ),
        SoundItemData(
            soundName = "花瓣",
            imageName = "花瓣",
            imgId = -1,
            imgPath = "20@2x.png",
            audioPath = "花瓣.mp3"
        ),
        SoundItemData(
            soundName = "树叶",
            imageName = "树叶",
            imgId = -1,
            imgPath = "26@2x.png",
            audioPath = "树叶.mp3"
        ),
        SoundItemData(
            soundName = "溪水",
            imageName = "溪水",
            imgId = -1,
            imgPath = "11@2x.png",
            audioPath = "溪水.mp3"
        ),
        SoundItemData(
            soundName = "树叶婆娑",
            imageName = "树叶婆娑",
            imgId = -1,
            imgPath = "8@2x.png",
            audioPath = "树叶婆娑.mp3"
        ),
        SoundItemData(
            soundName = "滴水",
            imageName = "滴水",
            imgId = -1,
            imgPath = "14@2x.png",
            audioPath = "滴水.wav"
        ),
        SoundItemData(
            soundName = "花叶",
            imageName = "花叶",
            imgId = -1,
            imgPath = "17@2x.png",
            audioPath = "花叶.mp3"
        ),
        SoundItemData(
            soundName = "晶体",
            imageName = "晶体",
            imgId = -1,
            imgPath = "23@2x.png",
            audioPath = "晶体.mp3"
        )
    )
    suspend fun loadSoundItemsFromConfig(): List<SoundItemData> = withContext(Dispatchers.IO) {
//        val configFile = File("/sdcard/test/config_sounds.txt")
        val configFile = File(
            Environment.getExternalStorageDirectory(), CONFIG_SOUNDS_PATH
//            CONFIG_ELE_PATH
        )

        if (!configFile.exists()) {
            Log.e("AISOUND", "Config file not found at: ${configFile.absolutePath}")
            Log.d("AISOUND", "default all sounds: ${allSounds.size}")
            return@withContext allSounds
        }

        val bitmask = configFile.readText().trim().toIntOrNull() ?: return@withContext emptyList()


        val selectedSounds = allSounds.filterIndexed { index, _ ->
            (bitmask shr index) and 1 == 1
        }

        Log.d("AISOUND", "Bitmask=$bitmask 选中了 ${selectedSounds.size} 个音频")
        return@withContext selectedSounds
    }

}