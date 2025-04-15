package com.desaysv.aicockpit.data.loader
 

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
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
    val allSounds = listOf(
        SoundItemData(
            soundName = "鸟叫",
            imageName = "鸟叫",
            imgId = -1,
            imgPath = "鸟叫.png",
            audioPath = "鸟叫.mp3"
        ),
        SoundItemData(
            soundName = "跑步",
            imageName = "跑步",
            imgId = -1,
            imgPath = "跑步.png",
            audioPath = "跑步.wav"
        ),
        SoundItemData(
            soundName = "汽车鸣笛",
            imageName = "汽车鸣笛",
            imgId = -1,
            imgPath = "汽车鸣笛.png",
            audioPath = "汽车鸣笛.mp2"
        ),
        SoundItemData(
            soundName = "瀑布",
            imageName = "瀑布",
            imgId = -1,
            imgPath = "瀑布.png",
            audioPath = "瀑布.mp3"
        ),
        SoundItemData(
            soundName = "鸟群拍打翅膀",
            imageName = "鸟群拍打翅膀",
            imgId = -1,
            imgPath = "鸟群拍打翅膀.png",
            audioPath = "鸟群拍打翅膀.mp3"
        ),
        SoundItemData(
            soundName = "鹿",
            imageName = "鹿",
            imgId = -1,
            imgPath = "鹿.png",
            audioPath = "鹿.mp3"
        ),
        SoundItemData(
            soundName = "丛林",
            imageName = "丛林",
            imgId = -1,
            imgPath = "丛林.png",
            audioPath = "丛林.wav"
        ),
        SoundItemData(
            soundName = "奶牛",
            imageName = "奶牛",
            imgId = -1,
            imgPath = "奶牛.png",
            audioPath = "奶牛.mp3"
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