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
import androidx.compose.runtime.LaunchedEffect
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
import kotlin.math.ceil
import kotlin.math.floor
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
fun InfiniteCircularLazyList_5(
    onItemSelected: (SoundItemData) -> Unit,
    onItemInt: (SoundItemData) -> Unit={},
    soundItemDataList_: List<SoundItemData>,
    visibleNums: Int = 4,
) {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    var startIndex by remember { mutableStateOf(0) }

    val baseSizes = getBaseSize()
    val sizes = getLoopedSizes(baseSizes, visibleNums)

    val len = soundItemDataList_.size
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    val direction = dragOffset.value.compareTo(0f)

    fun getVisibleItems(): List<SoundItemData> {
        return List(visibleNums + 2) { i ->
            val index = (startIndex + i - 1 + len) % len
            soundItemDataList_[index]
        }
    }

    LaunchedEffect(soundItemDataList_) {
        if (!soundItemDataList_.isEmpty()){
            onItemSelected(soundItemDataList_[startIndex])
        }
    }

    val visibleItems = getVisibleItems()

    val boundsList = with(LocalDensity.current) {
        val spacing = 120f
        val centers = mutableListOf<Float>()
        var currentCenter = -200f

        for (i in sizes.indices) {
            centers += currentCenter
            val half = sizes[i].width.toPx() / 2
            if (i < sizes.lastIndex) {
                val nextHalf = sizes[i + 1].width.toPx() / 2
                currentCenter += half + spacing + nextHalf
            }
        }

        List(sizes.size) { i ->
            Bounds(
                x = centers[i] - sizes[i].width.toPx() / 2,
                dpSize = sizes[i],
                alpha = when (i) {
                    0, sizes.size - 1 -> 0f
                    1 -> 1f
                    2 -> 0.75f
                    else -> 0.4f
                }
            )
        }
    }

    val lastIndex = sizes.lastIndex
    val transitions = if (direction >= 0) {
        sizes.indices.map { i ->
            if (i == lastIndex) lerpSlotV(boundsList[i], boundsList[i], 1f)
            else lerpSlotV(boundsList[i], boundsList[i + 1], t)
        }
    } else {
        (lastIndex downTo 0).map { i ->
            if (i == 0) lerpSlotV(boundsList[i], boundsList[i], 1f)
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
                            dragOffset.snapTo((dragOffset.value + delta).coerceIn(-threshold, threshold))
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            when {
                                dragOffset.value > threshold * 0.5f -> {
                                    dragOffset.animateTo(threshold, tween(200))
                                    delay(100)
                                    startIndex = (startIndex - 1 + len) % len
                                }
                                dragOffset.value < -threshold * 0.5f -> {
                                    dragOffset.animateTo(-threshold, tween(200))
                                    delay(100)
                                    startIndex = (startIndex + 1) % len
                                }
                            }
                            dragOffset.snapTo(0f)
                            onItemSelected(soundItemDataList_[startIndex])
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
                modifier = Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
                        alpha = state.alpha
                    }
                    .clickable {
//                        val clickedIndex = (startIndex + i - 1 + len) % len
////                        startIndex = clickedIndex
//                        onItemSelected(soundItemDataList_[clickedIndex])


                        scope.launch {
                            // 调用选中回调
                            onItemSelected(soundItemDataList_[startIndex])
                            // 计算点击项在 visibleItems 中的位置与活跃项（索引 1）的差值（步数差）
                            val steps = i - 1
                            if (steps > 0) {
                                // 点击项在右侧，向左滑动 steps 次
                                repeat(steps) {
                                    dragOffset.animateTo(-threshold, tween(300))
//                                    delay(30)
                                    startIndex = (startIndex + 1) % len
                                    dragOffset.snapTo(0f)
                                }
                            } else if (steps < 0) {
                                // 点击项在左侧，向右滑动 abs(steps) 次
                                repeat(-steps) {
                                    dragOffset.animateTo(threshold, tween(300))
//                                    delay(30)
                                    startIndex = (startIndex - 1 + len) % len
                                    dragOffset.snapTo(0f)
                                }
                            }
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
                    text = item.soundName,
                    style = TextStyle(fontSize = 24.getSP(), color = Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.72f.pxToDp(), bottom = 26.05f.pxToDp())
                )
            }
        }
    }
}

