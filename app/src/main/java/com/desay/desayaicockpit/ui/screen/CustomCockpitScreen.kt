package com.desay.desayaicockpit.ui.screen

import androidx.annotation.ColorRes
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.ElectricityItemData
import com.desay.desayaicockpit.data.SoundItemData
import com.desay.desayaicockpit.ui.screen.base.PicWithPic
import com.desay.desayaicockpit.ui.screen.base.PicWithText
import com.desay.desayaicockpit.utils.pxToDp
import com.desay.desayaicockpit.utils.pxToDpNum


/**
 *  AI座舱屏幕
 *  TODO 仪表盘&生成我的座舱按钮
 *  TODO 声
 *  声- 图像列表
 *  TODO 声- 移动跑马灯
 *  TODO 光
 *  TODO 光- 颜色滑块
 *  TODO 光- 颜色托盘
 *  TODO 光- 选色器
 *  TODO 电
 *  TODO 电- 图片文字项
 *  TODO 电- 图像列表
 *  TODO 电- 图片跑马灯
 *  声光电选择按钮
 *
 */

val mDeltaFontWeight=22

fun Int.getSP():TextUnit{
    return (this-mDeltaFontWeight).sp
}

enum class SoundLightElectricityTag{
    SOUND,LIGHT,ELECTRICITY
}
enum class ScreenTag{
    INS,CUS,SAVE
}
val tags= listOf("声","光","电")
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
        imgId = tSoundImgId[index]
    )
}
/**
 * Sound Light Electricity Selection Button
 */
@Composable
fun SoundLightElectricitySelectionButton(
    tag:SoundLightElectricityTag, chosen:Boolean,modifier: Modifier
){

    Box(modifier=modifier
        .size(
            height = 120.pxToDp(),
            width = 284.pxToDp(),
        )
        .background(Color.Transparent)) {
        if (chosen) {
            Image(
                contentDescription = "",
                painter = painterResource(R.drawable.choosen),
                contentScale = ContentScale.FillBounds,
                modifier = modifier.fillMaxSize())
        }

        Text(
            text = tags[tag.ordinal],
            style = TextStyle(
                fontSize = 32.getSP(),
                baselineShift = BaselineShift(0.2f), //这个参数还蛮有用的
                textAlign = TextAlign.Center
            ),
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier = modifier
                .padding(top = 44.06f.pxToDp(), start = 212.pxToDp())
                .size(width = 29.66f.pxToDp(), height = 28.83f.pxToDp())

        )
    }
}
@Preview
@Composable
fun SoundLightElectricitySelectionButton_(){
    Column {
        for (tag in SoundLightElectricityTag.entries){
            SoundLightElectricitySelectionButton(tag,false,Modifier)
        }

    }
}

/**
 * Sound Light Electricity Selection Buttons
 */
@Composable
fun SoundLightElectricitySelectionButtons(
    chosenTag: SoundLightElectricityTag,
    onTagChosen: (SoundLightElectricityTag)->Unit,
    modifier: Modifier
){

    Column(modifier=modifier) {
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.SOUND,
            SoundLightElectricityTag.SOUND==chosenTag,Modifier.clickable {
            onTagChosen(SoundLightElectricityTag.SOUND)
        })
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.LIGHT,
            SoundLightElectricityTag.LIGHT==chosenTag,Modifier.clickable {
            onTagChosen(SoundLightElectricityTag.LIGHT)
        })
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.ELECTRICITY,
            SoundLightElectricityTag.ELECTRICITY==chosenTag,Modifier.clickable {
            onTagChosen(SoundLightElectricityTag.ELECTRICITY)
        })

    }
}

/**
 * Sound Light Electricity Selection Buttons
 */
@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
fun SoundLightElectricitySelectionButtons_(){
    var chosenTag by rememberSaveable{ mutableStateOf(SoundLightElectricityTag.SOUND) }
    SoundLightElectricitySelectionButtons(chosenTag,{chosenTag=it},Modifier)
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
 * ElectricityItem
 */
@Preview(showBackground = true, backgroundColor = 0xff000000)
@Composable
fun ElectricityItem_(){

    ElectricityItem(
        electricityItemDataList[0].imgId,
        electricityItemDataList[0].themeName,
        true,
        modifier = Modifier)
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
//            .size(1516.pxToDp(),472.pxToDp()) //1044+472=1516
//            .padding(start = 2324.pxToDp(),top=120.pxToDp())
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
//                .padding(top = 120.pxToDp()
//                , bottom = 128.pxToDp()
//                , start = 2324.pxToDp()))
    }
    Text(1286.pxToDpNum().toString(), color = Color.White) //468
//    Text(1516.pxToDpNum().toString()) //423
//    Text(720.pxToDpNum().toString()) //262
//    Text(3840.pxToDpNum().toString()) //1396
//    Text(1636.pxToDpNum().toString()) //595
}


/**
 * SoundItem
 */
