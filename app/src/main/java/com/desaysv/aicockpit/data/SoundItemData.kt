package com.desaysv.aicockpit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sound_items")
data class SoundItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,//声的id
    val imageName: String="",  // 声的图片名称
    val soundName: String="",   // 声的名称
    val imgId: Int=0,   // 声的图片id
    val imgPath: String="",   //声的图片路径
    val audioPath: String =""  //声的音频路径
)
