package com.desaysv.aicockpit.ui.screen


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.dp
import com.desaysv.aicockpit.ui.theme.Orange


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
@Preview(showBackground = true, widthDp = 1000)
@Composable
fun CircularList_() {
    CircularList(
        items = listOf("A", "B", "C"),
//        items = electricityItemDataList,
        onItemClick = { Log.d("TAG", "Clicked: $it") }
    ) { item ->

    }

}