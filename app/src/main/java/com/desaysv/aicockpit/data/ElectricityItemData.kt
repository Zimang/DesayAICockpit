package com.desaysv.aicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "electricity_items")
data class ElectricityItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageName: String,  // 图片名称（如 "el_1.png"）
    val themeName: String,   // 主题名称（如 "默认主题"）
    val imgId: Int ,  // 主题名称（如 "默认主题"）
    val imgPath: String=""   //主题图片路径（如 "默认主题"）
)