@Composable
fun SoundItem(
    soundItemData: SoundItemData,
    modifier: Modifier
){
    PicWithText(
        imgPath =  soundItemData.imgId,
        text =  soundItemData.soundName,
        tStyle = TextStyle(
            fontSize = 30.getSP(),
            color = Color.White
        ),
        pSize =  Pair(342.pxToDp(),456.pxToDp()),
        tPadding = Pair(24.72f.pxToDp(),407.77f.pxToDp()),
        modifier = modifier

    )
}
/**
 * SoundItem
 */
@Preview(showBackground = true,
    backgroundColor = 0xff000000
)
@Composable
fun SoundItem_( ){
    SoundItem(soundItemDataList[0],Modifier)
}
/**
 * SoundList
 */
@Composable
fun SoundList(
    soundItemDataList: List<SoundItemData>,
    onThemeChosen:(Int)->Unit,
    modifier: Modifier){

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(120.pxToDp()),
        modifier = modifier
//            .size(1516.pxToDp(),472.pxToDp()) //1044+472=1516
//            .padding(start = 2324.pxToDp(),top=120.pxToDp())
    ) {
        itemsIndexed(soundItemDataList){ index, electItem->
            SoundItem(electItem
                ,modifier=modifier.clickable {
                    onThemeChosen(index)
                }
            )
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
fun SoundList_(){

    Box(Modifier
        .fillMaxSize()
        .padding(start = 120.pxToDp(), top = 120.pxToDp())
//        ,contentAlignment = Alignment.TopStart
    ){
        SoundList(soundItemDataList,{},Modifier)
    }
}

/**
 * SoundListWithMarqueeEffect
 */
@Composable
fun SoundListWithMarqueeEffect(){

}

/**
 * ElectricityList 跑马灯效果
 */
@Composable
fun ElectricityListWithMarqueeEffect(){

}


/**
 * LightColorSlider
 */
@Composable
fun LightColorSlider(
    modifier: Modifier = Modifier,
    onHueChanged: (Float) -> Unit){
    FullHueVerticalSlider(modifier,onHueChanged)

}

@Preview(showBackground = true,
    heightDp = 200
)
@Composable
fun FullHueVerticalSlider_() {
    FullHueVerticalSlider(Modifier.padding(top = 20f.pxToDp()),{})
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
//@Preview
//@Composable
//fun PreviewSaturationSelector(modifier: Modifier) {
//    var hue by remember { mutableStateOf(0f) }
//    var selectedSaturation by remember { mutableStateOf(0.5f) }
//
//    Row(modifier = modifier.padding(top = 120f.pxToDp(), bottom = 120f.pxToDp())) {
//        FullHueVerticalSlider(onHueChanged = {hue=it})
//        SaturationSelector(
//            hue = hue,
//            onSaturationSelected = { selectedSaturation = it },
//            modifier = Modifier.padding(start = 80f.pxToDp())
//                .size(992f.pxToDp(),480f.pxToDp())
//        )
//        Column(verticalArrangement = Arrangement.spacedBy(24f.pxToDp()),
//            modifier = Modifier.padding(start = 24f.pxToDp())) {
//            Box(modifier = Modifier.background(colorResource(R.color.cp_red),
//                shape = RectangleShape).size(220f.pxToDp(),102f.pxToDp()))
//
//            Box(modifier = Modifier.background(colorResource(R.color.cp_blue),
//                shape = RectangleShape).size(220f.pxToDp(),102f.pxToDp()))
//
//            Box(modifier = Modifier.background(colorResource(R.color.cp_green),
//                shape = RectangleShape).size(220f.pxToDp(),102f.pxToDp()))
//
//            Box(modifier = Modifier.background(colorResource(R.color.cp_white),
//                shape = RectangleShape).size(220f.pxToDp(),102f.pxToDp()))
//        }
//    }
//}

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
 * LightColorPicker
 */
@Composable
fun LightColorPicker(){

}

/**
 * final screen
 */
@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 1396,
    heightDp = 262
)
@Composable
fun FinalScreen(){
    CustomScreen()
 }


/**
 * 自定义座舱屏幕
 */
@Composable
fun CustomScreen(){
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons_()
        }
        Box(modifier = Modifier
            .size(width = (207+1286+143).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 143f.pxToDp())){
                BigPanel(modifier = Modifier)
            }
        }
        Box(modifier = Modifier.size(width = (284+1636).pxToDp(), height = 720f.pxToDp())){
            Row {
                SoundLightElectricitySelectionButtons(
                    tag,{tag=it}, modifier = Modifier.padding(top = 120f.pxToDp())
                )
                when(tag){
                    SoundLightElectricityTag.SOUND->
                        SoundList_()

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
fun BigPanel(
    modifier: Modifier=Modifier){
    Box(modifier.size(
        1286.pxToDp(),720.pxToDp()
    )){
        PicWithPic(
            R.drawable.gen_cockpit_text,
            R.drawable.gen_cockpit_bg,
            Pair(189.28f.pxToDp(), 29.79f.pxToDp()),
            Pair(340f.pxToDp(), 80f.pxToDp()),
            Pair(0.dp,0.dp),
            modifier.padding(
                start = 473f.pxToDp(),
                bottom = 72f.pxToDp(),
                top = 568f.pxToDp()
            )
        )
        Image(painter = painterResource(R.drawable.bg_pannel),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  modifier
                .padding(top = 202.3f.pxToDp())
        )
        Image(painter = painterResource(R.drawable.main_1),
            contentDescription = "",
            modifier =  modifier
                .padding(top = 233.75f.pxToDp(), start = 462.96f.pxToDp())
                .size(374.55f.pxToDp(), 70.23f.pxToDp())
        )

        Image(painter = painterResource(R.drawable.p_head),
            contentDescription = "",
            modifier =  modifier
                .padding(start = 361.pxToDp(), top = 158.pxToDp())
                .size(608.pxToDp(), 43.pxToDp())
        )

        Image(painter = painterResource(R.drawable.p_text),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  modifier
                .padding(top =72.86f.pxToDp() ,
                    start = 379.18f.pxToDp(),
                    end = 883.68f.pxToDp(),
                    bottom = 612.pxToDp()
                ))
        Image(painter = painterResource(R.drawable.zero_1),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  modifier
                .padding(top =72.24f.pxToDp() ,
                    start = 461.01f.pxToDp(),
                    end = 802.19f.pxToDp(),
                    bottom = 611.47f.pxToDp()
                ))
        Image(painter = painterResource(R.drawable.km_h),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  modifier
                .padding(top =92.44f.pxToDp() ,
                    start = 499.05f.pxToDp(),
                    end = 742.94f.pxToDp(),
                    bottom = 612.34f.pxToDp()
                )
        )

    }
//            .size(23.pxToDp(),35.pxToDp())
//            .size(23.14f.pxToDp(),35.14f.pxToDp())

//    Text(23.14f.pxToDp().toString(), color = Color.White)
//    Image(painter = painterResource(R.drawable.main_1),
//        contentDescription = "",
//        modifier =  modifier
//            .padding(top = 233.75f.pxToDp(), start =462.96f.pxToDp() )
//            .size(374.55f.pxToDp(),70.23f.pxToDp())
//    )
//
//    Image(painter = painterResource(R.drawable.p_head),
//        contentDescription = "",
//        modifier =  modifier
//            .padding(start = 361.pxToDp(), top=158.pxToDp() )
//            .size(608.pxToDp(),43.pxToDp())
//    )
}


@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 468,
    heightDp = 262
)
@Composable
fun BigPanel_(){
    BigPanel()
}


@Composable
fun BackButton(onExit: () -> Unit){
    Box(modifier = Modifier.size(
        284f.pxToDp(),120f.pxToDp()
    )){
        Image(painterResource(R.drawable.b_button),
            contentDescription = "",
            modifier = Modifier
                .padding(
                    start = 36f.pxToDp(), top = 40f.pxToDp()
                )
                .size(
                    40f.pxToDp()
                ))
        Image(
            painterResource(R.drawable.b_button_text),
            contentDescription = "",
            modifier = Modifier
                .padding(
                    start = 84.28f.pxToDp(),
                    top = 40.08f.pxToDp()
                )
                .size(
                    115.71f.pxToDp(),
                    37.28f.pxToDp()
                ))
    }
}


@Composable
fun BackButton_(){
    BackButton({})
}

@Composable
fun ThemeChangeButton(
    chosen: Boolean,
    tag: ScreenTag,
    modifier: Modifier,
    start:Float,
    onClick:(ScreenTag)->Unit={}
){
    Box(modifier=Modifier
        .size(
            height = 120.pxToDp(),
            width = 284.pxToDp(),
        )
        .background(Color.Transparent)) {
        if (chosen) {
            Image(
                contentDescription = "",
                painter = painterResource(R.drawable.choosen),
                contentScale = ContentScale.FillBounds,
                modifier = modifier
                    .size(
                        height = 120.pxToDp(),
                        width = 284.pxToDp(),
                    )
            )
        }
        Text(
            text = screenTags[tag.ordinal],
            style = TextStyle(
                fontSize = 32.getSP(),
//                baselineShift = BaselineShift(0.2f), //这个参数还蛮有用的
                textAlign = TextAlign.Right
            ),
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier = Modifier
                .padding(top = 43.86f.pxToDp()
                    ,end = 40.86f.pxToDp()
                    , start = start.pxToDp()
                )
        )

    }
}
@Composable
fun ThemeChangeButtons(
    chosenTag: ScreenTag,
    modifier: Modifier=Modifier,
    onChange:(ScreenTag)->Unit={},
    onExit:()->Unit={}
){
    Column(modifier=modifier) {
        BackButton(onExit)
        ThemeChangeButton(chosenTag==ScreenTag.CUS,
            ScreenTag.CUS,Modifier.padding(top = 24f.pxToDp()),84.58f,onChange)
        ThemeChangeButton(chosenTag==ScreenTag.INS,
            ScreenTag.INS,Modifier.padding(top=8f.pxToDp()),181.31f,onChange)
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

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
    widthDp = 200,
    heightDp = 100
)
@Composable
fun ThemeChangeButton_(){
    ThemeChangeButton(true,ScreenTag.INS,Modifier,84.58f)
}