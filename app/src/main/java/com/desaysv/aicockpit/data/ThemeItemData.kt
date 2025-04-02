package com.desaysv.aicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val electricityItemId: Int, // 图片
    val soundItemId: Int, // 关联 SoundItemData
    val themeName:String, //主题名称
    val isDefault: Boolean,
    val isApplied: Boolean=false
)
