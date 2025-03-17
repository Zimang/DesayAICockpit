package com.desaysv.aicockpit.data.repository

import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.db.ThemeItemDao
import kotlinx.coroutines.flow.Flow

class ThemeRepository(private val themeItemDao: ThemeItemDao){

    val allThemes: Flow<List<ThemeItemData>> = themeItemDao.getAllThemes()


    suspend fun addTheme(
        eId: Int,
        sId: Int,
        themeName:String,
        isDefault: Boolean,
        isApplied:Boolean) {
        themeItemDao.insert(
            ThemeItemData(
                electricityItemId = eId,
                soundItemId = sId,
                themeName = themeName,
                isDefault = isDefault,
                isApplied = isApplied
        )
        )
    }

    suspend fun deleteTheme(themeItemData: ThemeItemData) {
        val isDeletingApplied = themeItemData.isApplied
        themeItemDao.delete(themeItemData)

        // 如果删除的是当前应用的主题，则应用默认主题
        if (isDeletingApplied) {
            val defaultTheme = themeItemDao.getCurrentDefaultTheme()
            defaultTheme?.let {
                themeItemDao.updateAppliedTheme(it.id)
            }
        }
    }


    // **方法 1：清除当前 isApplied 并添加新主题**
    suspend fun setNewAppliedTheme(theme: ThemeItemData) {
        themeItemDao.replaceAppliedTheme(theme)
    }

    // **方法 2：切换某个已有主题为 isApplied = true**
    suspend fun switchAppliedTheme(themeId: Int) {
        themeItemDao.switchAppliedTheme(themeId)
    }


    // **切换默认主题**
    suspend fun switchDefaultTheme(themeId: Int) {
        themeItemDao.clearDefaultTheme() // 清除当前默认
        themeItemDao.setDefaultTheme(themeId) // 设置新默认
    }

    // **获取当前默认主题**
    suspend fun getCurrentDefaultTheme(): ThemeItemData? {
        return themeItemDao.getCurrentDefaultTheme()
    }

    // **确保至少有一个默认主题**
    suspend fun ensureDefaultThemeExists() {
        val defaultCount = themeItemDao.countDefaultThemes()
        if (defaultCount == 0) {
            themeItemDao.clearAppliedTheme()
            // 插入默认主题
            themeItemDao.insert(
                ThemeItemData(
                    electricityItemId = 1, // 默认数据
                    soundItemId = 1,       // 默认数据
                    themeName = "默认主题",
                    isDefault = true,
                    isApplied = true
                )
            )
        }
    }
}