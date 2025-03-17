package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.data.repository.ProjectRepository

class ProjectViewModelFactory(
    private val repository: ProjectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectViewModel(repository) as T
    }
}