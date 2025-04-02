package com.desaysv.aicockpit.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.demo.interfaces.ResourceLoader
import com.desaysv.aicockpit.utils.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

class BroadcastResourceLoader(
    private val context: Context
) : ResourceLoader<SoundItemData> {

    private val acceptAction = "top.zimang.BROADCAST_RESPONSE"
    private val acquireAction = "top.zimang.REQUEST_LOAD"

    override suspend fun loadOnce(): List<SoundItemData> =
        receiveBroadcastOnce()

    override fun observe(context: Context): Flow<List<SoundItemData>> = callbackFlow {
        val filter = IntentFilter(acceptAction)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val list = parseResult(intent)
                var recv= mutableListOf<SoundItemData>()
                for (item in list){
                    val rItem=item.copy(soundName = "被动接收")
                    recv.add(rItem)
                }
                trySend(recv).isSuccess
            }
        }

        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }



    private suspend fun receiveBroadcastOnce(): List<SoundItemData> = suspendCancellableCoroutine { cont ->
        val filter = IntentFilter(acceptAction)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val result = parseResult(intent)
                cont.resume(result) {}
                context.unregisterReceiver(this)
            }
        }
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)

        context.sendBroadcast(Intent(acquireAction))
        Log.d("发送广播 $acquireAction")
    }



    private fun parseResult(intent: Intent?): List<SoundItemData> {
        val json = intent?.getStringExtra("dataListJson") ?: return emptyList()
        return try {
            val type = object : TypeToken<List<SoundItemData>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}