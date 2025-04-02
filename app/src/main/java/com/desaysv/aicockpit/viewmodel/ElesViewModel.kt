package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ElectricityRepository
import com.desaysv.aicockpit.data.usecase.ElesUseCaseImpl

class ElesViewModel(
    repository: ElectricityRepository,
    private val useCase: ResourceUseCase<ElectricityItemData> = ElesUseCaseImpl(repository)
):ViewModel() {
    val eles=repository.allEles

    init {
        useCase.load()
        useCase.observe()

    }

    override fun onCleared() {
        useCase.clear()
        super.onCleared()
    }

}