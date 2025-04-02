package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.repository.ThemeRepository

class ThemeItemViewModelFactory(
    private val repository: ThemeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThemeItemViewModel(repository) as T
    }
}
class ThemeItemViewModelFactoryV2(
    private val repository: ThemeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThemeItemViewModelV2(repository) as T
    }
}