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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class InspirationUseCaseImpl(
    override val rep: ThemeRepository
):ResourceUseCase<ThemeItemData> {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    //这种流不太行，为一个一个设计，我们需要一列一列
//    private val _observeFlow = MutableSharedFlow<ThemeItemData>(extraBufferCapacity = 64)
//    private val _loadFlow = MutableSharedFlow<ThemeItemData>(extraBufferCapacity = 64)
//    override val flow: Flow<ThemeItemData> = merge(_observeFlow, _loadFlow)

    private val _flow = MutableSharedFlow<List<ThemeItemData>>(replay = 1)
    override val flow: Flow<List<ThemeItemData>> = _flow.asSharedFlow()


    private var observeJob: Job? = null

    override fun observe() {
        if (observeJob != null) return
        observeJob = scope.launch {
            rep.observeListFlow().collect {
                Log.d("收到清空的广播")
                rep.saveAll(it)
            }
        }
    }

    private suspend fun reload() {
        val list = rep.load()
        _flow.emit(list) // ✅ 一次性发射整个 list
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