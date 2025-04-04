package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desaysv.aicockpit.data.SoundItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(soundItem: SoundItemData): Long

    @Query("SELECT * FROM sound_items")
    suspend fun getAll(): List<SoundItemData>
    @Query("SELECT * FROM sound_items")
    fun getAllSounds(): Flow<List<SoundItemData>>

    @Query("DELETE FROM sound_items")
    suspend fun deleteAll()

    @Query("SELECT * FROM sound_items WHERE id = :id")
    suspend fun getById(id: Int): SoundItemData?
    @Query("SELECT * FROM sound_items WHERE imgPath = :p")
    suspend fun getByImgPath(p: String): SoundItemData?
    @Query("DELETE FROM sound_items WHERE id != 1")
    suspend fun deleteAllButDefault()

    @Delete
    suspend fun delete(soundItem: SoundItemData)
}
