package com.desaysv.aicockpit.ui.screen.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LogLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 👇 打印父组件接收到的约束
        println("🤖 [父] 接收到约束: min w=${constraints.minWidth}, max=${constraints.maxWidth}")
        println("🤖 [父] 接收到约束: min h=${constraints.minHeight}, max=${constraints.maxHeight}")

        // 测量所有子项
        val placeables = measurables.map { measurable ->
            // 👇 传递给子项的约束（可根据需要修改）
            val childConstraints = constraints.copy(
                minWidth = 0,   // 允许子项宽度从0到父最大宽度
                minHeight = 0  // 允许子项高度从0到父最大高度
            )
            // 👇 测量子项并打印日志
            measurable.measure(childConstraints).also { placeable ->
                println("👉 [子] 测量结果: ${placeable.width}x${placeable.height}")
            }
        }

        // 👇 计算父最终尺寸（这里简单取最大宽度和高度）
        val ownWidth = placeables.maxOfOrNull { it.width } ?: constraints.minWidth
        val ownHeight = placeables.sumOf { it.height }

        // 👇 打印父最终尺寸
        println("🎯 [父] 最终尺寸: $ownWidth x $ownHeight\n")

        // 布局所有子项
        layout(ownWidth, ownHeight) {
            var y = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = y)
                y += placeable.height
            }
        }
    }
}

@Preview
@Composable
fun LayoutLogDemo() {
    LogLayout(
        modifier = Modifier
            .fillMaxWidth()  // 父约束：宽度充满屏幕，高度不受限
            .background(Color.LightGray)
    ) {
        // 子项1：固定尺寸
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Red)
        )

        // 子项2：自适应文本
        Text(
            text = "Hello Jetpack Compose",
            modifier = Modifier
                .background(Color.Green)
        )

        // 子项3：充满剩余宽度
        Box(
            modifier = Modifier
                .fillMaxWidth()  // 尝试充满父宽度
                .height(50.dp)
                .background(Color.Blue)
        )
    }
}

@Composable
fun CircleLayout(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,        // 圆的半径（可自定义）
    startAngle: Float = 0f,      // 起始角度（0度为顶部）
    clockwise: Boolean = true,  // 是否顺时针排列
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        // 👇 测量所有子项（不限制子项尺寸）
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        // 👇 计算布局总尺寸（足够容纳整个圆 + 子项）
        val maxChildWidth = placeables.maxOfOrNull { it.width } ?: 0
        val maxChildHeight = placeables.maxOfOrNull { it.height } ?: 0
        val totalSize = (radius.toPx() * 2 + maxOf(maxChildWidth, maxChildHeight)).toInt()

        // 👇 计算圆心坐标（相对布局左上角）
        val centerX = totalSize / 2f
        val centerY = totalSize / 2f

        // 👇 确定每个子项的角度间隔
        val angleIncrement = 360f / placeables.size
        var currentAngle = startAngle

        // 👇 布局所有子项
        layout(totalSize, totalSize) {
            placeables.forEachIndexed { index, placeable ->
                // 计算当前角度对应的弧度
                val radians = Math.toRadians(currentAngle.toDouble())
                // 计算子项中心坐标
                val x = centerX + radius.toPx() * cos(radians) - placeable.width / 2
                val y = centerY + radius.toPx() * sin(radians) - placeable.height / 2
                // 放置子项
                placeable.place(x.toInt(), y.toInt())
                // 更新角度（顺时针或逆时针）
                currentAngle += if (clockwise) angleIncrement else -angleIncrement
            }
        }
    }
}

@Preview
@Composable
fun CircleLayoutDemo() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircleLayout(
            radius = 150.dp,
            startAngle = 90f, // 从右侧开始
            modifier = Modifier.background(Color.LightGray)

        ) {
            val times=23
            val deltaHue=360f/(times+1)
            repeat(times) { index ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.hsl(index * deltaHue, 0.5f, 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$index", color = Color.White)
                }
            }
        }
    }
}