package com.desay.desayaicockpit.data

data class ThemeItemData(
    val electricityItemData: ElectricityItemData,
    val soundItemData: SoundItemData,
    val themeName:String,
    val isDefault: Boolean
)
