package com.desaysv.aicockpit.ui.screen.base

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.getSP
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.pxToDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.abs
import kotlin.math.roundToInt
@Composable
fun getBaseSize()= listOf(
    DpSize(342.pxToDp(), 456.pxToDp()),
    DpSize(258.pxToDp(), 344.pxToDp()),
    DpSize(168.pxToDp(), 224.pxToDp()),
    DpSize(168.pxToDp(), 224.pxToDp())
)



@Preview
@Composable
fun MultiSlotImageDemo() {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    var startIndex by remember { mutableStateOf(1) }

    val dpSizes = getBaseSize()
    val boundsList = with(LocalDensity.current) {
        val sizes = listOf(
            dpSizes[3], // slot0 - 隐藏
            dpSizes[0], // slot1 - 最大
            dpSizes[1], // slot2
            dpSizes[2], // slot3
            dpSizes[3], // slot4
            dpSizes[0], // slot5 - 隐藏
        )
        val edgeSpacingPx = 120f
        val slotCount = sizes.size
        val centers = mutableListOf<Float>()

        // 初始化 slot1 的中心起点（slot0 稍微在左侧）
        var currentCenter = 700f

        for (i in 0 until slotCount) {
            centers += currentCenter
            val halfCurrent = sizes[i].width.toPx() / 2
            if (i < slotCount - 1) {
                val halfNext = sizes[i + 1].width.toPx() / 2
                currentCenter += halfCurrent + edgeSpacingPx + halfNext
            }
        }

        // 构造 Bounds 列表
        List(slotCount) { i ->
            Bounds(
                x = centers[i] - sizes[i].width.toPx() / 2,
                dpSize = sizes[i],
                alpha = if (i == 0 || i == 5) 0f else 1f
            )
        }
    }

    val allImages = listOf(
        "https://picsum.photos/id/1000/300/300",
        "https://picsum.photos/id/1001/300/300",
        "https://picsum.photos/id/1002/300/300",
        "https://picsum.photos/id/1003/300/300",
        "https://picsum.photos/id/1004/300/300",
        "https://picsum.photos/id/1005/300/300",
        "https://picsum.photos/id/1006/300/300",
        "https://images.unsplash.com/photo-1741986947217-d1a0ecc39149?q=80&w=1166&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
    )
    fun getVisibleImages(): List<String> {
        val indices = listOf(-1, 0, 1, 2, 3, 4).map {
            (startIndex + it + allImages.size) % allImages.size
        }
        return indices.map { allImages[it] }
    }
    val imagesUrls=getVisibleImages()

    val direction = dragOffset.value.compareTo(0f)

    val transitions = if (direction >= 0) {
        // 左滑：1→2, 2→3, ..., 5→6（第6不动）
        (0 until 6).map { i ->
            if(i==5){
                lerpSlotV(boundsList[5], boundsList[5], 1f)
            }else{
                lerpSlotV(boundsList[i], boundsList[i + 1], t)
            }
        }
    } else {
        // 右滑：6→5, 5→4, ..., 2→1（第1不动）
        (5 downTo  0).map { i ->
            if(i==0){
                lerpSlotV(boundsList[0], boundsList[0], 1f)
            }else{
                lerpSlotV(boundsList[i], boundsList[i - 1], -t)
            }
        }.reversed() // 重新排成 0~4 顺序对应图片
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            dragOffset.snapTo(
                                (dragOffset.value + delta).coerceIn(
                                    -threshold,
                                    threshold
                                )
                            )
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            if (dragOffset.value > threshold * 0.5f) {
                                dragOffset.animateTo(threshold, tween(150))
                                delay(100) //等待一帧
                                startIndex = (startIndex - 1 + allImages.size) % allImages.size
                            } else if (dragOffset.value < -threshold * 0.5f) {
                                dragOffset.animateTo(-threshold, tween(150))
                                delay(100)
                                startIndex = (startIndex + 1) % allImages.size
                            }
                            dragOffset.snapTo(0f) // 立即归位，数据已换
                        }
                    }
                )
            }
    ) {
        transitions.forEachIndexed { i, state ->
            Box(
                Modifier
                    .absoluteOffset { IntOffset(state.x.roundToInt(), 60) }
                    .size(state.size)
                    .graphicsLayer { alpha = state.alpha }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imagesUrls[i]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}



data class Slot(val x: Float, val scale: Float, val alpha: Float)
data class Bounds(val x: Float, val dpSize: DpSize, val alpha: Float)

data class SlotVisual(
    val x: Float,
    val size: DpSize,
    val alpha: Float
)

fun lerpSlotV(from: Bounds, to: Bounds, t: Float): SlotVisual {
    return SlotVisual(
        x = lerp(from.x, to.x, t),
        size = DpSize(
            lerp(from.dpSize.width, to.dpSize.width, t),
            lerp(from.dpSize.height, to.dpSize.height, t)
        ),
        alpha = lerp(from.alpha, to.alpha, t)
    )
}
fun lerpSlot(from: Slot, to: Slot, t: Float): Slot {
    return Slot(
        x = lerp(from.x, to.x, t),
        scale = lerp(from.scale, to.scale, t),
        alpha = lerp(from.alpha, to.alpha, t)
    )
}
fun lerp(a: Dp, b: Dp, t: Float): Dp = a + (b - a) * t




@Composable
fun InfiniteCircularLazyList_4(
    onItemSelected: (SoundItemData) -> Unit,
    soundItemDataList_: List<SoundItemData>,
    visiableNums:Int=4 ,//可见项
) {
    val scope = rememberCoroutineScope()
    val `dragOffset` = remember { Animatable(0f) }
    val threshold = 300f
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    var startIndex by remember { mutableStateOf(1) }

    Log.d("可见项 $visiableNums 数据集 ${soundItemDataList_.size}")
    val sizes= getLoopedSizes(getBaseSize(),visiableNums)
    val soundItemDataList= getLoopedItems(soundItemDataList_,visiableNums)
    val boundsList = with(LocalDensity.current) {
        val edgeSpacingPx = 120f
        val slotCount = sizes.size
        val centers = mutableListOf<Float>()

        var currentCenter = -200f

        for (i in 0 until slotCount) {
            centers += currentCenter
            val halfCurrent = sizes[i].width.toPx() / 2
            if (i < slotCount - 1) {
                val halfNext = sizes[i + 1].width.toPx() / 2
                currentCenter += halfCurrent + edgeSpacingPx + halfNext
            }
        }

        List(slotCount) { i ->
            Bounds(
                x = centers[i] - sizes[i].width.toPx() / 2,
                dpSize = sizes[i],
                alpha = when(i){
                    0,sizes.size-1->0f
                    1->1f
                    2->0.75f
                    else-> 0.4f
                }
            )
        }
    }

    Log.d("check len")
    val len = soundItemDataList.size
    Log.d("check len pass")

    fun getVisibleItems(): List<SoundItemData> {
        val indices = listOf(-1, 0, 1, 2, 3, 4).map {
            (startIndex + it + len) % len
        }
        return indices.map { soundItemDataList[it] }
    }


    val visibleItems = getVisibleItems()
    val direction = dragOffset.value.compareTo(0f)

    val lastIndex=sizes.size-1
    val transitions = if (direction >= 0) {
        sizes.indices.map { i ->
            if (i == lastIndex) lerpSlotV(boundsList[lastIndex], boundsList[lastIndex], 1f)
            else lerpSlotV(boundsList[i], boundsList[i + 1], t)
        }
    } else {
        (lastIndex downTo 0).map { i ->
            if (i == 0) lerpSlotV(boundsList[0], boundsList[0], 1f)
            else lerpSlotV(boundsList[i], boundsList[i - 1], -t)
        }.reversed()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            dragOffset.snapTo(
                                (dragOffset.value + delta).coerceIn(
                                    -threshold,
                                    threshold
                                )
                            )
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            if (dragOffset.value > threshold * 0.5f) {
                                dragOffset.animateTo(threshold, tween(150))
                                delay(100)
                                startIndex = (startIndex - 1 + len) % len
                            } else if (dragOffset.value < -threshold * 0.5f) {
                                dragOffset.animateTo(-threshold, tween(150))
                                delay(100)
                                startIndex = (startIndex + 1) % len
                            }
                            dragOffset.snapTo(0f)
                            onItemSelected(visibleItems[startIndex])
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        transitions.forEachIndexed { i, state ->
            val item = visibleItems[i]
            val painter = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            Box(
                Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
                        alpha = state.alpha
                    }
                    .clickable {
                        onItemSelected(item)

                        val clickedIndex = i
                        if (clickedIndex in 1..4) {
                            val len = soundItemDataList_.size
                            val indices = listOf(-1, 0, 1, 2, 3, 4).map {
                                (startIndex + it + len) % len
                            }
                            val clickedRealIndex = indices[clickedIndex]
                            startIndex = clickedRealIndex
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
//                    text = item.soundName + " " + item.id,
                    text = item.soundName ,
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
fun InfiniteCircularLazyList_3(
    onItemSelected: (SoundItemData) -> Unit,
    soundItemDataList_: List<SoundItemData>,
    visiableNums:Int=4 ,//可见项
) {
    val scope = rememberCoroutineScope()
    val `dragOffset` = remember { Animatable(0f) }
    val threshold = 300f
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    var startIndex by remember { mutableStateOf(1) }

    Log.d("可见项 $visiableNums 数据集 ${soundItemDataList_.size}")
    val sizes= getLoopedSizes(getBaseSize(),visiableNums)
    val soundItemDataList= getLoopedItems(soundItemDataList_,visiableNums)
    val boundsList = with(LocalDensity.current) {
        val edgeSpacingPx = 120f
        val slotCount = sizes.size
        val centers = mutableListOf<Float>()

        var currentCenter = -200f

        for (i in 0 until slotCount) {
            centers += currentCenter
            val halfCurrent = sizes[i].width.toPx() / 2
            if (i < slotCount - 1) {
                val halfNext = sizes[i + 1].width.toPx() / 2
                currentCenter += halfCurrent + edgeSpacingPx + halfNext
            }
        }

        List(slotCount) { i ->
            Bounds(
                x = centers[i] - sizes[i].width.toPx() / 2,
                dpSize = sizes[i],
                alpha = when(i){
                    0,sizes.size-1->0f
                    1->1f
                    2->0.75f
                    else-> 0.4f
                }
            )
        }
    }

    Log.d("check len")
    val len = soundItemDataList.size
    Log.d("check len pass")

    fun getVisibleItems(): List<SoundItemData> {
        val indices = listOf(-1, 0, 1, 2, 3, 4).map {
            (startIndex + it + len) % len
        }
        return indices.map { soundItemDataList[it] }
    }


    val visibleItems = getVisibleItems()
    val direction = dragOffset.value.compareTo(0f)

    val lastIndex=sizes.size-1
    val transitions = if (direction >= 0) {
        sizes.indices.map { i ->
            if (i == lastIndex) lerpSlotV(boundsList[lastIndex], boundsList[lastIndex], 1f)
            else lerpSlotV(boundsList[i], boundsList[i + 1], t)
        }
    } else {
        (lastIndex downTo 0).map { i ->
            if (i == 0) lerpSlotV(boundsList[0], boundsList[0], 1f)
            else lerpSlotV(boundsList[i], boundsList[i - 1], -t)
        }.reversed()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            dragOffset.snapTo(
                                (dragOffset.value + delta).coerceIn(
                                    -threshold,
                                    threshold
                                )
                            )
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            if (dragOffset.value > threshold * 0.5f) {
                                dragOffset.animateTo(threshold, tween(150))
                                delay(100)
                                startIndex = (startIndex - 1 + len) % len
                            } else if (dragOffset.value < -threshold * 0.5f) {
                                dragOffset.animateTo(-threshold, tween(150))
                                delay(100)
                                startIndex = (startIndex + 1) % len
                            }
                            dragOffset.snapTo(0f)
                            onItemSelected(visibleItems[startIndex])
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        transitions.forEachIndexed { i, state ->
            val item = visibleItems[i]
            val painter = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            Box(
                Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
                        alpha = state.alpha
                    }
                    .clickable {
                        onItemSelected(item)
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
//                    text = item.soundName + " " + item.id,
                    text = item.soundName ,
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


fun getLoopedItems(data: List<SoundItemData>, visibleCount: Int): List<SoundItemData> {
    return List(visibleCount ) { i ->
        when (i) {
            0 -> data[(visibleCount - 1) % data.size] // 前一个
            visibleCount + 1 -> data[0 % data.size]    // 后一个
            else -> data[(i - 1) % data.size]
        }
    }
}
fun getLoopedSizes(dpSizes: List<DpSize>, visibleCount: Int): List<DpSize> {
    return List(visibleCount + 2) { i ->
        when (i) {
            0 -> dpSizes[(visibleCount - 1) % dpSizes.size]
            visibleCount + 1 -> dpSizes[0 % dpSizes.size]
            else -> dpSizes[(i - 1) % dpSizes.size]
        }
    }
}

fun computeVisibleNum(dataSize: Int, maxVisible: Int = 4): Int {
    Log.d("数据集大小 $dataSize 可见项参数 $maxVisible")
    return if (dataSize >= maxVisible) maxVisible else dataSize
}
