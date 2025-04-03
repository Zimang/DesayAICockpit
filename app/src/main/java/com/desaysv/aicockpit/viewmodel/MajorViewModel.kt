package com.desaysv.aicockpit.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.business.ImageManager
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.ListTheme
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceUseCase
import com.desaysv.aicockpit.data.repository.ElectricityRepository
import com.desaysv.aicockpit.data.repository.SoundRepository
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.data.usecase.ElesUseCaseImpl
import com.desaysv.aicockpit.data.usecase.WujiThemeUseCaseImpl
import com.desaysv.aicockpit.utils.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MajorViewModel(
    electricityRepository: ElectricityRepository,
    themeRepository: ThemeRepository,
    private val soundRepository: SoundRepository,
    private val elesUseCaseImpl: ResourceUseCase<ElectricityItemData> = ElesUseCaseImpl(electricityRepository),
    private val themeUseCaseImpl: ResourceUseCase<ThemeItemData> = WujiThemeUseCaseImpl(themeRepository)
    
):ViewModel() {

    val eles=electricityRepository.allEles

    private val _soundItems = mutableStateListOf<SoundItemData>()
    val soundItems: List<SoundItemData> = _soundItems
    val themes=themeRepository.allThemes

    var isLoading by mutableStateOf(true)
        private set
 
    init {
        observeDatabaseUpdates()
        
        elesUseCaseImpl.load()
        elesUseCaseImpl.observe()


        themeUseCaseImpl.load()
        themeUseCaseImpl.observe()
    }

    fun genTheme(
        tName: String,
        imP: String,
        soudId:Int,
        hue:Float,
        sat:Float,
        context: Context //用于发送广播
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val ele = getEleByPath(imP) ?: return@launch
            addTheme(
                ele.id,
                soudId,
                tName = tName,
                isApplied = false,
                ele.imgPath
            )
            val listTheme=ListTheme(
                wallpaperPath = ele.imgPath,
                title = tName,
                hue = hue,
                saturation = sat,
                sId = soudId
            )
            val json=Gson().toJson(listTheme)
            val intent = Intent("com.desaysv.aicockpit.THEME_SEND_ACTION").apply {
                putExtra("themeJson", json)
            }
            context.sendBroadcast(intent)
            Log.d("发送广播 $intent json为 $json")
            Log.d("onSave ViewModel")
        }
    }

    private fun observeDatabaseUpdates() {
        viewModelScope.launch {
            soundRepository.dataUpdated.collect {
                val items = soundRepository.getAllSounds()
                _soundItems.clear()
                _soundItems.addAll(items)
            }
        }
    }

    fun startLoadingData(context: Context) {
        viewModelScope.launch {
            while (true) {
                isLoading = true

                val success = soundRepository.tryUpdateData(context)
                val items = soundRepository.getAllSounds()

                val hasInvalidFile = items.any { !File(it.imgPath).exists() }

                if (success && items.isNotEmpty() && !hasInvalidFile) {
                    _soundItems.clear()
                    _soundItems.addAll(items)
                    isLoading = false
                    break
                }

                delay(2000L) // 2秒后重试
            }
        }
    }

    // 注册被动广播接收器，收到广播后根据广播的文件夹路径更新数据
    fun registerPassiveBroadcast(context: Context) {
        ImageManager.registerPassiveReceiver(context) { folderPath ->
            viewModelScope.launch {
                soundRepository.updateFromFolderPath(folderPath)
            }
        }
    }

    fun getElectricityItemByPath(path: String, onResult: (ElectricityItemData?) -> Unit) {
        viewModelScope.launch {
            val result = elesUseCaseImpl.rep.getByPath(path)
            onResult(result)
        }
    }

    suspend fun getEleByPath(p:String):ElectricityItemData{
        return elesUseCaseImpl.rep.getByPath(p)?:ElectricityItemData(
            imageName = "默认壁纸",
            themeName = "默认主题",
            imgPath = "" //TODO 配置一个默认主题的资源路径
        )
    }



    override fun onCleared() {
        themeUseCaseImpl.clear()
        elesUseCaseImpl.clear()

        super.onCleared()
    }

    fun addTheme(eId:Int,sId:Int,tName:String,isApplied:Boolean,imP:String="")=viewModelScope.launch(
        Dispatchers.IO) {
        Log.d("onSave ViewModel")
        themeUseCaseImpl.rep.insert(
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
        themeUseCaseImpl.rep.agileOp(ID_OP_SAVE_AND_APPLIED,
            ThemeItemData(electricityItemId =  eId,
                soundItemId = sId,
                themeName =  tName,
                isApplied = true,
                isDefault = false)
        )
    }

    fun deleteTheme(themeItemData: ThemeItemData)=viewModelScope.launch{
        themeUseCaseImpl.rep.delete(themeItemData)
    }

    fun switchAppliedTheme(themeId:Int)=viewModelScope.launch {
        themeUseCaseImpl.rep.agileOp(ID_OP_SWITCH_APPLIED_THEME,id= themeId)
    }

}