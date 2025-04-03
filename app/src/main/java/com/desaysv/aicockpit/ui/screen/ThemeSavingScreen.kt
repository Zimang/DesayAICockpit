package com.desaysv.aicockpit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.ui.screen.base.BackgroundInputField
import com.desaysv.aicockpit.ui.screen.base.BackgroundInputFieldV1
import com.desaysv.aicockpit.ui.screen.base.PicWithPic_Save
import com.desaysv.aicockpit.ui.screen.base.PicWithPic_SaveApply
import com.desaysv.aicockpit.ui.screen.base.RegularButton
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.pxToDp
import com.desaysv.aicockpit.viewmodel.SoundViewModel
import com.desaysv.aicockpit.viewmodel.ThemeItemViewModelV2
import kotlinx.coroutines.launch

/**
 * 主题保存界面
 */

@Composable
fun SaveScreen(onSaveApply:(name:String)->Unit,onSave:(name:String)->Unit, viewModel: ThemeItemViewModelV2, onChange: (ScreenTag) -> Unit={}, onExit:()->Unit){


    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            Column() {
                BackButton(onExit)
            }
        }

        Box(modifier = Modifier
            .size(width = 1236.pxToDp(), height = 720f.pxToDp())){
            CockpitNamingScreenV1(onSaveApply,onSave,viewModel,onExit)
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
fun CockpitNamingScreenV1(
    onSaveApply:(String)->Unit,
    onSave:(String)->Unit,
    viewModel: ThemeItemViewModelV2, onExit: () -> Unit) {
    val scope = rememberCoroutineScope()
    var cockpitName by remember { mutableStateOf("梦幻座舱") }
    val maxLength = 10 // 最大输入长度
    Column(
        modifier = Modifier
//            .size(721f.pxToDp(),720f.pxToDp())
            .background(Color.Black)
            .padding(start =  221f.pxToDp())
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(160f.pxToDp()))
        Text(
            text = ResourceManager.getNameYourCabin()!!,
            color = Color.White,
            fontSize =32.getSP()
        )

        Spacer(Modifier.height(72.64f.pxToDp()))

        BackgroundInputFieldV1(R.drawable.save_input_bg){
            cockpitName=it
        }

        Spacer(Modifier.height(74f.pxToDp()))

        // 按钮区域
        Row(
            horizontalArrangement = Arrangement.spacedBy(41f.pxToDp())
        ) {
            RegularButton(R.drawable.save_bt_bg,ResourceManager.getSave()!!,
                ){
                scope.launch {
                    viewModel.addTheme(
                        200,200,cockpitName,false
                    )
                    onSave(cockpitName)
                    onExit()
                }
            }
            RegularButton(R.drawable.save_bt_bg,ResourceManager.getSaveApply()!!,
            ){
                scope.launch {
                    viewModel.addApplyingTheme(
                       200,200,cockpitName
                    )
                    onSaveApply(cockpitName)
                    onExit()
                }
            }
        }
    }
}
