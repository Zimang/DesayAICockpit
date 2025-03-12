package com.desay.desayaicockpit.ui.screen.base

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
            modifier =  modifier
        )

        Text(
            text = text,
            style= tStyle,
            modifier=modifier.padding(
                start =tPadding.first,
                top = tPadding.second
            )
        )
    }
}