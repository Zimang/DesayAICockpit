package com.desaysv.aicockpit.business.navigate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.ui.screen.CustomScreen
import com.desaysv.aicockpit.ui.screen.InspiratonScreen
import com.desaysv.aicockpit.ui.screen.SaveScreen
import com.desaysv.aicockpit.ui.screen.ScreenTag
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.rememberSoundPlayerController
import com.desaysv.aicockpit.viewmodel.MajorViewModel
import com.desaysv.aicockpit.viewmodel.MajorViewModelFactory
import kotlinx.coroutines.launch

sealed class Route(val route: String) {
    object ScreenCUS : Route("custom") // 对应CUS
    object ScreenINS : Route("inspiration") // 对应INS
    object ScreenSAVE : Route("save")
    object Exit : Route("exit")
}
val TAG="TEST"
const val THEME_NAME_DEFINE = "theme_name_define" // 主题名称（ai座舱和无极hmi传递过来）
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
    var eleImgPath by remember { mutableStateOf("/sdcard/Pictures/aicockpit/df.png") }
    var sid by remember { mutableStateOf(-1) }

    val context = LocalContext.current
    val scop = rememberCoroutineScope()
    val app = context.applicationContext as MyApplication
    val navController = rememberNavController()
    val currentRoute = rememberNavControllerCurrentRoute(navController)

    val rp=app.themeRepository
    val eRp=app.elesRepository
    val sRp=app.soundRepository

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

    //糟糕的设计，但是没有什么巧妙地办法不这样做
    val soundItems by majorViewModel.sounds.collectAsState(initial = emptyList())



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
                        sid=it.id
                        Log.d(it.toString())
                        if(it.imgId==-1){
                            soundPlayer.playFromAssets(it.audioPath)
                        }else{
                            soundPlayer.play(it.audioPath)
                        }
                    }, genCockpit = {
                        if(soundItems.isEmpty()){
                            Toast.makeText(context,ResourceManager.getSoundEmptyCantSave(),Toast.LENGTH_LONG).show()
                        }else{
                            navigateByTag(
                                ScreenTag.SAVE,
                                naviController = navController
                            )
                        }
                    }) }

            composable(Route.ScreenINS.route) { InspiratonScreen({ navigateByTag(it,navController) },
                majorViewModel) }

            composable(Route.ScreenSAVE.route) { SaveScreen(
                 onSaveApply = {name->

                     majorViewModel.genTheme(
                         name,
                         eleImgPath,
                         sid,
                         hue,saturation,context,true
                     )
                     Log.d("onSaveApplying")
                     scop.launch {
                         informingIPC(context,hue, saturation,name,majorViewModel.getEleByPath(eleImgPath))
                     }
                     Toast.makeText(context,ResourceManager.getAppliedSuccessfully(),Toast.LENGTH_LONG).show()

//                     informingIPC(context,)
               },
               onSave =  {name->
                   majorViewModel.genTheme(
                       name,
                       eleImgPath,
                       sid,
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




fun informingIPC(ctx : Context, hue:Float, saturation:Float,tname:String){
    sendColor(ctx,hue,saturation)
    sendApplyingTheme(ctx,tname)
}

fun informingIPC(ctx : Context, hue:Float, saturation:Float,tname:String,electricityItemData: ElectricityItemData){
    sendColor(ctx,hue,saturation)
    sendApplyingTheme(ctx,tname)
    electricityItemData.send2Wuji(ctx)
}

fun informingIPC(ctx : Context, themeItemData: ThemeItemData){
    sendColor(ctx,themeItemData.hue,themeItemData.saturation)
    sendApplyingTheme(ctx,themeItemData.themeName)
}

fun sendColor(ctx : Context, hue:Float, saturation:Float){
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

fun sendApplyingTheme(ctx : Context,tname: String){
    Log.d("发送主题$tname")
    Settings.Global.putString(
        ctx?.contentResolver,
        THEME_NAME_DEFINE,
        tname
    )
}

fun requestApplyingTheme(ctx : Context):String{
    val  tname=Settings.Global.getString(
        ctx.applicationContext.getContentResolver(),
        THEME_NAME_DEFINE
    )?:""
    Log.d("获取主题$tname")
    return tname
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

