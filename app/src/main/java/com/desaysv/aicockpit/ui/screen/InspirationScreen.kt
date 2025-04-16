package com.desaysv.aicockpit.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import coil.compose.rememberAsyncImagePainter
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.business.navigate.informingIPC
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.ui.screen.base.CustomToastHost
import com.desaysv.aicockpit.utils.Log
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.pxToDp
import com.desaysv.aicockpit.viewmodel.MajorViewModel
import kotlinx.coroutines.launch
import java.io.File

/**
 * 灵感屏幕
 */

@Composable
fun ThemeCard(themeItemData: ThemeItemData,
              isApplied:Boolean,
              onApply:(Int)->Unit={},
              onDelete:(ThemeItemData)->Unit={},
              onClicked:()->Unit={}
              ){

    val t=if (isApplied) ResourceManager.getApplying() else ResourceManager.getApply()
    val c=if (isApplied) colorResource(R.color.choosen) else colorResource(R.color.n_choosen)
    val img=if (isApplied) painterResource(R.drawable.app_1) else painterResource(R.drawable.app_2)
    val painter = if (themeItemData.imgPath != null ) {
        rememberAsyncImagePainter(File(themeItemData.imgPath))
    } else {
        painterResource(R.drawable.el_1)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16f.pxToDp())) {

        Box(modifier = Modifier.clickable {
            onClicked()
        }){
            Image(painter,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(
                    472f.pxToDp()
                ))
            Text(text = themeItemData.themeName, style = TextStyle(
                color = c, fontSize = 32.getSP()
            ), modifier = Modifier.align(Alignment.BottomStart)
                .padding(start = 40.06f.pxToDp(), bottom =40.32f.pxToDp() ))
        }

        if (themeItemData.isDefault){
            Box(modifier = Modifier.align(Alignment.CenterHorizontally).clickable {
                if(!isApplied){
                    onApply(themeItemData.id)
                }
            }){
                Image(painterResource(R.drawable.r_1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(
                        472f.pxToDp(),72f.pxToDp()
                    ))
                Text(text = t!!, style = TextStyle(
                    fontSize = 24.getSP(),
                    color = c
                ), modifier = Modifier.align(Alignment.Center))
            }
        }else{
            Row (horizontalArrangement = Arrangement.spacedBy(16f.pxToDp())){

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                ).clickable {
                    if (!isApplied){
                        onApply(themeItemData.id)
                    }
                }){
                    Image(img,contentDescription = null,
                        modifier = Modifier.size(
                            228.pxToDp(),72f.pxToDp()
                        ))
                    Text(text = t!!, style = TextStyle(
                        color = c, fontSize = 24.getSP()
                    ), modifier = Modifier.align(Alignment.Center))
                }

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                ).clickable {
                    onDelete(themeItemData)
                }){
                    Image(painterResource(R.drawable.app_2),contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                    Text(
                        text = ResourceManager.getDeleted()!!,
                        style = TextStyle(
                        color = colorResource(R.color.n_choosen),
                        fontSize = 24.getSP(),
                    ), modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 468,
    heightDp = 262
)
@Composable
fun ThemeCards_(){
    LazyRow(horizontalArrangement = Arrangement.spacedBy(64f.pxToDp())) {
    }
}

/**
 * 灵感屏幕
 */
@Composable
fun InspiratonScreen(onChange: (ScreenTag) -> Unit={},
                     viewModel: MajorViewModel,
                     toastMsg:String?=null,
                     onApplyUIChange: () -> Unit={},
){
    val contextApp=(LocalContext.current as? Activity)
    val context=LocalContext.current
    val scope = rememberCoroutineScope()

    val themes by viewModel.themes.collectAsState(initial = emptyList())

    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons(ScreenTag.INS, onChange =onChange, onExit = {
                scope.launch {
                    contextApp?.finish()
                }
            })
        }

        Box(modifier = Modifier
            .size(width = (207+1286+143+284+1636).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 200f.pxToDp())){
                LazyRow(horizontalArrangement = Arrangement.spacedBy(64f.pxToDp())) {
                    items(themes){ theme->
                        ThemeCard(
                            theme,theme.isApplied,
                            onApply = {scope.launch {
                                Log.d("apply screen")
                                viewModel.switchAppliedTheme(it)

                                informingIPC(context,theme.hue, theme.saturation,theme.themeName,viewModel.getEleByPath(theme.imgPath))
//                                Toast.makeText(context,ResourceManager.getAppliedSuccessfully(),
//                                    Toast.LENGTH_LONG).show()
                                onApplyUIChange()

                            }},
                            onDelete = {
                                scope.launch {
                                    viewModel.deleteTheme(it)
                                }
                            }
                        )
                    }
                }
            }
            CustomToastHost(toastMsg)
        }
    }
}

