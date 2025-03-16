package com.desay.desayaicockpit

import android.app.Application
import androidx.room.Room
import com.desay.desayaicockpit.data.db.AppDatabase
import com.desay.desayaicockpit.data.db.ProjectDao
import com.desay.desayaicockpit.data.repository.ProjectRepository

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
}