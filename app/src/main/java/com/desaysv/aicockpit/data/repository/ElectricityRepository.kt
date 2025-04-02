package com.desaysv.aicockpit.data.repository

import android.content.Context
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.db.ElectricityItemDao
import com.desaysv.aicockpit.data.db.ThemeItemDao
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.interfaces.ResourceRepository
import com.desaysv.aicockpit.data.loader.AvailablePicsLoader
import com.desaysv.aicockpit.data.loader.WujiJsonConfigLoader
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

class ElectricityRepository(
    private val electricityItemDao: ElectricityItemDao,
    private val context: Context,
    private val loader: ResourceLoader<ElectricityItemData> = AvailablePicsLoader(),
) :ResourceRepository<ElectricityItemData> {

    val allEles:Flow<List<ElectricityItemData>> =electricityItemDao.getAllEles()


    override fun observeFlow(): Flow<ElectricityItemData> {
        return loader.observe(context).flatMapConcat { it.asFlow() }
    }

    override suspend fun load(): List<ElectricityItemData> {
        return loader.loadOnce()
    }

    override suspend fun getAll(): List<ElectricityItemData> {
        return electricityItemDao.getAll()
    }

    override suspend fun deleteAll() {
        electricityItemDao.deleteAll()
    }

    override suspend fun agileOp(opId: Int, item: ElectricityItemData?, id: Int) {
        Log.d("电，没有拓展方法")
    }

    override suspend fun saveAll(items: List<ElectricityItemData>) {
        electricityItemDao.deleteAll()
        items.forEach{electricityItemDao.insert(it)}
    }

    override suspend fun delete(item: ElectricityItemData) {
        electricityItemDao.insert(item)
    }

    override suspend fun insert(item: ElectricityItemData) {
        TODO("Not yet implemented")
    }
}