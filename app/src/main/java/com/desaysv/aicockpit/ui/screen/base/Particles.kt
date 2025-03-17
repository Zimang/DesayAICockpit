package com.desaysv.aicockpit.ui.screen.base



import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.getSP
import com.desaysv.aicockpit.utils.pxToDp


@Composable
fun BackgroundInputField(
    @DrawableRes bg:Int,
    modifier: Modifier=Modifier,
    onTextChange:(String)->Unit
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
                onTextChange(it)
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 100f.pxToDp())) {
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

    }
}

@Preview(
    backgroundColor = 0xff000000,showBackground = true,
    widthDp = 700
)
@Composable
fun PreviewInfiniteCarousel() {
//    InfiniteCarousel()
}
@Preview(
    backgroundColor = 0xff000000,showBackground = true,
    widthDp = 650
)
@Composable
fun PreviewInfiniteCarouselC() {
//    InfiniteScalingImageListD()
    InfiniteScalingImageList()
//    InfiniteCarousel()
}




//TODO mature
@Preview(
    backgroundColor = 0xff000000,showBackground = true,
    widthDp = 700
)
@Composable
fun InfiniteScalingImageList(
    images: List<Int> = List(10){R.drawable.b_1_h}
) {
    val loopCount = 1000 // 复制列表次数，确保足够的数据
    val infiniteImages = List(loopCount) { images }.flatten() // 拼接成大列表
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = images.size)

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val totalSize = infiniteImages.size
        val realSize = images.size

        if (listState.firstVisibleItemIndex < realSize) {
            listState.scrollToItem(realSize * (loopCount / 2)) // 回到中间部分
        } else if (listState.firstVisibleItemIndex >= totalSize - realSize) {
            listState.scrollToItem(realSize * (loopCount / 2)) // 回到中间部分
        }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(infiniteImages) { index, imageRes ->
            val realIndex = index % images.size
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val relativeIndex = realIndex - (firstVisibleItemIndex % images.size)

            val scaleFactor = if (relativeIndex < 0) {
                1.0f
            } else {
                (1.0f - (relativeIndex * relativeIndex) * 0.07f).coerceIn(0.7f, 1.0f)
            }

            val alpha = (1.0f - relativeIndex * 0.15f).coerceIn(0.3f, 1.0f)
            Box(){
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer(
                            scaleX = scaleFactor,
                            scaleY = scaleFactor,
                            alpha = alpha
                        )
                )
                Column {
                    Text("index $index")
                    Text("rindex $realIndex")
                }
            }
        }
    }
}


@Composable
fun InfiniteScalingImageList_Sound(
    onThemeChosen:(Int)->Unit,
    soundItemDataList: List<SoundItemData>
) {
    val loopCount = 1000 // 复制列表次数，确保足够的数据
    val infiniteImages = List(loopCount) { soundItemDataList }.flatten() // 拼接成大列表
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = soundItemDataList.size)

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val totalSize = infiniteImages.size
        val realSize = soundItemDataList.size

        if (listState.firstVisibleItemIndex < realSize) {
            listState.scrollToItem(realSize * (loopCount / 2)) // 回到中间部分
        } else if (listState.firstVisibleItemIndex >= totalSize - realSize) {
            listState.scrollToItem(realSize * (loopCount / 2)) // 回到中间部分
        }
    }

    LazyRow(
        state = listState,
//        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(120f.pxToDp())
    ) {
        itemsIndexed(infiniteImages) { index, imageRes ->
            val realIndex = index % soundItemDataList.size
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val relativeIndex = realIndex - (firstVisibleItemIndex % soundItemDataList.size)

            val scaleFactor = if (relativeIndex < 0) {
                1.0f
            } else {
                (1.0f - (relativeIndex * relativeIndex) * 0.07f).coerceIn(0.7f, 1.0f)
            }

            val alpha = (1.0f - relativeIndex * 0.15f).coerceIn(0.3f, 1.0f)
            Box(){

                Image(painter = painterResource(imageRes.imgId),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(342.pxToDp(),456.pxToDp())
                        .graphicsLayer(
                            scaleX = scaleFactor,
                            scaleY = scaleFactor,
                            alpha = alpha
                        )
                )

                Text(
                    text = imageRes.soundName,
                    style = TextStyle(
                    fontSize = 30.getSP(),
                        color = Color.White
                    ),
                    modifier=Modifier.padding(
                        start =24.72f.pxToDp(),
                        top = 407.77f.pxToDp()
                    )
                )
            }
        }
    }
}