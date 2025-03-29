package com.desaysv.aicockpit.data.repository

import android.content.Context
import com.desaysv.aicockpit.business.ImageConstants
import com.desaysv.aicockpit.business.ImageManager
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.db.SoundItemDao
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

/**
 * todo 加入支持以下逻辑的接口
 * 1. 尝试使用最新数据 （首先使用广播获取指定路径，其次使用默认路径更新）
 * 2. 没有最新数据或者最新数据没有更新，使用数据库存储的数据
 * 3. 数据有问题->UI根据反馈展示加载组件
 */
class SoundRepository(
    private val soundItemDao: SoundItemDao
) {

    private val _dataUpdated = MutableSharedFlow<Unit>(replay = 0)
    val dataUpdated: SharedFlow<Unit> = _dataUpdated

    suspend fun tryUpdateData(context: Context): Boolean {
        val broadcastSuccess = updateFromBroadcastPath(context)
        if (broadcastSuccess) return true

        val defaultSuccess = updateFromDefaultPath()
        if (defaultSuccess) return true

        return getAllSounds().isNotEmpty()
    }

    //主动请求后接收并处理处理
    private suspend fun updateFromBroadcastPath(context: Context): Boolean {
        val completable = CompletableDeferred<List<String>>()

        withContext(Dispatchers.Main) {
            ImageManager.loadThemeImages(context) { result ->
                completable.complete(result)
            }
        }

        val result = try {
            withTimeoutOrNull(1500L) { completable.await() } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        return if (result.isNotEmpty()) {
            saveSoundItems(result)
            true
        } else false
    }

    private suspend fun updateFromDefaultPath(): Boolean {
        val images = ImageManager.getLocalImagesFromPath(ImageConstants.DEFAULT_PATH)
        return if (images.isNotEmpty()) {
            saveSoundItems(images)
            true
        } else false
    }

    private suspend fun saveSoundItems(paths: List<String>) {
        val soundItems = paths.map { path ->
            SoundItemData(
                id = 0,
                imageName = File(path).name,
                soundName = File(path).nameWithoutExtension,
                imgId = 22,
                imgPath = path
            )
        }

        soundItemDao.deleteAll()
        soundItems.forEach { soundItemDao.insert(it) }
        _dataUpdated.emit(Unit)
    }

    suspend fun getAllSounds(): List<SoundItemData> = soundItemDao.getAll()


    // 新增：根据广播中传递的文件夹路径更新数据
    suspend fun updateFromFolderPath(folderPath: String): Boolean {
        val images = ImageManager.getLocalImagesFromPath(folderPath)
        return if (images.isNotEmpty()) {
            saveSoundItems(images)
            true
        } else {
            false
        }
    }
}
