package com.desaysv.aicockpit.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.desaysv.aicockpit.R
import com.desaysv.aicockpit.business.ImageConstants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

object ResourceManager {
    fun copyAssetsImagesToSharedPictures(context: Context) {
        val assetManager = context.assets
        val imageList = listOf("布谷鸟.png", "树叶婆娑.png", "麋鹿.png", "奶牛.png", "df.png")

        val relativePath = ImageConstants.DEFAULT_SOUND_PICS_PATH


        val dirFile = File(relativePath)
        if (!dirFile.exists()) {
            val created = dirFile.mkdirs()
            Log.d("AssetCopy", if (created) "目录创建成功: $relativePath" else "目录创建失败或已存在: $relativePath")
        }

        for (filename in imageList) {
//
            val outFile = File(relativePath, filename)

            if (outFile.exists()) {
                Log.d("AssetCopy", "跳过已存在文件: $filename")
                continue
            }

            try {
                assetManager.open("images/$filename").use { inputStream ->
                    FileOutputStream(outFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d("AssetCopy", "写入成功: $filename")

                // 通知媒体库刷新
                MediaScannerConnection.scanFile(context, arrayOf(outFile.absolutePath), arrayOf("image/png"), null)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("AssetCopy", "写入失败: $filename")
            }
        }
    }
    fun copyAssetsAudiosToSharedPictures(context: Context) {
        val assetManager = context.assets
        val audioList = listOf("布谷鸟.mp3", "树叶婆娑.mp3", "奶牛.mp3", "麋鹿.mp3")

        val relativePath = ImageConstants.DEFAULT_SOUND_AUDIO_PATH

        val dirFile = File(relativePath)
        if (!dirFile.exists()) {
            val created = dirFile.mkdirs()
            Log.d("AssetCopy", if (created) "目录创建成功: $relativePath" else "目录创建失败或已存在: $relativePath")
        }

        for (filename in audioList) {
            val outFile = File(relativePath, filename)

            if (outFile.exists()) {
                Log.d("AssetCopy", "跳过已存在音频文件: $filename")
                continue
            }

            try {
                assetManager.open("audio/$filename" ).use { inputStream ->
                    FileOutputStream(outFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Log.d("AssetCopy", "音频写入成功: $filename")

                // 通知媒体库刷新
                MediaScannerConnection.scanFile(context, arrayOf(outFile.absolutePath), arrayOf("audio/mpeg"), null)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("AssetCopy", "音频写入失败: $filename")
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

    // ✅ 查询 MediaStore 是否存在相同音频文件
    fun findAudioInMediaStore(
        resolver: ContentResolver,
        displayName: String,
        relativePath: String
    ): Uri? {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.RELATIVE_PATH
        )

        val selection = "${MediaStore.Audio.Media.DISPLAY_NAME} = ? AND ${MediaStore.Audio.Media.RELATIVE_PATH} = ?"
        val selectionArgs = arrayOf(displayName, "$relativePath/") // 注意路径末尾要带斜杠

        resolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val id = cursor.getLong(idColumn)
                return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
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