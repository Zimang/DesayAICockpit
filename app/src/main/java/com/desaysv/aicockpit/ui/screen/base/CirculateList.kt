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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.rememberAsyncImagePainter
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.getSP
import com.desaysv.aicockpit.utils.pxToDp
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
                            dragOffset.snapTo((dragOffset.value + delta).coerceIn(-threshold, threshold))
                        }
                    },
                    onDragEnd = {
                        scope.launch {
//                            dragOffset.animateTo(0f, tween(300))
                            if (dragOffset.value > threshold * 0.5f) {
                                dragOffset.animateTo(threshold, tween(150))
                                startIndex = (startIndex - 1 + allImages.size) % allImages.size
                            } else if (dragOffset.value < -threshold * 0.5f) {
                                dragOffset.animateTo(-threshold, tween(150))
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




// 辅助数据类，描述一个槽位的目标状态
data class SlotParam(val x: Float, val scale: Float, val alpha: Float)

// 线性插值函数
fun lerpFloat(a: Float, b: Float, t: Float): Float = a + (b - a) * t



@Composable
fun InfiniteCircularLazyList_2(
    onItemSelected: (SoundItemData) -> Unit,
    soundItemDataList: List<SoundItemData>,
    usingLocalPath: Boolean = true
) {
    val len = soundItemDataList.size
    if (len < 5) return

    var startIndex by remember { mutableStateOf(0) }
    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val threshold = 342f

    // 图片大小 + alpha
    val baseSizes = listOf(
        DpSize(342.pxToDp(), 456.pxToDp()),
        DpSize(258.pxToDp(), 344.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp()),
        DpSize(168.pxToDp(), 224.pxToDp())
    )
    val baseAlphas = listOf(1f, 0.75f, 0.4f, 0.4f, 0.4f)
    val edgeSpacing = 120.pxToDp()

    // 槽位的绝对位置（防止重叠）
    val slotXOffsets = buildList {
        var accum = 0.dp
        for (size in baseSizes) {
            add(accum)
            accum += size.width + edgeSpacing
        }
    }

    // 构造静态槽位（位置 + 缩放 + alpha）
    val slotStates = with(LocalDensity.current){List(5) { i ->

        Slot(
            x = slotXOffsets[i].toPx(),
            scale = baseSizes[i].width / baseSizes[0].width,
            alpha = baseAlphas[i]
        )}
    }

    val t = (-dragOffset.value / threshold).coerceIn(0f, 1f)

    val visibleItems = List(5) { i ->
        val index = (startIndex + i - 1 + len) % len
        soundItemDataList[index]
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(456.pxToDp())
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, delta ->
                        scope.launch {
                            dragOffset.snapTo(dragOffset.value + delta)
                        }
                    },
                    onDragEnd = {
                        scope.launch {
                            if (abs(dragOffset.value) > threshold / 2) {
                                startIndex = (startIndex + if (dragOffset.value < 0) 1 else -1 + len) % len
                            }
                            dragOffset.animateTo(0f, tween(200))
                        }
                    }
                )
            },
        contentAlignment = Alignment.CenterStart
    ) {
        slotStates.forEach {
            Box(
                Modifier
                    .offset { IntOffset(it.x.roundToInt(), 350) }
                    .size(10.dp)
                    .background(Color.Red, CircleShape)
            )
        }

        visibleItems.forEachIndexed { i, item ->
            val direction = dragOffset.value.compareTo(0f)
            val from = slotStates[i]
            val to = when (direction) {
                -1 -> slotStates.getOrNull(i - 1) ?: from // 向左滑
                1 -> slotStates.getOrNull(i + 1) ?: from // 向右滑
                else -> from
            }
            val state = lerpSlot(from, to, t)

            val painter = if (usingLocalPath && item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            Box(
                Modifier
                    .absoluteOffset { IntOffset(state.x.roundToInt(), 100) }
                    .graphicsLayer {
                        scaleX = state.scale
                        scaleY = state.scale
                        alpha = state.alpha
                    }
                    .size(baseSizes[0])
                    .clickable { onItemSelected(item) },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Text(
                    text = item.soundName+" "+item.id,
                    style = androidx.compose.ui.text.TextStyle(
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

