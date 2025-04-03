package com.desaysv.aicockpit.data.interfaces

import com.desaysv.aicockpit.data.ThemeItemData
import kotlinx.coroutines.flow.Flow

/**
 * flow用不着的，我不需要从flow中读数据
 * 我只需要从rep中的数据读就行
 * flow还肯能出现重复，丢失的一些问题
 */
interface ResourceUseCase<T> {
    val flow: Flow<List<T>>
    val rep: ResourceRepository<T>
    fun observe()
    fun load()
    fun clear()

}
