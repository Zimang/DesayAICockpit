package com.desay.desayaicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class ThemeItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val electricityItemData: ElectricityItemData,
//    val soundItemData: SoundItemData,
    val electricityItemId: Int, // 关联 ElectricityItemData
    val soundItemId: Int, // 关联 SoundItemData
    val themeName:String,
    val isDefault: Boolean,
    val isApplied: Boolean=false
)
