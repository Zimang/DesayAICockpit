package com.desaysv.aicockpit.ui.screen

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.utils.pxToDp

val deltaFontWeight=-24


private data class SLEItem(
    val id: Int,
    val text: String
)

@Composable
fun SLEChooser(modifier: Modifier){
    val tags= listOf("声","光","电")
    var choosenId by rememberSaveable { mutableIntStateOf(0) }

    Column {

        SLEChooserItem(tags[0],choosenId==0,{
            choosenId=0
            Log.d("Test", "SLEChooser: 0")
        },modifier)
        SLEChooserItem(tags[1],choosenId==1,{
            choosenId=1
            Log.d("Test", "SLEChooser: 0")
        },modifier)
        SLEChooserItem(tags[2],choosenId==2,{
            choosenId=2
            Log.d("Test", "SLEChooser: 0")
        },modifier)
    }
}

@Preview(showBackground = true,backgroundColor = 0xff000000)
@Composable
fun SLEChooser_(){
    SLEChooser(Modifier)
}


@Composable
fun SLEChooserItem(tag:String, chosen:Boolean, onChoose:()->Unit,
                   modifier: Modifier){
    Box(modifier=modifier.size(
        height = 120.pxToDp(),
        width = 284.pxToDp(),
    ).background(Color.Transparent)) {
        if (chosen) {
            Image(contentDescription = "", painter = painterResource(R.drawable.choosen)
            , contentScale = ContentScale.FillBounds, modifier = modifier.fillMaxSize())
        }

        Text(
            text =tag,
            fontSize = (32+ deltaFontWeight).sp  ,
            color = if (chosen) colorResource(R.color.choosen)  else colorResource(R.color.n_choosen),
            modifier = modifier
                .padding(top = (44.06f).pxToDp(), start = 212.pxToDp())
                .size(width = 29.66f.pxToDp(), height = 28.83f.pxToDp())
                .clickable {
                    onChoose()
                }
        )
    }
}

@Preview(showBackground = true,backgroundColor = 0xff000000)
@Composable
fun SLEChooserItem_( ){
    SLEChooserItem("光",true,{},Modifier)
}

@Preview(showBackground = true,backgroundColor = 0xff000000)
@Composable
fun genMyCockpitButton_(){
    genMyCockpitButton("生成我的座舱",Modifier)
}

@Composable
fun genMyCockpitButton(text: String, modifier: Modifier){
    Box(modifier=modifier.size(
        height = 80.pxToDp(),
        width = 340.pxToDp()
    ), contentAlignment = Alignment.Center){
        Image(contentDescription = "",
            painter = painterResource(R.drawable.gen_b)
            , contentScale = ContentScale.Crop, modifier = modifier.fillMaxSize())

        Text(text, color = colorResource(R.color.choosen), fontSize = 28.sp)
    }
}

@Composable
fun PannelBG(modifier: Modifier){
     Box(){
         GeneralImage(img = R.drawable.bg_pannel,modifier=modifier, height = 1286.pxToDp(), width = (284.75f).pxToDp())
//         GeneralImage(img = )
     }
}


@Composable
fun GeneralImage(@DrawableRes img:Int,
                 ds:String="",
                 contentScale: ContentScale=ContentScale.Crop,
                 width:Dp,
                 height:Dp,
                 modifier: Modifier){
    Image(contentDescription = ds,
        painter = painterResource(img),
        contentScale =contentScale,
        modifier = modifier.fillMaxSize().
        size(width = width, height = height)
    )

}