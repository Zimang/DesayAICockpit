package com.desaysv.aicockpit.data.loader

import com.desaysv.aicockpit.data.ThemeItemData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class WujiJsonConfigLoaderTest {
    @Before
    fun disableLog() {
        com.desaysv.aicockpit.utils.Log.debugEnabled = false
    }
    @Test
    fun `loadOnce should parse valid config JSON correctly`() = runBlocking {
        // 准备一个临时 JSON 文件
        val tempFile = File.createTempFile("config", ".txt")
        tempFile.writeText(
            """
            [
              {
                "wallpaperPath": "theme1.jpg",
                "title": "Theme One"
              },
              {
                "wallpaperPath": "theme2.jpg",
                "title": "Theme Two"
              }
            ]
            """.trimIndent()
        )

        // 创建 loader，并注入自定义路径
        val loader = WujiJsonConfigLoader(configPath = tempFile.absolutePath)

        // 执行测试方法
        val result: List<ThemeItemData> = loader.loadOnce()

        // 断言
        assertEquals(2, result.size)
        assertEquals("Theme One", result[0].themeName)
        assertEquals("theme1.jpg", result[0].imgPath)

        assertEquals("Theme Two", result[1].themeName)
        assertEquals("theme2.jpg", result[1].imgPath)

        tempFile.deleteOnExit() // 测试结束删除文件
    }

    @Test
    fun `loadOnce should return empty list for invalid path`() = runBlocking {
        val loader = WujiJsonConfigLoader(configPath = "nonexistent/path.json")
        val result = loader.loadOnce()
        assertTrue(result.isEmpty())
    }
}