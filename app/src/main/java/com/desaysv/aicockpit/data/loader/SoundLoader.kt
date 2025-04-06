package com.desaysv.aicockpit.data.loader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.business.ImageConstants
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

/**
 *
 */
class SoundLoader:ResourceLoader<SoundItemData> {

    var folderFileLoader =FolderFileLoader<SoundItemData>(
        ResourceType.SOUND_RESOURCE,
        ImageConstants.DEFAULT_SOUND_PICS_PATH,
        ImageConstants.DEFAULT_SOUND_AUDIO_PATH,
    )

    override suspend fun loadOnce(): List<SoundItemData> {
        Log.d("loader load request sound")
        return folderFileLoader.loadOnce()
    }

    //接收到路径变更广播后，更新数据并返回
    override fun observe(context: Context): Flow<List<SoundItemData>> = callbackFlow {
        val filter = IntentFilter(ImageConstants.ACTION_RECEIVE_SOUNDS)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d("Broadcast received, reloading sounds")

//                val a_path= intent?.getStringExtra("audio_path")?: folderFileLoader.audioPath
//                val p_path= intent?.getStringExtra("pic_path") ?: folderFileLoader.picPath
                intent?.getStringExtra("audio_path")?.let{
                    folderFileLoader.audioPath=it
                }
                intent?.getStringExtra("pic_path")?.let{
                    folderFileLoader.picPath=it
                }
                try {
                    // 加载数据并发送到 flow
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = folderFileLoader.loadOnce()
                        trySend(data).isSuccess
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(receiver)
            Log.d("Sounds Loader", "BroadcastReceiver unregistered")
        }
    }
}