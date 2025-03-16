package com.desay.desayaicockpit.navigate

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.desay.desayaicockpit.ui.screen.CustomScreen
import com.desay.desayaicockpit.ui.screen.InspiratonScreen
import com.desay.desayaicockpit.ui.screen.SaveScreen
import com.desay.desayaicockpit.ui.screen.ScreenTag
import com.desay.desayaicockpit.ui.screen.ThemeChangeButtons
import com.desay.desayaicockpit.utils.pxToDp

sealed class Route(val route: String) {
    object ScreenCUS : Route("custom") // 对应CUS
    object ScreenINS : Route("inspiration") // 对应INS
    object ScreenSAVE : Route("save")
    object Exit : Route("exit")
}
val TAG="TEST"
@Composable
fun MainNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val currentRoute = rememberNavControllerCurrentRoute(navController)

    // 将路由映射到ScreenTag
    val chosenTag = when(currentRoute) {
        Route.ScreenCUS.route -> ScreenTag.CUS
        Route.ScreenCUS.route -> ScreenTag.INS
        else -> ScreenTag.CUS // 默认值
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            color = Color.Black
        )) {
//        // 左侧导航栏（固定位置）
//        ThemeChangeButtons(
//            chosenTag = chosenTag,
//            modifier = Modifier
//                .width(284.pxToDp())
//                .fillMaxHeight()
//                .align(Alignment.TopStart),
//            onChange = { tag ->
//                when(tag) {
//                    ScreenTag.CUS -> navigateToCUS(navController)
//                    ScreenTag.INS -> navigateToIns(navController)
//                    ScreenTag.SAVE -> TODO()
//                }
//            },
//            onExit = {
////                handleExit(context)
//            }
//        )

        // 右侧内容区域
        NavHost(
            navController = navController,
            startDestination = Route.ScreenCUS.route, //直接用ScreemCus报错
            modifier = Modifier
//                .padding(start = 284.pxToDp()) // 为左侧导航栏留出空间
                .fillMaxSize()
        ) {
            composable(Route.ScreenCUS.route) { CustomScreen({ navigateByTag(it,navController) }) }
            composable(Route.ScreenINS.route) { InspiratonScreen({ navigateByTag(it,navController) }) }
            composable(Route.ScreenSAVE.route) { SaveScreen() }
            composable(Route.Exit.route) {  (context as Activity).finish() }
        }
    }
}

private fun navigateByTag(screenTag: ScreenTag,naviController: NavController){
    Log.d(TAG, "navigateByTag: 点击")
    when(screenTag) {
        ScreenTag.CUS -> navigateToCUS(naviController)
        ScreenTag.INS -> navigateToIns(naviController)
        ScreenTag.SAVE -> navigateToSave(naviController)
    }
}

// 导航逻辑封装
private fun navigateToIns(navController: NavController) {
    navController.navigate(Route.ScreenINS.route) {
        launchSingleTop = true
        popUpTo(Route.ScreenINS.route) { inclusive = false }
    }
}

private fun navigateToCUS(navController: NavController) {
    navController.navigate(Route.ScreenCUS.route) {
        launchSingleTop = true
        popUpTo(Route.ScreenCUS.route) { inclusive = false }
    }
}

private fun navigateToSave(navController: NavController) {
    navController.navigate(Route.ScreenSAVE.route) {
        launchSingleTop = true
        popUpTo(Route.ScreenSAVE.route) { inclusive = false }
    }
}

