package com.desaysv.aicockpit.ui.screen

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModelProvider
import com.desaysv.aicockpit.MyApplication
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.ui.screen.base.InfiniteScalingImageList_SoundV1
import com.desaysv.aicockpit.ui.screen.base.PicWithText
import com.desaysv.aicockpit.ui.theme.Choosen
import com.desaysv.aicockpit.utils.LocaleManager
import com.desaysv.aicockpit.utils.ResourceManager
import com.desaysv.aicockpit.utils.pxToDp
import com.desaysv.aicockpit.utils.pxToDpNum
import com.desaysv.aicockpit.utils.pxToSp
import com.desaysv.aicockpit.viewmodel.SoundViewModel
import com.desaysv.aicockpit.viewmodel.SoundViewModelFactory
import kotlinx.coroutines.launch


/**
 *  AI座舱屏幕
 *  声光电选择按钮
 *
 */

val mDeltaFontWeight=0

@Composable
fun Int.getSP():TextUnit{
    return this.pxToSp()
}

enum class SoundLightElectricityTag{
    SOUND,LIGHT,ELECTRICITY
}
enum class ScreenTag{
    INS,CUS,SAVE
}


val screenTags= listOf("灵感","个性化座舱","保存")
val tElectricityPics= listOf("el_1.png","el_2.png","el_2.png")
val tElectricityName= listOf("默认主题","梦幻XXX","梦幻XXX")
val tElectricityImgId= listOf(R.drawable.el_1,R.drawable.el_2,R.drawable.el_3)
val electricityItemDataList = List(tElectricityPics.size) { index ->
    ElectricityItemData(
        imageName = tElectricityPics[index],
        themeName = tElectricityName[index],
        imgId = tElectricityImgId[index]
    )
}
val tSoundPics= listOf("b_1_h.png","b_2_h.png","b_3_h.png","b_4_h.png")
val tSoundName= listOf("布谷鸟","梦幻XXX","梦幻XXX","梦幻XXX")
val tSoundImgId= listOf(R.drawable.b_1_h,R.drawable.b_2_h,R.drawable.b_3_h,R.drawable.b_4_h)
val soundItemDataList = List(tSoundPics.size) { index ->
    SoundItemData(
        imageName = tSoundPics[index],
        soundName = tSoundName[index],
        imgId = tSoundImgId[index],
        imgPath = "no_path"
    )
}
/**
 * Sound Light Electricity Selection Button
 */
@Composable
fun SoundLightElectricitySelectionButtonV1(
    tag:SoundLightElectricityTag,
    chosen:Boolean,
    onClick:()->Unit
){

    val tags = ResourceManager.getTags()
    Box(modifier=Modifier.size(height = 120.pxToDp(), width = 284.pxToDp())
            .background(Color.Transparent)
        .clickable{onClick()},
        contentAlignment = Alignment.CenterEnd) {

        if (chosen) {
            Image(
                contentDescription = "",
                painter = painterResource(R.drawable.choosen),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize())
        }

        Text(
            text = tags[tag.ordinal]!!,
            style = TextStyle(
                fontSize = 32.getSP(),
            ),
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier = Modifier
                .padding(
                    end = 40.pxToDp()
                )

        )
    }
}


/**
 * Sound Light Electricity Selection Buttons
 */
@Composable
fun SoundLightElectricitySelectionButtonsV1(
    chosenTag: SoundLightElectricityTag,
    onTagChosen: (SoundLightElectricityTag)->Unit
){

    Column(modifier = Modifier.padding(top = 120f.pxToDp())
    ) {
        SoundLightElectricitySelectionButtonV1(
            SoundLightElectricityTag.SOUND,
            SoundLightElectricityTag.SOUND==chosenTag) {
                onTagChosen(SoundLightElectricityTag.SOUND)
            }
        SoundLightElectricitySelectionButtonV1(
            SoundLightElectricityTag.LIGHT,
            SoundLightElectricityTag.LIGHT==chosenTag) {
                onTagChosen(SoundLightElectricityTag.LIGHT)
            }
        SoundLightElectricitySelectionButtonV1(
            SoundLightElectricityTag.ELECTRICITY,
            SoundLightElectricityTag.ELECTRICITY==chosenTag) {
                onTagChosen(SoundLightElectricityTag.ELECTRICITY)
            }
    }
}

/**
 * ElectricityItem
 */
@Composable
fun ElectricityItem(
    @DrawableRes imgPath:Int,
    themeName:String,
    chosen: Boolean,
    modifier: Modifier
){
    Box(modifier
        .size(472.pxToDp())
    ){
        Image(painter = painterResource(imgPath),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  modifier
        )

        Text(
            text = themeName,
            style=TextStyle(
                fontSize = 32.getSP()
            ),
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier=modifier.padding(
                start = 40.pxToDp(),
                top = 400.15f.pxToDp()
            )
        )
    }
}

