package com.desaysv.aicockpit.di

import com.desaysv.aicockpit.data.ElectricityItemData
import com.desaysv.aicockpit.data.SoundItemData
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.data.interfaces.ResourceLoader
import com.desaysv.aicockpit.data.loader.AvailablePicsLoader
import com.desaysv.aicockpit.data.loader.FolderFileLoader
import com.desaysv.aicockpit.data.loader.SoundLoader
import com.desaysv.aicockpit.data.loader.WujiJsonConfigLoader
import org.koin.dsl.module


val appModule = module {

    // 🟢 注册 Loader（每个 Resource 类型一个实现）
    single<ResourceLoader<ThemeItemData>> { WujiJsonConfigLoader() }
    single<ResourceLoader<SoundItemData>> { SoundLoader() }
    single<ResourceLoader<ElectricityItemData>> { AvailablePicsLoader() }
//    single { FolderFileLoader() } // 非泛型注册

}