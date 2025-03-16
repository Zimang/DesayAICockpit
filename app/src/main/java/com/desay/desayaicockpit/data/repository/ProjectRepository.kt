package com.desay.desayaicockpit.data.repository

import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.db.Project
import com.desay.desayaicockpit.data.db.ProjectDao
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun addProject(name: String,imgResid:Int= R.drawable.b_1_h) {
        projectDao.insert(Project(name = name, imageResId = imgResid))
    }

    suspend fun deleteProject(project: Project) {
        projectDao.delete(project)
    }
}