package com.desay.desayaicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sound_items")
data class SoundItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageName: String,  // 图片名称（如 "el_1.png"）
    val soundName: String,   // 主题名称（如 "默认主题"）
    val imgId: Int   // 主题名称（如 "默认主题"）
)