/**
 * ElectricityList
 */
@Composable
fun ElectricityList(
    onThemeChosen:(Int)->Unit,
    electricityItemDataList: List<ElectricityItemData>,
    chosenElectricityData: ElectricityItemData,
    modifier: Modifier
){
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(64.pxToDp()),
        modifier = modifier
    ) {
        itemsIndexed(electricityItemDataList){index,electItem->
            ElectricityItem(electItem.imgId
                ,electItem.themeName
                ,electItem.imgId==chosenElectricityData.imgId
                ,modifier=modifier.clickable {
                    onThemeChosen(index)
                }
            )
        }
    }
}

/**
 * ElectricityList
 */
@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 595,
    heightDp = 262
)
@Composable
fun ElectricityList_(){
    Box(Modifier
        .fillMaxSize()
        .padding(start = 120.pxToDp(), top = 120.pxToDp())
//        ,contentAlignment = Alignment.TopStart
    ){
        ElectricityList({},
            electricityItemDataList,
            electricityItemDataList[0],
            Modifier)
    }
    Text(1236.pxToDpNum().toString(), color = Color.White) //468

}

/**
 * todo 需要接入数据库数据而非自定义数据
 */
@Composable
fun SoundList_(viewModel: SoundViewModel) {

    val context = LocalContext.current
    val isLoading = viewModel.isLoading
    val soundItems = viewModel.soundItems

    LaunchedEffect(Unit) {
        viewModel.startLoadingData(context)
        // 注册被动广播接收器，等待外部广播传递新路径
        viewModel.registerPassiveBroadcast(context)
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 120.pxToDp(), top = 120.pxToDp())
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            InfiniteScalingImageList_SoundV1(
                onThemeChosen = {},
                soundItemDataList = soundItems
            )
        }
    }
}


@Composable
fun FullHueVerticalSlider(
    modifier: Modifier = Modifier,
    onHueChanged: (Float) -> Unit
) {
    val trackHeight = 480f.pxToDp()
    val allHeight = 500.pxToDp()
    var progress by remember { mutableFloatStateOf(0f) }

    // 计算色相（0°-360°）
    val hue = (progress * 360f).coerceIn(0f, 360f)

    // HSV 转颜色（饱和度=1，明度=1）
    val currentColor = Color.hsv(hue, 1f, 1f)

    // 实时回调色相变化
    LaunchedEffect(hue) {
        onHueChanged(hue)
    }
    Box(
        modifier = modifier
            .height(allHeight)
            .width(80.pxToDp())
            .padding(2f.pxToDp())
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    val y = change.position.y.coerceIn(0f, size.height.toFloat())
                    progress = y / size.height // 直接映射 Y 轴位置到 0-1
                }
            }
    ){

        Box(
            modifier = modifier
                .width(40.pxToDp())
                .height(trackHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colors = buildFullHueColors() // 生成完整色相渐变色
                    )
                )
                .align(Alignment.TopCenter)
        )
        // 可拖动的滑块指示器
        Box(
            modifier = Modifier
                .offset(y = trackHeight * progress - 12.pxToDp())
                .size(80.pxToDp(), 40.pxToDp())
                .background(currentColor)
//                    .border(6.dp, Color.White, RoundedCornerShape(8.dp))
                .border(8.pxToDp(), Color.White)
        )
    }
}

// 生成完整色相环颜色（每 60° 一个关键帧）
private fun buildFullHueColors(): List<Color> {
    return listOf(
        0f,   // 红
        60f,  // 黄
        120f, // 绿
        180f, // 青
        240f, // 蓝
        300f, // 紫
        360f  // 红（闭环）
    ).map { hue -> Color.hsv(hue, 1f, 1f) }
}

