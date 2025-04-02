package com.desaysv.aicockpit.demo.interfaces

import kotlinx.coroutines.flow.Flow

interface ResourceRepository<T> {
    //热流
    fun observeFlow(): Flow<T>
    //冷流
    suspend fun load(): List<T>

    //数据库crud
    suspend fun getAll(): List<T>
    suspend fun insert(item: T)
    suspend fun delete(item: T)
    suspend fun deleteAll()
    suspend fun saveAll(items: List<T>)
}