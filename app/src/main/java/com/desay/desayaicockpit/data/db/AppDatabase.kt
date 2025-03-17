package com.desay.desayaicockpit.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.desay.desayaicockpit.data.ElectricityItemData
import com.desay.desayaicockpit.data.SoundItemData
import com.desay.desayaicockpit.data.ThemeItemData

@SuppressLint("RestrictedApi")
@Database( version = 1,entities = [ElectricityItemData::class,
    SoundItemData::class,
    ThemeItemData::class,
    Project::class]
    , exportSchema = false,
    autoMigrations = [])
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun electricityItemDao(): ElectricityItemDao
    abstract fun soundItemDao(): SoundItemDao
    abstract fun themeItemDao(): ThemeItemDao

//    companion object {
//        private var INSTANCE: AppDatabase? = null
//
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "project_db"
//                ).build().also { INSTANCE = it }
//            }
//        }
//    }
}