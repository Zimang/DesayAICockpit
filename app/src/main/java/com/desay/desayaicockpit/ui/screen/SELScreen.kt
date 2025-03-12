package com.desay.desayaicockpit.ui.screen


import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.ElectricityItemData
import com.desay.desayaicockpit.ui.theme.Orange
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalColorSlider() {
    var sliderValue by remember { mutableStateOf(0f) }
    val gradient = Brush.verticalGradient(listOf(Red, Orange))

    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(200.dp)
            .width(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // 垂直滑块核心实现
        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight()
                .background(gradient, RoundedCornerShape(4.dp))
        ) {
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(48.dp)
                    .rotate(270f), // 通过旋转实现垂直方向
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                thumb = {
                    // 矩形滑块指示器
                    Box(
                        modifier = Modifier
                            .size(36.dp, 24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Color.hsv(
                                    hue = 30 * (1 - sliderValue), // 色相反向计算
                                    saturation = 1f,
                                    value = 1f
                                )
                            )
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullHueVerticalSlider_() {
    FullHueVerticalSlider(Modifier,{})
}

@Composable
fun FullHueVerticalSlider(
    modifier: Modifier = Modifier,
    onHueChanged: (Float) -> Unit
) {
    val trackHeight = 480.dp
    val allHeight = 500.dp
    var progress by remember { mutableFloatStateOf(0f) }

    // 计算色相（0°-360°）
    val hue = (progress * 360f).coerceIn(0f, 360f)

    // HSV 转颜色（饱和度=1，明度=1）
    val currentColor = Color.hsv(hue, 1f, 1f)

    // 实时回调色相变化
    LaunchedEffect(hue) {
        onHueChanged(hue)
    }
    Box(
        modifier = modifier.height(allHeight)
            .width(80.dp)
            .padding(2.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    val y = change.position.y.coerceIn(0f, size.height.toFloat())
                    progress = y / size.height // 直接映射 Y 轴位置到 0-1
                }
            }
    ){

        Box(
            modifier = modifier
                .width(40.dp)
                .height(trackHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = buildFullHueColors() // 生成完整色相渐变色
                    )
                )
                .border(width = 20.dp, color = Transparent)
                .align(Alignment.Center)
        )
        // 可拖动的滑块指示器
        Box(
            modifier = Modifier
                .offset(y = trackHeight * progress - 12.dp)
                .size(80.dp, 40.dp)
                .background(currentColor, RoundedCornerShape(8.dp))
//                    .border(6.dp, Color.White, RoundedCornerShape(8.dp))
                .border(6.dp, Color.White)
        )
    }
}

// 生成完整色相环颜色（每 60° 一个关键帧）
private fun buildFullHueColors(): List<Color> {
    return listOf(
        0f,   // 红
        60f,  // 黄
        120f, // 绿
        180f, // 青
        240f, // 蓝
        300f, // 紫
        360f  // 红（闭环）
    ).map { hue -> Color.hsv(hue, 1f, 1f) }
}

private val buffer = 1 // load more when scroll reaches last n item, where n >= 1


@Composable
fun<T> CircularList(
    items: List<T>,
    modifier: Modifier = Modifier,
    onItemClick: (T) -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val listState = rememberLazyListState(Int.MAX_VALUE / 2)

    LazyRow(
        state = listState,
        modifier = modifier
    ) {
        items(count = Int.MAX_VALUE) { index ->
            val actualIndex = index % items.size
            val item = items[actualIndex]

            // 使用外部提供的 Composable 渲染项
            Box(
                modifier = Modifier
                    .clickable { onItemClick(item) }
                    .padding(8.dp)
            ) {
                itemContent(item)
            }
        }
    }
}
//@Preview(showBackground = true, widthDp = 1000)
@Composable
fun CircularList_() {
    CircularList(
//        items = listOf("A", "B", "C"),
        items = electricityItemDataList,
        onItemClick = { Log.d("TAG", "Clicked: $it") }
    ) { item ->

    }

}