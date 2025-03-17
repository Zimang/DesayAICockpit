package com.desaysv.aicockpit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.roundToInt


@Composable
fun Int.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}
@Composable
fun Int.pxToSp(): TextUnit {
    val density = LocalDensity.current
    return with(density) { this@pxToSp.toSp() }
}

@Composable
fun Float.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}
@Composable
fun Int.pxToDpNum(): Int {
    val density = LocalDensity.current
    return with(density) { this@pxToDpNum.toDp().value }.roundToInt()
}

@Composable
fun Long.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toFloat().toDp() }
}

//val deltaFontWeight=0
//
//fun Int.getSP():Int{
//    return this- deltaFontWeight
//}
