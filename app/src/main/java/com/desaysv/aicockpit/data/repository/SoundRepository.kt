package com.desaysv.aicockpit.data.repository

import android.content.Context
import com.desaysv.aicockpit.business.ImageConstants
import com.desaysv.aicockpit.business.ImageManager
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.db.SoundItemDao
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.interfaces.ResourceRepository
import com.desaysv.aicockpit.data.loader.SoundLoader
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

/**
 * 职责
 */
class SoundRepository(
    private val soundItemDao: SoundItemDao,
    private val context: Context,
    private val resourceLoader: ResourceLoader<SoundItemData> = SoundLoader
) :ResourceRepository<SoundItemData>{


    val allSounds: Flow<List<SoundItemData>> = soundItemDao.getAllSounds()

    private val _dataUpdated = MutableSharedFlow<Unit>(replay = 0)
    val dataUpdated: SharedFlow<Unit> = _dataUpdated



    //封装的行为
    //hai
    suspend fun tryUpdateData(): Boolean {
        val broadcastSuccess = updateFromBroadcastPath()
        if (broadcastSuccess) return true

        val defaultSuccess = updateFromDefaultPath()
        if (defaultSuccess) return true

        return getAllSounds().isNotEmpty()
    }

    //主动请求后接收并处理广播接收的指定路径下的图片
    //等待时间是1.5s
    private suspend fun updateFromBroadcastPath(): Boolean {
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

    //从默认文件夹读取文件，写入数据库
    private suspend fun updateFromDefaultPath(): Boolean {
        val images = ImageManager.getLocalImagesFromPath(ImageConstants.DEFAULT_PATH)
        return if (images.isNotEmpty()) {
            saveSoundItems(images)
            true
        } else false
    }

    //写入数据到数据库
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


    override fun observeFlow(): Flow<SoundItemData> {
        return resourceLoader.observe(context).flatMapConcat { it.asFlow() }
    }

    override fun observeListFlow(): Flow<List<SoundItemData>> {
        return resourceLoader.observe(context)
    }

    override suspend fun load(): List<SoundItemData> {
        Log.d("rep load request")
        return resourceLoader.loadOnce()
    }

    override suspend fun getAll(): List<SoundItemData> {
        return soundItemDao.getAll()
    }

    override suspend fun getByPath(p: String): SoundItemData? {
        return soundItemDao.getByImgPath(p)
    }

    override suspend fun deleteAll() {
        soundItemDao.deleteAll()
    }

    override suspend fun deleteAllButDefault() {
        soundItemDao.deleteAllButDefault()
    }

    override suspend fun agileOp(opId: Int, item: SoundItemData?, id: Int) {
        Log.d("暂不开放")
    }

    override suspend fun saveAll(items: List<SoundItemData>) {
        soundItemDao.deleteAllButDefault()
        items.forEach{
            soundItemDao.insert(it)
        }
    }

    override suspend fun delete(item: SoundItemData) {
        soundItemDao.delete(item)
    }

    override suspend fun insert(item: SoundItemData) {
        soundItemDao.insert(item)
    }


}
