package com.desaysv.aicockpit.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoundFlowViewModel(
    private val useCase: ResourceUseCase<SoundItemData>

) : ViewModel() {

    private val _items = MutableStateFlow<List<SoundItemData>>(emptyList())
    val items: StateFlow<List<SoundItemData>> = _items

    init {
        useCase.observe()

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
