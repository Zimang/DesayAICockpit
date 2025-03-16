package com.desay.desayaicockpit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desay.desayaicockpit.data.db.Project
import com.desay.desayaicockpit.data.repository.ProjectRepository
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