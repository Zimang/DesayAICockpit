package com.desay.desayaicockpit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "为你独一无二的座舱起个名字吧",
            color = Color.White,
            fontSize = 32.getSP(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 166.78f.pxToDp())
        )

        // 主题名称输入框
        TextField(
            value = cockpitName,
            onValueChange = { cockpitName = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = "主题名称",
                    color = Color.White.copy(alpha = 0.7f)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f)
            ),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )

        // 按钮区域
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 保存并应用按钮
            IconTextButton(
                icon = Icons.Default.CheckCircle,
                text = "保存并应用",
                onClick = { /* 处理保存并应用 */ }
            )

            // 保存按钮
            IconTextButton(
                icon = Icons.Default.Star,
                text = "保存",
                onClick = { /* 处理保存 */ }
            )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}