package com.desaysv.aicockpit.ui.screen.base

import android.content.Context
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
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



data class Slot(val x: Float, val scale: Float, val alpha: Float)
data class Bounds(val x: Float, val size: DpSize, val alpha: Float)

data class SlotVisual(
    val x: Float,
    val size: DpSize,
    val alpha: Float
)

fun lerpSlotV(from: Bounds, to: Bounds, t: Float): Bounds {
    return Bounds(
        x = lerp(from.x, to.x, t),
        size = DpSize(
            lerp(from.size.width, to.size.width, t),
            lerp(from.size.height, to.size.height, t)
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
fun LogRecompose(tag: String) {
    // 每次该函数所在的组合阶段被执行，SideEffect 就会跑一次
    SideEffect {
        Log.d("Recompose", tag)
    }
}

@Composable
fun RememberPreloadedCoil(context: Context, items: List<SoundItemData>) {
    val imageLoader = context.imageLoader
    LaunchedEffect(items) {
        items.forEach { item ->
            val paths = listOf(
                "file:///android_asset/images/${item.imgPath}",
                // reflection variant:
                "file:///android_asset/images/${item.imgPath.replace(".png","_.png")}"
            )
            paths.forEach { path ->
                imageLoader.enqueue(
                    ImageRequest.Builder(context)
                        .data(path)
                        .size(Size.ORIGINAL)     // ensure full resolution
                        .build()
                )
            }
        }
    }
}

/**
 * 1. 每次数据变动的时候都提前预加载，加载之前数据不变
 */
@Composable
fun InfiniteCircularLazyList_5_lt(
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

    // 1️  每次重组打印
    SideEffect {
        Log.d("Recompose", "InfiniteList startIndex=$startIndex dragOffset=${dragOffset.value}")
    }

    LaunchedEffect(dragOffset) {
        snapshotFlow { dragOffset.value }
            .collect { offset ->
                Log.d("DragOffset", "value=$offset")
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
                size = sizes[i],
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
                            when {
                                dragOffset.value > threshold * 0.5f -> {
                                    dragOffset.animateTo(threshold, tween(200))
//                                    delay(100)
                                    startIndex = (startIndex - 1 + len) % len
                                }

                                dragOffset.value < -threshold * 0.5f -> {
                                    dragOffset.animateTo(-threshold, tween(200))
//                                    delay(100)
                                    startIndex = (startIndex + 1) % len
                                }
                            }
                            dragOffset.snapTo(0f)
                            onSoundInvoke2Play(soundItemDataList_[startIndex])
                            Log.d("end at $startIndex")
                        }
                    }
                )
            }
            .clip(RectangleShape),
        contentAlignment = Alignment.CenterStart
    ) {
        transitions.forEachIndexed { i, state ->
            Log.d("SlotState", "item#$i → x=${state.x}  alpha=${state.alpha}")
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
                        if (i == 1) {
                            onSoundChosen(soundItemDataList_[startIndex])
                        } else {
                            Log.d("$i 并非最左边")
                        }
                        scope.launch {
                            // 调用选中回调
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
                            onSoundInvoke2Play(soundItemDataList_[startIndex])
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
//
//@Composable
//fun rememberPainterCache(soundItemDataList: List<SoundItemData>): Map<String, AsyncImagePainter> {
//    val painterCache = remember { mutableStateMapOf<String, AsyncImagePainter>() }
//
//    soundItemDataList.forEach { item ->
//        val mainPath = "file:///android_asset/images/${item.imgPath}"
//        val refPath = mainPath.replace(".png", "_.png")
//
//        if (mainPath !in painterCache) {
//            painterCache[mainPath] = rememberAsyncImagePainter(mainPath)
//        }
//
//        if (refPath !in painterCache) {
//            painterCache[refPath] = rememberAsyncImagePainter(refPath)
//        }
//    }
//
//    return painterCache
//}

@Composable
fun rememberPainterCache(soundItemDataList: List<SoundItemData>): Map<String, Painter> {
    return soundItemDataList.flatMap { item ->
        listOf(
            "file:///android_asset/images/${item.imgPath}",
            "file:///android_asset/images/${item.imgPath}".replace(".png", "_.png")
        )
    }.distinct().associateWith { path ->
        rememberAsyncImagePainter(path)
    }
}


@Composable
fun InfiniteCircularLazyList_5_lt_logged(
    onSoundInvoke2Play: (SoundItemData) -> Unit,
    onSoundChosen: (SoundItemData) -> Unit = {},
    soundItemDataList_: List<SoundItemData>,
    visibleNums: Int = 4,
    chosenUI: @Composable (Int) -> Unit,
) {
    val painterCache = rememberPainterCache(soundItemDataList_)
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    var startIndex by remember { mutableStateOf(0) }


    val len = soundItemDataList_.size
    val getVisibleItems = { List(visibleNums + 2) { i ->
        soundItemDataList_[(startIndex + i - 1 + len) % len]
    } }

    Box(
        Modifier
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
                            Log.d("DragEnd", "BEFORE  startIndex=$startIndex  offset=${dragOffset.value}")
                            when {
                                dragOffset.value > threshold * 0.5f -> {
                                    dragOffset.animateTo(threshold, tween(200))
                                    startIndex = (startIndex - 1 + len) % len
                                }
                                dragOffset.value < -threshold * 0.5f -> {
                                    dragOffset.animateTo(-threshold, tween(200))
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
        val visibleItems = getVisibleItems()

        val baseSizes = getBaseSize()
        val sizes = getLoopedSizes(baseSizes, visibleNums)
        val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
        val direction = dragOffset.value.compareTo(0f)
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
                    size = sizes[i],
                    alpha = when (i) {
                        0, sizes.size - 1 -> 0f
                        1 -> 1f
                        2 -> 0.75f
                        else -> 0.4f
                    }
                )
            }
        }
        val transitions = if (direction >= 0) {
            sizes.indices.map { i -> if (i==sizes.lastIndex) boundsList[i] else lerpSlotV(boundsList[i], boundsList[i+1], t) }
        } else {
            (sizes.lastIndex downTo 0).map { i -> if (i==0) boundsList[i] else lerpSlotV(boundsList[i], boundsList[i-1], -t) }.reversed()
        }

        transitions.forEachIndexed { i, state ->
            Log.d("SlotState", "item#$i  x=${"%.1f".format(state.x)}  alpha=${"%.2f".format(state.alpha)}")
            val item = visibleItems[i]
            val imageHeightPx = with(LocalDensity.current) { state.size.height.toPx() }

            // painter 用法同原来
            val pathMain = "file:///android_asset/images/${item.imgPath}"
            val pathRef  = pathMain.replace(".png","_.png")
//            val painter   = rememberAsyncImagePainter(pathMain)
//            val painter_  = rememberAsyncImagePainter(pathRef)
            val painter = painterCache[pathMain]!!
            val painter_ = painterCache[pathRef]!!

            Box(
                Modifier
                    .size(state.size)
                    .graphicsLayer { translationX = state.x }
                    .clickable {
                        if (i == 1) {
                            onSoundChosen(soundItemDataList_[startIndex])
                        } else {
                            Log.d("$i 并非最左边")
                        }
                        scope.launch {
                            // 调用选中回调
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
                            onSoundInvoke2Play(soundItemDataList_[startIndex])
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Image(painter = painter, contentDescription = null,
                    modifier = Modifier.fillMaxSize().graphicsLayer { alpha = state.alpha },
                    contentScale = ContentScale.Crop)
                if( state.alpha!=0f){
                    chosenUI(item.id)
                }
                Image(painter = painter_, contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if ( state.alpha==0f) 0f else 1f)
                        .graphicsLayer { translationY = imageHeightPx * 1.05f },
                    contentScale = ContentScale.Crop)
                Text(item.soundName,
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp, 0.dp, 0.dp, 26.dp)
                        .alpha(if ( state.alpha==0f) 0f else 1f),
                    fontSize = 24.sp,
                    color = Color.White
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

private val spacing = 120f

private val fixedBoundsList = listOf(
    Bounds(x = -342f - spacing - 258f / 2f,  size = DpSize(168.dp, 168.dp), alpha = 0f),
    Bounds(x = -342f - spacing / 2f,         size = DpSize(258.dp, 258.dp), alpha = 1f),
    Bounds(x = -342f / 2f,                   size = DpSize(342.dp, 342.dp), alpha = 0.75f),
    Bounds(x = 342f + spacing / 2f,          size = DpSize(258.dp, 258.dp), alpha = 0.4f),
    Bounds(x = 342f + spacing + 258f,        size = DpSize(168.dp, 168.dp), alpha = 0f),
    Bounds(x = 0f,                           size = DpSize(0.dp, 0.dp),     alpha = 0f), // 第6项备用位
)

@Composable
fun InfiniteCircularLazyList_5(
    onSoundInvoke2Play: (SoundItemData) -> Unit,
    onSoundChosen: (SoundItemData) -> Unit = {},
    soundItemDataList_: List<SoundItemData>,
    chosenUI: @Composable (Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val dragOffset = remember { Animatable(0f) }
    val threshold = 300f
    var startIndex by remember { mutableStateOf(0) }
    val len = soundItemDataList_.size

    val t = (dragOffset.value / threshold).coerceIn(-1f, 1f)
    val direction = dragOffset.value.compareTo(0f)

    val visibleItems by remember(startIndex, soundItemDataList_) {
        mutableStateOf(List(6) { i ->
            val index = (startIndex + i - 1 + len) % len
            soundItemDataList_[index]
        })
    }

    LaunchedEffect(soundItemDataList_) {
        if (soundItemDataList_.isNotEmpty()) {
            onSoundChosen(soundItemDataList_[startIndex])
        }
    }

    val transitions = if (direction >= 0) {
        fixedBoundsList.indices.map { i ->
            if (i == fixedBoundsList.lastIndex) fixedBoundsList[i]
            else lerpSlotV(fixedBoundsList[i], fixedBoundsList[i + 1], t)
        }
    } else {
        (fixedBoundsList.lastIndex downTo 0).map { i ->
            if (i == 0) fixedBoundsList[i]
            else lerpSlotV(fixedBoundsList[i], fixedBoundsList[i - 1], -t)
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

            val painter = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath}")
            }

            val painter_ = if (item.imgId != -1) {
                rememberAsyncImagePainter(File(item.imgPath + "_"))
            } else {
                rememberAsyncImagePainter("file:///android_asset/images/${item.imgPath.replace(".png", "_.png")}")
            }

            Box(
                modifier = Modifier
                    .size(state.size)
                    .graphicsLayer {
                        translationX = state.x
                    }
                    .clickable {
                        if (i == 1) {
                            onSoundChosen(soundItemDataList_[startIndex])
                        } else {
                            val steps = i - 1
                            scope.launch {
                                repeat(abs(steps)) {
                                    val forward = steps > 0
                                    dragOffset.animateTo(
                                        if (forward) -threshold else threshold,
                                        tween(300)
                                    )
                                    startIndex = (startIndex + if (forward) 1 else -1 + len) % len
                                    dragOffset.snapTo(0f)
                                }
                                onSoundInvoke2Play(soundItemDataList_[startIndex])
                            }
                        }
                    },
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { alpha = state.alpha },
                    contentScale = ContentScale.Crop
                )
                chosenUI(item.id)
                Image(
                    painter = painter_,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationY = state.size.height.value * 1.05f
                        }
                        .pointerInput(Unit) {}, // 禁止事件穿透
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
