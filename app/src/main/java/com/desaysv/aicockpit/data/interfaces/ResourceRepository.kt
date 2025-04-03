package com.desaysv.aicockpit.data.interfaces

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

interface ResourceRepository<T> {
    //热流
    fun observeFlow(): Flow<T>
    //热流
    fun observeListFlow(): Flow<List<T>> { return emptyFlow() }
    //冷流
    suspend fun load(): List<T>

    //数据库crud
    suspend fun getAll(): List<T>
    suspend fun getByPath(p:String): T?
    suspend fun insert(item: T)
    suspend fun delete(item: T)
    suspend fun deleteAll()
    suspend fun saveAll(items: List<T>)

    //灵活处理特殊操作
    suspend fun agileOp(opId:Int, item: T? =null,id:Int=0)
}
