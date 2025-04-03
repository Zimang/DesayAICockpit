package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ElectricityRepository
import com.desaysv.aicockpit.data.usecase.ElesUseCaseImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ElesViewModel(
    repository: ElectricityRepository,
    private val useCase: ResourceUseCase<ElectricityItemData> = ElesUseCaseImpl(repository)
):ViewModel() {
    val eles=repository.allEles

    init {
        useCase.load()
        useCase.observe()

    }

    fun getElectricityItemByPath(path: String, onResult: (ElectricityItemData?) -> Unit) {
        viewModelScope.launch {
            val result = useCase.rep.getByPath(path)
            onResult(result)
        }
    }

    suspend fun getEleByPath(p:String):ElectricityItemData{
        return useCase.rep.getByPath(p)?:ElectricityItemData(
            imageName = "默认壁纸",
            themeName = "默认主题",
            imgPath = "" //TODO 配置一个默认主题的资源路径
        )
    }

    override fun onCleared() {
        useCase.clear()
        super.onCleared()
    }

}