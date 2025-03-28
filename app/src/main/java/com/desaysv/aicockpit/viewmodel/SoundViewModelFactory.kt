package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.repository.ProjectRepository
import com.desaysv.aicockpit.data.repository.SoundRepository

class SoundViewModelFactory(
    private val repository: SoundRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SoundViewModel(repository) as T
    }
}