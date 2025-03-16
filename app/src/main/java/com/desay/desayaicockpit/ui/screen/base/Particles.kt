package com.desay.desayaicockpit.ui.screen.base



import android.content.res.Configuration
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.launch
import kotlin.math.abs


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

//@Preview(showBackground = true)
//@Composable
//fun BackgroundInputField_() {
//    BackgroundInputField(R.drawable.save_input_bg)
//
//}
@Composable
fun InfiniteCarousel() {
    // 图片资源列表（20个示例）
    val imageIds = remember { List(10) { R.drawable.b_1_h } }

    // 状态管理
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidthPx = with(density) { config.screenWidthDp.dp.toPx() }
    val itemWidthPx = remember { screenWidthPx / 4 } // 每项占屏幕1/4宽度

    val scrollOffset = remember { mutableStateOf(0f) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 自动重置循环位置
    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
        when {
            listState.firstVisibleItemIndex > imageIds.size * 100 -> {
                Log.d("TAG", "InfiniteCarousel: d")
                listState.scrollToItem(imageIds.size * 50)
            }
            listState.firstVisibleItemIndex < imageIds.size * 10 -> {
                listState.scrollToItem(imageIds.size * 50)
                Log.d("TAG", "InfiniteCarousel: d")
            }
        }
    }

    // 动态缩放计算
    fun calculateScale(itemLeft: Float): Float {
        val visibleRatio = 1 - (itemLeft / screenWidthPx).coerceIn(0f, 1f)
        return 1f + visibleRatio // 缩放范围：1x ~ 2x
    }

    LazyRow(
        state = listState,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        scrollOffset.value -= dragAmount.x
                    },
                    onDragEnd = {
                        scope.launch {
                            val decay = splineBasedDecay<Float>(this@pointerInput)
                            val target = decay.calculateTargetValue(
                                scrollOffset.value,
                                -scrollOffset.value / 10 // 模拟惯性
                            )
                            scrollOffset.value = target
                        }
                    }
                )
            }
    ) {
        items(Int.MAX_VALUE) { globalIndex ->
            val actualIndex = globalIndex % imageIds.size
            val translationX = scrollOffset.value - actualIndex * itemWidthPx
            val scale = calculateScale(translationX)

            Box(
                Modifier
                    .width(with(density) { itemWidthPx.toDp() })
                    .aspectRatio(1f)
                    .graphicsLayer {
                        this.translationX = translationX
                        scaleX = scale
                        scaleY = scale
                        alpha = if (scale > 1.5) 1f else 0.7f
                        println("ItemWidthPx: $itemWidthPx")
                        println("scale: $scale")
                    }
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = imageIds[actualIndex]),
                    contentDescription = null,
                    modifier = Modifier.size(300f.pxToDp()),
                    contentScale = ContentScale.Crop
                )
                Text("g $globalIndex r $actualIndex s $scale", color = Color.White)
            }
        }
    }
}

@Preview(
    backgroundColor = 0xff000000,showBackground = true,
    widthDp = 700
)
@Composable
fun PreviewInfiniteCarousel() {
    InfiniteCarouselB()
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

@Composable
fun InfiniteCarouselB() {
    val items = remember { List(20) { it } } // 原始数据
    val loopItems = remember { List(20 * 1000) { it % 20 } }
    val itemSize = 200.dp
    val spacing = 40.dp

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = items.size * 500)
    val screenWidthPx = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    // 自动重置循环位置
    LaunchedEffect(listState.firstVisibleItemIndex) {
        when {
            listState.firstVisibleItemIndex > items.size * 800 ->
                listState.scrollToItem(items.size * 500)
            listState.firstVisibleItemIndex < items.size * 200 ->
                listState.scrollToItem(items.size * 500)
        }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        contentPadding = PaddingValues(horizontal = (screenWidthPx / 2).dp) // 左右留白
    ) {
        items(loopItems.size) { index ->
            val actualIndex = index % items.size
            val visibleItemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }

            val scale = visibleItemInfo?.let {
                val itemLeft = it.offset.toFloat() // 获取列表项左侧偏移量
                val distanceFactor = itemLeft / screenWidthPx // 计算相对屏幕宽度的比例 (0 ~ 1)
                (1.0f + (1.2f - 1.0f) * (1 - distanceFactor)).coerceIn(1.0f, 1.2f)
            } ?: 1f // 1.0 ~ 1.2 值越小越大

            Box(
                Modifier
                    .size(itemSize)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale - 0.2f // 透明度联动
                    }
                    .background(Color.Gray, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Item $actualIndex",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ScalingImageList(
    images: List<Int>
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(images) { index, imageRes ->
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset.toFloat()

            val relativeIndex = index - firstVisibleItemIndex
            val scaleFactor = 1.0f - (relativeIndex * 0.1f) // 最左侧最大，向右依次缩小

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .graphicsLayer(
                        scaleX = scaleFactor.coerceIn(0.7f, 1.0f), // 限制缩放范围
                        scaleY = scaleFactor.coerceIn(0.7f, 1.0f)
                    )
            )
        }
    }
}

@Preview
@Composable
fun ScalingImageList_() {
    val images= List(10){ R.drawable.b_1_h }
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(images) { index, imageRes ->
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset.toFloat()

            val relativeIndex = index - firstVisibleItemIndex
            val scaleFactor = 1.0f - (relativeIndex * 0.1f) // 最左侧最大，向右依次缩小

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .graphicsLayer(
                        scaleX = scaleFactor.coerceIn(0.7f, 1.0f), // 限制缩放范围
                        scaleY = scaleFactor.coerceIn(0.7f, 1.0f)
                    )
            )
        }
    }
}


@Composable
fun ScalingImageListC(
    images: List<Int> = List(10){R.drawable.b_1_h}
) {
    val listState = rememberLazyListState()

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(images) { index, imageRes ->
            val firstVisibleItemIndex = listState.firstVisibleItemIndex
            val relativeIndex = index - firstVisibleItemIndex

            // 确保第一张图片退出屏幕后不再变化
            val scaleFactor = if (relativeIndex < 0) {
                1.0f
            } else {
                (1.0f - (relativeIndex * relativeIndex) * 0.07f).coerceIn(0.7f, 1.0f)
            }

            // 透明度随索引变化
            val alpha = (1.0f - relativeIndex * 0.15f).coerceIn(0.3f, 1.0f)

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
        }
    }
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
fun InfiniteScalingImageListD(
    images: List<Int> = List(10){R.drawable.b_1_h}
) {
    val loopCount = 3 // 复制列表次数，确保足够的数据
    val infiniteImages = List(loopCount) { images }.flatten() // 拼接成大列表
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = images.size)

    // 监听滚动位置
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val totalSize = infiniteImages.size
        val realSize = images.size
        val currentIndex = listState.firstVisibleItemIndex

        // 在接近两端时调整滚动
        if (currentIndex < realSize) {
            // 缓慢回到中间，避免突兀的回跳
            listState.animateScrollToItem(realSize * (loopCount / 2))
        } else if (currentIndex >= totalSize - realSize) {
            listState.animateScrollToItem(realSize * (loopCount / 2))
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
        }
    }
}


