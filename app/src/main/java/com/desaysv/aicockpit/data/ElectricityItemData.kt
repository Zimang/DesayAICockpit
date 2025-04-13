package com.desaysv.aicockpit.data

import android.content.Context
import android.content.Intent
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.business.ImageConstants.ACTION_UPDATE_ELE_LAUNCHER
import com.desaysv.aicockpit.utils.Log

const val LAUNCHER_PACKAGE="com.desaysv.ivi.launcher"

@Entity(
    tableName = "electricity_items",
    indices = [Index(value = ["imgPath"], unique = true)]
)
data class ElectricityItemData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageName: String,  // 图片名称（如 "df.png"）
    val themeName: String,   // 主题名称（如 "默认主题"）
    val imgId: Int = R.drawable.el_1,  // 主题名称（如 "默认主题"）

    //这里unique
    val imgPath: String,   //主题图片路径（如 "默认主题"）

    //无极配置需要保留
    val icon1: String="",
    val icon2: String="",
    val icon3: String="",
    val icon4: String="",
    val icon5: String="",
    val icon1_Path: String="",
    val icon2_Path: String="",
    val icon3_Path: String="",
    val icon4_Path: String="",
    val icon5_Path: String="",
    val layoutType: Int = 0,
){
    fun send2Wuji(context: Context){
        try {
            val it=Intent(ACTION_UPDATE_ELE_LAUNCHER)
            it.putExtra("theme_name",themeName)
            it.putExtra("theme_path",imgPath)
            it.putExtra("icon1_path",icon1_Path)
            it.putExtra("icon2_path",icon2_Path)
            it.putExtra("icon3_path",icon3_Path)
            it.putExtra("icon4_path",icon4_Path)
            it.putExtra("icon5_path",icon5_Path)
            it.putExtra("is_layout",layoutType)
            it.setPackage(LAUNCHER_PACKAGE)
            context.sendBroadcast(it)
            Log.d("发送Launcher壁纸更新广播$this")
            Log.d("发送Launcher壁纸更新广播intent ${Log.printAllBundleExtra(it)}")
        }catch (e:Exception){
            Log.d("failed to send broadcast:$e")
        }
    }
}
