package com.desaysv.aicockpit.demo

import android.content.Context
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.db.SoundItemDao
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.interfaces.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

class SoundRepositoryV2(
    private val dao: SoundItemDao,
    private val loader: ResourceLoader<SoundItemData>,
    private val context: Context
) : ResourceRepository<SoundItemData> {

    override fun observeFlow(): Flow<SoundItemData> {
        return loader.observe(context).flatMapConcat { list ->
            list.asFlow() // 把 Flow<List<T>> 转为 Flow<T>
        }
    }

    override suspend fun load(): List<SoundItemData> {
        return loader.loadOnce()
    }

    override suspend fun getAll(): List<SoundItemData> {
        return dao.getAll()
    }

    override suspend fun insert(item: SoundItemData) {
        dao.insert(item)
    }

    override suspend fun delete(item: SoundItemData) {
        dao.delete(item)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun saveAll(items: List<SoundItemData>) {
        dao.deleteAll()
        items.forEach { dao.insert(it) }
    }
}
