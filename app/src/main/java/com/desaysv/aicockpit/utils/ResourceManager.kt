package com.desaysv.aicockpit.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.desaysv.aicockpit.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

object ResourceManager {
    fun copyAssetsImagesToDefImgDir(context: Context) {
        val assetManager = context.assets
        val defImgDir = File(context.filesDir, "defImg") // ← 改这里

        if (!defImgDir.exists()) {
            defImgDir.mkdirs()
        }

//        val imageList = assetManager.list("images") ?: return // assets/images/

        val imageList = listOf("b_1_h.png", "b_2_h.png", "b_3_h.png",  "b_4_h.png")


        for (filename in imageList) {
            val targetFile = File(defImgDir, filename)

            if (!targetFile.exists()) {
                assetManager.open("images/$filename").use { input ->
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }
    fun copyAssetsImagesToSharedPictures(context: Context) {
        val assetManager = context.assets
        val imageList = listOf("b_1_h.png", "b_2_h.png", "b_3_h.png", "b_4_h.png")

        val resolver = context.contentResolver
        val relativePath = Environment.DIRECTORY_PICTURES + "/aicockpit"

        for (filename in imageList) {

            // ✅ 检查是否已存在该文件
            val existingUri = findImageInMediaStore(resolver, filename, relativePath)
            if (existingUri != null) {
                Log.d("AssetCopy", "跳过已存在文件: $filename")
                continue
            }

            // ✅ 创建 contentValues 并插入新文件
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
            }

            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                try {
                    assetManager.open("images/$filename").use { inputStream ->
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    Log.d("AssetCopy", "写入成功: $filename")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                Log.e("AssetCopy", "插入失败: $filename")
            }
        }
    }


    private var contextRef: WeakReference<Context>? = null //避免内存泄露

    fun init(context: Context) {
        contextRef = WeakReference(context.applicationContext)
    }

    fun getString(resourceId: Int): String? {
        return contextRef?.get()?.getString(resourceId)
    }

//    // 获取 tags 列表
//    fun getTags(): List<String?> {
//        return listOf(
//            contextRef?.get()?.getString(R.string.sound),
//            contextRef?.get()?.getString(R.string.light),
//            contextRef?.get()?.getString(R.string.screen))
//    }
//
//    // 获取 screenTags 列表
//    fun getScreenTags(): List<String?> {
//        return listOf(
//            contextRef?.get()?.getString(R.string.inspiration),
//            contextRef?.get()?.getString(R.string.personalized_cabin),
//            contextRef?.get()?.getString(R.string.save)
//        )
//    }

    // ✅ 查询 MediaStore 是否存在相同文件
    fun findImageInMediaStore(
        resolver: ContentResolver,
        displayName: String,
        relativePath: String
    ): Uri? {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.RELATIVE_PATH
        )

        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ? AND ${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(displayName, "$relativePath/") // 注意路径末尾要带斜杠

        resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = cursor.getLong(idColumn)
                return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            }
        }

        return null
    }


    fun getPersonalizedCabin(): String? =
        contextRef?.get()?.getString(R.string.personalized_cabin)

    fun getInspiration(): String? =
        contextRef?.get()?.getString(R.string.inspiration)

    fun getGenerateMyCabin(): String? =
        contextRef?.get()?.getString(R.string.generate_my_cabin)

    fun getSound(): String? =
        contextRef?.get()?.getString(R.string.sound)

    fun getLight(): String? =
        contextRef?.get()?.getString(R.string.light)

    fun getScreen(): String? =
        contextRef?.get()?.getString(R.string.screen)

    fun getAiCabin(): String? =
        contextRef?.get()?.getString(R.string.ai_cabin)

    fun getApply(): String? =
        contextRef?.get()?.getString(R.string.apply)

    fun getApplying(): String? =
        contextRef?.get()?.getString(R.string.applying)

    fun getDeleted(): String? =
        contextRef?.get()?.getString(R.string.deleted)


    fun getNameYourCabin(): String? =
        contextRef?.get()?.getString(R.string.name_your_cabin)

    fun getSchwarzwaldCabin(): String? =
        contextRef?.get()?.getString(R.string.schwarzwald_cabin)

    fun getSaveApply(): String? =
        contextRef?.get()?.getString(R.string.save_apply)

    fun getSave(): String? =
        contextRef?.get()?.getString(R.string.save)
    fun getThemeName(): String? =
        contextRef?.get()?.getString(R.string.theme_name)
    fun getAppliedSuccessfully(): String? =
        contextRef?.get()?.getString(R.string.applied_successfully)

    // 组合项：tags 列表
    fun getTags(): List<String?> =
        listOf(getSound(), getLight(), getScreen())

    // 组合项：screenTags 列表
    fun getScreenTags(): List<String?> =
        listOf(getInspiration(), getPersonalizedCabin(), getSave())

}