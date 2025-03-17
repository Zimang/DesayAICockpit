package com.desaysv.aicockpit.ui.screen.demo

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.ui.component.ProjectItem
import com.desaysv.aicockpit.utils.pxToDp
import com.desaysv.aicockpit.viewmodel.ProjectViewModel
import com.desaysv.aicockpit.viewmodel.ProjectViewModelFactory

@Composable
fun ProjectListScreen() {
    val context = LocalContext.current
    val viewModel = remember {
        val app = context.applicationContext as MyApplication
        ViewModelProvider(
            owner = (context as ComponentActivity),
            factory = ProjectViewModelFactory(app.repository)
        )[ProjectViewModel::class.java]
    }

    val projects by viewModel.projects.collectAsState(initial = emptyList())

    Column (verticalArrangement = Arrangement.spacedBy(10f.pxToDp())){
        LazyRow {
            items(projects) { project ->
                ProjectItem(
                    project = project,
                    onDelete = { viewModel.deleteProject(it) }
                )
            }
        }
        Button(onClick = {
            viewModel.addProject("name")
        }) {
            Text("add")
        }

    }
}