package com.desaysv.aicockpit.data.loader

import android.content.Context
import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import kotlinx.coroutines.flow.Flow

class AvailablePicsLoader:ResourceLoader<ElectricityItemData> {
    override suspend fun loadOnce(): List<ElectricityItemData> {
        TODO("Not yet implemented")
    }

    override fun observe(context: Context): Flow<List<ElectricityItemData>> {
        TODO("Not yet implemented")
    }
}