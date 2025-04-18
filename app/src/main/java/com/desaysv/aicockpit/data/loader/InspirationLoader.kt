package com.desaysv.aicockpit.data.loader


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.loader.InspirationLoader.loadOnce
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

///storage/emula/Android/data/com.desaysv.wuji/files/config.txt
//val CONFIG_PATH="Android/data/com.desaysv.wuji/files/config.txt"
const val ACTION_DELETE_INSPIRAION_1="RECEIVER_VPA_TYPE_ACTION"
const val ACTION_DELETE_INSPIRAION_2="com.desaysv.sceneengine.ACTION_SCENE_CHANGE_TOAPP"
const val INSPIRAION_1_SCOPE="VPA_TYPE"
const val INSPIRAION_1_SCOPE_VAL="0"
const val INSPIRAION_2_SCOPE="data"
const val INSPIRAION_2_SCOPE_VAL="4"
object GlobalInspirationReceiverHolder {
    private var receiver: BroadcastReceiver? = null

    fun register(context: Context,onReceiveInspirationCallback:(List<ThemeItemData>)->Unit ) {
        if (receiver != null) return // 避免重复注册

        receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                Log.d("GlobalReceiver", "收到广播: ${intent?.action}")
                Log.printAllBundleExtra(intent)
                intent?.let {
                    Log.d("getString ${it.getStringExtra(INSPIRAION_2_SCOPE)} ${it.getIntExtra(INSPIRAION_2_SCOPE,-1)}")
                    Log.d("getString ${it.getStringExtra(INSPIRAION_1_SCOPE)} ${it.getIntExtra(INSPIRAION_1_SCOPE,-1)}")

                    val isIPAD2Change= it.action==ACTION_DELETE_INSPIRAION_2
                            && it.getStringExtra(INSPIRAION_2_SCOPE)!=INSPIRAION_2_SCOPE_VAL
                            && it.getStringExtra(INSPIRAION_2_SCOPE)!=null

                    val isLauncherChange= it.action==ACTION_DELETE_INSPIRAION_1
                            && it.getStringExtra(INSPIRAION_1_SCOPE)!=INSPIRAION_1_SCOPE_VAL
                            && it.getStringExtra(INSPIRAION_1_SCOPE)!=null

                    if(isIPAD2Change||isLauncherChange){
                        CoroutineScope(Dispatchers.IO).launch {
                            val data = loadOnce()
                            onReceiveInspirationCallback(data)
                        }
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(ACTION_DELETE_INSPIRAION_1)
            addAction(ACTION_DELETE_INSPIRAION_2)
        }

        ContextCompat.registerReceiver(context, receiver!!, filter, ContextCompat.RECEIVER_EXPORTED)
        Log.d("GlobalReceiver", "广播接收器已注册")
    }

}



object InspirationLoader :ResourceLoader<ThemeItemData>{

    //无需load,直接从数据库读就可以了
    override suspend fun loadOnce(): List<ThemeItemData>
            =loadFromJSON()



    override fun observe(context: Context): Flow<List<ThemeItemData>> = callbackFlow {
//        val filter = IntentFilter()
//        filter.addAction(ACTION_DELETE_INSPIRAION_1)
//        filter.addAction(ACTION_DELETE_INSPIRAION_2)
//
//        val receiver = object : BroadcastReceiver() {
//            override fun onReceive(ctx: Context?, intent: Intent?) {
//                Log.d("Broadcast received, delete all inspiration")
//                try {
//                    // 加载数据并发送到 flow
//                    intent?.let {
//                        val isIPAD2Change= it.action=="com.desaysv.sceneengine.ACTION_SCENE_CHANGE_TOAPP"
//                                    && it.getStringExtra("data")!="4"
//                        val isLauncherChange= it.action=="RECEIVER_VPA_TYPEATION"
//                                    && it.getStringExtra("VPA_TYPE")!="0"
//                        if(isIPAD2Change||isLauncherChange){
//                            CoroutineScope(Dispatchers.IO).launch {
//                                val data = loadOnce()
//                                trySend(data).isSuccess
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()                    }
//            }
//        }
//
//        Log.d("注册广播接受器")
//        ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_EXPORTED)
//        awaitClose {
//            context.unregisterReceiver(receiver)
//            Log.d("注销 InspirationLoader 广播接受器")
//        }
    }

    //置空了
suspend fun loadFromJSON()= withContext(Dispatchers.IO){
            emptyList<ThemeItemData>()
    }
}