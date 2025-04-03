package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desaysv.aicockpit.data.ElectricityItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectricityItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(electricityItem: ElectricityItemData): Long

    @Query("SELECT * FROM electricity_items WHERE id = :id")
    suspend fun getById(id: Int): ElectricityItemData?

    @Query("SELECT * FROM electricity_items WHERE imgPath = :imgPath")
    suspend fun getByPath(imgPath: String): ElectricityItemData?

    @Delete
    suspend fun delete(electricityItem: ElectricityItemData)


    // 插入多条（忽略重复）
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllRaw(items: List<ElectricityItemData>): List<Long>


    // 👉 封装调用接口：批量插入（去重）
    suspend fun saveAll(items: List<ElectricityItemData>) {
        val existingPaths = getAllPaths().toSet()
        val newItems = items.filter { it.imgPath !in existingPaths }
        insertAllRaw(newItems)
    }


    // 获取所有 imgPath
    @Query("SELECT imgPath FROM electricity_items")
    suspend fun getAllPaths(): List<String>

    // 查询全部
    @Query("SELECT * FROM electricity_items")
    fun getAllEles(): Flow<List<ElectricityItemData>>


    // 查询全部
    @Query("SELECT * FROM electricity_items")
    suspend fun getAll(): List<ElectricityItemData>


    // 删除全部
    @Query("DELETE FROM electricity_items")
    suspend fun deleteAll()
}