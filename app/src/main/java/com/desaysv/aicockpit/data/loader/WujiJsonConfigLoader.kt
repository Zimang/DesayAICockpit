package com.desaysv.aicockpit.data.loader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.ListTheme
import com.desaysv.aicockpit.data.ThemeItemData
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

fun checkConfigDirState(): String {
    val configDir = File(Environment.getExternalStorageDirectory()?.parent ?: "", "files")
    return buildString {
        appendLine("路径: ${configDir.absolutePath}")
        appendLine("目录存在: ${configDir.exists()}")
        appendLine("可读: ${configDir.canRead()}")
        appendLine("可写: ${configDir.canWrite()}")
    }
}

//  val CONFIG_PATH="/test/config.txt"
val CONFIG_PATH="Android/data/com.desaysv.wuji/files/config.txt"
object WujiJsonConfigLoader :ResourceLoader<ThemeItemData>{

    override suspend fun loadOnce(): List<ThemeItemData>
        =loadFromJSON()


    override fun observe(context: Context): Flow<List<ThemeItemData>> = callbackFlow {
         val filter = IntentFilter("com.desaysv.AI_Cockpit.MY_ACTION")

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
                Log.d("WujiJsonConfigLoader", "BroadcastReceiver unregistered")
            }
    }

    suspend fun loadFromJSON()= withContext(Dispatchers.IO){
        Log.d("请求解析WUJI JSON")
        //这个路径有问题
        val configFile = File(
            Environment.getExternalStorageDirectory(),CONFIG_PATH
        )
        checkConfigDirState()
        if (!configFile.exists()) {
            Log.d("Config file not found at: ${configFile.absolutePath}")
            return@withContext emptyList<ThemeItemData>()
        }
        return@withContext try {
            FileReader(configFile).use { reader ->
                val type = object : TypeToken<MutableList<ListTheme>>() {}.type
                val themes: MutableList<ListTheme> = Gson().fromJson(reader, type)
                Log.d(themes.toString())
                themes.map {
                    ThemeItemData(
                        themeName = it.title,
                        imgPath = it.wallpaperPath,
                    )
                }.toList()
            }
        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }
}