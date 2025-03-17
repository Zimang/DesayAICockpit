package com.desay.desayaicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desay.desayaicockpit.data.SoundItemData

@Dao
interface SoundItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(soundItem: SoundItemData): Long

    @Query("SELECT * FROM sound_items WHERE id = :id")
    suspend fun getById(id: Int): SoundItemData?

    @Delete
    suspend fun delete(soundItem: SoundItemData)
}