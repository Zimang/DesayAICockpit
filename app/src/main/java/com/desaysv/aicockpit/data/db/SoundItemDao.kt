package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desaysv.aicockpit.data.SoundItemData

@Dao
interface SoundItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(soundItem: SoundItemData): Long

    @Query("SELECT * FROM sound_items")
    suspend fun getAll(): List<SoundItemData>

    @Query("DELETE FROM sound_items")
    suspend fun deleteAll()

    @Query("SELECT * FROM sound_items WHERE id = :id")
    suspend fun getById(id: Int): SoundItemData?
    @Query("SELECT * FROM sound_items WHERE imgPath = :p")
    suspend fun getByImgPath(p: String): SoundItemData?

    @Delete
    suspend fun delete(soundItem: SoundItemData)
}
