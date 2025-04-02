package com.desaysv.aicockpit.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

//@Composable
//fun BroadcastFlowScreen(viewModel: SoundViewModel = viewModel()) {
//    val itemList by viewModel.items.collectAsState()
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        Text(
//            text = "📡 Broadcast Flow Demo",
//            style = MaterialTheme.typography.titleLarge
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = { viewModel.load() }) {
//            Text("🔁 Trigger Load")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(itemList) { item ->
//                Text(
//                    text = "${item.soundName} (ID: ${item.id})",
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//            }
//        }
//    }
//}

@Composable
fun BroadcastFlowScreen() {
    val context = LocalContext.current
    val viewModel: SoundFlowViewModel = viewModel(
        factory = SoundFlowViewModelFactory(context)
    )

    val itemList by viewModel.items.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "📡 Broadcast Flow Demo",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.load() }) {
            Text("🔁 Trigger Load")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(itemList) { item ->
                Text(
                    text = "${item.soundName} (ID: ${item.id})",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}
