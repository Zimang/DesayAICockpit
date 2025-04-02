package com.desaysv.aicockpit.demo.interfaces

import kotlinx.coroutines.flow.Flow

interface ResourceUseCase<T> {
    val flow: Flow<T>
    fun observe()
    fun load()
    fun clear()
}
