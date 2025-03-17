package com.desay.desayaicockpit.data.repository

import androidx.room.Query
import com.desay.desayaicockpit.R
import com.desay.desayaicockpit.data.ThemeItemData
import com.desay.desayaicockpit.data.db.Project
import com.desay.desayaicockpit.data.db.ThemeItemDao
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
        themeItemDao.delete(themeItemData)
    }
}