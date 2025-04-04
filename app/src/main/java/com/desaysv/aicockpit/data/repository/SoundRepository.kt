package com.desaysv.aicockpit.data.repository

import android.content.Context
import com.desaysv.aicockpit.business.ImageConstants
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
