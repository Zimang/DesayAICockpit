package com.desay.desayaicockpit.navigate

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.desay.desayaicockpit.ui.screen.ScreenTag
import com.desay.desayaicockpit.ui.screen.ThemeChangeButtons
import com.desay.desayaicockpit.utils.pxToDp

//sealed class Route(val route: String) {
//    object ScreenCUS : Route("custom") // 对应CUS
//    object ScreenINS : Route("inspiration") // 对应INS
//    object ScreenSAVE : Route("save")
//    object Exit : Route("exit")
//}
//
//@Composable
//fun MainNavigation(context: Context) {
//    val navController = rememberNavController()
//    val currentRoute by navController.currentBackStackEntryAsState().value?.destination?.route
//
//    // 将路由映射到ScreenTag
//    val chosenTag = when(currentRoute) {
//        Route.ScreenCUS.route -> ScreenTag.CUS
//        Route.ScreenINS.route -> ScreenTag.INS
//        Route.ScreenSAVE.route -> ScreenTag.SAVE
//        else -> ScreenTag.CUS // 默认值
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // 左侧导航栏（固定位置）
//        ThemeChangeButtons(
//            chosenTag = chosenTag,
//            modifier = Modifier
//                .width(284.pxToDp())
//                .fillMaxHeight()
//                .align(Alignment.TopStart),
//            onChange = { tag ->
//                when(tag) {
//                    ScreenTag.CUS -> navigateToIns(navController)
//                    ScreenTag.INS -> navigateToCUS(navController)
//                    ScreenTag.SAVE -> navigateToCUS(navController)
//                }
//            },
//            onExit = { handleExit(context) }
//        )
//
//        // 右侧内容区域
//        NavHost(
//            navController = navController,
//            startDestination = Route.ScreenA.route,
//            modifier = Modifier
//                .padding(start = 284.pxToDp()) // 为左侧导航栏留出空间
//                .fillMaxSize()
//        ) {
//            composable(Route.ScreenA.route) { ScreenA(navController) }
//            composable(Route.ScreenB.route) { ScreenB(navController) }
//            composable(Route.ScreenC.route) { ScreenC(navController) }
//            composable(Route.Exit.route) { HandleExit(context) }
//        }
//    }
//}
//
//
//// 3. 导航逻辑封装
//private fun navigateToIns(navController: NavController) {
//    navController.navigate(Route.ScreenINS.route) {
//        launchSingleTop = true
//        popUpTo(Route.ScreenINS.route) { inclusive = false }
//    }
//}
//
//private fun navigateToCUS(navController: NavController) {
//    navController.navigate(Route.ScreenCUS.route) {
//        launchSingleTop = true
//        popUpTo(Route.ScreenCUS.route) { inclusive = false }
//    }
//}