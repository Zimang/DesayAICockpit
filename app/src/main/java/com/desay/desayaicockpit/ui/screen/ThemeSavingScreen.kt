package com.desay.desayaicockpit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.utils.pxToDp

/**
 * 主题保存界面
 * TODO 保存应用按钮
 * TODO 名称输入框
 * TODO 背景图片
 */

@Composable
fun SaveScreen(onChange: (ScreenTag) -> Unit={}){
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            Column() {
//                BackButton(onExit)
                BackButton({})
            }
        }
        Box(modifier = Modifier
            .size(width = 1236.pxToDp(), height = 720f.pxToDp())){

        }
        Box(modifier = Modifier.size(width = 2320.pxToDp(), height = 720f.pxToDp())){

        }
    }
}
@Preview(
widthDp = 446,
heightDp = 262
)
@Composable
fun CockpitNamingScreen() {
    var cockpitName by remember { mutableStateOf("梦幻座舱") }
//    val modifier=Modifier
    val maxLength = 10 // 最大输入长度
    Column(
        modifier = Modifier
            .size(721f.pxToDp(),720f.pxToDp())
            .background(Color.Black)
            .padding(start =  221f.pxToDp())
//        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "为你独一无二的座舱起个名字吧",
            color = Color.White,
            fontSize = 32.getSP(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                top = 166.78f.pxToDp())
        )
        Box(modifier = Modifier.size(721f.pxToDp(),72f.pxToDp())
            .padding(top = 72.64f.pxToDp())) {
            // 透明输入框
            BasicTextField(
                value = cockpitName,
                onValueChange = {
                    if (it.length <= maxLength) cockpitName = it
                },
                modifier = Modifier
                    .size(460f.pxToDp(),24f.pxToDp())
                .padding(start = 170.86f.pxToDp(),  top = 22.88f.pxToDp()), // 根据背景图文字区域调整

                textStyle = TextStyle(
                    color = Color.White, // 文字颜色根据背景图区域设置
                    fontSize = 24.getSP(),
                    fontWeight = FontWeight.Medium,
                ),
                cursorBrush = SolidColor(Color.White), // 光标颜色
                singleLine = true
            )

        }
        // 按钮区域
        Row(
            modifier = Modifier.size(721f.pxToDp(),72f.pxToDp())
                .padding(74f.pxToDp())
        ) {

            Row (horizontalArrangement = Arrangement.spacedBy(16f.pxToDp())){

                val c=colorResource(R.color.n_choosen)
                val img=painterResource(R.drawable.app_2)
                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                )){
                    Image(img,contentDescription = null,
                        modifier = Modifier.size(
                            228.pxToDp(),72f.pxToDp()
                        ))
                    Text(text = "保存并应用", style = TextStyle(
                        color = c, fontSize = 30.getSP()
                    ), modifier = Modifier.align(Alignment.Center))
                }

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                )){
                    Image(painterResource(R.drawable.app_2),contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                    Text(text = "保存",
                        style = TextStyle(
                            color = c,
                            fontSize = 30.getSP(),
                        ), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun IconTextButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2D2D2D),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Icon(
//                imageVector = icon,
//                contentDescription = null,
//                tint = Color.White
//            )
//            Text(
//                text = text,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium
//            )
//        }
    }
}