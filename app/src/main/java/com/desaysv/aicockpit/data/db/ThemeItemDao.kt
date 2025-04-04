package com.desaysv.aicockpit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.desaysv.aicockpit.data.ThemeItemData
import com.desaysv.aicockpit.utils.Log
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert_(theme: ThemeItemData): Long

    suspend fun insert(theme: ThemeItemData): Long{
        if(theme.isApplied){
            clearAppliedTheme()
        }
        return insert_(theme)
    }

    @Query("SELECT * FROM themes WHERE id = :id")
    suspend fun getById(id: Int): ThemeItemData?

    @Query("SELECT * FROM themes")
    fun getAllThemes(): Flow<List<ThemeItemData>>

    @Delete
    suspend fun delete(theme: ThemeItemData)

    @Query("DELETE FROM themes")
    suspend fun deleteAll()
    @Query("DELETE FROM themes WHERE id != 1")
    suspend fun deleteAllButDefault()

    // 1. 取消所有当前 isApplied = true 的主题
    @Query("UPDATE themes SET isApplied = 0 WHERE isApplied = 1")
    suspend fun clearAppliedTheme()

    // 2. 先清除 isApplied，然后插入新的主题（方法 1）
    @Transaction
    suspend fun replaceAppliedTheme(newTheme: ThemeItemData) {
        Log.d("取消应用")
        clearAppliedTheme() // 取消所有 isApplied = true
        Log.d("添加应用")
        insert(newTheme.copy(isApplied = true)) // 插入新的
    }

    // 3. 先清除 isApplied，然后把指定的 themeId 设为 isApplied = true（方法 2）
    @Transaction
    suspend fun switchAppliedTheme(themeId: Int) {
        Log.d("开始替换应用")
        clearAppliedTheme() // 取消所有 isApplied = true
        Log.d("取消应用")
        updateAppliedTheme(themeId) // 设定新主题

        Log.d("添加应用")
    }

    // 4. 将指定 ID 的主题设为 isApplied = true
    @Query("UPDATE themes SET isApplied = 1 WHERE id = :themeId")
    suspend fun updateAppliedTheme(themeId: Int)


    // 1. **清除当前默认主题**
    @Query("UPDATE themes SET isDefault = 0 WHERE isDefault = 1")
    suspend fun clearDefaultTheme()

    // 2. **设定某个 themeId 为默认**
    @Query("UPDATE themes SET isDefault = 1 WHERE id = :themeId")
    suspend fun setDefaultTheme(themeId: Int)

    // 3. **获取当前默认主题**
    @Query("SELECT * FROM themes WHERE isDefault = 1 LIMIT 1")
    suspend fun getCurrentDefaultTheme(): ThemeItemData?

    // 4. **计算当前有多少个默认主题**
    @Query("SELECT COUNT(*) FROM themes WHERE isDefault = 1")
    suspend fun countDefaultThemes(): Int

    // 5. **检查数据库中是否有 theme**
    @Query("SELECT COUNT(*) FROM themes")
    suspend fun countThemes(): Int

    // 6. **事务操作：更换默认主题**
    @Transaction
    suspend fun replaceDefaultTheme(newTheme: ThemeItemData) {
        clearDefaultTheme() // 清除旧的默认主题
        insert(newTheme.copy(isDefault = true)) // 插入新的默认主题
    }

    @Update
    suspend fun update(theme: ThemeItemData)

    @Query("SELECT * FROM themes")
    suspend fun getAll(): List<ThemeItemData>

    @Query("SELECT * FROM themes WHERE themeName = :name LIMIT 1")
    suspend fun getByName(name: String): ThemeItemData?
    @Query("SELECT * FROM themes WHERE imgPath = :path LIMIT 1")
    suspend fun getByPath(path: String): ThemeItemData?

}
