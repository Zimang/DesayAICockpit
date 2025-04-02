package com.desaysv.aicockpit.data.interfaces

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ResourceLoader<T> {
    //主动请求
    suspend fun loadOnce(): List<T>
    //被动接收
    fun observe(context: Context): Flow<List<T>>
}
