package com.desaysv.aicockpit.ui.component

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.desaysv.aicockpit.utils.StoragePermissionManager

@Composable
fun AppPermissionGate(
    onGranted: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val permissions = StoragePermissionManager.requiredPermissions()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionGranted = result.values.all { it }
        if (!permissionGranted) {
            Toast.makeText(context, "请授权访问图片资源，否则应用功能将受限", Toast.LENGTH_SHORT).show()
            StoragePermissionManager.openAppSettings(context)
        }
    }

    LaunchedEffect(Unit) {
        if (StoragePermissionManager.hasPermissions(context)) {
            permissionGranted = true
        } else {
            launcher.launch(permissions)
        }
    }

    if (permissionGranted) {
        // 权限通过后先触发业务逻辑
        LaunchedEffect(Unit) {
            onGranted()
        }

        // 显示主内容 UI
        content()
    } else {
        // 显示等待授权提示（可自定义）
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("正在请求权限...")
        }
    }
}
