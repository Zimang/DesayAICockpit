package com.desay.desayaicockpit.ui.screen


import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.ui.theme.Orange


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
fun PreviewVerticalColorSlider() {
    VerticalColorSlider()
}