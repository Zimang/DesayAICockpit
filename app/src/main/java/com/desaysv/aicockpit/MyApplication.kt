package com.desaysv.aicockpit

import android.app.Application
import androidx.room.Room
import com.desaysv.aicockpit.data.db.AppDatabase
import com.desaysv.aicockpit.data.repository.ProjectRepository
import com.desaysv.aicockpit.data.repository.SoundRepository
import com.desaysv.aicockpit.data.repository.ThemeRepository
import com.desaysv.aicockpit.utils.LocaleManager
import com.desaysv.aicockpit.utils.ResourceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    // 通过 lazy 延迟初始化数据库（线程安全）
    val database by lazy {
//        AppDatabase.getInstance(this)
        Room.databaseBuilder(
            applicationContext,AppDatabase::class.java,"proj"
        ).build()
    }

    // 初始化 Repository（依赖数据库 DAO）
    val repository by lazy { ProjectRepository(database.projectDao()) }
    val themeRepository by lazy { ThemeRepository(database.themeItemDao()) }
    val soundRepository by lazy { SoundRepository(database.soundItemDao()) }

    override fun onCreate() {
        super.onCreate()
        LocaleManager.init(this)
        ResourceManager.init(this)
        // 启动时检查默认主题
        CoroutineScope(Dispatchers.IO).launch {
            themeRepository.ensureDefaultThemeExists()
        }
    }
}