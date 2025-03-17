package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desaysv.aicockpit.data.ElectricityItemData

@Dao
interface ElectricityItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(electricityItem: ElectricityItemData): Long

    @Query("SELECT * FROM electricity_items WHERE id = :id")
    suspend fun getById(id: Int): ElectricityItemData?

    @Delete
    suspend fun delete(electricityItem: ElectricityItemData)
}