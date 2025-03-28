package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.repository.ThemeRepository
import kotlinx.coroutines.launch

class ThemeItemViewModel (private val repository: ThemeRepository):ViewModel(){
    val themes=repository.allThemes


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