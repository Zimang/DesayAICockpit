package com.desaysv.aicockpit.data.repository

import com.desaysv.aicockpit.business.ImageConstants
import com.desaysv.aicockpit.business.ImageManager
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.db.SoundItemDao
import java.io.File

class SoundRepository(
    private val soundItemDao: SoundItemDao
) {

    suspend fun updateFromBroadcastPath() {
        val defaultPath = ImageConstants.DEFAULT_PATH
        val images = ImageManager.getLocalImagesFromPath(defaultPath)

        val soundItems = images.mapIndexed { index, path ->
            SoundItemData(
                id = 0,
                imageName = File(path).name,
                soundName = File(path).nameWithoutExtension,
                imgId = 22, //之后会用到本地图片
                imgPath = path
            )
        }

        soundItems.forEach { soundItemDao.insert(it) }
    }

    suspend fun getAllSounds(): List<SoundItemData> {
        return soundItemDao.getAll()
    }
    suspend fun getSoundById(id:Int): SoundItemData? {
        return soundItemDao.getById(id)
    }

    suspend fun clearAllSounds() {
        soundItemDao.deleteAll()
    }
}
