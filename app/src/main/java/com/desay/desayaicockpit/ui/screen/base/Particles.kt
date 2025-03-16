package com.desay.desayaicockpit.ui.screen.base



import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.ui.screen.getSP
import com.desay.desayaicockpit.utils.pxToDp


@Composable
fun BackgroundInputField(
    @DrawableRes bg:Int,
    modifier: Modifier=Modifier
) {
    // 图片资源需放入res/drawable目录
    val backgroundImage = painterResource(bg)
    var text by remember { mutableStateOf("") }
    val maxLength = 20 // 最大输入长度

    Box(
        modifier = modifier
            .size(721f.pxToDp(), 72f.pxToDp()) // 根据背景图尺寸设置
            .background(Color.Transparent)
    ) {
        // 背景图片
        Image(
            painter = backgroundImage,
            contentDescription = "输入框背景",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // 透明输入框
        BasicTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxLength) text = it
            },
//            modifier = Modifier
//                .size(height = 24f.pxToDp()
//                    , width =500f.pxToDp()
//                ),
            textStyle = TextStyle(
                color = Color.White, // 文字颜色根据背景图区域设置
                fontSize =24.pxToDp().value.sp,
//                baselineShift = BaselineShift(-1f)
//                fontWeight = FontWeight.Medium
            ),
            cursorBrush = SolidColor(colorResource(R.color.choosen)), // 光标颜色
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize().padding(end = 100f.pxToDp())) {
                        Image(painterResource(R.drawable.save_lable),
                            contentDescription = "",
                            modifier = Modifier.padding(
                                start = 31.34f.pxToDp(),
                                end = 45.63f.pxToDp(),
                                top = 23.77f.pxToDp()
                            ))
//                        )
                        Box(modifier = Modifier.padding(
                            top =22.88f.pxToDp(),
//                            bottom = 25.12f.pxToDp()
                        )){
                            innerTextField() // 直接放置系统生成的输入框
                        }
                    }
                }
            }
        )

//        // 输入计数提示（可选）
//        Text(
//            text = "${text.length}/$maxLength",
//            color = Color.Gray,
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(end = 8.dp, bottom = 4.dp),
//            fontSize = 12.sp
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundInputField_() {
    BackgroundInputField(R.drawable.save_input_bg)
}