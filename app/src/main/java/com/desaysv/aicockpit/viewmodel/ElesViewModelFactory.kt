package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.repository.ElectricityRepository

class ElesViewModelFactory(
    private val repository: ElectricityRepository
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ElesViewModel(repository) as T
    }
}