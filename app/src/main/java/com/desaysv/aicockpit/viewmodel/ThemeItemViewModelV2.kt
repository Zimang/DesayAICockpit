package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.data.usecase.WujiThemeUseCaseImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ThemeItemViewModelV2(
    private val repository: ThemeRepository,
    private val useCase: ResourceUseCase<ThemeItemData> =WujiThemeUseCaseImpl(repository)
) : ViewModel() {

//    private val _themes = MutableStateFlow<List<ThemeItemData>>(emptyList())
//    val themes: StateFlow<List<ThemeItemData>> = _themes
    val themes=repository.allThemes

    init {
        useCase.load()
        useCase.observe()

//        viewModelScope.launch {
//            useCase.flow.collect { theme ->
//                _themes.update { it + theme }
//            }
//        }
    }

    override fun onCleared() {
        useCase.clear()
        super.onCleared()
    }

    fun addTheme(eId:Int,sId:Int,tName:String,isDefault: Boolean,isApplied:Boolean)=viewModelScope.launch {
        repository.addTheme(
            eId,sId,tName,isDefault, isApplied
        )
    }
    fun addApplyingTheme(eId:Int,sId:Int,tName:String)=viewModelScope.launch {
        repository.setNewAppliedTheme(
            ThemeItemData(electricityItemId =  eId,
                soundItemId = sId,
                themeName =  tName,
                isApplied = true,
                isDefault = false)
        )
    }

    fun deleteTheme(themeItemData: ThemeItemData)=viewModelScope.launch{
        repository.deleteTheme(themeItemData)
    }

    fun switchAppliedTheme(themeId:Int)=viewModelScope.launch {
        repository.switchAppliedTheme(themeId)
    }

}