package com.desaysv.aicockpit.utils

import android.content.Context
import com.desaysv.aicockpit.R
import java.io.File
import java.io.FileOutputStream
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