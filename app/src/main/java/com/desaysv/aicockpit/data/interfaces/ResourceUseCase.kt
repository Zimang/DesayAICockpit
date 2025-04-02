package com.desaysv.aicockpit.data.interfaces

import com.desaysv.aicockpit.data.ThemeItemData
import kotlinx.coroutines.flow.Flow

interface ResourceUseCase<T> {
    val flow: Flow<List<ThemeItemData>>
    fun observe()
    fun load()
    fun clear()
}
