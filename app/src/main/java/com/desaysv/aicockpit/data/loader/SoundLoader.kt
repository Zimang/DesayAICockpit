package com.desaysv.aicockpit.data.loader

import android.content.Context
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import kotlinx.coroutines.flow.Flow

object SoundLoader:ResourceLoader<SoundItemData> {

    var folderFileLoader =FolderFileLoader<SoundItemData>(
        ResourceType.SOUND_RESOURCE
    )

    override suspend fun loadOnce(): List<SoundItemData> {
        TODO("Not yet implemented")
    }

    override fun observe(context: Context): Flow<List<SoundItemData>> {
        TODO("Not yet implemented")
    }
}