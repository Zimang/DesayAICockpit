package com.desaysv.aicockpit.data.loader

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Context
import android.content.Intent
import com.desaysv.aicockpit.data.ThemeItemData
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

@RunWith(AndroidJUnit4::class)
class WujiJsonConfigLoaderAndroidTest {

    @Test
    fun testObserveEmitsDataAfterBroadcast() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val loader = WujiJsonConfigLoader()

        var receivedData: List<ThemeItemData>? = null
        val job = launch {
            loader.observe(context)
                .take(1) // 只取一次事件，避免无限收集
                .collectLatest { data ->
                    receivedData = data
                    println("received ${data.size} items from observe()")
                }
        }

        delay(300) // 等待 receiver 注册完成

        // 模拟广播发送
        context.sendBroadcast(Intent("com.desaysv.AI_Cockpit.MY_ACTION"))

        job.join()

        assertNotNull(receivedData) // 应该有数据
        // 可选：验证实际数据内容
    }
}