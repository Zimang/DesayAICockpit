package com.desaysv.aicockpit.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.desaysv.aicockpit.R

@Entity(
    tableName = "electricity_items",
    indices = [Index(value = ["imgPath"], unique = true)]
)
data class ElectricityItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageName: String,  // 图片名称（如 "df.png"）
    val themeName: String,   // 主题名称（如 "默认主题"）
    val imgId: Int = R.drawable.el_1,  // 主题名称（如 "默认主题"）
    val imgPath: String   //主题图片路径（如 "默认主题"）
)
