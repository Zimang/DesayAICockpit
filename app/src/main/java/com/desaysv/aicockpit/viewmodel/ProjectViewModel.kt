package com.desaysv.aicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desaysv.aicockpit.data.db.Project
import com.desaysv.aicockpit.data.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {
    val projects = repository.allProjects

    fun addProject(name: String) = viewModelScope.launch {
        repository.addProject(name)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {
        repository.deleteProject(project)
    }
}