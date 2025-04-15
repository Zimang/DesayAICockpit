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

///storage/emula/Android/data/com.desaysv.wuji/files/config.txt
//val CONFIG_PATH="Android/data/com.desaysv.wuji/files/config.txt"
const val ACTION_DELETE_INSPIRAION_1="RECEIVER_VPA_TYPE_ACTION"
const val ACTION_DELETE_INSPIRAION_2="com.desaysv.sceneengine.ACTION_SCENE_CHANGE_TOAPP"
object InspirationLoader :ResourceLoader<ThemeItemData>{

    //无需load,直接从数据库读就可以了
    override suspend fun loadOnce(): List<ThemeItemData>
            =loadFromJSON()


    override fun observe(context: Context): Flow<List<ThemeItemData>> = callbackFlow {
        val filter = IntentFilter()
        filter.addAction(ACTION_DELETE_INSPIRAION_1)
        filter.addAction(ACTION_DELETE_INSPIRAION_2)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d("Broadcast received, delete all inspiration")
                try {
                    // 加载数据并发送到 flow

                    if(intent?.getIntExtra("VPA_TYPE",0)!=0){
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = loadOnce()
                            trySend(data).isSuccess
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()                    }
            }
        }

        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(receiver)
            Log.d("InspirationLoader", "BroadcastReceiver unregistered")
        }
    }

    //置空了
suspend fun loadFromJSON()= withContext(Dispatchers.IO){
            emptyList<ThemeItemData>()
    }
}