@Composable
fun InfiniteCircularLazyList_6(
    onItemSelected: (SoundItemData) -> Unit,
    onItemInt: (SoundItemData) -> Unit = {},
    soundItemDataList_: List<SoundItemData>,
    visibleNums: Int = 4,
) {
    val scope = rememberCoroutineScope()
    // dragOffset 可累计多格动画，允许超过单步范围
    val dragOffset = remember { Animatable(0f) }
    // 单步的阈值（单位像素），点击或拖拽达到此值则移动一格
    val threshold = 300f
    // 初始的选中起始索引
    var startIndex by remember { mutableStateOf(0) }
    // 获得基本尺寸与循环后用于绘制的尺寸
    val baseSizes = getBaseSize()
    val sizes = getLoopedSizes(baseSizes, visibleNums)
    val len = soundItemDataList_.size

    // 使用 dragOffset 的累计值来计算“连续”进度：
    // fraction 表示当前拖动了几格（可能带小数）
    val fraction = dragOffset.value / threshold
    // fullSteps 为已跨越的完整步数；注意 floor 对负数同样生效（例如 -1.3 -> -2）
    val fullSteps = floor(fraction).toInt()
    // 当前步的进度（范围 0~1），例如 0.3 表示当前步完成 30%
    val progress = (fraction - fullSteps).toFloat()
    // 基于初始 startIndex 加上已跨越的完整步数，计算当前有效起始索引
    val effectiveStartIndex = (startIndex + fullSteps + len) % len

    // 生成当前可见项
    fun getVisibleItems(effectiveStart: Int): List<SoundItemData> {
        return List(visibleNums + 2) { i ->
            val index = (effectiveStart + i - 1 + len) % len
            soundItemDataList_[index]
        }
    }
    val visibleItems = getVisibleItems(effectiveStartIndex)

    // 根据当前可见尺寸计算 slot 的显示边界（位置、尺寸、透明度）
    val boundsList = with(LocalDensity.current) {
        val spacing = 120f
        val centers = mutableListOf<Float>()
        var currentCenter = -200f
        for (i in sizes.indices) {
            centers += currentCenter
            val half = sizes[i].width.toPx() / 2
            if (i < sizes.lastIndex) {
                val nextHalf = sizes[i + 1].width.toPx() / 2
                currentCenter += half + spacing + nextHalf
            }
        }
        List(sizes.size) { i ->
            Bounds(
                x = centers[i] - sizes[i].width.toPx() / 2,
                dpSize = sizes[i],
                alpha = when (i) {
                    0, sizes.size - 1 -> 0f
                    1 -> 1f
                    2 -> 0.75f
                    else -> 0.4f
                }
            )
        }
    }

    // 根据 current dragOffset 的方向与当前进度计算过渡动画效果
    val transitions = if (dragOffset.value >= 0f) {
        sizes.indices.map { i ->
            if (i == sizes.lastIndex)
                lerpSlotV(boundsList[i], boundsList[i], 1f)
            else
                lerpSlotV(boundsList[i], boundsList[i + 1], progress)
        }
    } else {
        sizes.indices.map { i ->
            if (i == 0)
                lerpSlotV(boundsList[i], boundsList[i], 1f)
            else
                lerpSlotV(boundsList[i], boundsList[i - 1], progress)
        }
    }

    // 初始化时通知当前选中项（未拖拽或点击前）
    LaunchedEffect(soundItemDataList_) {
        if (soundItemDataList_.isNotEmpty()) {
            onItemSelected(soundItemDataList_[startIndex])
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            // 处理横向拖拽，允许 dragOffset 超出单步范围
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            // 累加拖拽偏移，不做硬性限制，让其超出 threshold
                            dragOffset.snapTo(dragOffset.value + delta)
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            // 结束拖拽时，依据当前进度自动滚动到最近的整格
                            val targetFraction = if (progress >= 0.5f) ceil(fraction) else floor(fraction)
                            dragOffset.animateTo(targetFraction * threshold, tween(durationMillis = 150))
                            // 更新 startIndex 为原始 startIndex 加上整格偏移量（四舍五入）
                            val deltaSteps = targetFraction.toInt()
                            startIndex = (startIndex + deltaSteps + len) % len
                            // 重置拖拽偏移量为 0
                            dragOffset.snapTo(0f)
                            onItemSelected(soundItemDataList_[startIndex])
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        // 绘制每个可见项
        transitions.forEachIndexed { i, state ->
            val item = visibleItems[i]
            val painter = if (item.imgId != -1) {
                // 如果使用本地文件加载图片
                rememberAsyncImagePainter(java.io.File(item.imgPath))
            } else {
                // 否则从 assets 加载图片
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }
            Box(
                modifier = Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
                        alpha = state.alpha
                    }
                    .clickable {
                        scope.launch {
                            // 点击时计算点击项与当前活跃项（visibleItems 中索引 1）的步数差
                            val steps = i - 1
                            // 目标拖拽偏移量为 steps * (-threshold)
                            // 注意：当点击项在右侧时 steps > 0，此时目标为负，反之亦然
                            val target = steps * (-threshold)
                            // 一次连续动画，将 dragOffset 从 0 动画到目标值
                            dragOffset.animateTo(target, tween(durationMillis = abs(steps) * 150))
                            // 更新 startIndex（点击后，相对于原始 startIndex 直接加上整步数）
                            startIndex = (startIndex + steps + len) % len
                            // 重置拖拽偏移量
                            dragOffset.snapTo(0f)
                            onItemSelected(soundItemDataList_[startIndex])
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
                    text = item.soundName,
                    style = androidx.compose.ui.text.TextStyle(fontSize = 24.getSP(), color = androidx.compose.ui.graphics.Color.White),
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
