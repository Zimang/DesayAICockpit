package com.desaysv.aicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val electricityItemId: Int=0, // 图片
    val themeName:String, //主题名称
    val isDefault: Boolean=false,
    val isApplied: Boolean=false,
    val imgPath: String="",   //主题图片路径（如 "默认主题"）,
    val icon1_Path: String="",
    val icon2_Path: String="",
    val icon3_Path: String="",
    val icon4_Path: String="",
    val icon5_Path: String="",
    val layoutType: Int = 0,
    val hue:Float=0f,
    val soundItemId: Int=0, // 关联 SoundItemData
    val saturation:Float=0f,
)
