package com.desaysv.aicockpit.ui.screen.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.desaysv.aicockpit.ui.screen.base.list.InfiniteCarousel

@Composable
fun <T> InfiniteCarouselView(
    carousel: InfiniteCarousel<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (item: T, index: Int, isSelected: Boolean) -> Unit
) {
    carousel.Render(modifier = modifier, itemContent = itemContent)
}


class LazyRowLoopCarousel<T>(
    override val items: List<T>
) : InfiniteCarousel<T> {

    private var _selectedIndex by mutableStateOf(items.size) // 初始居中位置
    override val selectedIndex: Int
        get() = _selectedIndex % items.size

    private val loopedItems = items + items + items
    private val centerIndex = items.size

//    private val listState = rememberLazyListState(initialFirstVisibleItemIndex = centerIndex)

    override fun scrollTo(index: Int) {
        _selectedIndex = centerIndex + index
    }

    @Composable
    override fun Render(modifier: Modifier, itemContent: @Composable (T, Int, Boolean) -> Unit) {
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = centerIndex)

        LaunchedEffect(listState.firstVisibleItemIndex) {
            val idx = listState.firstVisibleItemIndex
            if (idx < items.size || idx >= items.size * 2) {
                listState.scrollToItem(centerIndex + selectedIndex)
            } else {
                _selectedIndex = idx
            }
        }

        LazyRow(
            state = listState,
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            itemsIndexed(loopedItems) { index, item ->
                val realIndex = index % items.size
                val isSelected = realIndex == selectedIndex
                itemContent(item, realIndex, isSelected)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InfiniteCarouselPreview() {
    val sampleImages = listOf(
        "https://picsum.photos/300/200?1",
        "https://picsum.photos/300/200?2",
        "https://picsum.photos/300/200?3"
    )

    val carousel = remember { LazyRowLoopCarousel(sampleImages) }

    carousel.Render(Modifier.width(1000.dp)) { item, _, isSelected ->
        val scale = if (isSelected) 1.2f else 1f
        Image(
            painter = rememberAsyncImagePainter(item),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size((150 * scale).dp, (100 * scale).dp)
        )
    }
}

