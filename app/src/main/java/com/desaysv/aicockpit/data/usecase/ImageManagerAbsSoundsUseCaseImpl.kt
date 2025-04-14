package com.desaysv.aicockpit.data.usecase

import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceRepository
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.loader.SoundLoader
import com.desaysv.aicockpit.data.repository.SoundRepository
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class ImageManagerAbsSoundsUseCaseImpl(
    override val rep: SoundRepository
) :ResourceUseCase<SoundItemData> {

    override val flow: Flow<List<SoundItemData>>
        get() = emptyFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var observeJob: Job? = null

    override fun observe() {
        if (observeJob != null) return
        observeJob = scope.launch {
            rep.observeListFlow().collect {
                Log.d("observe collect One")
                rep.saveAll(it)
            }
        }
    }

    private suspend fun reload() {
        val list = rep.load()
        rep.saveAll(list)
    }

    override fun load() {
        scope.launch {
            Log.d("load request")
            reload()
        }
    }

    override fun clear() {
        observeJob?.cancel()
        observeJob = null
    }
}