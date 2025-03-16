package com.desay.desayaicockpit.data.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@SuppressLint("RestrictedApi")
@Database( version = 1,entities = [Project::class]
    , exportSchema = false,
    autoMigrations = [])
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

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