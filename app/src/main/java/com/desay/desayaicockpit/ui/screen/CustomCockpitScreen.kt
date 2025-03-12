package com.desay.desayaicockpit.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.ElectricityItemData
import com.desay.desayaicockpit.utils.pxToDp
import com.desay.desayaicockpit.utils.pxToDpNum


/**
 *  AI座舱屏幕
 *  TODO 仪表盘&生成我的座舱按钮
 *  TODO 声
 *  TODO 声- 图像列表
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

fun Int.getSP():Int{
    return this-mDeltaFontWeight
}

enum class SoundLightElectricityTag{
    SOUND,LIGHT,ELECTRICITY
}
val tags= listOf("声","光","电")
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
                fontSize = 32.getSP().sp,
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

    Column {
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.SOUND,
            SoundLightElectricityTag.SOUND==chosenTag,modifier.clickable {
            onTagChosen(SoundLightElectricityTag.SOUND)
        })
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.LIGHT,
            SoundLightElectricityTag.LIGHT==chosenTag,modifier.clickable {
            onTagChosen(SoundLightElectricityTag.LIGHT)
        })
        SoundLightElectricitySelectionButton(SoundLightElectricityTag.ELECTRICITY,
            SoundLightElectricityTag.ELECTRICITY==chosenTag,modifier.clickable {
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
                fontSize = 32.getSP().sp
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
    widthDp = 1396,
     heightDp = 262
)
@Composable
fun ElectricityList_(){
    Box(Modifier.fillMaxSize()
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
//    Text(1516.pxToDpNum().toString()) //423
//    Text(720.pxToDpNum().toString()) //262
    Text(3840.pxToDpNum().toString()) //1396
}


/**
 * SoundItem
 */
@Composable
fun SoundItem(){

}
/**
 * SoundList
 */
@Composable
fun SoundList(){

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
fun LightColorSlider(){

}


/**
 * LightColorPicker
 */
@Composable
fun LightColorPicker(){

}
