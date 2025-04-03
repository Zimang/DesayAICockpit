package com.desaysv.aicockpit.data.repository

import android.content.Context
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.db.ThemeItemDao
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.interfaces.ResourceRepository
import com.desaysv.aicockpit.data.loader.WujiJsonConfigLoader
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat

val ID_OP_SAVE_AND_APPLIED=1
val ID_OP_SWITCH_APPLIED_THEME=1

class ThemeRepository(
    private val themeItemDao: ThemeItemDao,
    private val context: Context,
    private val loader: ResourceLoader<ThemeItemData> = WujiJsonConfigLoader,
):ResourceRepository<ThemeItemData>{

    val allThemes: Flow<List<ThemeItemData>> = themeItemDao.getAllThemes()

    override suspend fun agileOp(opId: Int, item: ThemeItemData?,id:Int) {
        when (opId){
            ID_OP_SAVE_AND_APPLIED->
                item?.apply {
                    setNewAppliedTheme(this)
                }
            ID_OP_SWITCH_APPLIED_THEME->
                switchAppliedTheme(id)
            else -> Unit
        }
    }

    suspend fun addTheme(
        eId: Int,
        sId: Int,
        themeName:String,
        isDefault: Boolean,
        isApplied:Boolean)
    {
        Log.d("onSave Rep")
        themeItemDao.insert(
            ThemeItemData(
                electricityItemId = eId,
                soundItemId = sId,
                themeName = themeName,
                isDefault = isDefault,
                isApplied = isApplied
            )
        )
        Log.d("add theme")
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
    suspend fun  setNewAppliedTheme(theme: ThemeItemData) {
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

    override fun observeFlow(): Flow<ThemeItemData> {
        return loader.observe(context).flatMapConcat { it.asFlow() }
    }

    override fun observeListFlow(): Flow<List<ThemeItemData>> {
        return loader.observe(context)
    }

    override suspend fun load(): List<ThemeItemData> {
        return loader.loadOnce()
    }

    override suspend fun getAll(): List<ThemeItemData> {
        return themeItemDao.getAll()
    }

    override suspend fun getByPath(p: String): ThemeItemData? {
        return themeItemDao.getByPath(p)
    }

    override suspend fun insert(item: ThemeItemData) {
        themeItemDao.insert(item)
    }

    override suspend fun delete(item: ThemeItemData) {
        themeItemDao.delete(item)
    }

    override suspend fun deleteAll() {
        themeItemDao.deleteAll()
    }

    override suspend fun saveAll(items: List<ThemeItemData>) {
        themeItemDao.deleteAll()
        items.forEach { themeItemDao.insert(it) }
    }

}