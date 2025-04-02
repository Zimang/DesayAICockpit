package com.desaysv.aicockpit.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.utils.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull

data class BroadcastPayload(
    val source: String,
    val items: List<SoundItemData>
)

class BroadcastResourceLoader(
    private val context: Context
) : ResourceLoader<SoundItemData> {

    private val acceptAction = "top.zimang.BROADCAST_RESPONSE"
    private val acquireAction = "top.zimang.REQUEST_LOAD"

    override suspend fun loadOnce(): List<SoundItemData> =withTimeoutOrNull(2000L) {
        receiveBroadcastOnce()
    } ?: emptyList()

    override fun observe(context: Context): Flow<List<SoundItemData>> = callbackFlow {
        val filter = IntentFilter(acceptAction)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val payload = parseResult(intent)
                if (payload.source == "observe") {
                    var recv= mutableListOf<SoundItemData>()
                    for (item in payload.items){
                        val rItem=item.copy(soundName = "被动接收")
                        recv.add(rItem)
                    }
                    trySend(recv)
                }
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
            override fun onReceive(ctx: Context?, intent: Intent?) {            val payload = parseResult(intent)
                if (payload.source == "load") {
                    cont.resume(payload.items) {}
                    context.unregisterReceiver(this)
                }
            }
        }
        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)

        context.sendBroadcast(Intent(acquireAction))
        Log.d("发送广播 $acquireAction")
    }



    fun parseResult(intent: Intent?): BroadcastPayload {
        val json = intent?.getStringExtra("dataListJson") ?: return BroadcastPayload("unknown", emptyList())
        val source = intent?.getStringExtra("source") ?: "unknown"
        val items = try {
            val type = object : TypeToken<List<SoundItemData>>() {}.type
            Gson().fromJson<List<SoundItemData>>(json, type)
        } catch (e: Exception) {
            emptyList()
        }
        return BroadcastPayload(source, items)
    }

}