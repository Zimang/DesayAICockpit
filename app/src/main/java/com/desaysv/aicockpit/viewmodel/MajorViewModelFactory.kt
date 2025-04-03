package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.repository.ElectricityRepository
import com.desaysv.aicockpit.data.repository.SoundRepository
import com.desaysv.aicockpit.data.repository.ThemeRepository

class MajorViewModelFactory(
    private val soundRepository: SoundRepository,
    private val electricityRepository: ElectricityRepository,
    private val themeRepository: ThemeRepository
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MajorViewModel(
            electricityRepository = electricityRepository,
            themeRepository = themeRepository,
            soundRepository = soundRepository,
        ) as T
    }
}