@Composable
fun SaturationSelector(
    hue: Float,
    modifier: Modifier = Modifier,
    onSaturationSelected: (Float) -> Unit = {}
) {
    var containerWidth by remember { mutableStateOf(0f) }
    var selectedSaturation by remember { mutableStateOf(0.5f) }

    val density = LocalDensity.current
    // 计算环半径（像素）
    val ringRadius = with(density) {480f.pxToDp().toPx() }
    val ringRadius_s = with(density) {16.pxToDp().toPx() }
    val innerRadius = with(density) {12.pxToDp().toPx() }
    val onePx = with(density) {1.pxToDp().toPx() }
    val twoPx = with(density) {1.pxToDp().toPx() }
    Canvas(
        modifier = modifier
            .height(480f.pxToDp())
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (containerWidth > 0) {

                        // 限制点击位置在有效区域
                        val maxX = containerWidth - ringRadius
                        val adjustedX = offset.x.coerceIn(ringRadius, maxX)

                        // 计算实际饱和度值
                        selectedSaturation = (adjustedX - ringRadius) /
                                (containerWidth - 2 * ringRadius)
                        onSaturationSelected(selectedSaturation)
                    }
                }
            }
            .onSizeChanged { size ->
                containerWidth = size.width.toFloat()
            }
    ) {
        // 绘制渐变背景（保持不变）
        drawRect(
            brush = Brush.horizontalGradient(
                colors = List(101) { i ->
                    Color.hsv(hue, i / 100f, 1f)
                }
            ),
            size = size
        )

        // 计算环位置
        val effectiveWidth = containerWidth - 2 * ringRadius_s
        val xPos = ringRadius_s + selectedSaturation * effectiveWidth

        // 外环（黑色描边）
        drawCircle(
            color = Color.Black,
            radius = ringRadius_s,
            center = Offset(xPos, center.y),
            style = Stroke(twoPx)
        )

        // 外环（白色描边）
        drawCircle(
            color = Color.White,
            radius = ringRadius_s -onePx,
            center = Offset(xPos, center.y),
            style = Stroke(onePx)
        )

        // 内圆（当前颜色）
        drawCircle(
            color = Color.hsv(hue, selectedSaturation, 1f),
            radius = innerRadius,
            center = Offset(xPos, center.y)
        )
    }
}
@Composable
fun LightPart(modifier: Modifier=Modifier){

    var hue by remember { mutableStateOf(0f) }
    var selectedSaturation by remember { mutableStateOf(0.5f) }

    Row(modifier = modifier.padding(
        top = 120f.pxToDp(),
        bottom = 120f.pxToDp(),
        end =120f.pxToDp()
    )) {
        FullHueVerticalSlider(onHueChanged = {hue=it})
        SaturationSelector(
            hue = hue,
            onSaturationSelected = { selectedSaturation = it },
            modifier = Modifier
                .padding(start = 80f.pxToDp())
                .size(992f.pxToDp(), 480f.pxToDp())
        )
        Column(verticalArrangement = Arrangement.spacedBy(24f.pxToDp()),
            modifier = Modifier.padding(start = 24f.pxToDp())) {
            Box(modifier = Modifier
                .background(
                    colorResource(R.color.cp_red),
                    shape = RectangleShape
                )
                .size(220f.pxToDp(), 102f.pxToDp()))

            Box(modifier = Modifier
                .background(
                    colorResource(R.color.cp_blue),
                    shape = RectangleShape
                )
                .size(220f.pxToDp(), 102f.pxToDp()))

            Box(modifier = Modifier
                .background(
                    colorResource(R.color.cp_green),
                    shape = RectangleShape
                )
                .size(220f.pxToDp(), 102f.pxToDp()))

            Box(modifier = Modifier
                .background(
                    colorResource(R.color.cp_white),
                    shape = RectangleShape
                )
                .size(220f.pxToDp(), 102f.pxToDp()))
        }
    }
}
@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 595,
    heightDp = 262
)
@Composable
fun LightPart_(){
    LightPart()
}



/**
 * 自定义座舱屏幕
 */
@Composable
fun CustomScreen(onChange: (ScreenTag) -> Unit={},onExit: () -> Unit={}){


    val context = LocalContext.current
    val soundViewModel = remember {
        val app = context.applicationContext as MyApplication
        ViewModelProvider(
            owner = (context as ComponentActivity),
            factory = SoundViewModelFactory(app.soundRepository)
        )[SoundViewModel::class.java]
    }

//    val sounds by soundViewModel._soundItems.collectAsState(initial = emptyList())

    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons_(onChange=onChange)
        }
        Box(modifier = Modifier
            .size(width = (207+1286+143).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 143f.pxToDp())){
                BigPanelV1(onChange)
//                BigPanel(onChange, modifier = Modifier)
            }
        }
        Box(modifier = Modifier.size(width = (284+1636).pxToDp(), height = 720f.pxToDp())){
            Row {
//                SoundLightElectricitySelectionButtons(
//                    tag,{tag=it}, modifier = Modifier.padding(top = 120f.pxToDp())
//                )
                SoundLightElectricitySelectionButtonsV1(tag){
                    tag=it
                }
                when(tag){
                    SoundLightElectricityTag.SOUND->
                        SoundList_(soundViewModel)

                    SoundLightElectricityTag.LIGHT ->
                        LightPart_()
                    SoundLightElectricityTag.ELECTRICITY ->
                        ElectricityList_()
                }
            }

        }
    }
}





