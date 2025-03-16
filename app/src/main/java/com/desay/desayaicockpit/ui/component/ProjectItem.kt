package com.desay.desayaicockpit.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.desay.desayaicockpit.data.db.Project

@Composable
fun ProjectItem(project: Project, onDelete: (Project) -> Unit) {
    Card {
        Row {
            Image(painterResource(project.imageResId), "项目图标")
            Text(project.name)
            IconButton({ onDelete(project) }) {
                Icon(Icons.Default.Build, "删除")
            }
        }
    }
}