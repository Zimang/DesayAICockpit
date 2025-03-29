package com.desaysv.aicockpit.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.business.ImageManager
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.repository.SoundRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * 加入以下逻辑
 */
class SoundViewModel(private val repository: SoundRepository) : ViewModel() {

    private val _soundItems = mutableStateListOf<SoundItemData>()
    val soundItems: List<SoundItemData> = _soundItems

    var isLoading by mutableStateOf(true)
        private set

    init {
        observeDatabaseUpdates()
    }

    private fun observeDatabaseUpdates() {
        viewModelScope.launch {
            repository.dataUpdated.collect {
                val items = repository.getAllSounds()
                _soundItems.clear()
                _soundItems.addAll(items)
            }
        }
    }

    fun startLoadingData(context: Context) {
        viewModelScope.launch {
            while (true) {
                isLoading = true

                val success = repository.tryUpdateData(context)
                val items = repository.getAllSounds()

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
                repository.updateFromFolderPath(folderPath)
            }
        }
    }
}