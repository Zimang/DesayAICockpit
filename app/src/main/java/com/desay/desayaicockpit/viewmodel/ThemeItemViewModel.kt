package com.desay.desayaicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desay.desayaicockpit.data.ThemeItemData
import com.desay.desayaicockpit.data.repository.ThemeRepository
import kotlinx.coroutines.launch

class ThemeItemViewModel (private val repository: ThemeRepository):ViewModel(){
    val themes=repository.allThemes

    fun addTheme(eId:Int,sId:Int,tName:String,isDefault: Boolean,isApplied:Boolean)=viewModelScope.launch {
        repository.addTheme(
            eId,sId,tName,isDefault, isApplied
        )
    }

    fun deleteTheme(themeItemData: ThemeItemData)=viewModelScope.launch{
        repository.deleteTheme(themeItemData)
    }
}