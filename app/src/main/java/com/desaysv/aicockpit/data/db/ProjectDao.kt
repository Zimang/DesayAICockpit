package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun getAllProjects(): Flow<List<Project>>
}