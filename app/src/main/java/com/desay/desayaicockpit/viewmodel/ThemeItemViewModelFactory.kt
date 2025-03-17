package com.desay.desayaicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desay.desayaicockpit.data.repository.ProjectRepository
import com.desay.desayaicockpit.data.repository.ThemeRepository

class ThemeItemViewModelFactory(
    private val repository: ThemeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThemeItemViewModel(repository) as T
    }
}