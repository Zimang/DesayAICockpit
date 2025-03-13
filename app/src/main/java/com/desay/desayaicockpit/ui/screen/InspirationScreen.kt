package com.desay.desayaicockpit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.ElectricityItemData
import com.desay.desayaicockpit.data.SoundItemData
import com.desay.desayaicockpit.data.ThemeItemData
import com.desay.desayaicockpit.utils.pxToDp

/**
 * 灵感屏幕
 * TODO 灵感项（选中与非选中按钮）
 * TODO 灵感项列表-跑马灯
 */

@Composable
fun ThemeCard(themeItemData: ThemeItemData,
              isApplied:Boolean,
              ){
    Column(verticalArrangement = Arrangement.spacedBy(16f.pxToDp())) {

        Box(){
            val c=if (isApplied) colorResource(R.color.choosen) else colorResource(R.color.n_choosen)
            Image(painterResource(R.drawable.el_1),
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
//        Image(painterResource(R.drawable.el_1),contentDescription = null,
//            modifier = Modifier.size(
//                472f.pxToDp()
//            ))
        if (themeItemData.isDefault){
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)){
                val t=if (isApplied) "应用中" else "应用"
                val c=if (isApplied) colorResource(R.color.choosen) else colorResource(R.color.n_choosen)
                Image(painterResource(R.drawable.r_1),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(
                        472f.pxToDp(),72f.pxToDp()
                    ))
                Text(text = t, style = TextStyle(
                    color = c
                ), modifier = Modifier.align(Alignment.Center))
            }
        }else{
            Row (horizontalArrangement = Arrangement.spacedBy(16f.pxToDp())){

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                )){
                    val t=if (isApplied) "应用中" else "应用"
                    val c=if (isApplied) colorResource(R.color.choosen) else colorResource(R.color.n_choosen)
                    val img=if (isApplied) painterResource(R.drawable.app_1) else painterResource(R.drawable.app_2)
                    Image(img,contentDescription = null,
                        modifier = Modifier.size(
                            228.pxToDp(),72f.pxToDp()
                        ))
                    Text(text = t, style = TextStyle(
                        color = c, fontSize = 30.getSP()
                    ), modifier = Modifier.align(Alignment.Center))
                }

                Box(modifier = Modifier.size(
                    228.pxToDp(),72f.pxToDp()
                )){
                    Image(painterResource(R.drawable.app_2),contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                    Text(text = "删除",
                        style = TextStyle(
                        color = colorResource(R.color.n_choosen),
                        fontSize = 30.getSP(),
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
//        val itemList= listOf(10){
//            ThemeItemData(
//                ElectricityItemData("测试","测试主题",R.drawable.el_1),
//                SoundItemData("测试","测试生效",R.drawable.b_1_h),
//                "呱呱叫",false)
//        }
//        itemsIndexed(itemList){key,item->
//            ThemeCard(key,
//                false
//            )
//
//        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",true),
                false
            )
        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",false),
                false
            )

        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",true),
                false
            )
        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",false),
                true
            )
        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",false),
                false
            )

        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",true),
                false
            )
        }
        item {
            ThemeCard(ThemeItemData(
                ElectricityItemData("测试","测试主题",R.drawable.el_1),
                SoundItemData("测试","测试生效",R.drawable.b_1_h),
                "呱呱叫",false),
                false
            )
        }

    }
}

/**
 * 灵感屏幕
 */
@Composable
fun InspiratonScreen(){
    var tag by remember { mutableStateOf(SoundLightElectricityTag.SOUND) }
    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.size(width = 284.pxToDp(), height = 720f.pxToDp())){
            ThemeChangeButtons(ScreenTag.INS)
        }
        Box(modifier = Modifier
            .size(width = (207+1286+143+284+1636).pxToDp(), height = 720f.pxToDp())){
            Row(modifier = Modifier.align(Alignment.CenterStart).padding(start = 200f.pxToDp())){
//                BigPanel(modifier = Modifier)
                ThemeCards_()
            }
        }
    }
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
fun FinalScreenINSPage(){
    InspiratonScreen()
}
