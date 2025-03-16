package com.desay.desayaicockpit.ui.screen.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.ui.screen.getSP
import com.desay.desayaicockpit.utils.pxToDp


/**
 * @param [imgPath]
 * @param [text]
 * @param [pSize]  宽 高
 * @param [tPadding] start top
 * @param [modifier]
 * @param [tStyle]
 */
@Composable
fun PicWithText(
    @DrawableRes imgPath:Int,
    text:String,
    pSize:Pair<Dp,Dp>,
    tPadding:Pair<Dp,Dp>,
    modifier: Modifier=Modifier,
    tStyle:TextStyle=TextStyle()
){
    Box(modifier
        .size(pSize.first,pSize.second)
    ){
        Image(painter = painterResource(imgPath),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier =  Modifier
        )

        Text(
            text = text,
            style= tStyle,
            modifier=Modifier.padding(
                start =tPadding.first,
                top = tPadding.second
            )
        )
    }
}
@Composable
fun PicWithPic(
    @DrawableRes imgPath:Int,
    @DrawableRes bgImgPath:Int,
    pSize:Pair<Dp,Dp>,
    bgSize:Pair<Dp,Dp>,
    modifier: Modifier=Modifier,
    pPadding:Pair<Dp,Dp> = Pair(0.dp,0.dp),
){
    val center=pPadding.first==0.dp&&pPadding.second==0.dp
    Box(modifier = modifier.size(bgSize.first,bgSize.second),
        contentAlignment = if (center) Alignment.Center else Alignment.TopStart
    ){
        Image(painter = painterResource(bgImgPath),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
//            modifier =  Modifier
        )
        Image(painter = painterResource(imgPath),
            contentDescription = "",
            modifier = if (center) Modifier else Modifier.size(pSize.first,pSize.second)
//                .padding(start = pPadding.first, top =  pPadding.second)
        )

    }
}

@Preview
@Composable
fun PicWithPic_(){
    PicWithPic(
        R.drawable.gen_cockpit_text,
        R.drawable.gen_b,
        Pair(189.28f.pxToDp(),29.79f.pxToDp()),
        Pair(340f.pxToDp(),80f.pxToDp()),
        pPadding =  Pair(0.pxToDp(),0.pxToDp()),
    )
}

@Preview
@Composable
fun PicWithPic_Save(){
    PicWithPic(
        R.drawable.save,
        R.drawable.save_bt_bg,
        Pair(189.28f.pxToDp(),29.79f.pxToDp()),
        Pair(340f.pxToDp(),80f.pxToDp()),
        pPadding =  Pair(0.pxToDp(),0.pxToDp()),
    )
}

@Preview
@Composable
fun PicWithPic_SaveApply(){
    PicWithPic(
        R.drawable.save_bt_save_and_apply,
        R.drawable.save_bt_bg,
        Pair(189.28f.pxToDp(),29.79f.pxToDp()),
        Pair(340f.pxToDp(),80f.pxToDp()),
        pPadding =  Pair(0.pxToDp(),0.pxToDp()),
    )
}