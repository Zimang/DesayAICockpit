package com.desay.desayaicockpit.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.desay.desayaicockpit.R

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @ColumnInfo(defaultValue = "1")
//    @ColumnInfo(defaultValue = R.drawable)
    val imageResId: Int
)