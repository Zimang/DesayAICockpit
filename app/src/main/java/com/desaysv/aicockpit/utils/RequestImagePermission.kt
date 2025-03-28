package com.desaysv.aicockpit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestImagePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current
    var shouldShowDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) onPermissionGranted() else onPermissionDenied()
        }
    )

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            onPermissionGranted()
        } else {
            shouldShowDialog = true
        }
    }

    if (shouldShowDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("请求权限") },
            text = { Text("我们需要访问您的图片以展示本地资源。") },
            confirmButton = {
                Button(onClick = {
                    shouldShowDialog = false
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }) {
                    Text("允许")
                }
            },
            dismissButton = {
                Button(onClick = {
                    shouldShowDialog = false
                    onPermissionDenied()
                }) {
                    Text("拒绝")
                }
            }
        )
    }
}
