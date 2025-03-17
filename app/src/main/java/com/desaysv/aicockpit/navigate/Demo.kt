package com.desaysv.aicockpit.navigate

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.desaysv.aicockpit.R


@Composable
fun NavigationBar(navController: NavHostController) {
    val currentRoute = rememberNavControllerCurrentRoute(navController)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(80.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            NavigationButton(
                label = "Home",
                route = "home",
                currentRoute = currentRoute,
                navController = navController
            )
            NavigationButton(
                label = "Profile",
                route = "profile",
                currentRoute = currentRoute,
                navController = navController
            )
            NavigationButton(
                label = "Settings",
                route = "settings",
                currentRoute = currentRoute,
                navController = navController
            )
        }
        // 退出按钮
        Button(
            onClick = {
                (context as Activity).finish()
            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Exit")
        }
    }
}

@Composable
fun rememberNavControllerCurrentRoute(navController: NavHostController): String? {
    return remember(navController) {
        derivedStateOf {
            navController.currentBackStackEntry?.destination?.route
        }
    }.value
}

@Composable
fun NavigationButton(
    label: String,
    route: String,
    currentRoute: String?,
    navController: NavHostController
) {
    val isSelected = remember(currentRoute) {
        derivedStateOf { currentRoute == route }
    }.value

    val backgroundColor = if (isSelected) {
        colorResource(R.color.choosen)
    } else {
        colorResource(R.color.n_choosen)
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Button(
        onClick = {
            navController.navigate(route) {
                popUpTo(route) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth().background(backgroundColor),
//        elevation = if (isSelected) ButtonDefaults.buttonElevation(
//            defaultElevation = 8.dp
//        ) else null
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Composable
fun NavigationContent(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.fillMaxSize()
    ) {
        composable("home") { HomeScreen() }
        composable("profile") { ProfileScreen(navController) }
        composable("settings") { SettingsScreen() }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Profile Screen", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // 返回Home并清理导航栈
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Back to Home")
            }
        }
    }
}
@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
    }
}