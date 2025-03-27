package com.desaysv.aicockpit.ui.component.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desaysv.aicockpit.ui.screen.getSP
import com.desaysv.aicockpit.utils.pxToDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

// 键盘行定义
val letterUpperRows = listOf(
    listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
    listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
    listOf("SH","Z", "X", "C", "V", "B", "N", "M","⌫"),
    listOf("123", "中", "mic", "换行")
)

// 键盘行定义
val letterRows = listOf(
    listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
    listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
    listOf("sh","z", "x", "c", "v", "b", "n", "m","⌫"),
    listOf("123", "中", "mic", "换行")
)

val numberRows = listOf(
    listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
    listOf("@", "#", "¥", "%", "&", "-", "+", "(", ")", "="),
    listOf(",", ".", "?", "!", ":", ";", "'"),
    listOf("ABC", "中", "mic", "换行")
)

@Composable
fun KeyButton(
    text: String,
    isFunctionKey: Boolean = false,
    width: Dp = defaultButtonWidth.pxToDp(),
    onKeyPress: (String) -> Unit,
) {

    Box(
        modifier = Modifier
//            .aspectRatio(1f)
            .size(height = defaultButtonHeight.pxToDp(), width = width)
            .padding(10.pxToDp())
            .background(
                color = Color.White, // 白色键帽
                shape = RoundedCornerShape(12.pxToDp())
            )
            .clickable { onKeyPress(text) }
        ,contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF333333), // 深灰文字
            fontSize = defaultFontSize.getSP()
        )
    }
}

val defaultButtonHeight =120
val defaultButtonWidth =100
val defaultChineseButtonWidth =110
val defaultLongButtonWidth =200
val defaultMicButtonWidth =500
val defaultFontSize=40
@Composable
fun KeyboardLayout(
    isNumeric: Boolean,
    isChinese: Boolean,
    isCap: Boolean,
    onKeyPress: (String) -> Unit
) {
//    val rows = if (isNumeric) numberRows else letterRows
    val rows = if (isNumeric) numberRows else
            if(isCap) letterUpperRows else letterRows

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5E5E5))
            .padding(8.pxToDp())
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.pxToDp()) ,
//                horizontalArrangement = Arrangement.spacedBy(6.pxToDp())
                horizontalArrangement = Arrangement.Center
            ) {
                row.forEach { key ->
                    KeyButton(
                        text = when {
                            key == "中" && isChinese -> "英"
                            key == "中" -> "中"
                            else -> key
                        },
                        width = run{
                            when (key) {
                                "123", "ABC", "换行" -> defaultLongButtonWidth.pxToDp()
                                "中","SH" -> defaultChineseButtonWidth.pxToDp()
                                "mic" -> defaultMicButtonWidth.pxToDp()
                                else -> defaultButtonWidth.pxToDp()
                            }
                        },
                        onKeyPress = onKeyPress
                    )
                    Spacer(Modifier.width(8.pxToDp()))
                }
            }
        }
    }
}

@Preview
@Composable
fun KeyboardScreen() {
    var isNumeric by remember { mutableStateOf(false) }
    var isChinese by remember { mutableStateOf(false) }
    var isCap by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        KeyboardLayout(
            isNumeric = isNumeric,
            isChinese = isChinese,
            isCap = isCap,
            onKeyPress = { key ->
                when (key) {
                    "123" -> isNumeric = true
                    "ABC" -> isNumeric = false
                    "中", "英" -> isChinese = !isChinese
                    "sh", "SH" -> {
                        isCap = !isCap
                        isChinese = false
                    }
                    // 其他按键处理...
                }
            }
        )
    }
}



@Preview
@Composable
fun KeyButtonPreview(){

    // 使用 remember 保存一个 MutableState，初始值为 false
    var isTrue by remember { mutableStateOf(false) }

    // 使用 LaunchedEffect 启动一个协程，每秒钟切换 isTrue 的值
    LaunchedEffect(Unit) {
        while (true) {
            isTrue = Random.nextBoolean() // 随机生成 true 或 false
            delay(1000) // 延迟 1 秒
        }
    }

    KeyButton("A",isTrue) { }
}

@Composable
fun ParentA(modifier: Modifier) {
    Box(
        modifier = modifier
            .offset(x = 20.pxToDp()) // 父的偏移
            .background(Color.Gray)
            .size(200.dp)
    ) {
        ChildB()
        ChildBFrom(modifier)
    }
}











@Preview
@Composable
fun Test() {
    ParentA(Modifier)
}

@Composable
fun ChildB() {
    Box(
        modifier = Modifier
            .offset(x = 20.dp) // 子的偏移
            .background(Color.Red)
            .size(100.dp)
    )
}
@Composable
fun ChildBFrom(modifier: Modifier) {
    Box(
        modifier = modifier
            .offset(x = 20.dp, y = 40.dp) // 子的偏移
            .background(Color.Green)
            .size(100.dp)
    )
}