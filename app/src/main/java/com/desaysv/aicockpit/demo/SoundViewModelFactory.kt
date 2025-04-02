package com.desaysv.aicockpit.demo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.db.AppDatabase
import com.desaysv.aicockpit.data.repository.SoundRepository

class SoundFlowViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.getInstance(context).soundItemDao()
        val loader = BroadcastResourceLoader(context)
        val repository = SoundRepositoryV2(dao, loader, context)
        val useCase = SoundFlowUseCase(repository, context)
        return SoundFlowViewModel(useCase) as T
    }
}
