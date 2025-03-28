package com.desaysv.aicockpit.ui.screen.base.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface InfiniteCarousel<T> {
    val items: List<T>

    /**
     * 当前选中的索引（对应 items 列表）
     */
    val selectedIndex: Int

    /**
     * 触发滚动到指定 index（默认可选）
     */
    fun scrollTo(index: Int)

    /**
     * Composable 内容渲染函数（回调传 index 和 item）
     */
    @Composable
    fun Render(modifier: Modifier , itemContent: @Composable (item: T, index: Int, isSelected: Boolean) -> Unit)
}
