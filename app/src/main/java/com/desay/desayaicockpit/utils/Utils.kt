package com.desay.desayaicockpit.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
fun Int.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}

@Composable
fun Float.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}
@Composable
fun Long.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toFloat().toDp() }
}