@Composable
fun InspiratonScreenV2(
    onChange: (ScreenTag) -> Unit = {},
    viewModel: MajorViewModel,
    toastMsg: String? = null,
    onApplyUIChange: () -> Unit = {},
) {
    val contextApp = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val themes by viewModel.themes.collectAsState(initial = emptyList())
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧部分（按钮区域）
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())) {
            ThemeChangeButtons(
                ScreenTag.INS,
                onChange = onChange,
                onExit = {
                    scope.launch { contextApp?.finish() }
                }
            )
        }

        // 右侧区域：主题卡列表
        Box(
            modifier = Modifier.size(
                width = (207 + 1286 + 143 + 284 + 1636).pxToDp(),
                height = 720f.pxToDp()
            )
        ) {
            // 记录容器的宽度（单位：像素），以便后续计算总内容宽度
            var containerWidthPx by remember { mutableStateOf(0) }
            val density = LocalDensity.current
            val containerWidthDp = with(density) { containerWidthPx.toDp() }

            // 假设每个 ThemeCard 的宽度为 300 像素换算成 dp（请根据实际情况调整）
            val itemWidth = 300.pxToDp()
            val spacing = 64f.pxToDp()

            // 计算内容项总宽度（不含尾部填充项）
            val totalContentWidth = if (themes.isNotEmpty()) {
                themes.size * itemWidth + (themes.size - 1) * spacing
            } else 0.dp

            // 如果内容宽度不足容器宽度，则尾部添加一个 Spacer 以扩展滚动区域
            val trailingSpacerWidth = if (totalContentWidth < containerWidthDp) {
                containerWidthDp - totalContentWidth
            } else 0.dp

            // 设置 LazyRow 状态
            val listState = rememberLazyListState()

            // 当第一个可见项正好为 themes 列表的最后一项时，说明已“滑到底”
            val isAtEnd by remember {
                derivedStateOf {
                    listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index == themes.lastIndex
                }
            }

            // 拦截向左拖拽与 fling：当 isAtEnd 且当前第一项 offset <= 0 时，消费所有负方向滚动
            val nestedScrollConnection = remember(isAtEnd, listState) {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        if (isAtEnd && available.x < 0f) {
                            val firstVisible = listState.layoutInfo.visibleItemsInfo.firstOrNull()
                            if (firstVisible != null && firstVisible.offset <= 0) {
                                return available
                            }
                        }
                        return Offset.Zero
                    }
                    override suspend fun onPreFling(available: Velocity): Velocity {
                        if (isAtEnd && available.x < 0f) {
                            return available.copy(x = 0f)
                        }
                        return Velocity.Zero
                    }
                    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                        if (isAtEnd && available.x < 0f) {
                            return available.copy(x = 0f)
                        }
                        return Velocity.Zero
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 200f.pxToDp())
                    // 测量容器宽度（这里 Row 的 onSizeChanged 获取到实际宽度）
                    .onSizeChanged { containerWidthPx = it.width }
            ) {
                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    // 使用 nestedScroll 修饰符附加拦截逻辑
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(nestedScrollConnection)
                ) {
                    itemsIndexed(themes) { index, theme ->
                        // 点击时动画滚动，将该项平滑滚动至左侧（offset = 0）
                        ThemeCard(
                            theme,
                            isApplied = theme.isApplied,
                            onApply = {
                                scope.launch {
                                    viewModel.switchAppliedTheme(it)
                                    informingIPC(
                                        context,
                                        theme.hue,
                                        theme.saturation,
                                        theme.themeName,
                                        viewModel.getEleByPath(theme.imgPath)
                                    )
                                    onApplyUIChange()
                                }
                            },
                            onDelete = {
                                scope.launch { viewModel.deleteTheme(it) }
                            },
                            onClicked = {
                                scope.launch {
                                    listState.animateScrollToItem(index, 0)
                                }
                            }
                        )
                    }
                    // 如果需要扩展滚动区域，则在列表末尾添加一个 Spacer
                    if (trailingSpacerWidth > 0.dp) {
                        item {
                            Spacer(modifier = Modifier.width(trailingSpacerWidth))
                        }
                    }
                }
            }
            CustomToastHost(toastMsg)
        }
    }
}

