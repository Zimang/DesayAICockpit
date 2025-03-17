package com.desaysv.aicockpit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.ui.screen.base.BackgroundInputField
import com.desaysv.aicockpit.ui.screen.base.PicWithPic_Save
import com.desaysv.aicockpit.ui.screen.base.PicWithPic_SaveApply
import com.desaysv.aicockpit.utils.pxToDp
import kotlinx.coroutines.launch

/**
 * 主题保存界面
 * TODO 保存应用按钮
 * TODO 名称输入框
 * TODO 背景图片
 */

@Composable
fun SaveScreen(onChange: (ScreenTag) -> Unit={},onExit:()->Unit){
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            Column() {
//                BackButton(onExit)
                BackButton(onExit)
            }
        }
        Box(modifier = Modifier
            .size(width = 1236.pxToDp(), height = 720f.pxToDp())){
            CockpitNamingScreen(onExit)
        }
        Box(modifier = Modifier.size(width = 2320.pxToDp(), height = 720f.pxToDp())){

            Image(painterResource(R.drawable.save_bg),contentDescription = null)
        }
    }
}
//@Preview(
//widthDp = 446,
//heightDp = 262,
//backgroundColor = 0xff000000,
//showBackground = true
//)
@Composable
fun CockpitNamingScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    val app = context.applicationContext as MyApplication
    val rp=app.themeRepository
    val scope = rememberCoroutineScope()

    var cockpitName by remember { mutableStateOf("梦幻座舱") }
//    val modifier=Modifier
    val maxLength = 10 // 最大输入长度
    Column(
        modifier = Modifier
//            .size(721f.pxToDp(),720f.pxToDp())
            .background(Color.Black)
            .padding(start =  221f.pxToDp())
//        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.save_head),contentDescription = null
            , modifier = Modifier
                .padding(
                top = 166.78f.pxToDp(), start =136.01f.pxToDp(),
//                bottom =520.64f.pxToDp(), end =431.01f.pxToDp(),
            ))
        BackgroundInputField(R.drawable.save_input_bg, modifier = Modifier.padding(top = 72.64f.pxToDp())){
            cockpitName=it
        }

        // 按钮区域
        Row(
            modifier = Modifier
//                .size(721f.pxToDp(),72f.pxToDp())
                .padding(top=74f.pxToDp()),
            horizontalArrangement = Arrangement.spacedBy(41f.pxToDp())
        ) {
            PicWithPic_Save{
                scope.launch {
                    rp.addTheme(
                        200,200,cockpitName,false,false
                    )
                    onExit()
                }
            }
            PicWithPic_SaveApply{
                scope.launch {
                    rp.setNewAppliedTheme(
                       ThemeItemData(electricityItemId =  200,
                           soundItemId = 200,
                           themeName =  cockpitName,
                           isApplied = true,
                           isDefault = false)
                    )
                    onExit()
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 1396,
    heightDp = 262
)
@Composable
fun FinalScreen_Save(){
    SaveScreen {  }
}
