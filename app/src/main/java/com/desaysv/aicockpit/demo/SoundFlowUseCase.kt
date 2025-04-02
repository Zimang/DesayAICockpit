package com.desaysv.aicockpit.demo

import android.content.Context
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.demo.interfaces.ResourceRepository
import com.desaysv.aicockpit.demo.interfaces.ResourceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class SoundFlowUseCase(
    private val repository: ResourceRepository<SoundItemData>,
    private val context: Context
) : ResourceUseCase<SoundItemData> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _observeFlow = MutableSharedFlow<SoundItemData>(extraBufferCapacity = 64)
    private val _loadFlow = MutableSharedFlow<SoundItemData>(extraBufferCapacity = 64)

    private var observeJob: Job? = null

    override val flow: Flow<SoundItemData> = merge(_observeFlow, _loadFlow)


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
            val data = repository.load()
            data.forEach { _loadFlow.emit(it) }
        }
    }

    override fun clear() {
        observeJob?.cancel()
        observeJob = null
    }
}
