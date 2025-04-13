package com.desaysv.aicockpit.ui.screen.base



import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import coil.compose.rememberAsyncImagePainter
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.getSP
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.pxToDp
import java.io.File
import kotlin.math.abs
import kotlin.math.min

val defaultSoundList= listOf(
    SoundItemData(
        id = 1,
        imageName = "布谷鸟",
        soundName = "布谷鸟",
        imgId =-1,
        imgPath = "布谷鸟.png",
        audioPath ="布谷鸟.mp3"
    ),
    SoundItemData(
        id = 2,
        imageName = "树叶婆娑",
        soundName = "树叶婆娑",
        imgId =-1,
        imgPath = "树叶婆娑.png",
        audioPath ="树叶婆娑.mp3"),
    SoundItemData(
        id = 3,
        imageName = "b_3_h",
        soundName = "b_3_h",
        imgId =-1,
        imgPath = "b_3_h.png",
        audioPath ="b_3_h.mp3"),
    SoundItemData(
        id = 4,
        imageName = "b_4_h",
        soundName = "b_4_h",
        imgId =-1,
        imgPath = "b_4_h.png",
        audioPath ="b_4_h.mp3"),
)

@Composable
fun BackgroundInputFieldV1(
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
            textStyle = TextStyle(
                color = Color.White, // 文字颜色根据背景图区域设置
                fontSize =24.pxToDp().value.sp,
            ),
            cursorBrush = SolidColor(colorResource(R.color.choosen)), // 光标颜色
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
//                            .padding(end = 100f.pxToDp())
                    ) {

                        Text(
                            text=ResourceManager.getThemeName()!!,
                            modifier=Modifier
                                .alpha(0.8f)
                                .padding(
                                    start = 31.34f.pxToDp(),
                                    end = 45.63f.pxToDp()
                                ),
                            fontSize = 24.getSP(),
                            color = Color.White
                        )

                        Box(modifier = Modifier.padding(
//                            top =22.88f.pxToDp(),
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
    widthDp = 650
)
@Composable
fun PreviewInfiniteCarouselC() {
    InfiniteScalingImageList()
}





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
    soundItemDataList: List<SoundItemData>,
    usingLocalPath:Boolean=false
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

                val painter = if (imageRes.imgPath != null) {
                    rememberAsyncImagePainter(File(imageRes.imgPath))
                } else {
                    painterResource(id = imageRes.imgId ?: android.R.drawable.ic_menu_gallery)
                }

                Image(painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(342.pxToDp(), 456.pxToDp())
                        .graphicsLayer(
                            scaleX = scaleFactor,
                            scaleY = scaleFactor,
                            alpha = alpha
                        )
                )

                Text(
//                    text = imageRes.soundName,
                    text = imageRes.imgPath,
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

@Composable
fun InfiniteScalingImageList_SoundV1(
    onThemeChosen:(Int)->Unit,
    soundItemDataList: List<SoundItemData>,
    usingLocalPath:Boolean=false
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

                val painter = if (imageRes.imgPath != null) {
                    rememberAsyncImagePainter(File(imageRes.imgPath))
                } else {
                    painterResource(id = imageRes.imgId ?: android.R.drawable.ic_menu_gallery)
                }

                Image(painter = painter,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(342.pxToDp(), 456.pxToDp())
                        .graphicsLayer(
                            scaleX = scaleFactor,
                            scaleY = scaleFactor,
                            alpha = alpha
                        )
                )

                Text(
                    text = imageRes.soundName,
//                    text = imageRes.imgPath,
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


@Composable
fun InfiniteScalingImageList_SoundV2(
    onThemeChosen: (SoundItemData) -> Unit,
    soundItemDataList_: List<SoundItemData>,
    usingLocalPath: Boolean = true
) {
    var len =soundItemDataList_.size
    var soundItemDataList:List<SoundItemData> = emptyList()
    if (len==0) {
        soundItemDataList= defaultSoundList
        len=soundItemDataList.size
        Log.d("使用默认配置Sounds")
    } else{
        soundItemDataList=soundItemDataList_
    }


    var startIndex by remember { mutableStateOf(0) }
    val dragOffset = remember { mutableStateOf(0f) }
    val threshold = 100f


    val visibleItems =  List(Math.min(len,4)) { i ->
        val realIndex = (startIndex + i) % soundItemDataList.size
        soundItemDataList[realIndex] to realIndex
    }

    val itemSizes = listOf(
        DpSize(342.pxToDp(), 456.pxToDp()),
        DpSize(258.pxToDp(), 344.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp())
    )
    val edgeSpacing = 120.pxToDp()
    val xOffsets = calculateXOffsets(itemSizes, edgeSpacing)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(456.pxToDp())
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        dragOffset.value += delta
                    },
                    onDragEnd = {
                        when {
                            dragOffset.value > threshold -> {
                                startIndex =
                                    (startIndex - 1 + soundItemDataList.size) % soundItemDataList.size
                            }

                            dragOffset.value < -threshold -> {
                                startIndex = (startIndex + 1) % soundItemDataList.size
                            }
                        }
                        dragOffset.value = 0f
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        visibleItems.forEachIndexed { i, (item, realIndex) ->
            val painter = if (usingLocalPath&&item.imgId!=-1) {
                Log.d(item.imgPath)
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                Log.d(item.imgPath)
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            val alpha = when (i) {
                0 -> 1f
                1 -> 0.75f
                else -> 0.4f
            }

            Box(
                modifier = Modifier
                    .absoluteOffset(x = xOffsets[i], y = 0.dp)
                    .size(itemSizes[i])
                    .clickable {
                        startIndex = realIndex
                        onThemeChosen(soundItemDataList[realIndex])
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    alpha = alpha
                )

                Text(
                    text = item.soundName,
                    style = TextStyle(
                        fontSize = 24.getSP(),
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.72f.pxToDp(), bottom = 26.05f.pxToDp())
                )
            }
        }
    }
}
@Composable
fun InfiniteCircularSoundList(
    onThemeChosen: (SoundItemData) -> Unit,
    soundItemDataList_: List<SoundItemData>,
    usingLocalPath: Boolean = true
) {
    val soundItemDataList = if (soundItemDataList_.isEmpty()) {
        Log.d("使用默认配置Sounds")
        defaultSoundList
    } else soundItemDataList_

    val len = soundItemDataList.size
    if (len < 5) return // 至少要 5 个元素

    var startIndex by remember { mutableStateOf(0) }
    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val thresholdPx = 342f  // 拖动到一个 item 的宽度就切换
    val edgeSpacing = 120.pxToDp()
    val slotCount = 5       // 固定 5 个 slot

    // 各个 slot 的大小和透明度（slot0 最大，slot4 最小）
    val baseSizes = listOf(
        DpSize(342.pxToDp(), 456.pxToDp()),
        DpSize(258.pxToDp(), 344.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp())
    )
    val baseAlphas = listOf(1f, 0.75f, 0.4f, 0.4f, 0.4f)

    val visibleItems = List(slotCount) { i ->
        val realIndex = (startIndex + i) % len
        soundItemDataList[realIndex] to realIndex
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(456.pxToDp())
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            dragOffset.snapTo((dragOffset.value + delta).coerceIn(-thresholdPx, thresholdPx))
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            val endOffset = dragOffset.value
                            if (endOffset > thresholdPx / 2) {
                                // 向右滑
                                dragOffset.animateTo(thresholdPx, tween(150))
                                startIndex = (startIndex - 1 + len) % len
                            } else if (endOffset < -thresholdPx / 2) {
                                // 向左滑
                                dragOffset.animateTo(-thresholdPx, tween(150))
                                startIndex = (startIndex + 1) % len
                            }
                            dragOffset.animateTo(0f, tween(200))
                        }
                    }
                )
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleItems.forEachIndexed { slotIndex, (item, realIndex) ->
            // 计算滑动进度：-1（左滑满）~ +1（右滑满）
            val progress = (dragOffset.value / thresholdPx).coerceIn(-1f, 1f)

            // 计算当前 slotIndex 在拖动方向下要过渡到哪个 slot
            val fromSlot = slotIndex
            val toSlot = if (progress < 0f) slotIndex + 1 else slotIndex - 1

            val clampedToSlot = toSlot.coerceIn(0, slotCount - 1)
            val size = lerp(baseSizes[fromSlot], baseSizes[clampedToSlot], abs(progress))
            val alpha = lerp(baseAlphas[fromSlot], baseAlphas[clampedToSlot], abs(progress))

            val painter = if (usingLocalPath && item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = size.width.value / baseSizes[0].width.value
                        scaleY = size.height.value / baseSizes[0].height.value
                        this.alpha = alpha
                    }
                    .padding(horizontal = edgeSpacing / 2)
                    .size(baseSizes[0]) // 每个 Box 占位大小固定，用 scale 实现视觉变化
                    .clickable {
                        startIndex = realIndex
                        onThemeChosen(soundItemDataList[realIndex])
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                Text(
                    text = item.soundName,
                    style = TextStyle(
                        fontSize = 24.getSP(),
                        color = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.72f.pxToDp(), bottom = 26.05f.pxToDp())
                )
            }
        }
    }
}


@Composable
fun ScalingCarousel(
    data: List<String>, // 图片路径或 URL
    modifier: Modifier = Modifier
) {
    var startIndex by remember { mutableStateOf(0) }
    val dragOffset = remember { mutableStateOf(0f) }
    val threshold = 100f // 拖动超过这个阈值才会触发翻页
    val visibleItems = List(4) { i -> data[(startIndex + i) % data.size] }
    val itemSizes = listOf(
        DpSize(342.dp, 456.dp),
        DpSize(258.dp, 344.dp),
        DpSize(168.dp, 224.dp),
        DpSize(168.dp, 224.dp)
    )
    val edgeSpacing = 120.dp
    val xOffsets = calculateXOffsets(itemSizes, edgeSpacing)

    Box(modifier = modifier
        .fillMaxWidth()
        .height(480.dp)
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { _, delta ->
                    dragOffset.value += delta
                },
                onDragEnd = {
                    when {
                        dragOffset.value > threshold -> {
                            startIndex = (startIndex - 1 + data.size) % data.size
                        }

                        dragOffset.value < -threshold -> {
                            startIndex = (startIndex + 1) % data.size
                        }
                    }
                    dragOffset.value = 0f
                }
            )
        },
        contentAlignment = Alignment.CenterStart
    ) {
        visibleItems.forEachIndexed { i, imageUrl ->
            Box(
                modifier = Modifier
                    .absoluteOffset(x = xOffsets[i], y = 0.dp)
                    .size(itemSizes[i])
                    .clickable {
                        // 可点击项，例如滚动到最前面或选中
                    },
                contentAlignment = Alignment.BottomStart
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // 示例标题，可以自定义
                Text(
                    text = "第${(startIndex + i) % data.size}项",
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(4.dp),
                    color = Color.White
                )
            }
        }

        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                startIndex = (startIndex - 1 + data.size) % data.size
            }) {
                Text("←")
            }
            Button(onClick = {
                startIndex = (startIndex + 1) % data.size
            }) {
                Text("→")
            }
        }

    }
}

private fun calculateXOffsets(sizes: List<DpSize>, spacing: Dp): List<Dp> {
    val offsets = mutableListOf<Dp>()
    var x = 0.dp
    for (size in sizes) {
        offsets.add(x)
        x += size.width + spacing
    }
    return offsets
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 600)
@Composable
fun PreviewScalingCarousel() {
    val sampleImages = listOf(
        "https://picsum.photos/seed/1/600/800",
        "https://picsum.photos/seed/2/600/800",
        "https://picsum.photos/seed/3/600/800",
        "https://picsum.photos/seed/4/600/800",
        "https://picsum.photos/seed/5/600/800"
    )
    ScalingCarousel(data = sampleImages)
}