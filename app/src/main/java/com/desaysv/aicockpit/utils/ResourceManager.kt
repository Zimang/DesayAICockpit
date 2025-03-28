package com.desaysv.aicockpit.utils

import android.content.Context
import com.desaysv.aicockpit.R
import java.lang.ref.WeakReference

object ResourceManager {

    private var contextRef: WeakReference<Context>? = null //避免内存泄露

    fun init(context: Context) {
        contextRef = WeakReference(context.applicationContext)
    }

    fun getString(resourceId: Int): String? {
        return contextRef?.get()?.getString(resourceId)
    }

    // 获取 tags 列表
    fun getTags(): List<String?> {
        return listOf(
            contextRef?.get()?.getString(R.string.sound),
            contextRef?.get()?.getString(R.string.light),
            contextRef?.get()?.getString(R.string.screen))
    }

    // 获取 screenTags 列表
    fun getScreenTags(): List<String?> {
        return listOf(
            contextRef?.get()?.getString(R.string.inspiration),
            contextRef?.get()?.getString(R.string.personalized_cabin),
            contextRef?.get()?.getString(R.string.save)
        )
    }
}