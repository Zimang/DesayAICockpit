package com.desaysv.aicockpit.data.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.desaysv.aicockpit.business.ImageConstants
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

val OP_ID_GET_BY_IMGE_SOURCE=1

class ElectricityRepository(
    private val electricityItemDao: ElectricityItemDao,
    private val context: Context,
    private val loader: ResourceLoader<ElectricityItemData> = AvailablePicsLoader(),
) :ResourceRepository<ElectricityItemData> {

    val allEles:Flow<List<ElectricityItemData>> =electricityItemDao.getAllEles()


    override fun observeFlow(): Flow<ElectricityItemData> {
        return loader.observe(context).flatMapConcat { it.asFlow() }
    }

    override fun observeListFlow(): Flow<List<ElectricityItemData>> {
        return loader.observe(context)
    }

    override suspend fun load(): List<ElectricityItemData> {
        return loader.loadOnce()
    }

    override suspend fun getAll(): List<ElectricityItemData> {
        return electricityItemDao.getAll()
    }

    override suspend fun getByPath(p: String): ElectricityItemData? {
        return electricityItemDao.getByPath(p)
    }

    override suspend fun deleteAll() {
        electricityItemDao.deleteAll()
    }

    override suspend fun deleteAllButDefault() {
        electricityItemDao.deleteAllButDefault()
    }

    override suspend fun agileOp(opId: Int, item: ElectricityItemData?, id: Int) {
        Log.d("电，没有拓展方法")
        when(opId){
            OP_ID_GET_BY_IMGE_SOURCE->
            {}
            else->
            {}
        }
    }

    override suspend fun saveAll(items: List<ElectricityItemData>) {
        electricityItemDao.deleteAllButDefault()
        items.forEach{
            try {
                electricityItemDao.insert(it)
            } catch (e: SQLiteConstraintException) {
                Log.e("RoomInsert", "约束冲突：${it.imgPath}")
            }

//            electricityItemDao.insert(it)
        }
    }

    override suspend fun delete(item: ElectricityItemData) {
        electricityItemDao.delete(item)
    }

    override suspend fun insert(item: ElectricityItemData) {
        try {
            electricityItemDao.insert(item)
        } catch (e: SQLiteConstraintException) {
            Log.e("RoomInsert", "约束冲突：${item.imgPath}")
        }

//        electricityItemDao.insert(item)
    }

    // **确保至少有一个主题存在--默认主题**
    suspend fun ensureEleExists() {
        val defaultCount = electricityItemDao.countEles()
        if (defaultCount == 0) {
            // 插入默认主题
            electricityItemDao.insert(
                ElectricityItemData(
                    id = 1,
                    imageName = "默认图片",
                    themeName = "默认主题",
                    imgId = 1,
                    imgPath = ImageConstants.DEFAULT_SOUND_PICS_PATH+"/df.png"
                )
            )
        }
    }


}