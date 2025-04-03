package com.desaysv.aicockpit.data

data class ListTheme(
    val Url: String="",
    val wallpaperPath: String,
    val title: String,
    val Description: Boolean=false,
    val icon1: String="",
    val icon1_Path: String="",
    val icon2: String="",
    val icon2_Path: String="",
    val icon3: String="",
    val icon3_Path: String="",
    val icon4: String="",
    val icon4_Path: String="",
    val icon5: String="",
    val icon5_Path: String="",
    val currentWallpaperContent: String="",
    val currentIconContent: String="",
    val layoutType: Int = 0,
    //颜色
    val hue:Float,
    val saturation:Float,
    //SoundId
    val sId:Int
)
