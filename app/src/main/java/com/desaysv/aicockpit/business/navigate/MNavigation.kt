package com.desaysv.aicockpit.business.navigate

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.CustomScreen
import com.desaysv.aicockpit.ui.screen.InspiratonScreen
import com.desaysv.aicockpit.ui.screen.SaveScreen
import com.desaysv.aicockpit.ui.screen.ScreenTag
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.rememberSoundPlayerController
import com.desaysv.aicockpit.viewmodel.MajorViewModel
import com.desaysv.aicockpit.viewmodel.MajorViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class Route(val route: String) {
    object ScreenCUS : Route("custom") // 对应CUS
    object ScreenINS : Route("inspiration") // 对应INS
    object ScreenSAVE : Route("save")
    object Exit : Route("exit")
}
val TAG="TEST"

@Composable
fun rememberNavControllerCurrentRoute(navController: NavHostController): String? {
    return remember(navController) {
        derivedStateOf {
            navController.currentBackStackEntry?.destination?.route
        }
    }.value
}

@Composable
fun MainNavigation() {

    //十分糟糕，我的将hue上升到这里

    var hue by remember { mutableStateOf(0f) }
    var saturation by remember { mutableStateOf(0.5f) }
    var eleImgPath by remember { mutableStateOf("") }
    var selSoundItemData by remember { mutableStateOf(SoundItemData()) }

    val context = LocalContext.current
    val scop = rememberCoroutineScope()
    val app = context.applicationContext as MyApplication
    val navController = rememberNavController()
    val currentRoute = rememberNavControllerCurrentRoute(navController)

    val rp=app.themeRepository
    val eRp=app.elesRepository

    val majorViewModel= remember {
        ViewModelProvider(
            owner = (context as ComponentActivity),
            factory = MajorViewModelFactory(
                app.soundRepository,
                app.elesRepository,
                app.themeRepository
            )
        )[MajorViewModel::class.java]
    }

    //播放器
    val soundPlayer = rememberSoundPlayerController()

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
        // 右侧内容区域
        NavHost(
            navController = navController,
            startDestination = Route.ScreenCUS.route, //直接用ScreemCus报错
            modifier = Modifier
//                .padding(start = 284.pxToDp()) // 为左侧导航栏留出空间
                .fillMaxSize()
        ) {

            composable(Route.ScreenCUS.route) {
                CustomScreen(
                    majorViewModel = majorViewModel,
                    hue = hue, onHueChanged = { hue = it }, saturation = saturation,
                    onSaturationChanged = { saturation = it }, imgPath = eleImgPath, onChange = { navigateByTag(it,navController) },
                    onThemeWallpaperChange = {
                        Log.d(it)
                        eleImgPath=it
                    }, onSoundChosen = {
                        Log.d(selSoundItemData.toString())
                        selSoundItemData=it
                        soundPlayer.play(it.audioPath)
                    }) }

            composable(Route.ScreenINS.route) { InspiratonScreen({ navigateByTag(it,navController) },
                majorViewModel) }

            composable(Route.ScreenSAVE.route) { SaveScreen(
                 onSaveApply = {name->
                     majorViewModel.genTheme(
                         name,
                         eleImgPath,
                         selSoundItemData.id,
                         hue,saturation,context
                     )
                     Log.d("onSaveApplying")
                     sendColor(context,hue, saturation)
               },
               onSave =  {name->
                   majorViewModel.genTheme(
                       name,
                       eleImgPath,
                       selSoundItemData.id,
                       hue,saturation,context
                   )
                },
                majorViewModel, onExit = {navController.navigateUp()}) }

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

fun hsvToColorInt(hue: Float, saturation: Float, value: Float = 1f): Int {
    val hsv = floatArrayOf(hue, saturation, value)
    return android.graphics.Color.HSVToColor(hsv)
}




private fun sendColor(ctx : Context, hue:Float, saturation:Float){
    val colorInt = hsvToColorInt(hue,saturation)
    val hex = String.format("#%06X", 0xFFFFFF and colorInt)

    // ✅ 发送广播
    val intent = Intent("ACTION_CABIN_LIGHTING").apply {
        `package` = "com.desaysv.sceneengine"
        putExtra("data", hex)
    }
    ctx.sendBroadcast(intent)

    Log.d("LightScreen", "广播已发送，RGB = $hex")

}

private fun saveCurrentTheme(){

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

private fun navigateToSave(navController: NavController,) {
    navController.navigate(Route.ScreenSAVE.route) {
        launchSingleTop = true
        popUpTo(Route.ScreenSAVE.route) { inclusive = false }
    }
}

