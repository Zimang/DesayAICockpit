package com.desay.desayaicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.desay.desayaicockpit.data.ThemeItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(theme: ThemeItemData): Long

    @Query("SELECT * FROM themes WHERE id = :id")
    suspend fun getById(id: Int): ThemeItemData?

    @Query("SELECT * FROM themes")
    fun getAllThemes(): Flow<List<ThemeItemData>>

    @Delete
    suspend fun delete(theme: ThemeItemData)
}
