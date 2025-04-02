package com.desaysv.aicockpit.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.SoundItemData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
//
//data class SoundItemData(
//    val name: String,
//    val id: Int
//)


class BroadcastFlowViewModel : ViewModel() {

    private val useCase = BroadcastFlowUseCase()

    private val _items = MutableStateFlow<List<SoundItemData>>(emptyList())
    val items: StateFlow<List<SoundItemData>> = _items

    init {
        useCase.observe() // Start observing when ViewModel is created

        //冷流不会有状态，所有这里是
        viewModelScope.launch {
            useCase.flow.collect { item ->
                _items.update { it + item }
            }
        }
    }

    fun load() {
        useCase.load()
    }

    override fun onCleared() {
        super.onCleared()
        useCase.clear()
    }
}
