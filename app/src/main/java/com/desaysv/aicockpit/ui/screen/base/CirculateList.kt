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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun InfiniteCircularLazyList_5(
    onSoundInvoke2Play: (SoundItemData) -> Unit,
    onSoundChosen: (SoundItemData) -> Unit={},
    soundItemDataList_: List<SoundItemData>,
    visibleNums: Int = 4,
    chosenUI: @Composable (Int)->Unit,
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
            onSoundChosen(soundItemDataList_[startIndex])
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
                            onSoundInvoke2Play(soundItemDataList_[startIndex])
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        transitions.forEachIndexed { i, state ->
            val item = visibleItems[i]
            val imageHeightPx = with(LocalDensity.current) { state.size.height.toPx() }
            val painter = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }
            val modifiedPath = item.imgPath.replace(".png", "_.png")

            val painter_ = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath+"_"))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/$modifiedPath")
            }

            Box(
                modifier = Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
//                        alpha = state.alpha
                    }
                    .clickable {
                        if(i==1){
                            onSoundChosen(soundItemDataList_[startIndex])
                        }else{
                            Log.d("$i 并非最左边")
                        }
                        scope.launch {
                            // 调用选中回调
                            onSoundInvoke2Play(soundItemDataList_[startIndex])
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
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )

                val imageModifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = state.alpha
                    }

                // 主图
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
                chosenUI(item.id)
//                    // 选中
//                    Image(
//                        painter =  rememberAsyncImagePainter("file:///android_asset/images/xz.png"),
//                        contentDescription = null,
//                        modifier = imageModifier,
//                        contentScale = ContentScale.Crop
//                    )

                // 倒影
                Image(
                    painter = painter_,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
//                            scaleY = -1f
                            translationY = imageHeightPx * 1.05f
                        }
                        .pointerInput(Unit) {}, // 禁用点击
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
fun InfiniteCircularLazyList_7(
    onItemInvoke2Play: (SoundItemData) -> Unit,
    onItemChosen: (SoundItemData) -> Unit = {},
    soundItemDataList: List<SoundItemData>,
    visibleNums: Int = 3,
    centerShift: Dp = 50.dp // 中间项左移量
) {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    val len = soundItemDataList.size
    // 当前中心项在数据列表中的下标
    var centerItemIndex by remember { mutableStateOf(0) }

    // 手势状态
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    val direction = dragOffset.value.compareTo(0f)

    // 取出带两端 buffer 的可见项
    fun getVisibleItems(): List<SoundItemData> =
        List(visibleNums + 2) { i ->
            val idx = (centerItemIndex + i - visibleNums / 2 - 1 + len) % len
            soundItemDataList[idx]
        }

    LaunchedEffect(soundItemDataList) {
        if (soundItemDataList.isNotEmpty()) {
            onItemChosen(soundItemDataList.last())
        }
    }
    val visibleItems = getVisibleItems()

    // 计算 UI 上槽位数量与中心槽位索引
    val totalSlots = visibleNums + 2
    val centerSlot = if (visibleNums % 2 == 0) visibleNums / 2 else totalSlots / 2

    // 尺寸与透明度定义
    val bigSize = DpSize(342.pxToDp(), 456.pxToDp())
    val smallSize = DpSize(258.pxToDp(), 344.pxToDp())
    val slotDpSizes = List(totalSlots) { if (it == centerSlot) bigSize else smallSize }
    val slotAlphas = List(totalSlots) { i ->
        when (i) {
            centerSlot -> 1f
            centerSlot - 1, centerSlot + 1 -> 0.75f
            else -> 0.5f
        }
    }

    // 计算每个槽的基础 Bounds
    val boundsList = with(LocalDensity.current) {
        val spacing = 120f
        val centers = mutableListOf<Float>()
        var currentX = -slotDpSizes.first().width.toPx() / 2 - spacing
        for (i in 0 until totalSlots) {
            centers += currentX + slotDpSizes[i].width.toPx() / 2
            val half = slotDpSizes[i].width.toPx() / 2
            currentX += half + spacing + if (i + 1 < totalSlots) slotDpSizes[i + 1].width.toPx() / 2 else half
        }
        val shiftPx = centerShift.toPx()
        List(totalSlots) { i ->
            val baseX = centers[i] - slotDpSizes[i].width.toPx() / 2
            val x = if (i == centerSlot) baseX - shiftPx else baseX
            Bounds(x = x, dpSize = slotDpSizes[i], alpha = slotAlphas[i])
        }
    }

    // 根据拖动方向插值
    val lastIdx = totalSlots - 1
    val transitions = if (direction >= 0) {
        (0..lastIdx).map { i ->
            if (i == lastIdx) lerpSlotV(boundsList[i], boundsList[i], 1f)
            else lerpSlotV(boundsList[i], boundsList[i + 1], t)
        }
    } else {
        (lastIdx downTo 0).map { i ->
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
                        scope.launch { dragOffset.snapTo((dragOffset.value + delta).coerceIn(-threshold, threshold)) }
                    },
                    onDragEnd = {
                        scope.launch {
                            when {
                                dragOffset.value > threshold * 0.5f -> {
                                    dragOffset.animateTo(threshold, tween(200)); delay(100)
                                    centerItemIndex = (centerItemIndex - 1 + len) % len
                                }
                                dragOffset.value < -threshold * 0.5f -> {
                                    dragOffset.animateTo(-threshold, tween(200)); delay(100)
                                    centerItemIndex = (centerItemIndex + 1) % len
                                }
                            }
                            dragOffset.snapTo(0f)
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        transitions.forEachIndexed { slotIdx, state ->
            if (state.alpha > 0f) {
                val item = visibleItems[slotIdx]
                val painter = rememberAsyncImagePainter(
                    if (item.imgId != -1) File(item.imgPath) else "file:///android_asset/images/${item.imgPath}"
                )
                Box(
                    modifier = Modifier
                        .size(state.size)
                        .graphicsLayer { translationX = state.x; alpha = state.alpha }
                        .clickable {
                            onItemInvoke2Play(item)
                            if (slotIdx == centerSlot) {
                                onItemChosen(item)
                            } else {
                                // 非中心，移动到中心后不触发 onItemChosen
                                val steps = slotIdx - centerSlot
                                scope.launch {
                                    repeat(abs(steps)) {
                                        if (steps > 0) {
                                            dragOffset.animateTo(-threshold, tween(200)); delay(100)
                                            centerItemIndex = (centerItemIndex + 1) % len
                                        } else {
                                            dragOffset.animateTo(threshold, tween(200)); delay(100)
                                            centerItemIndex = (centerItemIndex - 1 + len) % len
                                        }
                                        dragOffset.snapTo(0f)
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
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
