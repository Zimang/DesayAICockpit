package com.desaysv.aicockpit.demo

import com.desaysv.aicockpit.data.SoundItemData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class BroadcastFlowUseCase {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _loadFlow = MutableSharedFlow<SoundItemData>()
    private val _observeFlow = MutableSharedFlow<SoundItemData>()

    private var observeJob: Job? = null

    // Public combined flow
    val flow: Flow<SoundItemData> = merge(_observeFlow, _loadFlow)

    // Start observe (hot stream simulation)
    fun observe() {
        if (observeJob != null) return // already started

        println("Start observing (hot stream)")
        observeJob = scope.launch {
            while (isActive) {
                delay(3000)

                val item = SoundItemData(
                    soundName = "Observe-${Random.nextInt(100)}",
                )
                println("Observe pushed: $item")
                _observeFlow.emit(item)
            }
        }
    }

    // Trigger one-shot load (cold stream)
    fun load() {
        scope.launch {
            println("Sending broadcast request (load)...")
            delay(1000)
            val item = SoundItemData(soundName = "Load-${Random.nextInt(100)}")
            println("Load result: $item")
            _loadFlow.emit(item)
        }
    }

    fun clear() {
        observeJob?.cancel()
        observeJob = null
    }
}
