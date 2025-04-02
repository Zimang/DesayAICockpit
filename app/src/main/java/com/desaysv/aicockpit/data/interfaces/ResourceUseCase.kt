package com.desaysv.aicockpit.data.interfaces

import com.desaysv.aicockpit.data.ThemeItemData
import kotlinx.coroutines.flow.Flow

interface ResourceUseCase<T> {
    val flow: Flow<List<T>>
    val rep: ResourceRepository<T>
    fun observe()
    fun load()
    fun clear()

}
