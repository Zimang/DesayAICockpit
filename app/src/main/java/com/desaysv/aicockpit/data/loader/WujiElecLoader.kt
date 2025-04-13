package com.desaysv.aicockpit.data.loader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.ElectricityItemData
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

object WujiElecLoader : ResourceLoader<ElectricityItemData> {

    override suspend fun loadOnce(): List<ElectricityItemData>
            =loadFromJSON()


    override fun observe(context: Context): Flow<List<ElectricityItemData>> = callbackFlow {
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
            return@withContext emptyList<ElectricityItemData>()
        }
        return@withContext try {
            FileReader(configFile).use { reader ->
                val type = object : TypeToken<MutableList<ListTheme>>() {}.type
                val themes: MutableList<ListTheme> = Gson().fromJson(reader, type)
                Log.d(themes.toString())
                themes.map {
                    ElectricityItemData(
                        id = 0,
                        imageName = it.title,
                        themeName =it.title,
                        imgPath = it.wallpaperPath,
                        icon1 =it.icon1,
                        icon2 = it.icon2,
                        icon3 =it.icon3,
                        icon4 =it.icon4,
                        icon5 = it.icon5,
                        icon1_Path = it.icon1_Path,
                        icon2_Path = it.icon2_Path,
                        icon3_Path =it.icon3_Path,
                        icon4_Path =it.icon4_Path,
                        icon5_Path = it.icon5_Path,
                        layoutType = it.layoutType,
                    )
                }.toList<ElectricityItemData>()
            }
        } catch (e: Exception) {

            e.printStackTrace()
            emptyList<ElectricityItemData>()
        }
    }
}