@Composable
fun InspiratonScreenV3(
    onChange: (ScreenTag) -> Unit = {},
    viewModel: MajorViewModel,
    toastMsg: String? = null,
    onApplyUIChange: () -> Unit = {},
) {
    val contextApp = (LocalContext.current as? Activity)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val themes by viewModel.themes.collectAsState(initial = emptyList())
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧部分：按钮区域保持不变
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())) {
            ThemeChangeButtons(
                ScreenTag.INS,
                onChange = onChange,
                onExit = { scope.launch { contextApp?.finish() } }
            )
        }

        // 右侧区域：无限循环的主题卡列表
        Box(
            modifier = Modifier.size(
                width = (207 + 1286 + 143 + 284 + 1636).pxToDp(),
                height = 720f.pxToDp()
            )
        ) {
            // 当 themes 不为空时启用无限循环列表
            if (themes.isNotEmpty()) {
                val listSize = themes.size
                val infiniteItemCount = Int.MAX_VALUE
                val initialIndex = (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2) % listSize)
                val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

                // 定义每项宽度和项间间隔
                val itemWidth = 300.pxToDp()
                val spacing = 64f.pxToDp()

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 200f.pxToDp())
                ) {



                    var wwith=(472*listSize+64*(listSize-1)).pxToDp()
                    if(themes.size==1){

                        ThemeCard(
                            themes[0],
                            isApplied = themes[0].isApplied,
                            onApply = {
                                scope.launch {
                                    viewModel.switchAppliedTheme(it)
                                    informingIPC(
                                        context,
                                        themes[0].hue,
                                        themes[0].saturation,
                                        themes[0].themeName,
                                        viewModel.getEleByPath(themes[0].imgPath)
                                    )
                                    onApplyUIChange()
                                }
                            }
                        )

                        return
                    }


                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        modifier = Modifier.width(wwith)
                    ) {
                        // 利用无限项列表，并通过取模获得实际项的数据
                        items(infiniteItemCount) { index ->
                            val realIndex = index % listSize
                            val theme = themes[realIndex]
                            ThemeCard(
                                theme,
                                isApplied = theme.isApplied,
                                onApply = {
                                    scope.launch {
                                        viewModel.switchAppliedTheme(it)
                                        informingIPC(
                                            context,
                                            theme.hue,
                                            theme.saturation,
                                            theme.themeName,
                                            viewModel.getEleByPath(theme.imgPath)
                                        )
                                        onApplyUIChange()
                                    }
                                },
                                onDelete = {
                                    scope.launch { viewModel.deleteTheme(it) }
                                },
                                onClicked = {
                                    // 动画滚动，将当前点击项滚动至容器最左侧
                                    scope.launch {
                                        listState.animateScrollToItem(index, 0)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            CustomToastHost(toastMsg)
        }
    }
}

