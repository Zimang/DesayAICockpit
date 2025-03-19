package com.desaysv.aicockpit.ui.screen.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        println("🤖 [父] 接收到约束: min=${constraints.minWidth}, max=${constraints.maxWidth}")

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