@Composable
fun BigPanelV1(
    genCockpit:(ScreenTag)->Unit={}){
    Box(Modifier.size(
        1286.pxToDp(),720.pxToDp()
    )){

        Box(
            modifier = Modifier
                .padding(
                    start = 473f.pxToDp(),
                    bottom = 72f.pxToDp(),
                    top = 568f.pxToDp()
                )
                .size(340f.pxToDp(),80f.pxToDp())
                .clickable {genCockpit(ScreenTag.SAVE)},
            contentAlignment = Alignment.Center
        ){
            Image(painter = painterResource(R.drawable.gen_cockpit_bg),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
            )

            Text(text = ResourceManager.getGenerateMyCabin()!!,
                color = Choosen,
                fontSize = 24.getSP()
            )

        }

        Image(painter = painterResource(R.drawable.bg_pannel),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  Modifier
                .padding(top = 202.3f.pxToDp())
        )
        Image(painter = painterResource(R.drawable.main_1),
            contentDescription = "",
            modifier =  Modifier
                .padding(top = 233.75f.pxToDp(), start = 462.96f.pxToDp())
                .size(374.55f.pxToDp(), 70.23f.pxToDp())
        )

        Image(painter = painterResource(R.drawable.p_head),
            contentDescription = "",
            modifier =  Modifier
                .padding(start = 361.pxToDp(), top = 158.pxToDp())
                .size(608.pxToDp(), 43.pxToDp())
        )

        Image(painter = painterResource(R.drawable.p_text),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  Modifier
                .padding(top =72.86f.pxToDp() ,
                    start = 379.18f.pxToDp(),
                    end = 883.68f.pxToDp(),
                    bottom = 612.pxToDp()
                ))
        Image(painter = painterResource(R.drawable.zero_1),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  Modifier
                .padding(top =72.24f.pxToDp() ,
                    start = 461.01f.pxToDp(),
                    end = 802.19f.pxToDp(),
                    bottom = 611.47f.pxToDp()
                ))
        Image(painter = painterResource(R.drawable.km_h),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  Modifier
                .padding(top =92.44f.pxToDp() ,
                    start = 499.05f.pxToDp(),
                    end = 742.94f.pxToDp(),
                    bottom = 612.34f.pxToDp()
                )
        )
    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 468,
    heightDp = 262
)
@Composable
fun BigPanel_(){
    BigPanelV1()
}

@Composable
fun BackButton(onExit: () -> Unit){
    Box(modifier =
    Modifier.size(
        284f.pxToDp(),120f.pxToDp())
        .clickable { onExit() }
    ){
        Row(modifier = Modifier.fillMaxSize().padding(
            top = 40f.pxToDp()//这里似乎太长了
        )
        , verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.b_button),
                contentDescription = "",
                modifier = Modifier
                    .padding(
                        start = 36f.pxToDp()
                    )
                    .size(
                        40f.pxToDp()
                    ))
            Text(
                text = ResourceManager.getAiCabin()!!,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 37.getSP(),
                    baselineShift = BaselineShift(0.08f)
                ),
                modifier = Modifier.padding(
                    start = 8f.pxToDp(),
                )
            )
        }
    }
}

@Preview
@Composable
fun BackButton_(){
    BackButton({})
}

@Composable
fun ThemeChangeButtonV1(
    chosen: Boolean,
    tag: ScreenTag,
    onClick:(ScreenTag)->Unit={}
){
    Box(modifier=Modifier
        .size(
            height = 120.pxToDp(),
            width = 284.pxToDp(),
        )
        .background(Color.Transparent)
        .clickable(onClick = { onClick(tag) }),
        contentAlignment = Alignment.CenterEnd
    ) {
        if (chosen) {
            Image(
                contentDescription = "",
                painter = painterResource(R.drawable.choosen),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text =   ResourceManager.getScreenTags()[tag.ordinal]!!,
            style = TextStyle(
                fontSize = 32.getSP(),
                textAlign = TextAlign.Center
            ),
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier = if(LocaleManager.isEn())
            Modifier.width(250f.pxToDp()) else Modifier.padding(end = 40f.pxToDp())
        )
    }
}
@Composable
fun ThemeChangeButtons(
    chosenTag: ScreenTag,
    onChange:(ScreenTag)->Unit={},
    onExit: ()->Unit={}
){
    Column() {
        BackButton(onExit)
        Spacer(modifier = Modifier.height(24.pxToDp()))
        ThemeChangeButtonV1(chosenTag==ScreenTag.CUS,
            ScreenTag.CUS,onChange
        )
        Spacer(modifier = Modifier.height(8.pxToDp()))
        ThemeChangeButtonV1(chosenTag==ScreenTag.INS,
            ScreenTag.INS,onChange
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 200,
    heightDp = 400
)
@Composable
fun ThemeChangeButtons_(){
    ThemeChangeButtons(ScreenTag.CUS)
}
@Composable
fun ThemeChangeButtons_(onChange: (ScreenTag) -> Unit){
    val context=(LocalContext.current as? Activity)
    val scope = rememberCoroutineScope()
    ThemeChangeButtons(ScreenTag.CUS, onChange = onChange, onExit = {
        scope.launch {
            context?.finish()
        }

    })
}

