package com.desaysv.aicockpit.data.usecase

import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class WujiThemeUseCaseImpl(
    private val repository: ThemeRepository
):ResourceUseCase<ThemeItemData> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _observeFlow = MutableSharedFlow<ThemeItemData>(extraBufferCapacity = 64)
    private val _loadFlow = MutableSharedFlow<ThemeItemData>(extraBufferCapacity = 64)

    override val flow: Flow<ThemeItemData> = merge(_observeFlow, _loadFlow)

    private var observeJob: Job? = null

    override fun observe() {
        if (observeJob != null) return
        observeJob = scope.launch {
            repository.observeFlow().collect {
                _observeFlow.emit(it)
            }
        }
    }

    override fun load() {
        scope.launch {
            repository.load().forEach {
                _loadFlow.emit(it)
                Log.d(it.toString())
            }
        }
    }

    override fun clear() {
        observeJob?.cancel()
        observeJob = null
    }
}