package com.desaysv.aicockpit.data.loader

import android.content.Context
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileFilter

enum class ResourceType {
    SOUND_RESOURCE,  // 需要处理音频文件，图片文件
    THEME_RESOURCE,  // 需要处理图片文件
    ELE_RESOURCE,  // 需要处理图片文件
}

/**
 * FolderFileLoader主要监听一个路径值，路径变动就需要重新加载
 */
class FolderFileLoader<T>(
    val type: ResourceType,
    var picPath:String="",
    var audioPath:String=""
) :ResourceLoader<T> {

    override suspend fun loadOnce(): List<T> {
        Log.d("loader load request FolderFileLoader")
        return when(type){
            ResourceType.SOUND_RESOURCE-> loadSoundItems()
            ResourceType.THEME_RESOURCE-> emptyList<T>()
            ResourceType.ELE_RESOURCE-> emptyList<T>()
        }
    }

    override fun observe(context: Context): Flow<List<T>> {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadSoundItems(): List<T> {
        val picDir = File(picPath)
        val audioDir = File(audioPath)

        if (!picDir.exists() || !audioDir.exists()) {
            Log.d("音频文件夹 $audioDir ${audioDir.exists()}")
            Log.d("图片文件夹 $picDir ${picDir.exists()}")
            Log.d("sounds 的图片与音频文件夹有一个不可访问")
            return emptyList()
        }

        val imageFiles = picDir.listFiles { f -> f.extension.lowercase() in listOf("png", "jpg", "jpeg") } ?: return emptyList()
        val audioFiles = audioDir.listFiles { f -> f.extension.lowercase() in listOf("wav", "mp3") } ?: return emptyList()

        val imageMap = imageFiles.associateBy { it.nameWithoutExtension }
        val audioMap = audioFiles.associateBy { it.nameWithoutExtension }

        val commonNames = imageMap.keys.intersect(audioMap.keys)
        if (commonNames.size==0){
            Log.d("Sounds的音频与图片名称不匹配,不会存入数据库")
        }

        val result = commonNames.map { name ->
            SoundItemData(
                id = 0,
                imageName = name,
                soundName = name,
                imgPath = imageMap[name]!!.absolutePath,
                audioPath = audioMap[name]!!.absolutePath
            )
        }

        return result as List<T>
    }


    @Suppress("UNCHECKED_CAST")
    private fun loadThemeItems(): List<T> {
        val picDir = File(picPath)
        if (!picDir.exists()) return emptyList()
        val fileFilter :FileFilter= FileFilter {
            it.extension.lowercase() in listOf("png", "jpg", "jpeg")
        }
        val imageFiles = picDir.listFiles (fileFilter)

        val result = imageFiles.map { file ->
            ThemeItemData(
                themeName = file.nameWithoutExtension,
                imgPath = file.absolutePath
            )
        }

        return result as List<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadElectricityItems(): List<T> {
        val picDir = File(picPath)
        if (!picDir.exists()) return emptyList()

        val fileFilter :FileFilter= FileFilter {
            it.extension.lowercase() in listOf("png", "jpg", "jpeg")
        }
        val imageFiles = picDir.listFiles (fileFilter)
        val result = imageFiles.map { file ->
            ElectricityItemData(
                imageName = file.name,
                themeName = file.nameWithoutExtension,
                imgId = 0,
                imgPath = file.absolutePath
            )
        }

        return result as List<T>
    }

}