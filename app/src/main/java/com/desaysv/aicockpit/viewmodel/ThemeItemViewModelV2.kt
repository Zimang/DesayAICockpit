package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.data.usecase.WujiThemeUseCaseImpl
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


val ID_OP_SAVE_AND_APPLIED=1
val ID_OP_SWITCH_APPLIED_THEME=2

class ThemeItemViewModelV2(
    repository: ThemeRepository,
    private val useCase: ResourceUseCase<ThemeItemData> =WujiThemeUseCaseImpl(repository)
) : ViewModel() {

//    private val _themes = MutableStateFlow<List<ThemeItemData>>(emptyList())
//    val themes: StateFlow<List<ThemeItemData>> = _themes
    val themes=repository.allThemes

    init {
        useCase.load()
        useCase.observe()

    }

    override fun onCleared() {
        useCase.clear()
        super.onCleared()
    }

    fun addTheme(eId:Int,sId:Int,tName:String,isApplied:Boolean,imP:String="")=viewModelScope.launch(Dispatchers.IO) {
        Log.d("onSave ViewModel")
        useCase.rep.insert(
            ThemeItemData(
                electricityItemId = eId,
                soundItemId = sId,
                themeName = tName,
                isDefault = false,
                isApplied = isApplied,
                imgPath = imP
            )
        )
    }
    fun addApplyingTheme(eId:Int,sId:Int,tName:String)=viewModelScope.launch {
        useCase.rep.agileOp(ID_OP_SAVE_AND_APPLIED,
            ThemeItemData(electricityItemId =  eId,
                soundItemId = sId,
                themeName =  tName,
                isApplied = true,
                isDefault = false)
        )
    }

    fun deleteTheme(themeItemData: ThemeItemData)=viewModelScope.launch{
        useCase.rep.delete(themeItemData)
    }

    fun switchAppliedTheme(themeId:Int)=viewModelScope.launch {
        useCase.rep.agileOp(ID_OP_SWITCH_APPLIED_THEME,id= themeId)
    }

}