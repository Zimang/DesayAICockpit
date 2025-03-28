package com.desaysv.aicockpit.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.repository.SoundRepository
import kotlinx.coroutines.launch

class SoundViewModel(private val repository: SoundRepository) : ViewModel() {

    val soundItems = mutableStateListOf<SoundItemData>()

    fun loadSoundsFromBroadcast() {
        viewModelScope.launch {
            repository.updateFromBroadcastPath()
            soundItems.clear()
            soundItems.addAll(repository.getAllSounds())
        }
    }
}