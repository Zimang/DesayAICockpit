package com.desaysv.aicockpit.data

data class ListSound(
    val Url: String="",
    val wallpaperPath: String,
    val audioPath: String,
    val title: String,
    val Description: Boolean=false,
    val currentWallpaperContent: String="",
    val currentIconContent: String="",
)
