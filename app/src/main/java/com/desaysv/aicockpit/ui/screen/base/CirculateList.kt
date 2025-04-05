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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.withContext
import java.io.File
import java.time.format.TextStyle
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

    val dpSizes = getBaseSize()
    val boundsList = listOf(
        Bounds(200f, dpSizes[3], 0f),
        Bounds(700f, dpSizes[0], 1f),
        Bounds(1200f, dpSizes[1], 1f),
        Bounds(1700f, dpSizes[2], 1f),
        Bounds(2200f, dpSizes[3], 1f),
        Bounds(2700f, dpSizes[0], 0f)
    )

    val imageUrls = listOf(
        "https://picsum.photos/id/1001/300/300",
        "https://picsum.photos/id/1002/300/300",
        "https://picsum.photos/id/1003/300/300",
        "https://picsum.photos/id/1004/300/300",
        "https://picsum.photos/id/1005/300/300",
        "https://picsum.photos/id/1006/300/300"
    )

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
                            dragOffset.animateTo(0f, tween(300))
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
                    painter = rememberAsyncImagePainter(imageUrls[i]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Preview
@Composable
fun TwoSlotTwoImageDemo() {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    val t = (dragOffset.value / threshold).coerceIn(0f, 1f)

    // 槽点位置
    val slot0 = Slot(x = 600f, scale = 1f, alpha = 1f)
    val slot1 = Slot(x = 800f, scale = 0.4f, alpha = 0.4f)

    val dpSizes= getBaseSize()

    val p1Bounds=Bounds(600f,dpSizes[3],1f)
    val p2Bounds=Bounds(800f,dpSizes[0],1f)
    val p3Bounds=Bounds(1000f,dpSizes[1],1f)
    val p4Bounds=Bounds(1200f,dpSizes[2],1f)
    val p5Bounds=Bounds(1400f,dpSizes[3],1f)
    val p6Bounds=Bounds(1600f,dpSizes[0],1f)

    val directionP2P1= lerpSlot(p2Bounds,p1Bounds,t)
    val directionP1P2= lerpSlot(p1Bounds,p2Bounds,t)
    val directionP3P2= lerpSlot(p3Bounds,p2Bounds,t)
    val directionP2P3= lerpSlot(p2Bounds,p3Bounds,t)
    val directionP4P3= lerpSlot(p4Bounds,p3Bounds,t)
    val directionP3P4= lerpSlot(p3Bounds,p4Bounds,t)
    val directionP5P4= lerpSlot(p5Bounds,p4Bounds,t)
    val directionP4P5= lerpSlot(p4Bounds,p5Bounds,t)
    val directionP6P5= lerpSlot(p6Bounds,p5Bounds,t)
    val directionP5P6= lerpSlot(p5Bounds,p6Bounds,t)

    val stateA = lerpSlot(slot0, slot1, t) // A 从 0 -> 1
    val stateB = lerpSlot(slot1, slot0, t) // B 从 1 -> 0（反向）

    LaunchedEffect(dragOffset.value) {
        println("A: x=${stateA.x}, scale=${stateA.scale}, alpha=${stateA.alpha}")
        println("B: x=${stateB.x}, scale=${stateB.scale}, alpha=${stateB.alpha}")
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
                            dragOffset.animateTo(0f, tween(300))
                        }
                    }
                )
            }
    ) {

        // 图片 A（从槽0滑向槽1）
        Box(
            Modifier
                .absoluteOffset { IntOffset(stateA.x.roundToInt(), 300) }
                .size(200.dp)
                .graphicsLayer {
                    scaleX = stateA.scale
                    scaleY = stateA.scale
                    alpha = stateA.alpha
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/id/1015/300/300"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 图片 B（从槽1滑向槽0）
        Box(
            Modifier
                .absoluteOffset { IntOffset(stateB.x.roundToInt(), 300) }
                .size(200.dp)
                .graphicsLayer {
                    scaleX = stateB.scale
                    scaleY = stateB.scale
                    alpha = stateB.alpha
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/id/1025/300/300"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        // 槽点红圈
        listOf(slot0, slot1).forEach {
            Box(
                Modifier
                    .offset { IntOffset(it.x.roundToInt(), 400) }
                    .size(10.dp)
                    .background(Color.Red, CircleShape)
            )
        }
    }
}




@Preview
@Composable
fun OneSlotOneImageDemo() {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)


    // 槽点位置（固定在屏幕中间偏左）
    val anchorX = 600f
    val anchorY = 200f

    val dpSizes= getBaseSize()
    val p1Bounds=Bounds(anchorX,dpSizes[3],1f)
    val p2Bounds=Bounds(anchorX,dpSizes[0],1f)
    val p3Bounds=Bounds(anchorX,dpSizes[1],1f)
    val p4Bounds=Bounds(anchorX,dpSizes[2],1f)
    val p5Bounds=Bounds(anchorX,dpSizes[3],1f)
    val p6Bounds=Bounds(anchorX,dpSizes[0],1f)

    // 初始状态（图片偏移 0，scale=1f）
    val start = Slot(x = anchorX, scale = 1f, alpha = 1f)
    // 目标状态（图片偏移 +100，缩小，变淡）
    val end = Slot(x = anchorX + 150f, scale = 0.5f, alpha = 0.3f)

    // 插值结果
    val state = lerpSlot(start, end, t)
    LaunchedEffect(dragOffset.value) {
        println("State: x=${state.x}, scale=${state.scale}, alpha=${state.alpha}")
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
                            dragOffset.animateTo(0f, tween(300))
                        }
                    }
                )
            }
    ) {
        // 图片：应用插值状态
        Box(
            Modifier
                .absoluteOffset { IntOffset(anchorX.roundToInt(), anchorY.roundToInt()) }
                .size(200.dp)
                .background(Color.Gray)
        )
        // 锚点：红圈
        Box(
            Modifier
                .offset { IntOffset(anchorX.roundToInt(), anchorY.roundToInt()) }
                .size(10.dp)
                .background(Color.Red, CircleShape)
        )

        // 图片：应用插值状态
        Box(
            Modifier
                .absoluteOffset { IntOffset(state.x.roundToInt(), anchorY.roundToInt()) }
                .size(200.dp)
                .graphicsLayer {
                    scaleX = state.scale
                    scaleY = state.scale
                    alpha = state.alpha
                }
        ) {
            Image(
                painter = rememberAsyncImagePainter("https://picsum.photos/300/300"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
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
fun lerpSlot(from: Bounds, to: Bounds, t: Float): Bounds {
    Bounds(
        x = lerp(from.x,to.x,t),
        dpSize = DpSize(
            lerp(from.dpSize.width,to.dpSize.width,t),
            lerp(from.dpSize.height,to.dpSize.height,t)),
        alpha = TODO()
    )
}

fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t
fun lerp(a: Dp, b: Dp, t: Float): Dp = a + (b - a) * t




// 辅助数据类，描述一个槽位的目标状态
data class SlotParam(val x: Float, val scale: Float, val alpha: Float)

// 线性插值函数
fun lerpFloat(a: Float, b: Float, t: Float): Float = a + (b - a) * t
fun lerpSlotParam(from: SlotParam, to: SlotParam, t: Float): SlotParam {
    return SlotParam(
        x = lerpFloat(from.x, to.x, t),
        scale = lerpFloat(from.scale, to.scale, t),
        alpha = lerpFloat(from.alpha, to.alpha, t)
    )
}



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

